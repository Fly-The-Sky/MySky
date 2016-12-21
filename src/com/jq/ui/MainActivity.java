package com.jq.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import com.jq.R;
import com.jq.port.SerialPort;
import com.jq.printer.JQPrinter;
import com.jq.printer.Printer_define.PRINTER_MODEL;
import com.jq.ui.card_reader.MainCardReaderActivity;
import com.jq.ui.enforcement_bill.ebMainActivity;
import com.jq.ui.express.MainExpressActivity;
import com.jq.ui.express_form.ExpressFormMainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static int REQUEST_BT_ENABLE = 0;
	private final static int REQUEST_BT_ADDR = 1;

	private boolean mBtOpenSilent = true;
	private BluetoothAdapter btAdapter = null;
	private JQPrinter printer = new JQPrinter(PRINTER_MODEL.JLP351);

	private DemoApplication mApplication = null;
	private Button mButtonLawEnforcementBill = null;
	private Button mButtonExpressBill = null;
	private Button mButtonExpressForm = null;
	private Button mButtonCardReader = null;
	private Button mButtonBtScan = null;

	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;

	private long mLastTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("济强打印机Demo     ");

		mApplication = (DemoApplication) getApplication(); // tip:DemoApplication需要在AndroidManifest.xml的application中注册
															// android:name="com.jq.ui.DemoApplication"

		mButtonBtScan = (Button) findViewById(R.id.BTButton);
		mButtonBtScan.setText("选择打印机");
		mButtonLawEnforcementBill = (Button) findViewById(R.id.ButtonLawEnforcementBill);
		mButtonLawEnforcementBill.setVisibility(Button.INVISIBLE);

		mButtonExpressForm = (Button) findViewById(R.id.ButtonExpressForm);
		mButtonExpressForm.setVisibility(Button.INVISIBLE);

		mButtonExpressBill = (Button) findViewById(R.id.ButtonExpressBill);
		mButtonExpressBill.setVisibility(Button.INVISIBLE);

		mButtonCardReader = (Button) findViewById(R.id.ButtonCardReader);
		mButtonCardReader.setVisibility(Button.INVISIBLE);

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			Toast.makeText(this, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		mApplication.btAdapter = btAdapter;

		// 如果本地蓝牙没有开启，则开启
		// 以下操作需要在AndroidManifest.xml中注册权限android.permission.BLUETOOTH
		// ；android.permission.BLUETOOTH_ADMIN
		if (!btAdapter.isEnabled()) {
			Toast.makeText(this, "正在开启蓝牙", Toast.LENGTH_SHORT).show();
			if (!mBtOpenSilent) {
				// 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
				// 那么将会收到RESULT_OK的结果，
				// 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
				Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(mIntent, REQUEST_BT_ENABLE);
			} else { // 用enable()方法来开启，无需询问用户(无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
				btAdapter.enable();
				Toast.makeText(this, "本地蓝牙已打开", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "本地蓝牙已打开", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// menu.clear();
		menu.add(0, 0, 0, "退出");
		return true;
	}

	private void exit() {
		if (printer != null) {
			if (!printer.close())
				Toast.makeText(this, "打印机关闭失败", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "打印机关闭成功", Toast.LENGTH_SHORT).show();
			printer = null;
		}
		if (btAdapter != null) {
			if (btAdapter.isEnabled()) {
				btAdapter.disable();
				Toast.makeText(this, "关闭蓝牙成功", Toast.LENGTH_LONG).show();
			}
		}
		finish();
		System.exit(0); // 凡是非零都表示异常退出!--0表示正常退出!
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			exit();
			break;
		}

		this.buttonSetupSerialPort_click();
		// 返回false允许正常的菜单处理资源，若返回true，则直接在此毁灭它
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)// 主要是对这个函数的复写
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
			if (System.currentTimeMillis() - mLastTime > 2000) // 2s内再次选择back键有效
			{
				System.out.println(Toast.LENGTH_LONG);
				Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_LONG).show();
				mLastTime = System.currentTimeMillis();
			} else {
				exit();
			}
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(android.content.Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				if (printer != null) {
					if (printer.isOpen)
						printer.close();
				}
				Toast.makeText(context, "蓝牙连接已断开", 1).show();
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_BT_ENABLE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "不允许蓝牙开启", Toast.LENGTH_SHORT).show();
				exit();
				return;
			}
		} else if (requestCode == REQUEST_BT_ADDR) {
			if (resultCode == Activity.RESULT_OK) {
				String btDeviceString = data.getStringExtra(BtConfigActivity.EXTRA_BLUETOOTH_DEVICE_ADDRESS);
				if (btDeviceString != null) {
					mButtonBtScan.setText("名称:" + data.getStringExtra(BtConfigActivity.EXTRA_BLUETOOTH_DEVICE_NAME)
							+ "\r\n地址:" + btDeviceString);

					if (btAdapter.isDiscovering())
						btAdapter.cancelDiscovery();

					if (printer != null) {
						printer.close();
					}

					if (!printer.open(btDeviceString)) {
						Toast.makeText(this, "打印机Open失败", Toast.LENGTH_SHORT).show();
						return;
					}

					if (!printer.wakeUp())
						return;

					mApplication.printer = printer;
					Log.e("JQ", "printer open ok");
					setButtonVisible(true);

					IntentFilter filter = new IntentFilter();
					filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);// 蓝牙断开
					registerReceiver(mReceiver, filter);
				}
			} else {
				mButtonBtScan.setText("选择打印机");
			}
		}
	}

	// 蓝牙按键点击 ,此函数需要在 activity_main.xml文件中注册
	public void bt_button_click(View view) {
		if (btAdapter == null)
			return;
		mButtonBtScan.setText("请等待");
		setButtonVisible(false);
		Intent myIntent = new Intent(MainActivity.this, BtConfigActivity.class);
		startActivityForResult(myIntent, REQUEST_BT_ADDR);
	}

	public void ButtonEnforcement_click(View view) {
		// Cancel discovery because it will slow down the connection
		if (btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();

		if (!printer.waitBluetoothOn(5000)) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}
		if (!printer.isOpen) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}

		Intent myIntent = new Intent(MainActivity.this, ebMainActivity.class);
		startActivity(myIntent);
		return;
	}

	public void ButtonExpress_click(View view) {
		// Cancel discovery because it will slow down the connection
		if (btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();

		if (!printer.waitBluetoothOn(5000)) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}
		if (!printer.isOpen) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}

		Intent myIntent = new Intent(MainActivity.this, MainExpressActivity.class);
		startActivity(myIntent);
	}

	private void setButtonVisible(boolean visible) {
		int state;
		if (visible)
			state = Button.VISIBLE;
		else
			state = Button.INVISIBLE;
		mButtonLawEnforcementBill.setVisibility(state);
		mButtonExpressBill.setVisibility(state);
		mButtonExpressForm.setVisibility(state);
		mButtonCardReader.setVisibility(state);

	}

	public void ButtonCardReader_click(View view) {
		// Cancel discovery because it will slow down the connection
		if (btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();

		if (!printer.waitBluetoothOn(5000)) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}
		if (!printer.isOpen) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}

		Intent myIntent = new Intent(MainActivity.this, MainCardReaderActivity.class);
		startActivity(myIntent);

	}

	public void ButtonExpressForm_click(View view) {
		// Cancel discovery because it will slow down the connection
		if (btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();

		if (!printer.waitBluetoothOn(5000)) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}
		if (!printer.isOpen) {
			mButtonBtScan.setText("选择打印机");
			setButtonVisible(false);
			return;
		}

		Intent myIntent = new Intent(MainActivity.this, ExpressFormMainActivity.class);
		startActivity(myIntent);
	}

	public void buttonSetupSerialPort_click() {
		startActivity(new Intent(MainActivity.this, SerialPortPreferences.class));
	}

	public void buttonSPOpen_click(View view) {
		try {
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

		} catch (SecurityException e) {
		} catch (IOException e) {
		} catch (InvalidParameterException e) {
		}

	}

}
