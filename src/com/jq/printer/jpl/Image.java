package com.jq.printer.jpl;

import com.jq.printer.PrinterParam;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Image extends BaseJPL {
	public Image(PrinterParam param) {
		super(param);
	}

	// <summary>
	/// 旋转角度
	/// </summary>
	public enum IMAGE_ROTATE {
		x0, x90, x180, x270
	}

	public boolean drawOut(int x, int y, int width, int height, char[] data) {
		if (width < 0 || height < 0)
			return false;
		int HeightWriteUnit = 10;
		int WidthByte = ((width - 1) / 8 + 1);
		int HeightWrited = 0;
		int HeightLeft = height;

		_cmd[0] = 0x1A;
		_cmd[1] = 0x21;
		_cmd[2] = 0x00;
		_cmd[3] = (byte) x;
		_cmd[4] = (byte) (x >> 8);
		_cmd[7] = (byte) width;
		_cmd[8] = (byte) (width >> 8);
		while (true) {
			if (HeightLeft <= HeightWriteUnit) {
				_cmd[5] = (byte) y;
				_cmd[6] = (byte) (y >> 8);
				_cmd[9] = (byte) HeightLeft;
				_cmd[10] = (byte) (HeightLeft >> 8);
				_port.write(_cmd, 0, 11);
				_port.write(data, HeightWrited * WidthByte, HeightLeft * WidthByte, 1);
				return true;
			} else {
				_cmd[5] = (byte) y;
				_cmd[6] = (byte) (y >> 8);
				_cmd[9] = (byte) HeightWriteUnit;
				_cmd[10] = (byte) (HeightWriteUnit >> 8);
				_port.write(_cmd, 0, 11);
				_port.write(data, HeightWrited * WidthByte, HeightWriteUnit * WidthByte, 1);
				y += HeightWriteUnit;
				HeightWrited += HeightWriteUnit;
				HeightLeft -= HeightWriteUnit;
			}
		}
	}

	public boolean drawOut(int x, int y, int width, int height, byte[] data, boolean Reverse, IMAGE_ROTATE Rotate,
			int EnlargeX, int EnlargeY) {
		if (width < 0 || height < 0)
			return false;
		int WidthByte = ((width - 1) / 8 + 1);
		int dataSize = WidthByte * height;
		if (dataSize != data.length)
			return false;

		short ShowType = 0;
		if (Reverse)
			ShowType |= 0x0001;
		ShowType |= (Rotate.ordinal() << 1) & 0x0006;
		ShowType |= (EnlargeX << 8) & 0x0F00;
		ShowType |= (EnlargeY << 14) & 0xF000;

		int HeightWriteUnit = 10;
		int HeightWrited = 0;
		int HeightLeft = height;

		_cmd[0] = 0x1A;
		_cmd[1] = 0x21;
		_cmd[2] = 0x01;
		_cmd[7] = (byte) (width);
		_cmd[8] = (byte) (width >> 8);
		_cmd[11] = (byte) (ShowType);
		_cmd[12] = (byte) (ShowType >> 8);
		while (true) {
			_cmd[3] = (byte) (x);
			_cmd[4] = (byte) (x >> 8);
			_cmd[5] = (byte) (y);
			_cmd[6] = (byte) (y >> 8);

			if (HeightLeft > HeightWriteUnit) {
				_cmd[9] = (byte) (HeightWriteUnit);
				_cmd[10] = (byte) (HeightWriteUnit >> 8);
				_port.write(_cmd, 0, 13);

				_port.write(data, HeightWrited * WidthByte, HeightWriteUnit * WidthByte);
				switch (Rotate) {
				case x0:
					y += HeightWriteUnit * ((int) EnlargeX + 1);
					break;
				case x90:
					x -= HeightWriteUnit * ((int) EnlargeY + 1);
					break;
				case x180:
					y -= HeightWriteUnit * ((int) EnlargeX + 1);
					break;
				case x270:
					x += HeightWriteUnit * ((int) EnlargeY + 1);
					break;
				}
				HeightWrited += HeightWriteUnit;
				HeightLeft -= HeightWriteUnit;
			} else {
				_cmd[9] = (byte) (HeightLeft);
				_cmd[10] = (byte) (HeightLeft >> 8);
				_port.write(_cmd, 0, 13);
				return _port.write(data, HeightWrited * WidthByte, HeightLeft * WidthByte);
			}
		}
	}

	private boolean PixelIsBlack(int color, int gray_threshold) {
		int red = ((color & 0x00FF0000) >> 16);
		int green = ((color & 0x0000FF00) >> 8);
		int blue = color & 0x000000FF;
		int grey = (int) ((float) red * 0.299 + (float) green * 0.587 + (float) blue * 0.114);
		return (grey < gray_threshold);
	}

	public byte[] CovertImageHorizontal(Bitmap bitmap, int gray_threshold) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int BytesPerLine = (width - 1) / 8 + 1;

		byte[] data = new byte[BytesPerLine * height];
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
		int index = 0;

		// 根据位图各点的灰度值，确定打印位图相应的点的黑白色
		int x = 0, y = 0; // 位图的x，y坐标值；
		for (int i = 0; i < height; i++) // 每次判断一像素行，需要判断BmpHeight次
		{
			for (int j = 0; j < BytesPerLine; j++) // 每行需要LengthRow字节存放数据，
			{
				for (int k = 0; k < 8; k++) // 每个字节存放8个点，即1点1位；
				{
					x = (j << 3) + k; // x坐标为已计算字节×8+当前字节的位k；
					y = i; // 当前行；
					if (x >= width) // 如果位图当前像素点的y坐标大于实际位图宽度(位图实际宽度可能不为8的整数倍)，不对该点颜色进行判断；
					{
						continue;
					} else {
						if (PixelIsBlack(bitmap.getPixel(x, y), gray_threshold)) {
							data[index] |= (byte) (0x01 << k);
						}
					}
				}
				index++; // 一行像素行8点判断完毕后，位图数据实际长度加1；
			}
			x = 0;
		}
		return data;
	}

	public boolean drawOut(int x, int y, Resources res, int id, IMAGE_ROTATE rotate) {
		Bitmap bitmap = BitmapFactory.decodeResource(res, id);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width > _param.pageWidth || height > _param.pageHeight)
			return false;
		return drawOut(x, y, width, height, CovertImageHorizontal(bitmap, 128), false, rotate, 0, 0);
	}
}
