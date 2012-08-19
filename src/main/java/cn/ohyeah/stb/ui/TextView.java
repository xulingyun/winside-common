package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Font;

import cn.ohyeah.stb.game.SGraphics;

/**
 * 文字显示工具
 * 
 * 特效命令：<br/>
 * #R 红色<br/>
 * #G 绿色<br/>
 * #B 蓝色<br/>
 * #K 黑色<br/>
 * #Y 黄色<br/>
 * #W 白色<br/>
 * #cFF00FF 其他自定义颜色值<br/>
 * #b 闪烁字<br/>
 * #a 坐标锚定<br/>
 * #x123E 相对锚定点的X偏移,E为数字结束字符<br/>
 * #n 恢复<br/>
 * #r 换行<br/>
 * ## 输出#号<br/>
 * @author maqian
 * @version 1.0
 */
public class TextView {
	public static final byte STYLE_NORMAL = 0;			/*无特殊样式*/
	public static final byte STYLE_ALIGN_CENTER = 1;	/*居中对齐*/
	public static final byte STYLE_ALIGN_RIGHT = 2;		/*右对齐*/
	public static final byte STYLE_SCROLL = 3;				/*循环滚动*/
	public static final byte STYLE_SCROLL_LEFT_RIGHT = 4; 	/*左右来回滚动*/
	
	private static final byte EFFECT_CMD_NONE = 0;
	private static final byte EFFECT_CMD_COLOR = 1;
	private static final byte EFFECT_CMD_BLINK = 2;
	private static final byte EFFECT_CMD_RESUME = 3;
	private static final byte EFFECT_CMD_NEW_LINE = 4;
	private static final byte EFFECT_CMD_NEW_CHAR = 5;
	private static final byte EFFECT_CMD_ANCHOR_ALL = 6;
	private static final byte EFFECT_CMD_ANCHOR_X = 7;
	private static final byte EFFECT_CMD_ANCHOR_Y = 8;
	private static final byte EFFECT_CMD_SKIP_ALL = 9;
	private static final byte EFFECT_CMD_SKIP_X = 10;
	private static final byte EFFECT_CMD_SKIP_Y = 11;
	
	private int currentColor = -1;
	private int destColor = -1;
	
	private static TextView textView = new TextView();
	
