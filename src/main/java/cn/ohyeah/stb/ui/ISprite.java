package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;

/**
 * 简单精灵接口
 * @author maqian
 * @version 1.0
 */
public interface ISprite {
	public void setTransformation(int transformation);
	public void setFrameIndex(int frameIndex);
	public int getFrameIndex();
	public int getFrameWidth();
	public int getFrameHeight();
	public void setFrameSequence(byte []sequence);
	public void setImage(Image image, int frameWidth, int frameHeight);
	public void nextFrame();
	public void prevFrame();
	public void show(SGraphics g, int x, int y);
	public void show(SGraphics g);
	public void setX(int x);
	public void setY(int y);
	public void setPosition(int x, int y);
	public int getX();
	public int getY();
	public void move(int dx, int dy);
}
