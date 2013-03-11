package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.itvgame.model.SubscribePayType;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.res.ResourceManager;
import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.TextView;

/**
 * 掌世界天威充值界面
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
	private static final short PIC_ID_SUCCESS = NUM_PICS++;
	private static final short PIC_ID_FAIL = NUM_PICS++;
	
	private static final String[] imagePaths = {
		"/business/recharge-bg.jpg",
		"/business/recharge-title.png",
		"/business/ok0.png",
		"/business/cancel0.png",
		"/business/select-btn.png",
		"/business/success.png",
		"/business/fail.png",
	};
	
	private String info="游戏充值为天威游戏增值业务，产生的费用均为真实费用，费用" +
						"会在次日通过和高清机顶盒智能卡关联的银行卡中扣除";
	private String info2 = "按确定键进行充值，返回键退出充值";
	private String info3 = "天威高清游戏每日充值上限100元整";
	private String info4 = "对应的金额将在您的智能卡";
	private String info5 = "关联银行账号扣除，请确定充值";
	
	private IEngine engine;
	private EngineService engineService;
	private Configurations conf;
	private ResourceManager resource;
	private int amount;
	private String resultMsg;
	private int result = -1;
	
	private byte state;
	
	private static final byte STATE_SELECT_AMOUNT = 0;
	private static final byte STATE_CONFIRM = 1;
	private static final byte STATE_RECHARGE_RESULT = 2;
	
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
		case STATE_RECHARGE_RESULT:
			handleRechargeResult(key);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleRechargeResult(KeyState key) {
		if(key.containsAndRemove(KeyCode.OK)){
			//run = false;
			state = STATE_SELECT_AMOUNT;
			clear();
		}
	}

	private void handleConfirm(KeyState key) {
		if(key.containsAndRemove(KeyCode.NUM0)||key.containsAndRemove(KeyCode.BACK)){
			state = STATE_SELECT_AMOUNT;
		}else if(key.containsAndRemove(KeyCode.RIGHT)){
			confirmIndex = 1;
		}else if(key.containsAndRemove(KeyCode.LEFT)){
			confirmIndex = 0;
		}else if(key.containsAndRemove(KeyCode.OK)){
			if(confirmIndex==0){
				PopupText pt = UIResource.getInstance().buildDefaultPopupText();
				pt.setText("正在"+engineService.getRechargeCommand()+"，请稍后...");
				pt.show(engine.getSGraphics());
				engine.flushGraphics();
				ServiceWrapper sw = engine.getServiceWrapper();
				try {
					sw.recharge(amount*engineService.getCashToPointsRatio(), SubscribePayType.PAY_TYPE_POINTS, 
								engineService.getProductName()
								+engineService.getRechargeCommand()
								+amount*engineService.getCashToPointsRatio()    
								+engineService.getPointsUnit(), "");
					if (sw.isServiceSuccessful()) {
						resultMsg = "成功充值"+amount+"元，获得"+amount*engineService.getSubscribeCashToAmountRatio()+
						"个"+engineService.getExpendAmountUnit()+",获得"+engineService.getExpendAmountUnit()
						+"稍后将进入你的账号内，请注意查收";
					}
					else {
						resultMsg = sw.getServiceMessage();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					resultMsg = e.getMessage();
				}
				finally {
					state = STATE_RECHARGE_RESULT;
					result = sw.getServiceResult();
					clear();
				}
			}else{
				state = STATE_SELECT_AMOUNT;
				clear();
			}
		}
	}

	private int amountIndex, index, groupIndex;
	private void handleSelectAmount(KeyState key) {
		if(key.containsAndRemove(KeyCode.NUM0) || key.containsAndRemove(KeyCode.BACK)){
			run = false;
			clear();
		}else if(key.containsAndRemove(KeyCode.UP)){
			groupIndex = 0;
		}else if(key.containsAndRemove(KeyCode.DOWN)){
			groupIndex = 1;
		}else if(key.containsAndRemove(KeyCode.RIGHT)){
			if(groupIndex==0){
				if(amountIndex<amountList.length-1){
					amountIndex ++;
				}
			}else{
				index=1;
			}
		}else if(key.containsAndRemove(KeyCode.LEFT)){
			if(groupIndex==0){
				if(amountIndex>0){
					amountIndex --;
				}
			}else{
				index=0;
			}
			
		}else if(key.containsAndRemove(KeyCode.OK)){
			if(index==1){
				run = false;
			}else{
				amount = amountList[amountIndex];
				state = STATE_CONFIRM;
				if(conf.isSubscribeFocusOk()){
					confirmIndex=0;
				}else{
					confirmIndex=1;
				}
			}
			clear();
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
		case STATE_RECHARGE_RESULT:
			showRechargeResult(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	private void showRechargeResult(SGraphics g) {
		Image bg = resource.loadImage(PIC_ID_RECHARGE_BG);
		Image ok = resource.loadImage(PIC_ID_OK0);
		Image resultImg = null;
		if(result==0){
			resultImg = resource.loadImage(PIC_ID_SUCCESS);
		}else{
			resultImg = resource.loadImage(PIC_ID_FAIL);
		}
		g.drawImage(bg, 0, 0, 20);
		g.drawImage(resultImg, 230, 125, 20);
		g.drawImage(ok, 275, 375, 20);
		g.setColor(0x000000);
		engine.setFont(30, true);
		TextView.showMultiLineText(g, resultMsg, 2, 107, 210, 425, 56);
		engine.setDefaultFont();
	}

	private int confirmIndex;
	private void showConfirm(SGraphics g) {
		Image bg = resource.loadImage(PIC_ID_RECHARGE_BG);
		Image title = resource.loadImage(PIC_ID_RECHARGE_TITLE);
		Image ok = resource.loadImage(PIC_ID_OK0);
		Image cancel = resource.loadImage(PIC_ID_CANCEL0);
		g.drawImage(bg, 0, 0, 20);
		g.drawImage(title, 265, 125, 20);
		g.drawImage(ok, 142, 375, 20);
		g.drawImage(cancel, 415, 375, 20);
		g.setColor(0xffff00);
		if(confirmIndex==0){
			DrawUtil.drawRect(g, 142, 375, ok.getWidth(), ok.getHeight(), 2);
		}else{
			DrawUtil.drawRect(g, 415, 375, ok.getWidth(), ok.getHeight(), 2);
		}
		g.setColor(0x000000);
		engine.setFont(23, true);
		String str = "充值"+amount+engineService.getSubscribeAmountUnit()+","
					 +info4+"("+engineService.getUserId()+")"+info5;
		TextView.showMultiLineText(g, str, 2, 107, 210, 425, 90);
		engine.setDefaultFont();
	}

	private void showSelectAmount(SGraphics g) {
		Image bg = resource.loadImage(PIC_ID_RECHARGE_BG);
		Image title = resource.loadImage(PIC_ID_RECHARGE_TITLE);
		Image select_btn = resource.loadImage(PIC_ID_SELECT_BTN);
		Image ok = resource.loadImage(PIC_ID_OK0);
		Image cancel = resource.loadImage(PIC_ID_CANCEL0);
		g.drawImage(bg, 0, 0, 20);
		g.drawImage(title, 265, 125, 20);
		
		String subscribeUnit = engineService.getSubscribeAmountUnit();
		String unit = engineService.getExpendAmountUnit();
		g.setColor(0x000000);
		engine.setFont(20, true);
		String s1 = "请选择充值金额：";
		String s2 = "1"+subscribeUnit+"=10"+unit;
		g.drawString(s1, 107, 160, 20);
		g.setColor(0xffff00);
		g.drawString(s2, 418, 160, 20);
		
		int offX = 117, offY = 200;
		int btnW = select_btn.getWidth()/2, btnH = select_btn.getHeight();
		for(int i = 0; i < amountList.length; ++i){
			int bx = offX+(btnW+2)*i, by = offY;
			if(amountIndex==i){
				g.drawRegion(select_btn, btnW, 0, btnW, btnH, 0, bx, by, 20);
			}else{
				g.drawRegion(select_btn, 0, 0, btnW, btnH, 0, bx, by, 20);
			}
			
			String amount = amountList[i]+subscribeUnit;
			Font font = g.getFont();
			
			bx = bx+btnW/2-font.stringWidth(amount)/2;
			by += 7;
			g.drawString(amount, bx, by, 20);
		}
		
		g.setColor(0x000000);
		engine.setFont(12, true);
		TextView.showMultiLineText(g, info, 2, 107, 240, 425, 56);
		//g.drawString(info3, 190, 335, 20);
		TextView.showSingleLineText(g, info3, 107, 335, 425, 25, 1);
		
		engine.setFont(23, true);
		//g.drawString(info2, 140, 305, 20);
		TextView.showSingleLineText(g, info2, 107, 305, 425, 30, 1);
		engine.setDefaultFont();
		g.setColor(0xffffff);
		
		g.drawImage(ok, offX+35, 375, 20);
		g.drawImage(cancel, 415, 375, 20);
		if(groupIndex==1){
			g.setColor(0xffff00);
			if(index==0){
				DrawUtil.drawRect(g, offX+35, 375, ok.getWidth(), ok.getHeight(), 2);
			}else{
				DrawUtil.drawRect(g, 415, 375, ok.getWidth(), ok.getHeight(), 2);
			}
			g.setColor(0xffffff);
		}
	}

	public void clear() {
		resource.clear();
	}
}