	private void showText(SGraphics g, String text, int gap, int x, int y, int w, int h, boolean isSingleLine) {
		if (text == null) {
			return;
		}
		int originalColor = g.getColor();
		Font font = g.getFont();
		int textLen = text.length();
		int charX = x, charY = y;
		int charW = 0, charH = font.getHeight();
		int charsWidth = 0;
		int scanPos = 0;
		char scanChar = ' ';
		char[] scanStr = new char[64];
		int scanStrLen = 0;
		int anchorX = x, anchorY = y;
		while (scanPos<textLen) {
			scanChar = text.charAt(scanPos);
			++scanPos;
			if ('#' == scanChar) {
				int[] result = parseEffectCmd(text, scanPos);
				if (result[0] == EFFECT_CMD_COLOR){
					if (scanStrLen > 0) {
						g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
						scanStrLen = 0;
						charX = x+charsWidth;
					}
					g.setColor(result[1]);
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_BLINK) {
					if (scanStrLen > 0) {
						g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
						scanStrLen = 0;
						charX = x+charsWidth;
					}
					scanPos += result[2];
					
					if (destColor == -1) {
						currentColor = g.getColor();
						destColor = 0;
					}
					
					if (destColor == 0) {
						currentColor = RenderUtil.turnBright(currentColor, -16, destColor);
						if ((currentColor&0XFFFFFF) == (destColor&0XFFFFFF)) {
							destColor = g.getColor();
						}
					}
					else {
						currentColor = RenderUtil.turnBright(currentColor, 16, destColor);
						if ((currentColor&0XFFFFFF) == (destColor&0XFFFFFF)) {
							destColor = 0;
						}
					}
					g.setColor(currentColor);
				}
				else if (result[0] == EFFECT_CMD_RESUME) {
					if (scanStrLen > 0) {
						g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
						scanStrLen = 0;
						charX = x+charsWidth;
					}
					g.setColor(originalColor);
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_NEW_LINE) {
					if (scanStrLen > 0) {
						g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
						scanStrLen = 0;
					}
					charsWidth = 0;
					charX = x;
					charY += (charH+gap);
					scanPos += result[2];
					if (isSingleLine) {
						return;
					}
				}
				else if (result[0] == EFFECT_CMD_NEW_CHAR) {
					scanStr[scanStrLen++] = '#';
					charW = font.charWidth('#');
					charsWidth += charW;
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_SKIP_X) {
					if (scanStrLen > 0) {
						g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
						scanStrLen = 0;
						charX = x+charsWidth;
					}
					if (anchorX+result[1] < w) {
						charsWidth = result[1]+(anchorX-x);
						charX = anchorX + result[1];
					}
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_SKIP_Y) {
					if (scanStrLen > 0) {
						g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
						scanStrLen = 0;
						charX = x+charsWidth;
					}
					if (anchorY+result[1] < h) {
						charY = anchorY + result[1];
					}
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_SKIP_ALL) {
					if (scanStrLen > 0) {
						g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
						scanStrLen = 0;
						charX = x+charsWidth;
					}
					if (anchorX+result[1] < w && anchorY+result[1] < h) {
						charsWidth = result[1]+(anchorX-x);
						charX = anchorX + result[1];
						charY = anchorY + result[1];
					}
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_ANCHOR_X) {
					anchorX = charX;
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_ANCHOR_Y) {
					anchorY = charY;
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_ANCHOR_ALL) {
					anchorX = charX;
					anchorY = charY;
					scanPos += result[2];
				}
				else if (result[0] == EFFECT_CMD_NONE) {
					scanStr[scanStrLen++] = scanChar;
					charW = font.charWidth(scanChar);
					charsWidth += charW;
				}
			}
			else if ('\r' == scanChar) {
				//null
			}
			else if ('\n' == scanChar) {
				if (scanStrLen > 0) {
					g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
					scanStrLen = 0;
				}
				charsWidth = 0;
				charX = x;
				charY += (charH+gap);
				if (isSingleLine) {
					return;
				}
			}
			else {
				scanStr[scanStrLen++] = scanChar;
				charW = font.charWidth(scanChar);
				charsWidth += charW;
				
				if (scanStrLen >= scanStr.length) {
					g.drawString(new String(scanStr), charX, charY, 20);
					scanStrLen = 0;
					charX = charsWidth;
				}
			}
			
			if (charsWidth>=w) {
				if (charsWidth > w) {
					g.drawString(new String(scanStr, 0, scanStrLen-1), charX, charY, 20);
					scanStrLen = 0;
					scanStr[scanStrLen++] = scanChar;;
					charsWidth = charW;
					charX = x;
					charY += (charH+gap);
				}
				else {
					g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
					scanStrLen = 0;
					charsWidth = 0;
					charX = x;
					charY += (charH+gap);
				}
				if (isSingleLine) {
					return;
				}
			}
			
			if (charY >= y+h) {
				break;
			}
		}
			
		if (scanPos >= textLen) {
			if (scanStrLen > 0) {
				g.drawString(new String(scanStr, 0, scanStrLen), charX, charY, 20);
			}
		}
	}
	
