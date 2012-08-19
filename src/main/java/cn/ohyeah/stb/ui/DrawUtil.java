package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;

/**
 * 绘制工具
 * @author maqian
 * @version 1.0
 */
public class DrawUtil {
	/**
	 * 计算自适应按钮的宽度
	 * @param btnW 按钮背景图片的宽度
	 * @param btnBorder 按钮边框宽度
	 * @param text 按钮文字
	 * @param font 当前字体
	 * @return
	 */
	public static int calcAdaptiveButtonWidth(int btnW, int btnBorder, String text, Font font) {
		int textW = font.stringWidth(text);
		int btnSw = textW+(btnBorder<<1);
		if (btnSw < btnW) {
			btnSw = btnW;
		}
		return btnSw;
	}
	
	/**
	 * 绘制自适应按钮（按钮的宽度自动适应文字的宽度）
	 * @param g
	 * @param btnBg 按钮背景源图片
	 * @param btnSrcX 按钮背景在源图片中的X偏移
	 * @param btnSrcY 按钮背景在源图片中的Y偏移
	 * @param btnW 按钮宽度
	 * @param btnH 按钮高度
	 * @param btnBorder 按钮边框宽度
	 * @param text 按钮文字
	 * @param x 按钮显示位置
	 * @param y 按钮显示位置
	 * @param textColor 按钮文字颜色
	 */
	public static void drawAdaptiveButton(SGraphics g, Image btnBg, int btnSrcX, int btnSrcY, int btnW, int btnH, 
			int btnBorder, String text, int x, int y, int textColor) {
		
		int btnMidW = btnW-btnBorder-btnBorder;
		int btnFillW = btnW-btnBorder;
		Font font = g.getFont();
		int textW = font.stringWidth(text);
		int btnSw = textW+(btnBorder<<1);
		if (btnSw < btnW) {
			btnSw = btnW;
		}
		int sx = x;
		int sy = y;
		if (btnSw > btnW) {
			g.drawRegion(btnBg, btnSrcX, btnSrcY, btnFillW, btnH, 0, sx, sy, 20);
			sx += btnFillW;
			if (btnSw <= (btnFillW+btnFillW)) {
				g.drawRegion(btnBg, btnSrcX+(btnW-(btnSw-btnFillW)), btnSrcY, btnSw-btnFillW, btnH, 0, 
						sx, sy, 20);
			}
			else {
				int w = btnSw-btnFillW-btnFillW;
				while (w > 0) {
					if (w > btnMidW) {
						g.drawRegion(btnBg, btnSrcX+btnBorder, btnSrcY, btnMidW, btnH, 0, 
								sx, sy, 20);
						sx += btnMidW;
						w -= btnMidW;
					}
					else {
						g.drawRegion(btnBg, btnSrcX+btnBorder, btnSrcY, w, btnH, 0, 
								sx, sy, 20);
						sx += w;
						w -= w;
					}
				}
				g.drawRegion(btnBg, btnSrcX+btnBorder, btnSrcY, btnFillW, btnH, 0, 
						sx, sy, 20);
			}
		}
		else {
			g.drawRegion(btnBg, btnSrcX, btnSrcY, btnW, btnH, 0, sx, sy, 20);
		}
		sx = x+((btnSw-textW)>>1);
		sy = y+((btnH-font.getHeight())>>1);
		g.setColor(textColor);
		g.drawString(text, sx, sy, 20);
	}
	
	/**
	 * 绘制普通按钮（非自适应宽度）
	 * @param g
	 * @param btnBg 按钮背景图片
	 * @param text 按钮文字
	 * @param x 按钮显示位置
	 * @param y 按钮显示位置
	 * @param textColor 按钮文字颜色
	 */
	public static void drawButton(SGraphics g, Image btnBg, String text, int x, int y, int textColor) {
		Font font = g.getFont();
		int sx = x + ((btnBg.getWidth()-font.stringWidth(text))>>1);
		int sy = y + ((btnBg.getHeight()-font.getHeight())>>1);
		g.drawImage(btnBg, x, y, 20);
		g.setColor(textColor);
		g.drawString(text, sx, sy, 20);
	}
	
	/**
	 * 绘制图片数字，数组图片要求按0~9顺序横向排列，每个数字元素等宽
	 * @param g
	 * @param img 数组图片
	 * @param num 待显示的数组
	 * @param x 显示位置
	 * @param y 显示位置
	 * @param gap 每个数字间的间距
	 * @return
	 */
	public static int drawNumber(SGraphics g, Image img, int num, int x, int y, int gap) {
		String number = Integer.toString(num);
		int imgW = img.getWidth()/10;
		int imgH = img.getHeight();
		int sx = x;
		int sy = y;
		for (int i = 0; i < number.length(); ++i) {
			g.drawRegion(img, imgW*Character.digit(number.charAt(i), 10), 0, imgW, imgH, 0, sx, sy, 20);
			sx += imgW+gap;
		}
		return sx-x;
	}
	
	/**
	 * 绘制带符号的图片数字，数组图片要求按0~9,+,-顺序横向排列，每个数字元素等宽
	 * @param g
	 * @param img 数组图片
	 * @param num 待显示的数字
	 * @param x 显示位置
	 * @param y 显示位置
	 * @param gap 每个数字间的间距
	 * @return
	 */
	public static int drawNumberWithSymbol(SGraphics g, Image img, int num, int x, int y, int gap) {
		String number = num>=0?("+"+Integer.toString(num)):Integer.toString(num);
		int imgW = img.getWidth()/12;
		int imgH = img.getHeight();
		int sx = x;
		int sy = y;
		
		if (number.charAt(0) == '+') {
			g.drawRegion(img, imgW*10, 0, imgW, imgH, 0, sx, sy, 20);
		}
		else {
			g.drawRegion(img, imgW*11, 0, imgW, imgH, 0, sx, sy, 20);
		}
		sx += imgW+gap;
		for (int i = 1; i < number.length(); ++i) {
			g.drawRegion(img, imgW*Character.digit(number.charAt(i), 10), 0, imgW, imgH, 0, sx, sy, 20);
			sx += imgW+gap;
		}
		return sx-x;
	}
	
	/**
	 * 绘制矩形框
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param lineW
	 * @param color
	 */
	public static void drawRect(SGraphics g, int x, int y, int w, int h, int lineW, int color) {
		g.setColor(color);
		drawRect(g, x, y, w, h, lineW);
	}
	
	/**
	 * 绘制矩形框
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param lineW
	 */
	public static void drawRect(SGraphics g, int x, int y, int w, int h, int lineW) {
		int sx = x-1, sy = y-1, sw = w+1, sh = h+1;
		g.drawRect(sx, sy, sw, sh);
		for (int i = 1; i < lineW; ++i) {
			--sx;
			--sy;
			sw+=2;
			sh+=2;
			g.drawRect(sx, sy, sw, sh);
		}
	}
}
