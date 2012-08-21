package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class SGraphics {
	private final Graphics g;
	private final int offsetX;
	private final int offsetY;
	SGraphics(Graphics g, int offsetX, int offsetY) {
		this.g = g;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public void clipRect(int x, int y, int width, int height) {
		g.clipRect(x+offsetX, y+offsetY, width, height);
	}
	public void copyArea(int x_src, int y_src, int width, int height,
			int x_dest, int y_dest, int anchor) {
		// TODO Auto-generated method stub
		g.copyArea(x_src, y_src, width, height, x_dest, y_dest, anchor);
	}
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		// TODO Auto-generated method stub
		g.drawArc(x, y, width, height, startAngle, arcAngle);
	}
	public void drawChar(char character, int x, int y, int anchor) {
		// TODO Auto-generated method stub
		g.drawChar(character, x, y, anchor);
	}
	public void drawChars(char[] data, int offset, int length, int x, int y,
			int anchor) {
		// TODO Auto-generated method stub
		g.drawChars(data, offset, length, x, y, anchor);
	}
	public void drawImage(Image img, int x, int y, int anchor) {
		g.drawImage(img, x+offsetX, y+offsetY, anchor);
	}
	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(x1+offsetX, y1+offsetY, x2+offsetX, y2+offsetY);
	}
	public void drawRGB(int[] rgbData, int offset, int scanlength, int x,
			int y, int width, int height, boolean processAlpha) {
		// TODO Auto-generated method stub
		g.drawRGB(rgbData, offset, scanlength, x, y, width, height, processAlpha);
	}
	public void drawRect(int x, int y, int width, int height) {
		g.drawRect(x+offsetX, y+offsetY, width, height);
	}
	public void drawRegion(Image src, int x_src, int y_src, int width,
			int height, int transform, int x_dest, int y_dest, int anchor) {
		g.drawRegion(src, x_src, y_src, width, height, transform, x_dest+offsetX, y_dest+offsetY,
				anchor);
	}
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		g.drawRoundRect(x+offsetX, y+offsetY, width, height, arcWidth, arcHeight);
	}
	public void drawString(String str, int x, int y, int anchor) {
		g.drawString(str, x+offsetX, y+offsetY, anchor);
	}
	public void drawSubstring(String str, int offset, int len, int x, int y,
			int anchor) {
		g.drawSubstring(str, offset, len, x+offsetX, y+offsetY, anchor);
	}
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		g.fillArc(x+offsetX, y+offsetY, width, height, startAngle, arcAngle);
	}
	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(x+offsetX, y+offsetY, width, height);
	}
	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		g.fillRoundRect(x+offsetX, y+offsetY, width, height, arcWidth, arcHeight);
	}
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		g.fillTriangle(x1+offsetX, y1+offsetY, x2+offsetX, y2+offsetY, x3+offsetX, y3+offsetY);
	}
	public int getBlueComponent() {
		// TODO Auto-generated method stub
		return g.getBlueComponent();
	}
	public int getClipHeight() {
		// TODO Auto-generated method stub
		return g.getClipHeight();
	}
	public int getClipWidth() {
		// TODO Auto-generated method stub
		return g.getClipWidth();
	}
	public int getClipX() {
		// TODO Auto-generated method stub
		return g.getClipX();
	}
	public int getClipY() {
		// TODO Auto-generated method stub
		return g.getClipY();
	}
	public int getColor() {
		// TODO Auto-generated method stub
		return g.getColor();
	}
	public int getDisplayColor(int color) {
		// TODO Auto-generated method stub
		return g.getDisplayColor(color);
	}
	public Font getFont() {
		// TODO Auto-generated method stub
		return g.getFont();
	}
	public int getGrayScale() {
		// TODO Auto-generated method stub
		return g.getGrayScale();
	}
	public int getGreenComponent() {
		// TODO Auto-generated method stub
		return g.getGreenComponent();
	}
	public int getRedComponent() {
		// TODO Auto-generated method stub
		return g.getRedComponent();
	}
	public int getStrokeStyle() {
		// TODO Auto-generated method stub
		return g.getStrokeStyle();
	}
	public int getTranslateX() {
		// TODO Auto-generated method stub
		return g.getTranslateX();
	}
	public int getTranslateY() {
		// TODO Auto-generated method stub
		return g.getTranslateY();
	}
	public void setClip(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		g.setClip(x+offsetX, y+offsetY, width, height);
	}
	public void setColor(int red, int green, int blue) {
		// TODO Auto-generated method stub
		g.setColor(red, green, blue);
	}
	public void setColor(int RGB) {
		// TODO Auto-generated method stub
		g.setColor(RGB);
	}
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		g.setFont(font);
	}
	public void setGrayScale(int value) {
		// TODO Auto-generated method stub
		g.setGrayScale(value);
	}
	public void setStrokeStyle(int style) {
		// TODO Auto-generated method stub
		g.setStrokeStyle(style);
	}
	public void translate(int x, int y) {
		// TODO Auto-generated method stub
		g.translate(x, y);
	}

	
}
