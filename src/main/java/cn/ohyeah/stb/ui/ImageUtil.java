package cn.ohyeah.stb.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * 图像处理工具类
 * @author maqian
 * @version 1.0
 */
public class ImageUtil {
	/**
	 * 逐行绘制灰度变换图像（J2ME中只支持PNG格式图片），内存不够时调用次方法，绘制效率偏低
	 * @param g
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param destX 显示的X坐标
	 * @param destY 显示的Y坐标
	 */
	public static void drawGrayByLine(Graphics g, Image pic, 
			int srcX, int srcY, int w, int h, int destX, int destY) {
		int []rgbData = new int[w];
		for (int hd = h-1; hd>=0; --hd) {
			pic.getRGB(rgbData, 0, w, srcX, srcY+hd, w, 1);
			for (int i = rgbData.length-1; i>=0; --i) {
		        rgbData[i] = RenderUtil.turnGray(rgbData[i]);
			}
			g.drawRGB(rgbData, 0, w, destX, destY+hd, w, 1, true);
		}
	}
	
	/**
	 * 绘制灰度变换图像（J2ME中只支持PNG格式图片），内存空间足够时调用此方法
	 * @param g
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param destX 显示的X坐标
	 * @param destY 显示的Y坐标
	 */
	public static void drawGray(Graphics g, Image pic, 
			int srcX, int srcY, int w, int h, int destX, int destY) {
		int []rgbData = new int[w*h];
		Image grayImg;
		pic.getRGB(rgbData, 0, w, srcX, srcY, w, h);
		for (int i = rgbData.length-1; i>=0; --i) {
			rgbData[i] = RenderUtil.turnGray(rgbData[i]);
		}
		grayImg = Image.createRGBImage(rgbData, w, h, true);
		g.drawImage(grayImg, destX, destY, 0);
	}
	
	/**
	 * 创建灰度图像（J2ME中只支持PNG格式图片），内存空间足够时调用此方法
	 * @param pic 源图片
	 * @param srcX 处理块在源图片中的X偏移
	 * @param srcY 处理块在源图片中的Y偏移
	 * @param w 处理块宽度
	 * @param h 处理块高度
	 * @return
	 */
	public static Image createGray(Image pic, int srcX, int srcY, int w, int h) {
		int []rgbData = new int[w*h];
		pic.getRGB(rgbData, 0, w, srcX, srcY, w, h);
		for (int i = rgbData.length-1; i>=0; --i) {
			rgbData[i] = RenderUtil.turnGray(rgbData[i]);
		}
		return Image.createRGBImage(rgbData, w, h, true);
	}

	/**
	 * 逐行绘制透明变换图像（J2ME中只支持PNG格式图片），内存不够时调用次方法，绘制效率偏低
	 * @param g
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param destX 显示的X坐标
	 * @param destY 显示的Y坐标
	 * @param alpha alpha通道值
	 */
	public static void drawTransparenceByLine(Graphics g, Image pic, 
			int srcX, int srcY, int w, int h, int destX, int destY, int alpha) {

		int []rgbData = new int[w];
		if (alpha < 0 || alpha > 255) {
			return;
		}
		for (int hd = h-1; hd>=0; --hd) {
			pic.getRGB(rgbData, 0, w, srcX, srcY+hd, w, 1);
			for (int i = rgbData.length-1; i>=0; --i) {
				rgbData[i] = RenderUtil.turnTransparence(rgbData[i], alpha);
			}
			g.drawRGB(rgbData, 0, w, destX, destY+hd, w, 1, true);
		}
	}
	/**
	 * 绘制透明变换图像（J2ME中只支持PNG格式图片），内存空间足够时调用此方法
	 * @param g
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param destX 显示的X坐标
	 * @param destY 显示的Y坐标
	 * @param alpha alpha通道值
	 */
	public static void drawTransparence(Graphics g, Image pic, 
			int srcX, int srcY, int w, int h, int destX, int destY, int alpha) {
		
		int []rgbData = new int[w*h];
		Image transparenceImg;
		if (alpha < 0 || alpha > 255) {
			return;
		}
		pic.getRGB(rgbData, 0, w, srcX, srcY, w, h);
		for (int i = rgbData.length-1; i>=0; --i) {
			rgbData[i] = RenderUtil.turnTransparence(rgbData[i], alpha);
		}
		transparenceImg = Image.createRGBImage(rgbData, w, h, true);
		g.drawImage(transparenceImg, destX, destY, 0);
	}
	
