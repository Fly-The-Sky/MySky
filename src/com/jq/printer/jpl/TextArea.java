package com.jq.printer.jpl;

import com.jq.printer.PrinterParam;
import com.jq.printer.Printer_define.ALIGN;
import com.jq.printer.Printer_define.FONT_ID;
import com.jq.printer.jpl.JPL.ROTATE;
import com.jq.printer.jpl.Text.TEXT_ENLARGE;

public class TextArea extends BaseJPL{
	
	public TextArea(PrinterParam param) {
		super(param);
	}
	
	/*
	 * 设置文本区域的区域大小
	 * x,y：文本区域的原点
	 * width:文本区域的宽度
	 * height:文本区域的高度
	 */
	public boolean setArea(int x, int y, int width, int height) {
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x7F;
		_cmd[3] = (byte) x;
		_cmd[4] = (byte) (x >> 8);
		_cmd[5] = (byte) y;
		_cmd[6] = (byte) (y >> 8);
		_cmd[7] = (byte) width;
		_cmd[8] = (byte) (width >> 8);
		_cmd[9] = (byte) height;
		_cmd[10] = (byte) (height >> 8);
		return _port.write(_cmd, 0, 11);
	}
	
	/*
	 * 设置文本区域的字符间距和行间距
	 * charSpace：字符间距
	 * lineSpace：行间距
	 */
	public boolean setSpace(int charSpace, int lineSpace) {
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x7E;
		_cmd[3] = (byte) charSpace;
		_cmd[4] = (byte) (charSpace >> 8);
		_cmd[5] = (byte) lineSpace;
		_cmd[6] = (byte) (lineSpace >> 8);
		return _port.write(_cmd, 0, 7);
	}
	
	/*
	 * 设置文本区域字体
	 * ascID:英文字体ID
	 * hzID:汉字字体ID
	 */
	public boolean setFont(FONT_ID ascID, FONT_ID hzID) //page_textarea_set_font
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x7D;
		_cmd[3] = (byte) ascID.value();
		_cmd[4] = (byte) (ascID.value() >> 8);
		_cmd[5] = (byte) hzID.value();
		_cmd[6] = (byte) (hzID.value() >> 8);
		return _port.write(_cmd, 0, 7);
	} 
	
	/*
	 * 设置字体效果
	 * bold：加粗效果
 	 * underLine：下划线
 	 * reverse：反白
 	 * deleteLine： 删除线
	 * charRotate：字符旋转方向, 
	 * enlargeX: 水平方向字符放大倍数
	 * enlargeY： 垂直方向字符放大倍数
	 */
	public boolean setFontEffect(boolean bold, boolean underLine, boolean reverse, boolean deleteLine ,ROTATE charRotate, TEXT_ENLARGE enlargeX, TEXT_ENLARGE enlargeY) 
	{
		int fontType = 0;
		if (bold)
			fontType |= 0x0001;
		if (underLine)
			fontType |= 0x0002;
		if (reverse)
			fontType |= 0x0004;
		if (deleteLine)
			fontType |= 0x0008;
		
		switch (charRotate) {
		case x90:
			fontType |= 0x0010;
			break;
		case x180:
			fontType |= 0x0020;
			break;
		case x270:
			fontType |= 0x0030;
			break;
		default:
			break;
		}
		int ex = enlargeX.ordinal();
		int ey = enlargeY.ordinal();
		ex &= 0x000F;
		ey &= 0x000F;
		fontType |= (ex << 8);
		fontType |= (ey << 12);
		
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x7C;
		_cmd[3] = (byte) fontType;
		_cmd[4] = (byte) (fontType >> 8);
		
		return _port.write(_cmd, 0, 5);		
	}
	
	/*
	 * 设置字体加粗效果
	 */
	public boolean setFontBold(boolean bold)
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x7B;
		_cmd[3] = (byte)(bold?1:0);
		
		return _port.write(_cmd, 0, 4);	
	}
	/*
	 * 设置字体下划线效果
	 */
	public boolean setFontUnderLine(boolean underLine) 
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x7A;
		_cmd[3] = (byte)(underLine?1:0);
		
		return _port.write(_cmd, 0, 4);	
	}
	
	/*
	 * 设置字体反白效果
	 */
	public boolean setFontReverse(boolean reverse) 
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x79;
		_cmd[3] = (byte)(reverse?1:0);
		return _port.write(_cmd, 0, 4);	
	}
	
	/*
	 * 设置字体删除线效果
	 */
	public boolean setFontDeleteLine(boolean deleteLine) 
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x78;
		_cmd[3] = (byte)(deleteLine?1:0);
		return _port.write(_cmd, 0, 4);	
	}
	
	/*
	 * 设置字符旋转效果
	 */
	public boolean setFontCharRotate(ROTATE charRotate) 
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x77;
		_cmd[3] = (byte)(charRotate.value());
		return _port.write(_cmd, 0, 4);	
	}
	
	/*
	 * 设置字符放大效果
	 */
	public boolean setFontEnlarge(TEXT_ENLARGE enlargeX, TEXT_ENLARGE enlargeY)
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x76;
		_cmd[3] = (byte)(enlargeX.ordinal());
		_cmd[4] = (byte)(enlargeY.ordinal());
		return _port.write(_cmd, 0, 5);	
	}
	
	/*
	 * 依次(一个字符接一个字符)输出文本
	 */
	public boolean  drawOut(String text)	
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x00;
		_port.write(_cmd, 0, 3);
		return _port.write(text);
	}
	
	/*
	 * 从x,y坐标开始输出文本
	 */
	public boolean  drawOut(int x,int y,String text)	
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x01;
		_cmd[3] = (byte)x;
		_cmd[4] = (byte)(x>>8);
		_cmd[5] = (byte)y;
		_cmd[6] = (byte)(y>>8);
		_port.write(_cmd, 0, 7);
		return _port.write(text);
	}
	
	/*
	 * 根据对齐方式输出文本
	 */
	public boolean drawOut(ALIGN align ,String text)	//page_textarea_text_by_align
	{
		_cmd[0] = 0x1A;
		_cmd[1] = 0x74;
		_cmd[2] = 0x02;
		_cmd[3] = (byte)(align.ordinal());
		_port.write(_cmd, 0, 4);
		return _port.write(text);
	}
}
