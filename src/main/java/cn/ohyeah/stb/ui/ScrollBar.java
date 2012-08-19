package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import cn.ohyeah.stb.game.SGraphics;

/**
 * 滚动条
 * @author maqian
 * @version 1.0
 */
public class ScrollBar {
	public static final byte SCROLL_UP_DOWN = 0;
	public static final byte SCROLL_LEFT_RIGHT = 1;
	
	private Image image;
	private short[][] frameRegion;
	private short scrollX, scrollY;
	private short scrollW, scrollH;
	private short scrollBarLen;
	private short viewLen;
	private short contentLen;
	private short curPage;
	private short totalPage;
	private byte scrollType;
	
	public void show(SGraphics g) {
		if (scrollBarLen == 0) {
			calculate();
		}
		
		int spos = 0;
		/*上下滚动*/
		if (scrollType == SCROLL_UP_DOWN) {
			if (totalPage > 1) {
				spos = scrollY+(curPage)*(scrollH-scrollBarLen)/(totalPage-1);
			}
			else {
				spos = scrollY;
			}
			if (scrollBarLen > (frameRegion[0][3]<<1)) {
				//上半部分
				g.drawRegion(image, frameRegion[0][0], frameRegion[0][1], frameRegion[0][2], frameRegion[0][3], 
						0, scrollX, spos, 20);
				spos += frameRegion[0][3];
				
				//中间部分
				int midLen = scrollBarLen-(frameRegion[0][3]<<1);
				while (midLen > 0) {
					if (midLen > frameRegion[1][3]) {
						g.drawRegion(image, frameRegion[1][0], frameRegion[1][1], frameRegion[1][2], frameRegion[1][3], 
								0, scrollX, spos, 20);
						spos += frameRegion[1][3];
						midLen -= frameRegion[1][3];
					}
					else {
						g.drawRegion(image, frameRegion[1][0], frameRegion[1][1], frameRegion[1][2], midLen, 
								0, scrollX, spos, 20);
						spos += midLen;
						midLen -= midLen;
					}
				}
				
				//下半部分
				g.drawRegion(image, frameRegion[2][0], frameRegion[2][1], frameRegion[2][2], frameRegion[2][3], 
						0, scrollX, spos, 20);
			}
			else {
				int halfLen = (scrollBarLen>>1);
				//上半部分
				g.drawRegion(image, frameRegion[0][0], frameRegion[0][1], frameRegion[0][2], halfLen, 
						0, scrollX, spos, 20);
				spos += halfLen;
				
				//下半部分
				g.drawRegion(image, frameRegion[2][0], frameRegion[2][1], frameRegion[2][2], scrollBarLen-halfLen, 
						0, scrollX, spos, 20);
			}
		}
		else {//左右滚动
			if (totalPage > 1) {
				spos = scrollX+(curPage)*(scrollW-scrollBarLen)/(totalPage-1);
			}
			else {
				spos = scrollX;
			}
			if (scrollBarLen > (frameRegion[0][3]<<1)) {
				//左半部分
				g.drawRegion(image, frameRegion[0][0], frameRegion[0][1], frameRegion[0][2], frameRegion[0][3], 
						Sprite.TRANS_ROT270, spos, scrollY, 20);
				spos += frameRegion[0][3];
				
				//中间部分
				int midLen = scrollBarLen-(frameRegion[0][3]<<1);
				while (midLen > 0) {
					if (midLen > frameRegion[1][3]) {
						g.drawRegion(image, frameRegion[1][0], frameRegion[1][1], frameRegion[1][2], frameRegion[1][3], 
								Sprite.TRANS_ROT270, spos, scrollY, 20);
						spos += frameRegion[1][3];
						midLen -= frameRegion[1][3];
					}
					else {
						g.drawRegion(image, frameRegion[1][0], frameRegion[1][1], frameRegion[1][2], midLen, 
								Sprite.TRANS_ROT270, spos, scrollY, 20);
						spos += midLen;
						midLen -= midLen;
					}
				}
				
				//右半部分
				g.drawRegion(image, frameRegion[2][0], frameRegion[2][1], frameRegion[2][2], frameRegion[2][3], 
						Sprite.TRANS_ROT270, spos, scrollY, 20);
			}
			else {
				int halfLen = (scrollBarLen>>1);
				//上半部分
				g.drawRegion(image, frameRegion[0][0], frameRegion[0][1], frameRegion[0][2], halfLen, 
						Sprite.TRANS_ROT270, spos, scrollY, 20);
				spos += halfLen;
				
				//下半部分
				g.drawRegion(image, frameRegion[2][0], frameRegion[2][1], frameRegion[2][2], scrollBarLen-halfLen, 
						Sprite.TRANS_ROT270, spos, scrollY, 20);
			}
		}

	}
	
	public void calculate() {
		if (viewLen > contentLen) {
			if (scrollType == SCROLL_UP_DOWN) {
				scrollBarLen = scrollH;
			}
			else {
				scrollBarLen = scrollW;
			}
		}
		else {
			if (scrollType == SCROLL_UP_DOWN) {
				scrollBarLen = (short)(scrollH*viewLen/contentLen);
			}
			else {
				scrollBarLen = (short)(scrollW*viewLen/contentLen);
			}
		}
	}
	
	public void prevPage() {
		if (curPage > 0) {
			--curPage;
		}
	}
	
	public void nextPage() {
		if (curPage < totalPage) {
			++curPage;
		}
	}
	
	public void setImage(Image image) {
		this.image = image;
		if (image.getWidth() > image.getHeight()) {
			throw new RuntimeException("请使用竖直的滚动条");
		}
	}
	
	public void setScrollType(byte type) {
		this.scrollType = type;
	}

	public void setFrameRegion(short[][] frameRegion) {
		this.frameRegion = frameRegion;
	}

	public void setViewLen(int viewLen) {
		this.viewLen = (short)viewLen;
	}
	
	public void setContentLen(int contentLen) {
		this.contentLen = (short)contentLen;
	}
	
	public void setCurPage(int curPage) {
		this.curPage = (short)curPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = (short)totalPage;
	}
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setPosition(int x, int y) {
		this.scrollX = (short)x;
		this.scrollY = (short)y;
	}
	
	public void setSize(int w, int h) {
		this.scrollW = (short)w;
		this.scrollH = (short)h;
	}

}
