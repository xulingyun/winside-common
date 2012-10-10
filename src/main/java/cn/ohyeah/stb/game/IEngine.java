package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;

import cn.ohyeah.stb.key.KeyState;

/**
 *  ÒýÇæ½Ó¿Ú
 * @author maqian
 * @version 1.0
 */
public interface IEngine {
	abstract public String getAppProperty(String key);
	abstract long getAppStartMillis();
	
	abstract void quitCurrentState();
	
	abstract public EngineService getEngineService();
	abstract public ServiceWrapper getServiceWrapper();
	
	abstract public boolean isRunning();
	abstract public void trySleep();
	abstract public void trySleep(int milliseconds);
	abstract public void setExit();
	abstract public void setLoopCircle(int milliseconds);
	abstract public int getLoopCircle();
	
	abstract public int getScreenWidth();
	abstract public int getScreenHeight();
	abstract public void flushGraphics();
	abstract public SGraphics getSGraphics();
	abstract public KeyState getKeyState();
	abstract public Font getFont();
	abstract public void setFont(Font font);
	abstract public void setFont(int size, boolean isBold);
	abstract public void setDefaultFont();
	
	abstract public boolean isOffline();
	abstract public boolean isDebugMode();
	abstract public void addDebugUserMessage(String msg);

}
