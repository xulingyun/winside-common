package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.IEngine;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

/**
 * 弹出确认框
 * @author maqian
 * @version 1.0
 */
public class PopupConfirm {
	private IEngine engine;
	
	private Image textBg;
	private short textBgX, textBgY;
	
	private String text;
	private short textOffsetX, textOffsetY;
	private short textW, textH;
	private int textColor;
	
	private Image btnBg;
	private short btnXOffset[];
	private String btnText[];
	private short btnW;
	private short btnH;
	private short btnBorder;
	private short btnIndex;
	private int btnTextColor;

	public PopupConfirm(IEngine engine) {
		this.engine = engine;
	}
	
	public void setTextBg(Image textBg) {
		this.textBg = textBg;
	}
	
	public Image getTextBg() {
		return this.textBg;
	}
	
	public void setTextBgPos(int x, int y) {
		textBgX = (short)x;
		textBgY = (short)y;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setTextRegion(int offx, int offy, int w, int h) {
		this.textOffsetX = (short)offx;
		this.textOffsetY = (short)offy;
		this.textW = (short)w;
		this.textH = (short)h;
	}
	
	public void setTextOffset(int offx, int offy) {
		this.textOffsetX = (short)offx;
		this.textOffsetY = (short)offy;
	}
	
	public void setTextSize(int w, int h) {
		this.textW = (short)w;
		this.textH = (short)h;
	}
	
	public void setTextColor(int color) {
		this.textColor = color;
	}
	
	public void setButtonBg(Image btnBg, int w, int h) {
		this.btnBg = btnBg;
		this.btnW = (short)w;
		this.btnH = (short)h;
		if (btnBg.getWidth()%w != 0) {
			throw new RuntimeException("图片宽度不能整除");
		}
		if (btnBg.getHeight()%h != 0) {
			throw new RuntimeException("图片高度不能整除");
		}
	}
	
	public void setButtonBg(Image btnBg) {
		this.btnBg = btnBg;
		this.btnW = (short)btnBg.getWidth();
		this.btnH = (short)btnBg.getHeight();
	}
	
	public Image getButtonBg() {
		return this.btnBg;
	}
	
	public void setButtonText(String[] text) {
		btnText = text;
		btnXOffset = null;
	}
	
	public void setButtonNormalTextColor(int color) {
		this.btnTextColor = color;
	}
	
	private void calcBtnPos(Font font) {
		if (btnXOffset == null) {
			btnXOffset = new short[btnText.length];
			int btnLen = 0;
			for (int i = 0; i < btnText.length; ++i) {
				btnXOffset[i] = (short)DrawUtil.calcAdaptiveButtonWidth(btnW, btnBorder, btnText[i], font);
				btnLen += btnXOffset[i];
			}
			int space = (textW-btnLen)/(btnText.length+1);
			short prevBtnEndPos = textOffsetX;
			short curBtnLen = 0;
			for (int i = 0; i < btnXOffset.length; ++i) {
				prevBtnEndPos += space;
				curBtnLen = btnXOffset[i];
				btnXOffset[i] = prevBtnEndPos;
				prevBtnEndPos += curBtnLen;
			}
		}
	}
	
	public void show(SGraphics g) {
		if (textBg == null) {
			throw new RuntimeException("背景图片未设置");
		}
		if (text == null) {
			throw new RuntimeException("文字未设置");
		}
		if (btnText == null) {
			throw new RuntimeException("按钮文字未设置");
		}

		g.drawImage(textBg, textBgX, textBgY, 20);
		g.setColor(textColor);
		TextView.showMultiLineText(g, text, 1, textBgX+textOffsetX, textBgY+textOffsetY, textW, textH);
		
		Font font = g.getFont();
		int sx = 0;
		int sy = textBgY+textOffsetY+textH-btnH;
		calcBtnPos(font);
		for (int i = 0; i < btnText.length; ++i) {
			sx = textBgX+btnXOffset[i];
			DrawUtil.drawAdaptiveButton(g, btnBg, i==btnIndex?btnW:0, 0, btnW, btnH, btnBorder, 
					btnText[i], sx, sy, btnTextColor);
			if (btnW == btnBg.getWidth()) {
				DrawUtil.drawRect(g, sx, sy, btnW, btnH, 2, 0XFFFF00);
			}
		}
	}
	
	public void resetButtonIndex() {
		btnIndex = 0;
	}
	
	public int getButtonIndex() {
		return btnIndex;
	}
	
	public void prevButton() {
		btnIndex = (byte)((btnIndex+btnText.length-1)%btnText.length);
	}
	
	public void nextButton() {
		btnIndex = (byte)((btnIndex+1)%btnText.length);
	}
	
	public int popup() {
		int confirmIndex = 0;
		boolean run = true;
		KeyState keyState = engine.getKeyState();
		SGraphics g = engine.getSGraphics();
		while (run) {
			if (keyState.containsAndRemove(KeyCode.OK)) {
				confirmIndex = getButtonIndex();
				run = false;
			}
			if (keyState.containsAndRemove(KeyCode.LEFT)) {
				prevButton();
			}
			if (keyState.containsAndRemove(KeyCode.RIGHT)) {
				nextButton();
			}
			show(g);
			engine.flushGraphics();
			engine.trySleep();
		}
		return confirmIndex;
	}
	
	public void setButtonBorder(short btnBorder) {
		this.btnBorder = btnBorder;
	}
	
	public void clear() {
		textBg = null;
		btnBg = null;
	}

}
