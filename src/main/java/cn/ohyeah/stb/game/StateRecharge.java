package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.Configurations;
import cn.ohyeah.stb.game.EngineService;
import cn.ohyeah.stb.res.ResourceManager;
import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.modelv2.SubscribePayType;

public class StateRecharge {
	
	private static final byte STATE_SELECT_AMOUNT = 0;
	private static final byte STATE_CONFIRM = 1;
	private static final byte STATE_INPUT_PWD = 2;
	
	private static final byte SUB_STATE_INPUT_PWD_VIEW = 0;
	private static final byte SUB_STATE_INPUT_PWD_SELECT_CHAR = 1;
	
	private static short NUM_PICS = 0;
	private static final short PIC_ID_RECHARGE_BG = NUM_PICS++;
	private static final short PIC_ID_RECHARGE_TITLE = NUM_PICS++;
	private static final short PIC_ID_EXCHANGE_TITLE = NUM_PICS++;
	private static final short PIC_ID_AMOUNTS_BG = NUM_PICS++;
	private static final short PIC_ID_CONFIRM_BG = NUM_PICS++;
	private static final short PIC_ID_CHECKED = NUM_PICS++;
	private static final short PIC_ID_UNCHECKED = NUM_PICS++;
	private static final short PIC_ID_OK0 = NUM_PICS++;
	//private static final short PIC_ID_OK1 = NUM_PICS++;
	private static final short PIC_ID_CANCEL0 = NUM_PICS++;
	//private static final short PIC_ID_CANCEL1 = NUM_PICS++;
	//private static final short PIC_ID_BACK0 = NUM_PICS++;
	private static final short PIC_ID_BACK1 = NUM_PICS++;
	private static final short PIC_ID_RECHARGE0 = NUM_PICS++;
	//private static final short PIC_ID_RECHARGE1 = NUM_PICS++;
	private static final short PIC_ID_EXCHANGE0 = NUM_PICS++;
	//private static final short PIC_ID_EXCHANGE1 = NUM_PICS++;
	private static final short PIC_ID_PASSWORD_BG = NUM_PICS++;
	
	private static final String[] imagePaths = {
		"/business/recharge-bg.jpg",
		"/business/recharge-title.png",
		"/business/exchange-title.png",
		"/business/amounts-bg.png",
		"/business/confirm-bg.png",
		"/business/checked.png",
		"/business/unchecked.png",
		"/business/ok0.png",
		//"/business/ok1.png",
		"/business/cancel0.png",
		//"/business/cancel1.png",
		//"/business/back0.png",
		"/business/back1.png",
		"/business/recharge0.png",
		//"/business/recharge1.png",
		"/business/exchange0.png",
		//"/business/exchange1.png",
		"/business/password-bg.jpg",
	};
	
