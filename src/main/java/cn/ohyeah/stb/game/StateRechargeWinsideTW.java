package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.res.ResourceManager;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.TextView;

/**
 * 掌世界天威电信充值界面
 * @author Administrator
 *
 */
public class StateRechargeWinsideTW {
	
	private static short NUM_PICS = 0;
	private static final short PIC_ID_RECHARGE_BG = NUM_PICS++;
	private static final short PIC_ID_RECHARGE_TITLE = NUM_PICS++;
	private static final short PIC_ID_OK0 = NUM_PICS++;
	private static final short PIC_ID_CANCEL0 = NUM_PICS++;
	private static final short PIC_ID_SELECT_BTN = NUM_PICS++;
	
	private static final String[] imagePaths = {
		"/business/recharge-bg.jpg",
		"/business/recharge-title.png",
		"/business/ok0.png",
		"/business/cancel0.png",
		"/business/select_btn.png",
	};
	
	private String info="游戏充值为天威游戏增值业务，产生的费用均为真实费用，费用" +
						"会在次日通过和高清机顶盒智能卡关联的银行卡中扣除";
	private String info2 = "按确定键进行充值，返回键退出充值";
	private String info3 = "天威高清游戏每日充值上限100元整";
	
	private IEngine engine;
	private EngineService engineService;
	private Configurations conf;
	private ResourceManager resource;
	
	private byte state;
	
	private static final byte STATE_SELECT_AMOUNT = 0;
	private static final byte STATE_CONFIRM = 1;
	
	private int[] amountList;
	
	public StateRechargeWinsideTW(IEngine engine) {
		this.engine = engine;
		engineService = engine.getEngineService();
		conf = Configurations.getInstance();
		amountList = engineService.getRechargeAmounts();
		resource = ResourceManager.createImageResourceManager(imagePaths);
	}

	boolean run = true;
	public int recharge() {
		int result = 0;
		SGraphics g = engine.getSGraphics();
		KeyState KeyState = engine.getKeyState();
		try {
			while (run) {
				handle(KeyState);
				show(g);
				engine.flushGraphics();
				//execute();
				engine.trySleep();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		finally {
			clear();
		}
		return result;
	}
	
	private void handle(KeyState key) {
		switch (state){
		case STATE_SELECT_AMOUNT:
			handleSelectAmount(key);
			break;
		case STATE_CONFIRM:
			handleConfirm(key);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleConfirm(KeyState key) {
		
	}

	private void handleSelectAmount(KeyState key) {
		if(key.containsAndRemove(KeyCode.NUM0) || key.containsAndRemove(KeyCode.BACK)){
			run = false;
		}
	}

	private void show(SGraphics g) {
		switch (state){
		case STATE_SELECT_AMOUNT:
			showSelectAmount(g);
			break;
		case STATE_CONFIRM:
			showConfirm(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void showConfirm(SGraphics g) {
		// TODO Auto-generated method stub
		
	}

	private void showSelectAmount(SGraphics g) {
		Image bg = resource.loadImage(PIC_ID_RECHARGE_BG);
		Image title = resource.loadImage(PIC_ID_RECHARGE_TITLE);
		Image select_btn = resource.loadImage(PIC_ID_SELECT_BTN);
		g.drawImage(bg, 0, 0, 20);
		g.drawImage(title, 265, 125, 20);
		
		String subscribeUnit = engineService.getSubscribeAmountUnit();
		String unit = engineService.getExpendAmountUnit();
		g.setColor(0x000000);
		engine.setFont(20, true);
		String s1 = "请选择充值金额：";
		String s2 = "一"+subscribeUnit+"=10"+unit;
		g.drawString(s1, 107, 160, 20);
		g.setColor(0xffff00);
		g.drawString(s2, 418, 160, 20);
		
		int offX = 107, offY = 205;
		int btnW = select_btn.getWidth()/2, btnH = select_btn.getHeight();
		for(int i = 0; i < amountList.length; ++i){
			int bx = offX+btnW*i+10, by = offY;
			g.drawRegion(select_btn, 0, 0, btnW, btnH, 0, bx, by, 20);
			
			bx += 10;
			by += 5;
			g.drawString(amountList[i]+subscribeUnit, bx, by, 20);
		}
		
		g.setColor(0x000000);
		TextView.showMultiLineText(g, info, 2, 107, 240, 425, 56);
		g.drawString(info3, 160, 335, 20);
		
		engine.setFont(30, true);
		g.drawString(info2, 150, 305, 20);
		engine.setDefaultFont();
		g.setColor(0xffffff);
		
		
	}

	public void clear() {
		resource.clear();
	}
}
