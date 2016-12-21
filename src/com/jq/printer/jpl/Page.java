package com.jq.printer.jpl;

import com.jq.printer.PrinterParam;

public class Page  extends BaseJPL
{
	public static enum PAGE_ROTATE
	{
		x0,
		x90,
	}
	/*
	 * 构造函数
	 */
	public Page(PrinterParam param) {
		super(param);
	}

	/*
	 * 打印页面开始 使用打印机缺省参数绘制打印机页面 缺省页面宽576dots(72mm),高640dots（80mm）
	 */
	public boolean start() {
		_param.pageWidth = 576;
		_param.pageHeight = 640;
		_cmd[0] = 0x1A;
		_cmd[1] = 0x5B;
		_cmd[2] = 0x00;		
		return _port.write(_cmd,0,3);
	}

	/*
	 * 打印页面开始
	 */
	public boolean start(int originX, int originY, int pageWidth, int pageHeight, PAGE_ROTATE rotate) 
	{
		if (originX < 0) originX = 0;
		if (originY < 0) originY = 0;
		if (rotate == PAGE_ROTATE.x0)
		{
			if (originX > 575) originX = 0;
			if (originX + pageWidth > 576) pageWidth = 576 - originX;
		}
		else
		{
			if (originY > 575) originY = 0;
			if (originY + pageHeight > 576) pageHeight = 576 - originY;
		}
		
		_param.pageWidth = pageWidth;
		_param.pageHeight = pageHeight;
		
		_cmd[0] = 0x1A;
		_cmd[1] = 0x5B;
		_cmd[2] = 0x01;
		_cmd[3] = (byte)originX;
		_cmd[4] = (byte)(originX>>8);
		_cmd[5] = (byte)originY;
		_cmd[6] = (byte)(originY>>8);
		_cmd[7] = (byte)pageWidth;
		_cmd[8] = (byte)(pageWidth>>8);
		_cmd[9] = (byte)pageHeight;
		_cmd[10] = (byte)(pageHeight>>8);
		_cmd[11] = (byte)rotate.ordinal();

		return _port.write(_cmd,0,12);
	}

	/*
	 * 绘制打印页面结束
	 */
	public boolean end() {
		_cmd[0] = 0x1A;
		_cmd[1] = 0x5D;
		_cmd[2] = 0x00;
		return _port.write(_cmd, 0, 3);
	}

	/*
	 * 打印页面内容 之前做的页面处理，只是把打印对象画到内存中，必须要通过这个方法把内容打印出来
	 */
	public boolean print() {
		_cmd[0] = 0x1A;
		_cmd[1] = 0x4F;
		_cmd[2] = 0x00;
		return _port.write(_cmd, 0, 3);
	}
	
	/*
	 * 当前页面重复打印几次
	 */
	public boolean print(int count) {
		_cmd[0] = 0x1A;
		_cmd[1] = 0x4F;
		_cmd[2] = 0x01;
		_cmd[3] = (byte) count;
		return _port.write(_cmd, 0, 4);
	}
	
}
