package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.IEngine;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

/**
 * ÎÄ×Öµ¯³ö¿ò
 * @author maqian
 * @version 1.0
 */
public class PopupText {
	private IEngine engine;
	
	private Image textBg;
	private short textBgX, textBgY;
	
	private String text;
	private short textOffsetX, textOffsetY;
	private short textW, textH;
	private int textColor;
	
	private Image btnBg;
	private String btnText;
	private short btnSrcX, btnSrcY;
	private short btnW, btnH;
	private short btnBorder;
	private short btnXOffset;
	private int btnTextColor;
	
	private int waitMillisSeconds;
	private long recordTime;
	
	public PopupText(IEngine engine) {
		this.engine = engine;
	}
	
	private boolean timePass(int millisSeconds) {
		long curTime = System.currentTimeMillis();
		if (recordTime <= 0) {
			recordTime = curTime;
		}
		else {
			if (curTime-recordTime >= millisSeconds) {
				recordTime = 0;
				return true;
			}
		}
		return false;
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
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public void show(SGraphics g) {
		if (textBg == null) {
			throw new RuntimeException("±³¾°Í¼Æ¬Î´ÉèÖÃ");
		}
		if (text == null) {
			throw new RuntimeException("ÎÄ×ÖÎ´ÉèÖÃ");
		}
		g.drawImage(textBg, textBgX, textBgY, 20);
		g.setColor(textColor);
		TextView.showMultiLineText(g, text, 1, textBgX+textOffsetX, textBgY+textOffsetY, textW, textH);
		
		if (btnBg != null) {
			calcBtnPos(g.getFont());
			DrawUtil.drawAdaptiveButton(g, btnBg, btnSrcX, btnSrcY, btnW, btnH, btnBorder, 
					btnText, textBgX+btnXOffset,  textBgY+textOffsetY+textH-btnH, btnTextColor);
		}
	}
	
	private void calcBtnPos(Font font) {
		if (btnXOffset == 0) {
			int btnLen = DrawUtil.calcAdaptiveButtonWidth(btnW, btnBorder, btnText, font);
			btnXOffset = (short)(textOffsetX+((textW-btnLen)>>1));
		}
	}

	public void popup() {
		boolean run = true;
		KeyState key = engine.getKeyState();
		SGraphics g = engine.getSGraphics();
		while (run) {
			if (key.containsAndRemove(KeyCode.OK)) {
				key.clear();
				recordTime = 0;
				run = false;;
			}
			if (waitMillisSeconds > 0) {
				if (timePass(waitMillisSeconds)) {
					key.clear();
					run = false;
				}
			}
			show(g);
			engine.flushGraphics();
			engine.trySleep();
		}
	}

	public void setWaitMillisSeconds(int waitMillisSeconds) {
		this.waitMillisSeconds = waitMillisSeconds;
	}
	
	public void setButtonBg(Image btnBg) {
		this.btnBg = btnBg;
		btnSrcX = 0;
		btnSrcY = 0;
		if (btnBg != null) {
			btnW = (short)btnBg.getWidth();
			btnH = (short)btnBg.getHeight();
		}
	}

	public void setButtonBg(Image btnBg, short srcX, short srcY, short w, short h) {
		this.btnBg = btnBg;
		btnSrcX = srcX;
		btnSrcY = srcY;
		btnW = w;
		btnH = h;
	}
	
	public Image getButtonBg() {
		return this.btnBg;
	}

	public void setButtonText(String btnText) {
		this.btnText = btnText;
		btnXOffset = 0;
	}

	public void setButtonSize(short w, short h) {
		btnW = w;
		btnH = h;
	}

	public void setButtonBorder(short btnBorder) {
		this.btnBorder = btnBorder;
	}

	public void setButtonTextColor(int btnTextColor) {
		this.btnTextColor = btnTextColor;
	}
	
	public void clear() {
		textBg = null;
		btnBg = null;
	}

}
