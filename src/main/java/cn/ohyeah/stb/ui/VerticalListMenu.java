package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;

/**
 * 竖排列表菜单
 * @author maqian
 * @version 1.0
 */
public class VerticalListMenu implements IMenu{
	
	public static final byte AlignCenter = 0;
	public static final byte AlignLeft = 1;
	public static final byte AlignRight = 2;
	
	private Image menuBg;
	private short frameWidth;
	private short frameHeight;
	private byte rows;
	private byte cols;
	
	private byte hilightIndex;
	private byte align;
	
	private short itemsCoordinate[][];
	private String[] itemsText;
	private int itemNormalColor;
	private int itemHilightColor;
	
	public VerticalListMenu() {
	}
	
	public int getHilightIndex() {
		return hilightIndex;
	}
	  
	public void resetIndex() {
		hilightIndex = 0;
	}
	
	public void setMenuBgImage(Image image) {
		this.menuBg = image;
	}
	
	public void setMenuBgImage(Image image, int frameWidth, int frameHeight) {
		this.menuBg = image;
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
	
	public void setItemsText(String[] text) {
		itemsText = text;
	}
	
	public void setItemsCoordinate(short [][] coordiante) {
		itemsCoordinate = coordiante;
	}
	
	public void setAlign(byte align) {
		this.align = (byte)align;
	}
	
	public int getAlign() {
		return align;
	}
	
	public void setItemHilightColor(int color) {
		this.itemHilightColor = color;
	}
	
	public void setItemNormalColor(int color) {
		this.itemNormalColor = color;
	}
	
	public void show(SGraphics g) {
		int sx, sy;
		Font font = g.getFont();
		int col = 0, row = 0;
		for (int i = 0; i < itemsCoordinate.length; ++i) {
			if (menuBg != null){
				col = (i*2)%cols;
				row = ((i*2)/cols)%rows;
				if (i == hilightIndex) {
					++col;
				}
				g.drawRegion(menuBg, col*frameWidth, row*frameHeight, frameWidth, frameHeight, 0, itemsCoordinate[i][0], itemsCoordinate[i][1], 20);
			}
			if (itemsText != null && itemsText[i] != null) {
				if (i == hilightIndex) {
					g.setColor(itemHilightColor);
				}
				else {
					g.setColor(itemNormalColor);
				}
				if (align == AlignCenter) {
					sx = itemsCoordinate[i][0]+(frameWidth-font.stringWidth(itemsText[i]))/2;
					sy = itemsCoordinate[i][1]+(frameHeight-font.getHeight())/2;
				}
				else if (align == AlignLeft) {
					throw new RuntimeException("不支持左对齐");
				}
				else if (align == AlignRight) {
					throw new RuntimeException("不支持右对齐");
				}
				else {
					throw new RuntimeException("不支持的对齐方式, aligh="+align);
				}
				g.drawString(itemsText[i], sx, sy, 0);
			}
		}
	}

	public void prevItem() {
		hilightIndex = (byte)((hilightIndex+itemsCoordinate.length-1)%itemsCoordinate.length);
	}

	public void nextItem() {
		hilightIndex = (byte)((hilightIndex+1)%itemsCoordinate.length);
	}

	public void setHilightIndex(int index) {
		hilightIndex = (byte)(index);
	}
	
}
