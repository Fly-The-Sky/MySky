package com.jq.printer.jpl;

import com.jq.port.Port;
import com.jq.printer.PrinterParam;

public class BaseJPL 
{
	protected PrinterParam _param;
	protected Port _port;
	protected byte _cmd[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	/*
	 * ¹¹Ôìº¯Êý
	 */
	public BaseJPL(PrinterParam param) {
		_param = param;	
		_port = _param.port;
	}
}
