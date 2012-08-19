package cn.ohyeah.stb.game;

import cn.ohyeah.stb.ui.TextView;

/**
 * 调试模块
 * @author maqian
 * @version 1.0
 */
public class DebugModule {
	private static final String __DEBUG_MODE_CMD = "9958";
	private static final String __ENGINEERING_MODE_CMD = "4444";
	
	//private static final byte __ENGINEERING_MODE_STATE_MAIN = 0;
	
	private int __DEBUG_MODE_CMD_POS;
	private int __ENGINEERING_MODE_CMD_POS;
	private long __DEBUG_TIME_MILLIS;
	private boolean __DEBUG_MODE;
	private boolean __ENGINEERING_MODE;
	//private int __ENGINEERING_MODE_STATE;
	private String __DEBUG_MSG;
	private String __DEBUG_USER_MSG;
	private int __DEBUG_MAX_TIME;
	private int __DEBUG_MAX_MEM;
	private int __KEY_CODE;
	private IEngine engine;
	
	public DebugModule(IEngine engine) {
		this.engine = engine;
	}
	
	public void checkDebugCmd(int keyCode, char c) {
		__KEY_CODE = keyCode;
		if (__DEBUG_MODE_CMD.charAt(__DEBUG_MODE_CMD_POS)==c) {
			if (__DEBUG_MODE_CMD_POS==__DEBUG_MODE_CMD.length()-1) {
				__DEBUG_MODE  = !__DEBUG_MODE;
				if (!__DEBUG_MODE) {
					__ENGINEERING_MODE = false;
				}
				__DEBUG_MODE_CMD_POS ^= __DEBUG_MODE_CMD_POS;
			}
			else {
				__DEBUG_MODE_CMD_POS++;
			}
		}
		else {
			__DEBUG_MODE_CMD_POS ^= __DEBUG_MODE_CMD_POS;
		}
		
		if (__DEBUG_MODE) {
			if (__ENGINEERING_MODE_CMD.charAt(__ENGINEERING_MODE_CMD_POS)==c) {
				if (__ENGINEERING_MODE_CMD_POS==__ENGINEERING_MODE_CMD.length()-1) {
					__ENGINEERING_MODE  = !__ENGINEERING_MODE;
					__ENGINEERING_MODE_CMD_POS ^= __ENGINEERING_MODE_CMD_POS;
				}
				else {
					__ENGINEERING_MODE_CMD_POS++;
				}
			}
			else {
				__ENGINEERING_MODE_CMD_POS ^= __ENGINEERING_MODE_CMD_POS;
			}
		}
	}
	
	private void calcDebugInfo() {
		long t1 = System.currentTimeMillis();
		if (__DEBUG_TIME_MILLIS == 0) {
			__DEBUG_TIME_MILLIS = t1;
		}
		int interval = (int)(t1-__DEBUG_TIME_MILLIS);
		if (interval > __DEBUG_MAX_TIME) {
			__DEBUG_MAX_TIME = interval;
		}
		__DEBUG_TIME_MILLIS = t1;
		int totalMem = (int)(Runtime.getRuntime().totalMemory()>>10);
		int freeMem = (int)(Runtime.getRuntime().freeMemory()>>10);
		int useMem = totalMem-freeMem;
		if (__DEBUG_MAX_MEM == 0) {
			__DEBUG_MAX_MEM = useMem;
		}
		if (useMem > __DEBUG_MAX_MEM) {
			__DEBUG_MAX_MEM = useMem;
		}
		__DEBUG_MSG = "KEY:"+__KEY_CODE+", MT: "+__DEBUG_MAX_TIME+"ms"+", CT: "+interval+"ms"
			+", TM: "+totalMem+"kb"+", MM: "+__DEBUG_MAX_MEM+"kb"+", CM: "+useMem+"kb\n";
	}
	
	public void showDebugInfo(SGraphics g) {
		calcDebugInfo();
		int sy = engine.getScreenHeight()-66;
		g.setColor(0);
		g.fillRect(0, sy, engine.getScreenWidth(), engine.getScreenHeight());
		g.setColor(0XFFFFFF);
		TextView.showMultiLineText(g, __DEBUG_MSG+__DEBUG_USER_MSG, 1, 0, sy, engine.getScreenWidth(), engine.getScreenHeight());
		__DEBUG_USER_MSG = null;
	}
	
	public void addDebugUserMessage(String msg) {
		if (__DEBUG_USER_MSG != null) {
			__DEBUG_USER_MSG += msg;
		}
		else {
			__DEBUG_USER_MSG = msg;
		}
	}
	
	public boolean isDebugMode() {
		return __DEBUG_MODE;
	}

	/*
	private void EngineeringModeState() {
		while (__ENGINEERING_MODE) {
			try {
				switch (__ENGINEERING_MODE_STATE) {
				case __ENGINEERING_MODE_STATE_MAIN:
					engineeringModeMainState();
					break;
				default:break;
				}
				
				super.flushGraphics();
				Thread.sleep(125);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void engineeringModeMainState() {
		showEngineeringModeMain();
	}
	
	private void showEngineeringModeMain(Graphics g) {
		//ParamManager pm = engine.getParamManager();
		//long curTimeMillis = System.currentTimeMillis();
		//String msg = "累计游戏时长 ==> "+DateUtil.getTimeStr(curTimeMillis-engine.getAppStartMillis())+"\n";
		String msg = engine.getEngineService().toString();
		g.setColor(0);
		g.fillRect(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
		g.setColor(0XFF0000);
		g.drawString("[工程模式]: 1:协议测试", 8, 8, 20);
		g.setColor(0XFFFFFF);
		TextView.showMultiLineText(g, msg, 2, 8, 30, engine.getScreenWidth()-16, engine.getScreenHeight()-38);
	}*/
}