	/**
	 * 创建透明变换图像（J2ME中只支持PNG格式图片），内存空间足够时调用此方法
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param alpha alpha通道值
	 * @return
	 */
	public static Image createTransparence(Image pic, 
			int srcX, int srcY, int w, int h, int alpha) {

		int []rgbData = new int[w*h];
		if (alpha < 0 || alpha > 255) {
			return null;
		}
		pic.getRGB(rgbData, 0, w, srcX, srcY, w, h);
		for (int i = rgbData.length-1; i>=0; --i) {
			rgbData[i] = RenderUtil.turnTransparence(rgbData[i], alpha);
		}
		return Image.createRGBImage(rgbData, w, h, true);
	}
	/**
	 * 逐行绘制亮度变换图像（J2ME中只支持PNG格式图片），内存不够时调用次方法，绘制效率偏低
	 * @param g
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param destX 显示的X坐标
	 * @param destY 显示的Y坐标
	 * @param delta
	 */
	public static void drawBrightByLine(Graphics g, Image pic, 
			int srcX, int srcY, int w, int h, int destX, int destY, int delta) {

		int []rgbData = new int[w];
		if (delta == 0) {
			return;
		}
		for (int hd = h-1; hd >= 0; --hd) {
			pic.getRGB(rgbData, 0, w, srcX, srcY+hd, w, 1);
			for (int i = rgbData.length-1; i>=0; --i) {
				rgbData[i] = RenderUtil.turnBright(rgbData[i], delta);
			}
			g.drawRGB(rgbData, 0, w, destX, destY+hd, w, 1, true);
		}
	}
	/**
	 * 绘制亮度变换图像（J2ME中只支持PNG格式图片），内存空间足够时调用此方法
	 * @param g
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param destX 显示的X坐标
	 * @param destY 显示的Y坐标
	 * @param delta
	 */
	public static void drawBright(Graphics g, Image pic, 
			int srcX, int srcY, int w, int h, int destX, int destY, int delta) {
		
		int []rgbData = new int[w*h];
		Image brightImg;
		if (delta == 0) {
			return;
		}
		pic.getRGB(rgbData, 0, w, srcX, srcY, w, h);
		for (int i = rgbData.length-1; i>=0; --i) {
			rgbData[i] = RenderUtil.turnBright(rgbData[i], delta);
		}
		brightImg = Image.createRGBImage(rgbData, w, h, true);
		g.drawImage(brightImg, destX, destY, 0);
	}
	/**
	 * 创建亮度变换图像（J2ME中只支持PNG格式图片），内存空间足够时调用此方法
	 * @param pic
	 * @param pic 源图片
	 * @param srcX 绘制块在源图片的X偏移
	 * @param srcY 绘制块在源图片的Y偏移
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param destX 显示的X坐标
	 * @param destY 显示的Y坐标
	 * @param delta
	 * @return
	 */
	public static Image createBright(Image pic, 
			int srcX, int srcY, int w, int h, int destX, int destY, int delta) {
		
		int []rgbData = new int[w*h];
		if (delta == 0) {
			return null;
		}
		pic.getRGB(rgbData, 0, w, srcX, srcY, w, h);
		for (int i = rgbData.length-1; i>=0; --i) {
			rgbData[i] = RenderUtil.turnBright(rgbData[i], delta);
		}
		return Image.createRGBImage(rgbData, w, h, true);
	}
	
	/**
	 * 图片缩放（J2ME中只支持PNG格式图片）
	 * @param pic 源图片
	 * @param srcX 处理块在源图片的X偏移
	 * @param srcY 处理块在源图片的Y偏移
	 * @param w 处理块的宽度
	 * @param h 处理块的高度
	 * @param desW 变换后的图片宽度
	 * @param desH 变换后的图片高度
	 * @return
	 */
	public static Image zoomImage(Image pic, int srcX, int srcY, int w, int h, int desW, int desH) {  
        // 计算插值表  
        int[] tabY = new int[desH];  
        int[] tabX = new int[desW];  
  
        int sb = 0;  
        int db = 0;  
        int tems = 0;  
        int temd = 0;  
        int distance = h > desH ? h : desH;  
        for (int i = 0; i <= distance; i++) { /* 垂直方向 */  
            tabY[db] = sb;  
            tems += h;  
            temd += desH;  
            if (tems > distance) {
                tems -= distance;  
                sb++;  
            }
            if (temd > distance) {
                temd -= distance;  
                db++;  
            }
        }
  
        sb = 0;  
        db = 0;  
        tems = 0;  
        temd = 0;  
        distance = w > desW ? w : desW;  
        for (int i = 0; i <= distance; i++) { /* 水平方向 */  
            tabX[db] = (short) sb;  
            tems += w;  
            temd += desW;  
            if (tems > distance) {
                tems -= distance;  
                sb++;  
            }
            if (temd > distance) {
                temd -= distance;  
                db++;  
            }
        }
  
        // 生成放大缩小后图形像素buf  
        int[] srcBuf = new int[w * h];
        pic.getRGB(srcBuf, 0, w, srcX, srcY, w, h);  
        int[] desBuf = new int[desW * desH];  
        int dx = 0;  
        int dy = 0;  
        int sy = 0;  
        int oldy = -1;  
        for (int i = 0; i < desH; i++) {
            if (oldy == tabY[i]) {
                System.arraycopy(desBuf, dy - desW, desBuf, dy, desW);  
            } else {
                dx = 0;  
                for (int j = 0; j < desW; j++) {  
                    desBuf[dy + dx] = srcBuf[sy + tabX[j]];  
                    dx++;  
                }  
                sy += (tabY[i] - oldy) * w;  
            }
            oldy = tabY[i];  
            dy += desW;  
        }
  
        return Image.createRGBImage(desBuf, desW, desH, true);  
    } 
}
