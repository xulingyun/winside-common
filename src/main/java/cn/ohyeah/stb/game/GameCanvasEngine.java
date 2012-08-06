package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

import cn.ohyeah.stb.protocolv2.HeadWrapper;
import cn.ohyeah.stb.res.GamePropsManager;
import cn.ohyeah.stb.res.ResourceManager;
import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.servicev2.AbstractService;
import cn.ohyeah.stb.servicev2.FrameDecoder;
import cn.ohyeah.stb.ui.TextView;
import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

/**
 * 使用GameCanvas的游戏引擎
 * @author maqian
 * @version 1.0
 */
abstract public class GameCanvasEngine extends GameCanvas implements Runnable, IEngine{

	private static final byte STATE_USER_LOOP = 0;
	private static final byte STATE_DEBUG = 1;
	private static final byte STATE_LOADING = 2;
	private static final byte STATE_ERROR = 3;
	private static final byte STATE_LOAD_CONF = 4;
	private static final byte STATE_LOAD_PARAM = 5;
	private static final byte STATE_PLAY_LOGO = 6;
	private static final byte STATE_USER_LOGIN = 7;
	
	private static final String IMG_LOGO_OHYEAH = "/common/ohyeah.jpg";
	private static final String IMG_LOGO_CHINAGAMES = "/common/chinagames.png";
	private static final String IMG_PROGRESS1 = "/common/progress1.png";
	private static final String IMG_PROGRESS2 = "/common/progress2.png";
	private static final String IMG_LOADING_TEXT = "/common/loadingText.png";
	
	private static boolean __RELEASE = true;
	private static final int __INIT_LOOP_CIRCLE = 175;
	
	private int state;
	private int subState;
	private byte[] stateStack;
	private int stateStackPointer;
	protected NetDaemon netDaemon;
	protected MIDlet midlet;
	protected KeyState keyState;
	protected Graphics g;
	protected int screenWidth;
	protected int screenHeight;
	protected int loopCircle;
	protected boolean exit;
	protected long appStartTimeMillis;
	protected EngineService engineService;
	protected int smallFontSize, mediumFontSize, largeFontSize;
	protected DebugModule debugModule;
	private long recordMillis;
	protected int loadingProgress;
	protected String loadingMessage;
	private Image logoPic;
	private Image progressPic1;
	private Image progressPic2;
	private Image loadingTextPic;
	private String errorMessage;
	
	private long recordTime;
	private boolean timePass(int millisSeconds) {
		long curTime = System.currentTimeMillis();
		if (recordTime <= 0) {
			recordTime = curTime;
		}
		else {
			if (curTime-recordTime >= millisSeconds) {
				recordTime = 0;
				return true;
			}
		}
		return false;
	}
	
	protected GameCanvasEngine(MIDlet midlet) {
		super(false);
		this.midlet = midlet;
		loopCircle = __INIT_LOOP_CIRCLE;
		appStartTimeMillis = System.currentTimeMillis();
		setFullScreenMode(true);
		keyState = new KeyState();
		g = super.getGraphics();
		initFontSize();
		setDefaultFont();
		screenWidth = getWidth();
		screenHeight = getHeight();
		
		engineService = new EngineService(this);
		stateStack = new byte[16];
		debugModule = new DebugModule(this);
		UIResource.registerEngine(this);
		GamePropsManager.registerService(engineService);
		
		//TODO test code
		AsynServices.registerEngine(this);
		ByteBuffer heartBeat = buildHeartBeat();
		netDaemon = NetDaemon.create(heartBeat, 10000);
		AbstractService.registerAsynService(netDaemon);
		AbstractService.registerFrameDecoder(new FrameDecoder());
	}
	
	protected ByteBuffer buildHeartBeat() {
		ByteBuffer heartBeat = new ByteBuffer(8);
		HeadWrapper head = new HeadWrapper();
		head.setAckparam(1);
		heartBeat.writeInt(head.getHead());
		heartBeat.writeInt(0);
		return heartBeat;
	}
	
	protected void registerPropClasses(Class c) {
		GamePropsManager.getInstance().registerGamePropClass(c);
	}
	
	private void pushState(byte state) {
		stateStack[stateStackPointer++] = state;
	}
	
	private int popState() {
		return stateStack[--stateStackPointer];
	}
	
	public boolean isDebugMode() {
		return !__RELEASE&&debugModule.isDebugMode();
	}
	
	public boolean isReleaseVersion() {
		return __RELEASE;
	}
	
	protected void setRelease(boolean b) {
		__RELEASE = b;
	}
	
	public boolean isRunning() {
		return !exit;
	}
	
	public void setExit() {
		exit = true;
	}
	
	public void setLoopCircle(int circle) {
		loopCircle = circle;
	}
	
	public int getLoopCircle() {
		return loopCircle;
	}
	
