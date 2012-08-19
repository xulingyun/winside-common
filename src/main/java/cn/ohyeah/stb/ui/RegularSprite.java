package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import cn.ohyeah.stb.game.SGraphics;

/**
 * 规则的精灵类
 * @author maqian
 * @version 1.0
 */
public class RegularSprite implements ISprite {
	private Image image;
	private short frameWidth;
	private short frameHeight;
	private byte rows;
	private byte cols;
	private byte frameIndex;
	private byte []frameSequence;
	private int transform;
	private short x, y;
	
	public RegularSprite() {
		transform = Sprite.TRANS_NONE;
	}

	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public void setFrameIndex(int frameIndex) {
		this.frameIndex = (byte)frameIndex;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setFrameSequence(byte[] sequence) {
		this.frameSequence = sequence;
	}

	public void setImage(Image image, int frameWidth, int frameHeight) {
		this.image = image;
		this.frameWidth = (short)frameWidth;
		this.frameHeight = (short)frameHeight;
		if (image.getWidth()%frameWidth != 0) {
			throw new RuntimeException("图片宽度不能整除");
		}
		else {
			cols = (byte)(image.getWidth()/frameWidth);
		}
		if (image.getHeight()%frameHeight != 0) {
			throw new RuntimeException("图片高度不能整除");
		}
		else {
			rows = (byte)(image.getHeight()/frameHeight);
		}
	}

	public void nextFrame() {
		frameIndex = (byte)(++frameIndex%frameSequence.length);
	}

	public void prevFrame() {
		frameIndex = (byte)((frameIndex+frameSequence.length-1)%frameSequence.length);
	}

	public void show(SGraphics g, int x, int y) {
		int col = frameSequence[frameIndex]%cols;
		int row = frameSequence[frameIndex]/cols;
		g.drawRegion(image, col*frameWidth, row*frameHeight, frameWidth, frameHeight, transform, x, y, 20);
	}

	public void setTransformation(int transformation) {
		this.transform = transformation;
	}
	
	public void setTransNone() {
		this.transform = Sprite.TRANS_NONE;
	}
	
	public void setTransMirrorHorizontal() {
		this.transform = Sprite.TRANS_MIRROR;
	}
	
	public void setTransMirrorVertical() {
		this.transform = Sprite.TRANS_MIRROR_ROT180;
	}

	public void show(SGraphics g) {
		show(g, x, y);
	}

	public void setX(int x) {
		this.x = (short)x;
	}

	public void setY(int y) {
		this.y = (short)y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public void setPosition(int x, int y) {
		this.x = (short)x;
		this.y = (short)y;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

}