	private static char[][] inputChars = {
		{'0'},
		{'1'},
		{'2', 'a', 'A', 'b', 'B', 'c', 'C'},
		{'3', 'd', 'D', 'e', 'E', 'f', 'F'},
		{'4', 'g', 'G', 'h', 'H', 'i', 'I'},
		{'5', 'j', 'J', 'k', 'K', 'l', 'L'},
		{'6', 'm', 'M', 'n', 'N', 'o', 'O'},
		{'7', 'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S'},
		{'8', 't', 'T', 'u', 'U', 'v', 'V'},
		{'9', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z'}
	};
	
	private IEngine engine;
	private EngineService engineService;
	private Configurations conf;
	private byte curPayType;
	private byte payTypeCount;
	private byte groupIndex;
	private byte payTypeIndex;
	private byte confirmIndex;
	private byte amountIndex;
	private byte state;
	private byte subState;
	private byte pwdGroupIndex;
	private byte pwdBtnIndex;
	private int rechargeAmount;
	private boolean back;
	private int[] amountList;
	private ResourceManager resource;
	private String password;
	private int pwdCharIndex;
	private char[] pwdChars;
	private int cursorFrame;
	
	
	public StateRecharge(IEngine engine) {
		this.engine = engine;
		engineService = engine.getEngineService();
		conf = Configurations.getInstance();
		amountList = engineService.getRechargeAmounts();
		resource = ResourceManager.createImageResourceManager(imagePaths);
	}
	
	public int recharge() {
		int result = 0;
		Graphics g = engine.getGraphics();
		KeyState KeyState = engine.getKeyState();
		if (engineService.isSupportSubscribeByPoints()) {
			payTypeCount = 2;
		}
		else {
			payTypeCount = 1;
		}
		groupIndex = 1;
		boolean run = true;
		try {
			while (run) {
				handle(KeyState);
				show(g);
				engine.flushGraphics();
				execute();
				
				if (back) {
					break;
				}
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
	
	public void clear() {
		resource.clear();
	}
	
	private void execute() {
		switch(state) {
		case STATE_SELECT_AMOUNT: break;
		case STATE_CONFIRM: break;
		case STATE_INPUT_PWD: 
			cursorFrame = (cursorFrame+1)%8;
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void show(Graphics g) {
		switch(state) {
		case STATE_SELECT_AMOUNT: 
			showSelectAmount(g);
			break;
		case STATE_CONFIRM: 
			showConfirm(g);
			break;
		case STATE_INPUT_PWD: 
			showInputPwd(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void gotoStatePassword() {
		state = STATE_INPUT_PWD;
		subState = SUB_STATE_INPUT_PWD_VIEW;
		pwdGroupIndex = 0;
		cursorFrame = 0;
		password = "";
	}
	
	private void drawPassword(Graphics g, int x, int y, int w, int h, boolean drawCursor, boolean drawChar) {
		g.setColor(0);
		Font font = g.getFont();
		int charW = font.charWidth('*');
		int charH = font.getHeight();
		int sx = x;
		int sy  = y + (h-charH)/2;
		int len = password.length();
		if (len > 0) {
			for (int i = 0; i < len; ++i) {
				g.drawChar('*', sx, sy, 20);
				sx += charW+2;
			}
		}
		if (drawCursor) {
			if (cursorFrame < 4) {
				g.drawLine(sx, sy, sx, sy+charH);
			}
		}
		if (drawChar) {
			g.setColor(215, 215, 215);
			g.fillRect(sx, sy, 22, charH);
			g.setColor(0);
			g.drawChar(pwdChars[pwdCharIndex], sx+(22-font.charWidth(pwdChars[pwdCharIndex]))/2, sy, 20);
		}
	}
	
	private void drawSelectChars(Graphics g, int x, int y) {
		int sx = x;
		int sy = y;
		int sl = 22;
		Font font = g.getFont();
		int deltaH = (sl-font.getHeight())/2;
		g.setColor(215, 215, 215);
		g.fillRect(sx, sy, sl*pwdChars.length, sl);
		
		for (int i = 0; i < pwdChars.length; ++i) {
			g.setColor(0);
			g.drawChar(pwdChars[i], sx+(sl-font.charWidth(pwdChars[i]))/2, sy+deltaH, 20);
			if (i == pwdCharIndex) {
				g.setColor(0XFFFF00);
				DrawUtil.drawRect(g, sx, sy, sl, sl, 2);
			}
			sx += sl;
		}
	}
	
	private void showInputPwd(Graphics g) {
		Image pwdBg = resource.loadImage(PIC_ID_PASSWORD_BG);
		int bgX = (engine.getScreenWidth()-pwdBg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-pwdBg.getHeight())>>1;
		g.drawImage(pwdBg, bgX, bgY, 20);
		
		if (subState == SUB_STATE_INPUT_PWD_VIEW) {
			if (pwdGroupIndex == 0) {
				drawPassword(g, bgX+85, bgY+97, 285, 40, true, false);
			}
			else {
				drawPassword(g, bgX+85, bgY+97, 285, 40, false, false);	
			}
		}
		else {
			drawPassword(g, bgX+85, bgY+97, 285, 40, false, true);
		}
		
		if (subState == SUB_STATE_INPUT_PWD_SELECT_CHAR) {
			drawSelectChars(g, bgX+77, bgY+140);
		}
		
		Image ok = resource.loadImage(PIC_ID_OK0);
		g.drawImage(ok, bgX+120, bgY+265, 20);
		
		Image cancel = resource.loadImage(PIC_ID_CANCEL0);
		g.drawImage(cancel, bgX+266, bgY+265, 20);
		
		if (subState == SUB_STATE_INPUT_PWD_VIEW) {
			if (pwdGroupIndex == 1) {
				if (pwdBtnIndex == 0) {
					DrawUtil.drawRect(g, bgX+120, bgY+265, ok.getWidth(), ok.getHeight(), 2, 0XFFFF00);
				}
				else {
					DrawUtil.drawRect(g, bgX+266, bgY+265, cancel.getWidth(), cancel.getHeight(), 2, 0XFFFF00);
				}
			}
		}
	}

	private void showConfirm(Graphics g) {
		Image bgImg = resource.loadImage(PIC_ID_CONFIRM_BG);
		int confirmX = (engine.getScreenWidth()-bgImg.getWidth())>>1;
		int confirmY = (engine.getScreenHeight()-bgImg.getHeight())>>1;
		g.drawImage(bgImg, confirmX, confirmY, 20);
		
		if (Configurations.getInstance().isServiceProviderWinside()) {
			g.setColor(0XFF0000);
			g.drawString("iTV体验期内订购需正常付费", confirmX+70, confirmY+138, 20);
		}
		
		String productName = "<<"+engineService.getProductName()+">>"+engineService.getRechargeCommand();
		Font font = g.getFont();
		int textDelta = (25-font.getHeight())>>1;
		int sx = confirmX+170;
		int sy = confirmY+179+textDelta;
		g.setColor(0XFFFF00);
		g.drawString(productName, sx, sy, 20);
		
		String ss = null;
		if (curPayType == 0) {
			ss = rechargeAmount*engineService.getSubscribeCashToAmountRatio()+engineService.getSubscribeAmountUnit();
		}
		else {
			ss = rechargeAmount*engineService.getCashToPointsRatio()+engineService.getPointsUnit();
		}
		sy = confirmY+216+textDelta;
		g.drawString(ss, sx, sy, 20);
		
		
		Image confirmBtn = resource.loadImage(PIC_ID_OK0);
		sx = confirmX+121;
		sy = confirmY+253;
		g.drawImage(confirmBtn, sx, sy, 20);
		if (confirmIndex == 0) {
			DrawUtil.drawRect(g, sx, sy, confirmBtn.getWidth(), confirmBtn.getHeight(), 3, 0XFF0000);
		}
		
		Image backBtn = resource.loadImage(PIC_ID_CANCEL0);
		sx = confirmX+253;
		g.drawImage(backBtn, sx, sy, 20);
		if (confirmIndex == 1) {
			DrawUtil.drawRect(g, sx, sy, confirmBtn.getWidth(), confirmBtn.getHeight(), 3, 0XFF0000);
		}
	}

	private void showSelectAmount(Graphics g) {
		g.setColor(43, 39, 36);
		g.fillRect(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
		
		Image bgImg = resource.loadImage(PIC_ID_RECHARGE_BG);
		g.drawImage(bgImg, 0, 0, 20);
		
		Font font = g.getFont();
		Image amountsBg = resource.loadImage(PIC_ID_AMOUNTS_BG);
		int amountsBgX = (engine.getScreenWidth()-amountsBg.getWidth())>>1;
		int amountsBgY = (engine.getScreenHeight()-amountsBg.getHeight())>>1;
		g.drawImage(amountsBg, amountsBgX, amountsBgY, 20);
		
		Image title = null;
		if (engineService.getRechargeCommand().equals("兑换")) {
			title = resource.loadImage(PIC_ID_EXCHANGE_TITLE);
		}
		else {
			title = resource.loadImage(PIC_ID_RECHARGE_TITLE);
		}
		
		if (title != null) {
			g.drawImage(title, 
					amountsBgX + ((amountsBg.getWidth()-title.getWidth())>>1), 
					amountsBgY + 17, 
					20);
		}
		
		/*显示支付方式*/
		g.setColor(0XFFFF00);
		int sx, sy, sw, sh;
		String ss = "支付方式:";
		sx = amountsBgX+35;
		sy = amountsBgY+61;
		g.setColor(242, 202, 0);
		g.drawString(ss, sx, sy, 20);
		sx += font.stringWidth(ss);
		sx += 8;
		Image checkedImg = resource.loadImage(PIC_ID_CHECKED);
		Image uncheckedImg = resource.loadImage(PIC_ID_UNCHECKED);
		sw = checkedImg.getWidth();
		sh = checkedImg.getHeight();
		if (curPayType == 0) {
			g.drawImage(checkedImg, sx, sy, 20);
		}
		else  {
			g.drawImage(uncheckedImg, sx, sy, 20);
		}
		if (groupIndex == 0 && payTypeIndex == 0) {
			DrawUtil.drawRect(g, sx, sy, sw, sh, 2, 0XFFFF00);
		}
		sx += sw+2;
		ss = "账单支付";
		g.setColor(242, 202, 0);
		g.drawString(ss, sx, sy, 20);
		sx += font.stringWidth(ss);
		sx += 8;
		
		if (payTypeCount > 1) {
			if (curPayType == 1) {
				g.drawImage(checkedImg, sx, sy, 20);
			}
			else  {
				g.drawImage(uncheckedImg, sx, sy, 20);
			}
			if (groupIndex == 0 && payTypeIndex == 1) {
				DrawUtil.drawRect(g, sx, sy, sw, sh, 2, 0XFFFF00);
			}
			sx += sw+2;
			ss = "积分支付";
			g.setColor(242, 202, 0);
			g.drawString(ss, sx, sy, 20);
		}
		
		/*显示金额列表*/
		
		sw = 276;
		sh = 33;
		int amountY = 0;
		String unit = "";
		int amount = 0;
		Image recharge = null;
		if (engineService.getRechargeCommand().equals("兑换")) {
			recharge = resource.loadImage(PIC_ID_EXCHANGE0);
		}
		else {
			recharge = resource.loadImage(PIC_ID_RECHARGE0);
		}
		
		int textDelta = (sh-font.getHeight())>>1;
		int btnDelta = (sh-recharge.getHeight())>>1;
		
		int btnX = amountsBgX+330;
		sx = amountsBgX+33;
		sy = amountsBgY+90;
		for (int i = 0; i < amountList.length; ++i) {
			g.setColor(20, 14, 7);
			g.fillRect(sx, sy, sw, sh);
			g.setColor(172, 166, 83);
			g.drawRect(sx, sy, sw, sh);
			
			if (curPayType == 1) {
				amount = amountList[i]*engineService.getCashToPointsRatio();
				unit = engineService.getPointsUnit();
			} else {
				amount = amountList[i]*engineService.getSubscribeCashToAmountRatio();
				unit = engineService.getSubscribeAmountUnit();
			}
			
			g.setColor(0XFFFF00);
			g.drawString(amount+unit, sx+2, sy+textDelta, 20);
			
			ss = amountList[i]*engineService.getRechargeRatio()+engineService.getExpendAmountUnit();
			g.setColor(0XFFFF00);
			g.drawString(ss, sx+sw-font.stringWidth(ss)-2, sy+textDelta, 20);
			
			g.drawImage(recharge, btnX, sy+btnDelta, 20);
			if (groupIndex == 1 && amountIndex == i) {
				amountY = sy;
			}
			sy += sh+10;
		}
		
		if (curPayType == 1) {
			sx = amountsBgX+33;
			ss = "您当前可用积分: "+engineService.getAvailablePoints();
			g.drawString(ss, sx, sy, 20);
		}
		
		Image back = resource.loadImage(PIC_ID_BACK1);
		sx = amountsBgX+((amountsBg.getWidth()-back.getWidth())>>1);
		sy = amountsBgY+284;
		g.drawImage(back, sx, sy, 20);
		
		if (groupIndex == 1) {
			DrawUtil.drawRect(g, btnX, amountY, recharge.getWidth(), recharge.getHeight(), 2, 0XFFFF00);
		}
		else if (groupIndex == 2){
			DrawUtil.drawRect(g, sx, sy, back.getWidth(), back.getHeight(), 2, 0XFFFF00);
		}
	}
	
	private void handle(KeyState key) {
		switch(state) {
		case STATE_SELECT_AMOUNT: 
			handleSelectAmount(key);
			break;
		case STATE_CONFIRM: 
			handleConfirm(key);
			break;
		case STATE_INPUT_PWD: 
			handleInputPwd(key);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleInputPwdSelectChar(KeyState key) {
		if (key.containsAndRemove(KeyCode.BACK|KeyCode.UP)) {
			subState = SUB_STATE_INPUT_PWD_VIEW;
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (pwdCharIndex > 0) {
				pwdCharIndex--;
			}
			else {
				pwdCharIndex = pwdChars.length-1;
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (pwdCharIndex < pwdChars.length-1) {
				pwdCharIndex++;
			}
			else {
				pwdCharIndex = 0;
			}
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			subState = SUB_STATE_INPUT_PWD_VIEW;
			password += pwdChars[pwdCharIndex];
		}
	}
	
	private void handleInputPwdView(KeyState key) {
		if (key.containsAndRemove(KeyCode.NUM0)) {
			key.clear();
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[0];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM1)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[1];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM2)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[2];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM3)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[3];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM4)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[4];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM5)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[5];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM6)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[6];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM7)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[7];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM8)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[8];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM9)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[9];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.BACK)) {
			if (password.length()>1) {
				password = password.substring(0, password.length()-1);
			}
			else {
				password = "";
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (pwdGroupIndex == 0) {
				pwdGroupIndex = 1;
				pwdBtnIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.UP)) {
			if (pwdGroupIndex == 1) {
				pwdGroupIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (pwdGroupIndex == 1) {
				if (pwdBtnIndex == 1) {
					pwdBtnIndex = 0;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (pwdGroupIndex == 1) {
				if (pwdBtnIndex == 0) {
					pwdBtnIndex = 1;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			if (pwdGroupIndex == 0) {
				pwdGroupIndex = 1;
				pwdBtnIndex = 0;
			}
			else {
				if (pwdBtnIndex == 0) {
					String resultMsg = "";
					PopupText pt = UIResource.getInstance().buildDefaultPopupText();
					pt.setText("正在"+engineService.getRechargeCommand()+"，请稍后...");
					pt.show(engine.getGraphics());
					engine.flushGraphics();
					AsynServices serv = AsynServices.createAsynServices();
					AsynServiceResult result = null;
					try {
						if (curPayType == 0) {
							result = serv.recharge(rechargeAmount, SubscribePayType.PAY_TYPE_BILL, 
										engineService.getProductName()
										+engineService.getRechargeCommand()
										+rechargeAmount
										+engineService.getSubscribeAmountUnit(), password);
						}
						else {
							result = serv.recharge(rechargeAmount*engineService.getCashToPointsRatio(), SubscribePayType.PAY_TYPE_POINTS, 
										engineService.getProductName()
										+engineService.getRechargeCommand()
										+rechargeAmount*engineService.getCashToPointsRatio()
										+engineService.getPointsUnit(), password);
						}
						result.get();
						if (result.isSuccessful()) {
							resultMsg = engineService.getRechargeCommand()+"成功";
						}
						else {
							resultMsg = engineService.getRechargeCommand()+"失败，原因："+result.getErrorMessage();
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						resultMsg = engineService.getRechargeCommand()+"失败, 原因: "+e.getMessage();
					}
					finally {
						pt.setText(resultMsg);
						pt.popup();
						if (result.isSuccessful()) {
							resource.freeImage(PIC_ID_PASSWORD_BG);
							state=STATE_SELECT_AMOUNT;
						}
						else {
							if (isPasswordError(result.getErrorMessage())) {
								password = "";
								pwdGroupIndex = 0;
							}
							else {
								resource.freeImage(PIC_ID_PASSWORD_BG);
								state=STATE_SELECT_AMOUNT;
							}
						}
					}
				}
				else {
					resource.freeImage(PIC_ID_PASSWORD_BG);
					state = STATE_SELECT_AMOUNT;
				}
			}
		}
	}

	private void handleInputPwd(KeyState key) {
		if (subState == SUB_STATE_INPUT_PWD_VIEW) {
			handleInputPwdView(key);
		}
		else {
			handleInputPwdSelectChar(key);
		}
	}
	
	private boolean isPasswordError(String msg) {
		boolean pwdError = false;
		if (msg.indexOf("密码")>=0) {
			if (msg.indexOf("错误") > 0
				|| msg.indexOf("校验失败")>0) {
				pwdError = true;
			}
		}
		return pwdError;
	}

	private void handleConfirm(KeyState key) {
		
		if (key.containsAndRemove(KeyCode.NUM1)) {
			gotoStatePassword();
			state = STATE_INPUT_PWD;
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (confirmIndex == 1) {
				confirmIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (confirmIndex == 0) {
				confirmIndex = 1;
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			resource.freeImage(PIC_ID_RECHARGE_BG);
			resource.freeImage(PIC_ID_OK0);
			resource.freeImage(PIC_ID_CANCEL0);
			state=STATE_SELECT_AMOUNT;
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			if (confirmIndex == 0) {
				String resultMsg = "";
				PopupText pt = UIResource.getInstance().buildDefaultPopupText();
				pt.setText("正在"+engineService.getRechargeCommand()+"，请稍后...");
				pt.show(engine.getGraphics());
				engine.flushGraphics();
				AsynServices serv = AsynServices.createAsynServices();
				AsynServiceResult result = null;
				try {
					if (curPayType == 0) {
						result = serv.recharge(rechargeAmount, SubscribePayType.PAY_TYPE_BILL, 
									engineService.getProductName()
									+engineService.getRechargeCommand()
									+rechargeAmount
									+engineService.getSubscribeAmountUnit(), "");
					}
					else {
						result = serv.recharge(rechargeAmount*engineService.getCashToPointsRatio(), SubscribePayType.PAY_TYPE_POINTS, 
									engineService.getProductName()
									+engineService.getRechargeCommand()
									+rechargeAmount*engineService.getCashToPointsRatio()
									+engineService.getPointsUnit(), "");
					}
					result.get();
					if (result.isSuccessful()) {
						resultMsg = engineService.getRechargeCommand()+"成功";
					}
					else {
						resultMsg = engineService.getRechargeCommand()+"失败，原因："+result.getErrorMessage();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					resultMsg = engineService.getRechargeCommand()+"失败, 原因: "+e.getMessage();
				}
				finally {
					if (result.isSuccessful()) {
						pt.setText(resultMsg);
						pt.popup();
						resource.freeImage(PIC_ID_CONFIRM_BG);
						state=STATE_SELECT_AMOUNT;
					}
					else {
						if (isPasswordError(result.getErrorMessage())) {
							gotoStatePassword();
						}
						else {
							pt.setText(resultMsg);
							pt.popup();
							resource.freeImage(PIC_ID_CONFIRM_BG);
							state=STATE_SELECT_AMOUNT;
						}
					}
				}
			}
			else {
				resource.freeImage(PIC_ID_CONFIRM_BG);
				state=STATE_SELECT_AMOUNT;
			}
		}
	}

	private void handleSelectAmount(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (groupIndex == 1){
				if (amountIndex > 0) {
					--amountIndex;
				}
				else {
					groupIndex = 0;
				}
			}
			else if (groupIndex == 2){
				groupIndex = 1;
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (groupIndex == 0) {
				groupIndex = 1;
			}
			else if (groupIndex == 1) {
				if (amountIndex < amountList.length-1) {
					++amountIndex;
				}
				else {
					groupIndex = 2;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (groupIndex == 0) {
				if (payTypeCount > 1) {
					payTypeIndex = (byte)((payTypeIndex+1)%payTypeCount);
				}
			}
			else if (groupIndex == 1) {
				groupIndex = 2;
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (groupIndex == 0) {
				if (payTypeCount > 1) {
					payTypeIndex = (byte)((payTypeIndex+payTypeCount-1)%payTypeCount);
				}
			}
			else if (groupIndex == 2) {
				groupIndex = 1;
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			clear();
			back = true;
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			if (groupIndex == 0) {
				if (payTypeCount > 1) {
					curPayType = payTypeIndex;
				}
			}
			else if (groupIndex == 1) {
				if (engineService.isSupportRecharge()) {
					rechargeAmount = amountList[amountIndex];
					boolean canRecharge = true;
					if (curPayType == 1) {
						if (engineService.getAvailablePoints() < rechargeAmount*engineService.getCashToPointsRatio()) {
							PopupText pt = UIResource.getInstance().buildDefaultPopupText();
							pt.setText("您的积分不足，无法使用积分兑换");
							pt.popup();
							canRecharge = false;
						}
					}
					if (canRecharge) {
						resource.freeImage(PIC_ID_AMOUNTS_BG);
						state = STATE_CONFIRM;
						if (conf.isSubscribeFocusOk()) {
							confirmIndex = 0;
						}
						else {
							confirmIndex = 1;
						}
					}
				}
				else {
					PopupText pt = UIResource.getInstance().buildDefaultPopupText();
					pt.setText("游戏内不支持充值"+engineService.getExpendAmountUnit()+"，请返回大厅充值");
					pt.popup();
				}
			}
			else {
				back = true;
				resource.freeImage(PIC_ID_RECHARGE_BG);
			}
		}
	}
}
