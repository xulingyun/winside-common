package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

/**
 * 文字输入框，目前仅支持数字
 * @author maqian
 * @version 1.0
 */
public class TextBox {
	private short bgX, bgY;
	private short bgW, bgH;
	private Image textBg;
	private int overideBgColor;
	private int overideTextColor;
	private int textColor;
	private Image cursor;
	private int cursorColor;
	private short cursorFrame;
	private short cursorOffset;
	private short charLen;
	private int maxValue;
	private int value;
	private boolean insertMode;
	private boolean disabled;
	private char[] textBuffer;
	
	public void setTextBgPos(int bgX, int bgY) {
		this.bgX = (short)bgX;
		this.bgY = (short)bgY;
	}
	
	public void setTextBgSize(int bgW, int bgH) {
		this.bgW = (short)bgW;
		this.bgH = (short)bgH;
	}
	
	public void setTextBgImage(Image textBg) {
		this.textBg = textBg;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public void setCursorImage(Image cursor) {
		this.cursor = cursor;
	}

	public void setCursorColor(int cursorColor) {
		this.cursorColor = cursorColor;
	}
	
	public void clearTextBuffer() {
		charLen = 0;
		cursorOffset = 0;
	}
	
	public void setValueMax() {
		String maxvStr = Integer.toString(maxValue);
		maxvStr.getChars(0, maxvStr.length(), textBuffer, 0);
		charLen = (short)maxvStr.length();
		value = maxValue;
	}
	
	private boolean canInsertChar() {
		return !insertMode||(charLen < textBuffer.length);
	}
	
	private void insertChar(char c) {
		if (insertMode) {
			if (cursorOffset != charLen) {
				for (int i = charLen; i > cursorOffset; --i) {
					textBuffer[i] = textBuffer[i-1];
				}
			}
			textBuffer[cursorOffset] = c;
			++charLen;
			calcValue();
		}
		else {
			textBuffer[0] = c;
			charLen = 1;
			cursorOffset = 0;
			insertMode = true;
			calcValue();
		}
		if (value <= maxValue) {
			++cursorOffset;
		}
		else {
			setValueMax();
			cursorOffset = charLen;
		}
	}
	
	public boolean canCursorMoveLeft() {
		if (!insertMode || (cursorOffset>0)) {
			return true;
		}
		return false;
	}
	
	public void cursorMoveLeft() {
		if (insertMode) {
			--cursorOffset;
		}
		else {
			cursorOffset = 0;
			insertMode = true;
		}
	}
	
	public boolean canCursorMoveRight() {
		if (!insertMode || (cursorOffset<charLen)) {
			return true;
		}
		return false;
	}
	
	public void cursorMoveRight() {
		if (insertMode) {
			++cursorOffset;
		}
		else {
			cursorOffset = charLen;
			insertMode = true;
		}
	}
	
	public void calcValue() {
		if (charLen == 0) {
			value = 0;
		}
		else {
			value = Integer.parseInt(String.valueOf(textBuffer, 0, charLen));
		}
	}
	
	public void setOverideMode() {
		insertMode = false;
	}

	public void show(SGraphics g) {
		if (charLen == 0) {
			insertMode = true;
		}
		if (textBg != null) {
			g.drawImage(textBg, bgX, bgY, 20);
		}
		Font font = g.getFont();
		int fontH = font.getHeight();
		int deltaH = (bgH-fontH)>>1;
		int textW = 0;
		if (charLen > 0) {
			textW = font.charsWidth(textBuffer, 0, charLen);
		}
		else {
			textW = font.charWidth('0');
		}
		int sx = bgX+((bgW-textW)>>1);
		int sy = bgY+deltaH;
		if (!insertMode) {
			g.setColor(overideBgColor);
			g.fillRect(sx, sy, textW, fontH);
			g.setColor(overideTextColor);
		}
		else {
			g.setColor(textColor);
		}
		if (insertMode&&!disabled) {
			for (int i = 0; i < charLen; ++i) {
				g.drawChars(textBuffer, 0, charLen, sx, sy, 20);
			}
		}
		else {
			if (charLen > 0) {
				for (int i = 0; i < charLen; ++i) {
					g.drawChars(textBuffer, 0, charLen, sx, sy, 20);
				}
			}
			else {
				g.drawChar('0', sx, sy, 20);
			}
		}
		
		int cursorX = sx;
		for (int i = 0; i < cursorOffset; ++i) {
			cursorX += font.charWidth(textBuffer[i]);
		}
		
		/*显示光标*/
		if (insertMode && !disabled) {
			if (cursorFrame % 8 < 4) {
				if (cursor != null) {
					g.drawImage(cursor, cursorX, sy, 20);
				}
				else {
					g.setColor(cursorColor);
					g.drawLine(cursorX, sy-2, cursorX, sy+fontH+2);
					g.drawLine(cursorX+1, sy-2, cursorX+1, sy+fontH+2);
				}
			}
			cursorFrame = (short)((cursorFrame+1)%8);
		}
	}
	
	public void handle(KeyState keyState) {
		if (charLen == 0) {
			insertMode = true;
		}
		if (keyState.contains(KeyCode.LEFT)) {
			if (canCursorMoveLeft()) {
				keyState.remove(KeyCode.LEFT);
				cursorMoveLeft();
			}
		}
		if (keyState.contains(KeyCode.RIGHT)) {
			if (canCursorMoveRight()) {
				keyState.remove(KeyCode.RIGHT);
				cursorMoveRight();
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM0)) {
			if (canInsertChar()) {
				insertChar('0');
			}
		}
		
		if (keyState.containsAndRemove(KeyCode.BACK|KeyCode.ASTERISK)) {
			removeChar();
		}
		
		if (keyState.containsAndRemove(KeyCode.NUM1)) {
			if (canInsertChar()) {
				insertChar('1');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM2)) {
			if (canInsertChar()) {
				insertChar('2');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM3)) {
			if (canInsertChar()) {
				insertChar('3');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM4)) {
			if (canInsertChar()) {
				insertChar('4');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM5)) {
			if (canInsertChar()) {
				insertChar('5');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM6)) {
			if (canInsertChar()) {
				insertChar('6');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM7)) {
			if (canInsertChar()) {
				insertChar('7');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM8)) {
			if (canInsertChar()) {
				insertChar('8');
			}
		}
		if (keyState.containsAndRemove(KeyCode.NUM9)) {
			if (canInsertChar()) {
				insertChar('9');
			}
		}
	}

	private void removeChar() {
		if (!insertMode) {
			charLen = 0;
			cursorOffset = 0;
			value = 0;
			insertMode = true;
		}
		else {
			if (charLen>0 && cursorOffset>0) {
				for (int i = cursorOffset; i < charLen; ++i) {
					textBuffer[i-1] = textBuffer[i];
				}
				--charLen;
				--cursorOffset;
				calcValue();
			}
		}
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		String maxvStr = Integer.toString(maxValue);
		if (textBuffer==null || textBuffer.length!=maxvStr.length()) {
			textBuffer = new char[maxvStr.length()];
		}
		charLen = 0;
		cursorOffset = 0;
		value = 0;
	}

	public void setInsertMode(boolean insertMode) {
		this.insertMode = insertMode;
	}

	public void setOverideBgColor(int overideBgColor) {
		this.overideBgColor = overideBgColor;
	}

	public void setOverideTextColor(int overideTextColor) {
		this.overideTextColor = overideTextColor;
	}

	public int getValue() {
		return value;
	}

	public void setDisabled(boolean disabled) {
		if (this.disabled && !disabled) {
			insertMode = false;
		}
		this.disabled = disabled;
		
	}

	public boolean isDisabled() {
		return disabled;
	}
}
