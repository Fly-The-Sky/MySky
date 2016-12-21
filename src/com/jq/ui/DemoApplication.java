package com.jq.ui;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import com.jq.port.SerialPort;
import com.jq.port.SerialPortFinder;
import com.jq.printer.JQPrinter;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class DemoApplication extends Application {
	public JQPrinter printer = null;
	public BluetoothAdapter btAdapter = null;
	@Override
    public void onCreate() {
            super.onCreate();            
    }
	
	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialPort mSerialPort = null;

	//ͨ�������ļ����򿪴���
	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort != null)
			return mSerialPort;

		/* Read serial port parameters */
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String path = sharedPref.getString("DEVICE", "");
		int baudrate = Integer.decode(sharedPref.getString("BAUDRATE", "-1"));
		
		/* Check parameters */
		if ((path.length() == 0) || (baudrate == -1)) {
			
			Toast.makeText(this, "��ѡ�񴮿ںͲ�����", Toast.LENGTH_LONG).show();
			return null;
		}

		/* Open the serial port */
		mSerialPort = new SerialPort(new File(path), baudrate, 0);

		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort == null) return;
		mSerialPort.close();
		mSerialPort = null;
	}
}