	private void initFontSize() {
		Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		smallFontSize = font.getHeight();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		mediumFontSize = font.getHeight();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
		largeFontSize = font.getHeight();
	}
	
	public void setFont(int size) {
		Font font = null;
		if (size <= smallFontSize) {
			font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		}
		else if (size <= mediumFontSize) {
			if (size >= smallFontSize+((mediumFontSize-smallFontSize)>>1)) {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
			}
			else {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
			}
		}
		else if (size <= largeFontSize) {
			if (size >= mediumFontSize+((largeFontSize-mediumFontSize)>>1)) {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
			}
			else {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
			}
		}
		else {
			font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
		}
		g.setFont(font);
	}

	public void setDefaultFont() {
		setFont(20);
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public final void flushGraphics() {
		if (isDebugMode()) {
			debugModule.showDebugInfo(g);
		}
		super.flushGraphics();
	}
	
	public Graphics getGraphics() {
		return g;
	}

	public KeyState getKeyState() {
		return keyState;
	}
	
	public Font getFont() {
		return g.getFont();
	}
	
	public void setFont(Font font) {
		g.setFont(font);
	}
	
	public final void keyPressed(int keyCode) {
		keyState.keyPressed(keyCode);
		if (!__RELEASE) {
			debugModule.checkDebugCmd(keyCode, keyState.getKeyChar());
		}
	}
	
	public final void keyReleased(int keyCode) {
		keyState.keyReleased(keyCode);
	}

	public final void run() {
		state = STATE_LOAD_CONF;
		runLoop();
		midlet.notifyDestroyed();
	}
	
	private void runLoop() {
		recordMillis = System.currentTimeMillis();
		while (isRunning()) {
			try {
				switch (state) {
				case STATE_USER_LOOP: 
					loop();
					break;
				case STATE_LOADING:
					loading();
					break;
				case STATE_LOAD_CONF:
					loadConf();
					break;
				case STATE_LOAD_PARAM:
					loadParam();
					break;
				case STATE_PLAY_LOGO:
					playLogo();
					break;
				case STATE_USER_LOGIN:
					userLogin();
					break;
				case STATE_DEBUG:
					debug();
					break;
				case STATE_ERROR:
					error();
					break;
				default:
					throw new RuntimeException("无效的状态参数: state="+state);
				}
				flushGraphics();
				logicAfterShow();
				trySleep();
			}
			catch (Throwable t) {
				t.printStackTrace();
				System.out.println("\n程序发生异常，请记录异常发生时的上下文，并将所有异常信息提交开发人员分析, Thanks!");
				System.gc();
				errorMessage = "程序发生异常，"+t.getMessage()
							+"\n对您造成的不便深表歉意，我们会尽快修复此问题，感谢您的关注!\n请按任意键退出!";
				state = STATE_ERROR;
			}
		}
	}
	
	private void showLogo(String path, int bgColor) {
		if (logoPic == null) {
			logoPic = ResourceManager.loadImage(path);
		}
		g.setColor(bgColor);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.drawImage(logoPic, (screenWidth-logoPic.getWidth())>>1, (screenHeight-logoPic.getHeight())>>1, 20); 
	}
	
	private void userLogin() {
		if (subState == 0) {
			recordTime = System.currentTimeMillis();
			showLogo(IMG_LOGO_OHYEAH, 0);
			if (engineService.userLogin()) {
				subState = 2;
			}
			else {
				errorMessage = "用户登录失败。"+"\n";
				errorMessage += "原因： "+engineService.getLoginMessage()+"\n\n";
				errorMessage += "#R请按#Y确认\\OK键#R重试，请按#Y其他键#R退出";
				showError();
				subState = 1;
			}
		}
		if (subState == 1) {
			if (keyState.containsAndRemove(KeyCode.OK)) {
				subState = 0;
				errorMessage = "";
			}
			else {
				if (keyState.containsAnyKey()) {
					setExit();
				}
			}
		}
		if (subState == 2) {
			if (timePass(3000)) {
				state = STATE_USER_LOOP;
			}
		}
	}
	
	private void playLogo() {
		if (Configurations.getInstance().isTelcomOperatorsTelcomgd()) {
			showLogo(IMG_LOGO_CHINAGAMES, 0XC7A774);
			if (timePass(3000)) {
				state = STATE_USER_LOGIN;
				logoPic = null;
			}
		}
		else {
			state = STATE_USER_LOGIN;
		}
	}

	private void loadParam() {
		ParamManager pm = getParamManager();
		if (pm.parse()) {
			state = STATE_PLAY_LOGO;
		}
		else {
			errorMessage = "校验参数失败。"+"\n";
			errorMessage += pm.getErrorMessage()+"\n";
			errorMessage += "#R请按#Y任意键#R退出。";
			state = STATE_ERROR;
		}
	}

	private void loadConf() {
		Configurations.loadConfigurations();
		if (Configurations.isLoadConfSuccess()) {
			state = STATE_LOAD_PARAM;
		}
		else {
			errorMessage = "读取配置失败。"+"\n";
			errorMessage += "原因: "+Configurations.getErrorMessage()+"\n";
			errorMessage += "#R请按#Y任意键#R退出。";
			state = STATE_ERROR;
		}
	}

	private void loading() {
		loadingProgress = loading(loadingProgress);
		drawLoading(loadingProgress, loadingMessage);
		if (loadingProgress >= 100) {
			quitLoading();
		}
	}
	
	public void gotoLoading() {
		pushState((byte)state);
		state = STATE_LOADING;
		loadingProgress = 0;
		loadingMessage = "";
		initLoadingRes();
	}
	
	private void quitLoading() {
		clearLoadingRes();
		state = popState();
	}
	
	private void initLoadingRes() {
		if (logoPic == null) {
			logoPic = ResourceManager.loadImage(IMG_LOGO_OHYEAH);
		}
		if (progressPic1 == null) {
			progressPic1 = ResourceManager.loadImage(IMG_PROGRESS1);
		}
		if (progressPic2 == null) {
			progressPic2 = ResourceManager.loadImage(IMG_PROGRESS2);
		}
		if (loadingTextPic == null) {
			loadingTextPic = ResourceManager.loadImage(IMG_LOADING_TEXT);
		}
	}
	
	private void clearLoadingRes() {
		logoPic = null;
		progressPic1 = null;
		progressPic2 = null;
		loadingTextPic = null;
	}
	
	protected void drawLoading(int progress, String message) {
		initLoadingRes();
		g.setColor(0);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.drawImage(logoPic, 4, 4, 20);
		
		int progress1W = progressPic1.getWidth();
		int progress1H = progressPic1.getHeight();
		int progress2W = progressPic2.getWidth();
		int progress2H = progressPic2.getHeight();
		int loadingTextH = loadingTextPic.getHeight();
		
		int deltaX = (progress1W-progress2W)>>1;
		int deltaY = (progress1H-progress2H)>>1;
		int sx = (screenWidth-progress1W)>>1;
		int sy = (screenHeight>>1)+(((screenHeight>>1)-progress1H-loadingTextH-15)>>1);
		g.drawImage(loadingTextPic, sx, sy, 20);
		sy += loadingTextH+15;
		g.drawImage(progressPic1, sx, sy, 20);
		int len = progress2W*progress/100;
		g.drawRegion(progressPic2, 0, 0, len, progress2H, 0, sx+deltaX, sy+deltaY, 20);
	}
	
	protected int loading(int progress) {
		return 100;
	}
	
	public void setLoadingMessage(String msg) {
		this.loadingMessage = msg;
	}
	
	private void error() {
		if (keyState.containsAnyKey()) {
			setExit();
		}
		showError();
	}
	
	protected void showError() {
		g.setColor(0);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.setColor(0XFFFFFF);
		TextView.showMultiLineText(g, errorMessage, 1, 10, 20, screenWidth, screenHeight);
	}
	
	private void debug() {
		// TODO Auto-generated method stub
		throw new RuntimeException("暂不支持");
	}
	
	public void addDebugUserMessage(String msg) {
		if (isDebugMode()) {
			debugModule.addDebugUserMessage(msg);
		}
	}

	protected void loop() {
		String msg = "请重写loop方法，实现游戏循环";
		System.out.println(msg);
		int sx = (screenWidth-g.getFont().stringWidth(msg))/2;
		int sy = (screenHeight-g.getFont().getHeight())/2;
		g.setClip(0, 0, screenWidth, screenHeight);
		g.setColor(0);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.setColor(-1);
		g.drawString(msg, sx, sy, 0);
	}
	
	protected void logicAfterShow() {}
	

	public String getAppProperty(String key) {
		return midlet.getAppProperty(key);
	}

	public EngineService getEngineService() {
		return engineService;
	}

	public ParamManager getParamManager() {
		return engineService.getParamManager();
	}

	public ServiceWrapper getServiceWrapper() {
		return engineService.getServiceWrapper();
	}
	
	public ServiceWrapper getServiceWrapper(String server) {
		return engineService.getServiceWrapper(server);
	}

	public boolean isOffline() {
		return engineService.isOffline();
	}

	public long getAppStartMillis() {
		return appStartTimeMillis;
	}

	public void quitCurrentState() {
		state = popState();
	}

	public void trySleep() {
		this.trySleep(loopCircle);
	}
	
	public void trySleep(int milliseconds) {
		long now = System.currentTimeMillis();
		int sleepTime = (int)(milliseconds-(now-recordMillis));
		recordMillis = now;
		if (sleepTime < 0) {
			sleepTime = 0;
		}
		else if (sleepTime > milliseconds){
			sleepTime = milliseconds;
		}
		try {
			System.gc();
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
