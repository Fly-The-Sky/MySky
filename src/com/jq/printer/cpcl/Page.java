package com.jq.printer.cpcl;

import com.jq.printer.PrinterParam;


public class Page extends BaseCPCL {
	public Page(PrinterParam param) {
		super(param);
	}
	
	/*
	 * 页面开始
	 * pageHeight:打印标签的最大高度点数（1点=0.125mm）。
	 * printCount: 打印标签数量. 最大为 1024
	 */
	public boolean start(int pageHeight, int Pagewidth, int printCount )
	{
		String cmd = "! 0 200 200 " + pageHeight + " " + printCount;
		portSendCmd(cmd);
		cmd = "PAGE-WIDTH " + Pagewidth;
		portSendCmd(cmd);
		return true;
	}
	
	/*
	 * 设置页面宽度
	 * width:页面宽度点数
	 */
	public boolean setPageWidth(int width)
	{
		String cmd = "PAGE-WIDTH " + width;
		return portSendCmd(cmd);
	}
	
	/*
	 * 打印页面
	 */
	public boolean print()
	{
		String cmd = "PRINT";
		return portSendCmd(cmd);
	}
	
	/*
	 * 走纸到定位点
	 * 必须和<BAR-SENSE>/<GAP-SENSE>指令配合
	 * 控制内容的注释，不会被打印出来
	 * 只能在<!> Commands和PRINT指令之间才有效。
	 */
	public boolean feed()
	{
		String cmd = "FORM";
		return portSendCmd(cmd);
	}
		
	/*
	 * 注释
	 */
	public boolean notes(String text)
	{
		String cmd = ";" + text;
		return portSendCmd(cmd);
	}
	
	public boolean bar_sense_right()
	{
		String cmd = "BAR-SENSE";
		return portSendCmd(cmd);
	}
	
	public boolean bar_sense_left()
	{
		String cmd = "BAR-SENSE LEFT";
		return portSendCmd(cmd);
	}
	
	public boolean gap_sense()
	{
		String cmd = "GAP-SENSE";
		return portSendCmd(cmd);
	}
	
	public boolean left()
	{
		String cmd = "LEFT";
		return portSendCmd(cmd);
	}
	
	public boolean center()
	{
		String cmd = "CENTER";
		return portSendCmd(cmd);
	}
	
	public boolean right()
	{
		String cmd = "RIGHT";
		return portSendCmd(cmd);
	}
	
	public boolean end()
	{
		String cmd = "END";
		return portSendCmd(cmd);
	}
	
	public boolean abort()
	{
		String cmd = "ABORT";
		return portSendCmd(cmd);
	}
	
	public boolean contrast(int level)
	{
		String cmd = "CONTRAST " + level ;
		return portSendCmd(cmd);
	}
	
	public boolean speed(int speed_level)
	{
		String cmd = "CONTRAST " + speed_level ;
		return portSendCmd(cmd);
	}
	
	public boolean prefeed(int length)
	{
		String cmd = "PREFEED " + length ;
		return portSendCmd(cmd);
	}
	
	public boolean postfeed(int length)
	{
		String cmd = "POSTFEED " + length ;
		return portSendCmd(cmd);
	}
}
