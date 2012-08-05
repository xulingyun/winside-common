package cn.ohyeah.stb.res;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Image;
import cn.ohyeah.stb.util.IOUtil;

/**
 * 资源管理器
 * @author maqian
 * @version 1.0
 */
public class ResourceManager {
	private static ResourceManager instance = new ResourceManager();
	private String[] uri;
	private Object[] res;
	
	private ResourceManager(){}
	
	private ResourceManager(String[] uri) {
		this.uri = uri;
		this.res = new Object[uri.length];
	}
	
	public static ResourceManager createImageResourceManager(String[] imagePaths) {
		return new ResourceManager(imagePaths);
	}
	
	public static String loadString(String filePath, String charset) {
		InputStream is = null;
		try {
			is = instance.getClass().getResourceAsStream(filePath);
			byte[] data = IOUtil.readAllBytesOnly(is);
			return new String(data, charset);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Image loadImage(String filePath) {
		Image image = null;
		try {
			image = Image.createImage(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("加载图片("+filePath+")失败, "+e.getMessage());
		}
		return image;
	}
	
	public Image loadImage(int id) {
		if (res[id] == null) {
			res[id] = loadImage(uri[id]);
		}
		return (Image)res[id];
	}
	
	public void freeImage(int id) {
		res[id] = null;
	}
	
	public void clear() {
		if (res != null) {
			for (int i = 0; i < res.length; ++i) {
				res[i] = null;
			}
		}
	}
	
	public Image getImage(int id) {
		return (Image)res[id];
	}
	
	public void setImage(int id, Image img) {
		res[id] = img;
	}
}
