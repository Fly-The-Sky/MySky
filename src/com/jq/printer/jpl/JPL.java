package com.jq.printer.jpl;

import com.jq.port.Port;
import com.jq.printer.PrinterParam;

public class JPL {
	/*
	 * 枚举类型：对象旋转方式
	 */	
	public static enum ROTATE
	{
		x0(0x00),
		x90(0x01),
		x180(0x10),
		x270(0x11);
		private int _value;
		private ROTATE(int mode)
		{
			_value = mode;
		}
		public int value()
		{
			return _value;
		}
	}
	public static enum COLOR
	{
		White,
		Black,		
	}	
	private Port _port;
	private byte[] _cmd = { 0, 0, 0, 0, 0, 0};
	private PrinterParam _param;
	public Page page;
	public Barcode barcode;
	public Text text;
	public TextArea textarea;
	public Graphic graphic;
	public Image image;
		
	public JPL(PrinterParam param) {
		_param = param;
		_port = _param.port;
		page = new Page(_param);
		barcode = new Barcode(_param);
		text = new Text(_param);
		graphic = new Graphic(_param);
		image = new Image(_param);
		textarea = new TextArea(_param);
	}
	
	/*
	 * 走纸到下一张标签开始
	 */
	public boolean feedNextLabelBegin() {
		_cmd[0] = 0x1A;
		_cmd[1] = 0x0C;
		_cmd[2] = 0x00;
		return _port.write(_cmd, 0, 3);
	}
	public static enum FEED_TYPE
	{
		MARK_OR_GAP,
		LABEL_END,
		MARK_BEGIN,
		MARK_END,
		BACK, //后退
	}

	private boolean feed(FEED_TYPE feed_type, int offset) {
		_cmd[0] = 0x1A;
		_cmd[1] = 0x0C;
		_cmd[2] = 0x01;
		_cmd[3] = (byte) feed_type.ordinal();
		_cmd[4] = (byte) offset;
		_cmd[5] = (byte) (offset >> 8);
		return _port.write(_cmd, 0, 6);
	}
	
	//进入JPL模式
	public boolean intoGPL(int timerout_read)
    {
		_port.flushReadBuffer();
		_cmd[0] = 0x1b;
		_cmd[1] = 0x23;
		_cmd[2] = 0x00;
		if (!_port.write(_cmd,0,3))
			return false;
		return true;
    }
	
	//退出JPL模式
	public boolean exitGPL(int timerout_read)
    {
		_port.flushReadBuffer();
		_cmd[0] = 0x1a;
		_cmd[1] = 0x23;
		_cmd[2] = 0x00;
		if (!_port.write(_cmd,0,3))
			return false;
		return true;
    }
	/*
	 * 打印纸后退
	 * 注意:1.需要打印机JLP351的固件版本3.0.0.0及以上
	 *      2.需要设置软件设置打印机，使能FeedBack状态
	 */
	public boolean feedBack(int dots)
	{
		return feed(FEED_TYPE.BACK,dots);
	}
	
	public boolean feedNextLabelEnd(int dots)
	{
		return feed(FEED_TYPE.LABEL_END,dots);
	}
	
	public boolean feedMarkOrGap(int dots)
	{
		return feed(FEED_TYPE.MARK_OR_GAP,dots);
	}
	
	public boolean feedMarkEnd(int dots)
	{
		return feed(FEED_TYPE.MARK_END,dots);
	}
	
	public boolean feedMarkBegin(int dots)
	{
		return feed(FEED_TYPE.MARK_BEGIN,dots);
	}
}
