package com.jq.ui.express;

import java.text.SimpleDateFormat;

import com.jq.R;
import com.jq.printer.JQPrinter;
import com.jq.printer.Printer_define.ALIGN;
import com.jq.printer.Printer_define.FONT_ID;
import com.jq.printer.Printer_define.PRINTER_MODEL;
import com.jq.printer.jpl.Barcode.BAR_ROTATE;
import com.jq.printer.jpl.Barcode.BAR_UNIT;
import com.jq.printer.jpl.Barcode.QRCODE_ECC;
import com.jq.printer.jpl.Image.IMAGE_ROTATE;
import com.jq.printer.jpl.JPL;
import com.jq.printer.jpl.JPL.ROTATE;
import com.jq.printer.jpl.Page.PAGE_ROTATE;
import com.jq.printer.jpl.Text.TEXT_ENLARGE;
import com.jq.ui.DemoApplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainExpressActivity extends Activity {
	int startIndex;// ��ʼ��ӡ�����
	int amount;// ��ӡ������
	boolean rePrint = false;// �Ƿ���Ҫ���´�ӡ
	private JQPrinter Printer = new JQPrinter(PRINTER_MODEL.JLP351);
	private Button buttonPrint = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_express);

		buttonPrint = (Button) findViewById(R.id.buttonExpressPrint);

		DemoApplication app = (DemoApplication) getApplication();
		if (app.printer != null) {
			Printer = app.printer;
		} else {
			Log.e("JQ", "app.printer null");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_express, menu);
		return true;
	}

	// private boolean getPrinterState() {
	// if (!Printer.getPrinterState(3000)) {
	// Toast.makeText(this, "��ȡ��ӡ��״̬ʧ��", Toast.LENGTH_SHORT).show();
	// return false;
	// }
	//
	// if (Printer.isCoverOpen) {
	// Toast.makeText(this, "��ӡ��ֽ�ָ�δ�ر�", Toast.LENGTH_SHORT).show();
	// return false;
	// } else if (Printer.isNoPaper) {
	// Toast.makeText(this, "��ӡ��ȱֽ", Toast.LENGTH_SHORT).show();
	// return false;
	// }
	// return true;
	// }

	public boolean print(String order_num) {
		if (!Printer.getJPLsupport()) {
			Toast.makeText(this, "��֧��JPL����������ȷ�Ĵ�ӡ���ͺţ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		JPL jpl = Printer.jpl;
		if (startIndex <= 0)
			startIndex = 1;
		for (; startIndex <= amount; startIndex++) {
			jpl.page.start(0, 0, 576, 424, PAGE_ROTATE.x90);
			jpl.image.drawOut(0, 0, this.getResources(), R.drawable.ic_launcher, IMAGE_ROTATE.x0);
			String bar_str = String.format(order_num + "-%03d-%03d", startIndex, amount);
			jpl.barcode.code128(ALIGN.CENTER, 0, 575, 0, 64, BAR_UNIT.x2, BAR_ROTATE.ANGLE_0, bar_str);
			// printer.jpl.barcode.code128(16, 0, 64, BAR_UNIT.x3,
			// BAR_ROTATE.ANGLE_0, bar_str);
			// printer.jpl.barcode.PDF417(16, 68, 5, 3, 4, BAR_UNIT.x2,
			// ROTATE.x0, bar_str);
			jpl.text.drawOut(ALIGN.CENTER, 0, 575, 66, bar_str, 16, false, false, false, false, TEXT_ENLARGE.x2,
					TEXT_ENLARGE.x1, ROTATE.x0);
			// printer.jpl.text.drawOut(96,64, bar_str);
			// printer.jpl.barcode.QRCode(0, 120, 0, QRCODE_ECC.LEVEL_M,
			// BAR_UNIT.x3, ROTATE.x0, bar_str);

			jpl.graphic.line(8, 96, 568, 96, 3);
			jpl.graphic.line(8, 160, 568, 160, 3);
			jpl.graphic.line(8, 224, 568, 224, 3);
			jpl.graphic.line(8, 288, 568, 288, 3);

			jpl.graphic.line(8, 96, 8, 288, 3);
			jpl.graphic.line(568, 96, 568, 288, 3);
			jpl.graphic.line(304, 96, 304, 224, 3);
			jpl.graphic.line(456, 96, 456, 160, 3);

			jpl.text.drawOut(14, 104, "���ݡ���" + "�Ϻ���", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x2,
					ROTATE.x0);
			jpl.text.drawOut(320, 104, String.valueOf(startIndex) + "/" + amount, 48, true, false, false, false,
					TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(464, 104, "�ؿ�", 48, true, true, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(14, 168, "�Ϻ���ǿ���ӿƼ����޹�˾", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
					ROTATE.x0);
			jpl.text.drawOut(320, 168, "����", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
					ROTATE.x0);
			jpl.text.drawOut(320, 168 + 26, "021-61645760", 24, false, false, false, false, TEXT_ENLARGE.x1,
					TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(14, 232, "�Ϻ��ֶ����·258��2��¥2¥", 24, true, false, false, false, TEXT_ENLARGE.x1,
					TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(14, 296, "ȫ������www.qs-express.com", 32, true, false, false, false, TEXT_ENLARGE.x1,
					TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.page.end();
			jpl.page.print();
			jpl.feedMarkOrGap(0);// printer.jpl.feedNextLabelEnd(48);//printer.jpl.feedNextLabelBegin();
			int i = 0;
			for (i = 0; i < 10; i++) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!Printer.getPrinterState(4000))// �˴��Ķ���ʱ��Ҫ���ϴ�ӡ���ݵ�ʱ�䡣����ݴ�ӡ���ݵ���,������ӡ�����ݸ��࣬����Ҫ���ø����ʱ�䡣
				{
					Toast.makeText(this, "��ȡ��ӡ��״̬ʧ��", Toast.LENGTH_SHORT).show();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				if (Printer.isCoverOpen) {
					buttonPrint.setText("ֽ��δ��--���´�ӡ");
					rePrint = true;
					return true;
				} else if (Printer.isNoPaper) {
					buttonPrint.setText("ȱֽ--���´�ӡ");
					rePrint = true;
					return true;
				}
				if (Printer.isPrinting) {
					Toast.makeText(this, "���ڴ�ӡ", Toast.LENGTH_SHORT).show();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					break;
				}
			}

		}
		Toast.makeText(this, "��ӡ����", Toast.LENGTH_SHORT).show();
		return true;
	}

	private boolean printExpress() {
		Printer.jpl.intoGPL(1000);
		if (!Printer.getJPLsupport()) {
			Toast.makeText(this, "��֧��JPL����������ȷ�Ĵ�ӡ���ͺţ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		JPL jpl = Printer.jpl;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");

		int y = 0, y1, y2, y3, y4, x, x1, x2, x3;
		if (!jpl.page.start(0, 0, 576, 655 + 28 + 13 + 70, PAGE_ROTATE.x0))
			return false;
		jpl.image.drawOut(175, 0, this.getResources(), R.drawable.shenzhenhangkong, IMAGE_ROTATE.x0);
		y += 48;
		if (!jpl.text.drawOut(0, y, "4008-999-999", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;

		y += 32;
		if (!jpl.text.drawOut(0, y, "www.jqtek.com", 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		y = 100;
		y += 2;

		y += 30;
		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;

		String bar_str = "101213-12312312-231313";
		y += 8;
		if (!jpl.barcode.code128(ALIGN.CENTER, 0, 575, y, 80, BAR_UNIT.x2, BAR_ROTATE.ANGLE_0, bar_str))
			return false;
		y += 81;
		jpl.text.drawOut(ALIGN.CENTER, 0, 575, y, bar_str, 24, false, false, false, false, TEXT_ENLARGE.x1,
				TEXT_ENLARGE.x1, ROTATE.x0);

		y += 25;
		y1 = y;
		if (!jpl.graphic.line(0, y1, 575, y1, 3))
			return false;
		/////////////////////
		y += 4;
		if (!jpl.text.drawOut(3, y, "�ļ�����Ϣ:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(200, y, "�Լġ� ��ȡ�� ԭ�ĵ�:021CA", 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		boolean textarea_enable = true;
		y += 28;
		if (!textarea_enable) {
			if (!jpl.text.drawOut(3, y, "�Ϻ���ǿ���ӿƼ����޹�˾", 16, false, false, false, false, TEXT_ENLARGE.x1,
					TEXT_ENLARGE.x1, ROTATE.x0))
				return false;

			y += 20;
			jpl.text.drawOut(3, y, "�Ϻ��ֶ��������·258��2��¥2¥����", 16, false, false, false, false, TEXT_ENLARGE.x1,
					TEXT_ENLARGE.x1, ROTATE.x0);

			y += 20;
			if (!jpl.text.drawOut(3, y, "���Ŵ��� 133-3333-3333", 16, false, false, false, false, TEXT_ENLARGE.x1,
					TEXT_ENLARGE.x1, ROTATE.x0))
				return false;
			y += 20;
		} else {
			int height = 58;
			jpl.textarea.setArea(3, y, 576, height);
			jpl.textarea.setSpace(0, 4);
			jpl.textarea.setFont(FONT_ID.ASCII_8x16, FONT_ID.GBK_16x16);
			jpl.textarea.drawOut("�Ϻ���ǿ���ӿƼ����޹�˾\r" + "�Ϻ��ֶ��������·258��2��¥2¥����\n" + "���Ŵ��� 133-3333-3333");
			y += height;
		}

		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;
		//////////////////////
		y += 4;
		if (!jpl.text.drawOut(3, y, "�ռ�����Ϣ:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(350, y, "Ŀ�ĵ�:488", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		y += 26;
		if (!textarea_enable) {
			if (!jpl.text.drawOut(3, y, "������ǿ���ӿƼ����޹�˾"))
				return false;
			y += 26;
			jpl.text.drawOut(3, y, "�������������·258��2��¥2¥����");
			y += 26;
			jpl.text.drawOut(3, y, "������ʤ 188-8888-8888");
			y += 27;
		} else {
			int height = 24 * 4 + 4 * 4;
			jpl.textarea.setArea(3, y, 576, height);
			jpl.textarea.setSpace(0, 4);
			jpl.textarea.setFont(FONT_ID.ASCII_12x24, FONT_ID.GBK_24x24);
			jpl.textarea.drawOut("ShangHai New JiQiang MicroElectroinic Supper Technology Ltd.\r\n"
					+ "�Ϻ��ֶ��������·258��2��¥2¥����\n" + "���Ŵ��� 133-3333-3333");
			y += height;
		}
		y2 = y;
		if (!jpl.graphic.line(0, y2, 575, y2, 3))
			return false;
		//////////////////////
		y += 4;
		x1 = 288 - 3;
		if (!jpl.text.drawOut(3, y, "��Ʒ��Ϣ:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		if (!jpl.text.drawOut(x1, y, "���ӷ���:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;

		y += 26;
		if (!textarea_enable) {
			if (!jpl.text.drawOut(3, y, "  ����һ̨", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
					ROTATE.x0))
				return false;
			jpl.text.drawOut(x1, y, "  ����10000Ԫ", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
					ROTATE.x0);
			y += 48;
		} else {
			int height = (16 + 4) * 3;
			jpl.textarea.setArea(4, y, 288 - 15, height);
			jpl.textarea.setSpace(0, 4);
			jpl.textarea.setFont(FONT_ID.ASCII_8x16, FONT_ID.GBK_16x16);
			jpl.textarea.drawOut(ALIGN.LEFT, "�ʼǱ�����һ̨,ȫ�µ�������һֻ,���ϴ�˹Ь��һ˫,δ֪������ö\r���ж�������");

			jpl.textarea.setArea(x1, y, 576, height);
			jpl.textarea.drawOut(ALIGN.CENTER, "����һǧ������ʮ������ǧ����Ԫ��");

			y += height;
		}

		y3 = y;
		if (!jpl.graphic.line(0, y3, 575, y3, 3))
			return false;
		//////////////////////
		y += 5;
		jpl.text.drawOut(3, y, "ҵ������:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		if (!jpl.text.drawOut(3 + 6 * 16, y, "��׼���", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;

		if (!jpl.text.drawOut(x1, y, "��ɢ:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
			return false;
		x2 = 430;
		if (!jpl.text.drawOut(x2, y, "��׼���", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;

		y += 20;
		if (!jpl.text.drawOut(3, y, "���ʽ:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(3 + 6 * 16, y, "�ĸ�", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		if (!jpl.text.drawOut(x1, y, "ʵ������:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(x2, y, "1", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 20;
		if (!jpl.text.drawOut(3, y, "�½��ʺ�:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(3 + 6 * 16, y, "02178782212", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		if (!jpl.text.drawOut(x1, y, "����:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
			return false;
		jpl.text.drawOut(x2, y, "1", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 20;
		if (!jpl.text.drawOut(3, y, "����������:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(3 + 6 * 16, y, "\\", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		if (!jpl.text.drawOut(x1, y, "��������:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(x2, y, "12", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 20;
		// printer.jpl.text.drawOut(3, y, " ", 16, true, false, false, false,
		// TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		// printer.jpl.text.drawOut(3 + 6 * 16, y, " ", 16, false, false, false,
		// false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		if (!jpl.text.drawOut(x1, y, "���úϼ�:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(x2, y, "12", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 20;
		y4 = y;
		if (!jpl.graphic.line(0, y4, 575, y4, 3))
			return false;
		//////////////////////
		y += 4;
		x3 = 150;
		if (!jpl.text.drawOut(3, y, "�ķ�ǩ��:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(x3, y, "�ռ�Ա:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x1, y, "�շ�ǩ��:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x2, y, "�ɼ�Ա:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 30;
		if (!jpl.text.drawOut(x3, y, "����", 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
			return false;

		y += 30;
		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;
		jpl.graphic.line(x2 - 5, y3, x2 - 5, y, 2);// ����
		jpl.graphic.line(x3 - 5, y4, x3 - 5, y, 2);// ����
		//////////////////////
		y += 4;
		if (!jpl.text.drawOut(3, y, "�ļ�����:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(3 + 16 * 5, y, formatter.format(System.currentTimeMillis()), 16, false, false, false, false,
				TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x1, y, "�ռ�����:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 20;
		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;
		//////////////////////
		jpl.graphic.line(x1 - 5, y2, x1 - 5, y, 2);// ����

		if (!jpl.graphic.line(0, y1, 0, y, 3))
			return false;// ����
		jpl.graphic.line(575, y1, 575, y, 3);// ����
		jpl.page.end();
		jpl.page.print();
		if (!Printer.esc.feedLines(1))
			return false;
		// ��һ������
		// printer.jpl.feedNextLabelBegin();
		if (!jpl.page.start(0, 0, 576, 500, PAGE_ROTATE.x0))
			return false;

		y = 2;
		x = 3;
		if (!jpl.text.drawOut(x, y, "4008-999-999", 32, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		x1 = x + 16 * 13;
		jpl.text.drawOut(x1, y, "www.jqtek.com", 32, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);
		x2 = x1 + 16 * 14;
		jpl.text.drawOut(x2, y, "�ͻ���", 32, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 36;
		y1 = y;
		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;
		y += 4;
		jpl.text.drawOut(x, y, "�˵�����:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x + 5 * 24, y, bar_str, 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		y += 28;
		jpl.graphic.line(0, y, 575, y, 3);

		y += 4;
		if (!jpl.text.drawOut(x, y, "�ļ�����Ϣ:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(200, y, "�Լġ� ��ȡ��", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		y += 20;
		if (!jpl.text.drawOut(x, y, "�Ϻ���ǿ���ӿƼ����޹�˾", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;

		y += 20;
		jpl.text.drawOut(x, y, "�Ϻ��ֶ��������·258��2��¥2¥����", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		y += 20;
		if (!jpl.text.drawOut(x, y, "���Ŵ��� 133-3333-3333", 16, false, false, false, false, TEXT_ENLARGE.x1,
				TEXT_ENLARGE.x1, ROTATE.x0))
			return false;

		y += 20;
		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;
		//////////////////////
		y += 4;
		jpl.text.drawOut(x, y, "�ռ�����Ϣ:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		y += 20;
		jpl.text.drawOut(x, y, "������ǿ���ӿƼ����޹�˾", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);
		y += 20;
		jpl.text.drawOut(x, y, "�������������·258��2��¥2¥����", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);
		y += 20;
		jpl.text.drawOut(x, y, "������ʤ 188-8888-8888", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);
		y += 22;
		y2 = y;
		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;
		//////////////////////
		y += 4;
		x1 = x + 6 * 16;
		jpl.text.drawOut(x, y, "��Ʒ��Ϣ:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x1, y, "����һ̨", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		x2 = 576 * 2 / 3 - 5;
		jpl.text.drawOut(x2, y, "��ɨ��2ά������APP", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		y3 = y + 25;
		if (!jpl.barcode.QRCode(x2 + 8, y3, 0, QRCODE_ECC.LEVEL_M, BAR_UNIT.x4, ROTATE.x0,
				"http://www.jqsh.com/front/bin/ptdetail.phtml?Part=pro_08&Rcg=1"))
			return false;
		// printer.jpl.barcode.QRCode(x2 + 8, y3, 0, QRCODE_ECC.LEVEL_M,
		// BAR_UNIT.x3, ROTATE.x0, "13413");
		y += 40;
		jpl.text.drawOut(x, y, "���ӷ���:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x1, y, "����10000Ԫ", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0);

		y += 48;
		if (!jpl.graphic.line(0, y, x2 - 3, y, 3))
			return false;
		//////////////////////
		y += 5;
		jpl.text.drawOut(x, y, "ҵ������:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		jpl.text.drawOut(x1, y, "��׼���", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		y += 20;
		jpl.text.drawOut(x, y, "���ʽ:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x1, y, "�ĸ�", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		y += 20;
		if (!jpl.text.drawOut(x, y, "���úϼ�:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1,
				ROTATE.x0))
			return false;
		jpl.text.drawOut(x1, y, "12", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		y += 20;
		jpl.text.drawOut(x, y, "�ļ�����:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
		jpl.text.drawOut(x1, y, formatter.format(System.currentTimeMillis()), 16, false, false, false, false,
				TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

		y += 22;
		if (!jpl.graphic.line(0, y, 575, y, 3))
			return false;
		jpl.graphic.line(x2 - 3, y2, x2 - 3, y, 2);// ����

		jpl.graphic.line(0, y1, 0, y, 2);// ����
		jpl.graphic.line(575, y1, 575, y, 2);// ����
		// return jpl.page.print();

		// return jpl.feedNextLabelBegin();
		jpl.page.print();
		return Printer.jpl.exitGPL(1000);
		// return Printer.esc.feedLines(2);
	}

	public void ButtonExpressPrint_click(View view) {
		buttonPrint.setText("��ӡ");
		buttonPrint.setVisibility(Button.INVISIBLE);

		if (!Printer.isOpen) {
			this.finish();
			return;
		}
		// if (!getPrinterState())
		// {
		// buttonPrint.setVisibility(Button.VISIBLE);
		// return;
		// }
		if (!rePrint) {
			amount = 1;
			startIndex = 1;
		} else {
			// ����޷��жϵ�ǰ��ӡ�������Ƿ��ӡ��ã�������Ҫ���´�ӡ��ǰ�š����������һ����ť�������Ǵ�ǰ�Ż��Ǵ���һ�š�
			rePrint = false;
		}
		if (printExpress())
			// if (print("437801"));
			buttonPrint.setVisibility(Button.VISIBLE);
	}
}