	private int[] parseEffectCmd(String text, int scanPos) {
		char scanChar = text.charAt(scanPos);
		int cmd = EFFECT_CMD_NONE;
		int info = 0;
		int scanLen = 0;
		int pos = 0;
		switch (scanChar) {
		case 'R': 
			scanLen = 1;
			cmd = EFFECT_CMD_COLOR;
			info = 0XFF0000;
			break;
		case 'G': 
			scanLen = 1;
			cmd = EFFECT_CMD_COLOR;
			info = 0XFF00;
			break;
		case 'B': 
			scanLen = 1;
			cmd = EFFECT_CMD_COLOR;
			info = 0XFF;
			break;
		case 'K': 
			scanLen = 1;
			cmd = EFFECT_CMD_COLOR;
			info = 0;
			break;
		case 'Y': 
			scanLen = 1;
			cmd = EFFECT_CMD_COLOR;
			info = 0XFFFF00;
			break;
		case 'W': 
			scanLen = 1;
			cmd = EFFECT_CMD_COLOR;
			info = 0XFFFFFF;
			break;
		case 'a':	/*锚定*/
			scanChar = text.charAt(scanPos+1);
			if (scanChar == 'a') {
				scanLen = 2;
				cmd = EFFECT_CMD_ANCHOR_ALL;
			}
			else if (scanChar == 'x') {
				scanLen = 2;
				cmd = EFFECT_CMD_ANCHOR_X;
			}
			else if (scanChar == 'y') {
				scanLen = 2;
				cmd = EFFECT_CMD_ANCHOR_Y;
			}
			else {
				scanLen = 0;
				cmd = EFFECT_CMD_NONE;
			}
			break;
		case 's':
			scanChar = text.charAt(scanPos+1);
			if (scanChar == 'a') {
				scanLen = 2;
				cmd = EFFECT_CMD_ANCHOR_ALL;
				pos = text.indexOf('E', scanPos+2);	/*结束标示*/
				info = Integer.parseInt(text.substring(scanPos+2, pos));
				scanLen = pos-scanPos+2;
			}
			else if (scanChar == 'x') {
				scanLen = 2;
				cmd = EFFECT_CMD_ANCHOR_X;
				pos = text.indexOf('E', scanPos+2);	/*结束标示*/
				info = Integer.parseInt(text.substring(scanPos+2, pos));
				scanLen = pos-scanPos+2;
			}
			else if (scanChar == 'y') {
				scanLen = 2;
				cmd = EFFECT_CMD_ANCHOR_Y;
				pos = text.indexOf('E', scanPos+2);	/*结束标示*/
				info = Integer.parseInt(text.substring(scanPos+2, pos));
				scanLen = pos-scanPos+2;
			}
			else {
				scanLen = 0;
				cmd = EFFECT_CMD_NONE;
			}
			break;
		case 'b': 	/*闪烁字*/
			scanLen = 1;
			cmd = EFFECT_CMD_BLINK;
			break;
		case 'c': 
			scanLen = 1+6;
			cmd = EFFECT_CMD_COLOR;
			info = Integer.parseInt(text.substring(scanPos+1, scanPos+1+6), 16);
			break;
		case 'n':	/*恢复*/
			scanLen = 1;
			cmd = EFFECT_CMD_RESUME;
			break;
		case 'r':	/*换行*/
			scanLen = 1;
			cmd = EFFECT_CMD_NEW_LINE;
			break;
		case '#':	/*输出#号*/
			scanLen = 1;
			cmd = EFFECT_CMD_NEW_CHAR;
			info = '#';
			break;
		default: 
			scanLen = 0;
			cmd = EFFECT_CMD_NONE;
			break;
		}
		return new int[]{cmd, info, scanLen};
	}
	
	public void showSingleLine(SGraphics g, String text, int x, int y, int w, int h, int style) {
		Font font = g.getFont();
		int sx = 0, sy = 0;
		
		switch (style) {
		case STYLE_NORMAL:
			sx = x;
			sy = y;
			w = Integer.MAX_VALUE>>1;
			h = Integer.MAX_VALUE>>1;
			showText(g, text, 0, sx, sy, w, h, true);
			break;
		case STYLE_ALIGN_CENTER: 
			sx = x+((w-font.stringWidth(text))>>1);
			sy = y+((h-font.getHeight())>>1);
			showText(g, text, 0, sx, sy, w, h, true);
			break;
		case STYLE_ALIGN_RIGHT: 
			sx = x+w-font.stringWidth(text);
			sy = y+((h-font.getHeight())>>1);
			showText(g, text, 0, sx, sy, w, h, true);
			break;
		case STYLE_SCROLL: 
			//TODO
			throw new RuntimeException("暂不支持");
		case STYLE_SCROLL_LEFT_RIGHT: 
			//TODO
			throw new RuntimeException("暂不支持");
		default: throw new RuntimeException("不支持的样式, style="+style);
		}
		
	}
	
	public void showSingleLine(SGraphics g, String text, int x, int y) {
		showSingleLine(g, text, x, y, 0, 0, STYLE_NORMAL);
	}
	
	public void showMultiLine(SGraphics g, String text, int gap, int x, int y, int w, int h) {
		showText(g, text, gap, x, y, w, h, false);
	}
	
	public static void showSingleLineText(SGraphics g, String text, int x, int y) {
		showSingleLineText(g, text, x, y, 0, 0, STYLE_NORMAL);
	}
	
	public static void showSingleLineText(SGraphics g, String text, int x, int y, int w, int h, int style) {
		textView.showSingleLine(g, text, x, y, w, h, style);
	}
	
	public static void showMultiLineText(SGraphics g, String text, int gap, int x, int y, int w, int h) {
		textView.showMultiLine(g, text, gap, x, y, w, h);
	}

}
