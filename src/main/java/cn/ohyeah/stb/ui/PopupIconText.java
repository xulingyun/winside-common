package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.IEngine;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

/**
 * Í¼Æ¬ÎÄ×Öµ¯³ö¿ò
 * @author maqian
 * @version 1.0
 */
public class PopupIconText {
	private IEngine engine;
	
	private Image textBg;
	private short textBgX, textBgY;
	
	private Image icon;
	private short iconX, iconY;
	private String iconDescText;
	private short iconDescTextX, iconDescTextY;
	
	private String text;
	private short textOffsetX, textOffsetY;
	private short textW, textH;
	private int textColor;
	
	private Image btnBg;
	private String btnText;
	private short btnX, btnY;
	private short btnSrcX, btnSrcY;
	private short btnW, btnH;
	private short btnBorder;
	private int btnTextColor;
	
	private int waitMillisSeconds;
	private long recordTime;
	
	public PopupIconText(IEngine engine) {
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
	
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	public void setIconPos(int x, int y) {
		iconX = (short)x;
		iconY = (short)y;
	}
	
	public void setIconDescText(String text) {
		iconDescText = text;
	}
	
	public void setTextBgImage(Image textBgImage) {
		this.textBg = textBgImage;
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
		if (icon == null) {
			throw new RuntimeException("Í¼±êÎ´ÉèÖÃ");
		}
		g.drawImage(textBg, textBgX, textBgY, 20);
		g.drawImage(icon, textBgX+iconX, textBgY+iconY, 20);
		g.setColor(textColor);
		int iconW = icon.getWidth();
		Font font = g.getFont();
		iconDescTextX = (short)(iconX+((iconW-font.stringWidth(iconDescText))>>1));
		g.drawString(iconDescText, textBgX+iconDescTextX, textBgY+iconDescTextY, 20);
		TextView.showMultiLineText(g, text, 1, textBgX+textOffsetX, textBgY+textOffsetY, textW, textH);
		drawBtn(g);
	}
	
	private void drawBtn(SGraphics g) {
		if (btnText != null) {
			Font font = g.getFont();
			int fontH = font.getHeight();
			int sx, sy;
			int textW;
			int btnSw;
			int btnMidW = btnW-btnBorder-btnBorder;
			int btnFillW = btnW-btnBorder;
			
			textW = font.stringWidth(btnText);
			btnSw = textW+(btnBorder<<1);
			if (btnSw < btnW) {
				btnSw = btnW;
			}
			btnX = (short)((textBg.getWidth()-btnSw)>>1);
			sx = textBgX+btnX;
			sy = textBgY+btnY;
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
			sx = textBgX+btnX+((btnSw-textW)>>1);
			sy = textBgY+btnY+((btnH-fontH)>>1);
			g.setColor(btnTextColor);
			g.drawString(btnText, sx, sy, 20);
		}
		else {
			btnX = (short)((textBg.getWidth()-btnW)>>1);
			g.drawRegion(btnBg, btnSrcX, btnSrcY, btnW, btnH, 0, 
					textBgX+btnX, textBgY+btnY, 20);
		}
	}
	
	public void popup() {
		try {
			boolean run = true;
			KeyState key = engine.getKeyState();
			SGraphics g = engine.getSGraphics();
			while (run) {
				if (key.containsAndRemove(KeyCode.OK)) {
					key.clear();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setWaitMillisSeconds(int waitMillisSeconds) {
		this.waitMillisSeconds = waitMillisSeconds;
	}
	
	public void setIconDescTextPos(short x, short y) {
		this.iconDescTextX = x;
		this.iconDescTextY = y;
	}
	
	public void setButtonBgImage(Image btnBg, short srcX, short srcY, short w, short h) {
		this.btnBg = btnBg;
		btnSrcX = srcX;
		btnSrcY = srcY;
		btnW = w;
		btnH = h;
	}
	
	public void setButtonText(String btnText) {
		this.btnText = btnText;
	}
	
	public void setButtonPos(short x, short y) {
		btnX = x;
		btnY = y;
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
}
