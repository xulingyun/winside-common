package cn.ohyeah.stb.game;

/**
 * 参数管理类，加载校验jad参数
 * @author maqian
 * @version 1.0
 */
final class ParamManager {
	public static final String ENTRANCE_OHYEAH = "ohyeah";
	public static final String ENTRANCE_THE9 = "the9";
	public static final String ENTRANCE_WINSIDE = "winside";
	public static final String ENTRANCE_WINSIDEGD = "winsidegd";
	
	
	int[] rechargeAmounts;	/*充值金额列表*/
	
	String stbType;			/*中游特需参数*/
	String enterURL;		/*中游特需参数*/
	String zyUserToken;		/*中游特需参数*/
	String myDXScore;		/*中游特需参数*/
	String hosturl;			/*中游特需参数, 收藏参数*/
	String code;			/*中游特需参数*/
	String timeStmp;		/*中游特需参数*/
	
	String dijoyAppID;			/*鼎亿特需参数*/
	String dijoyRechargeUrl;	/*鼎亿特需参数, 充值页面URL地址*/
	String dijoyPlatformExt;	/*鼎亿特需参数, 网页平台传递给应用的扩展信息*/
	String dijoyReturnUrl;		/*鼎亿特需参数*/
	String dijoyHomeUrl;		/*鼎亿特需参数*/
	//String dijoyAppExt;		/*鼎亿特需参数*/
	
	String shengyiCPID;			/*盛翼特需参数*/
	String shengyiCPPassWord;	/*盛翼特需参数*/
	String shengyiUserIdType;	/*盛翼特需参数*/
	String shengyiProductId;	/*盛翼特需参数*/
	
	String feeaccount;			/*付费账号*/
	String returnurl;			/*返回地址*/
	String dwjvl;				/*平台加密验证*/
	String opcomkey;			/*区域标识*/
	String paysubway;			/*充值类型，主动/被动*/
	String user_group_id;		/*运营商地区标识*/
	
	String spid;			/*供应商ID，中游和掌世界充值时需要*/
	String gameid;			/*游戏ID，中游和掌世界充值时需要*/
	String buyURL;			/*查询元宝和扣除元宝服务器地址*/
	String checkKey;		/*MD5加密字符串*/
	
	String server;			/*游戏服务器地址*/
	int accountId;			/*用户账号*/
	String userId;			/*机顶盒ID*/
	String accountName;		/*用户昵称*/
	String userToken;		
	int productId;			/*产品ID*/
	String productName;		/*产品中文名称*/
	String appName;			/*产品英文名称*/
	
	boolean offline;
	
	private String errorMessage;
	private boolean parseSuccessful;
	private IEngine engine;
	
	public ParamManager(IEngine engine) {
		this.engine = engine;
	}
	
	public boolean parse() {
		try {
			parseSuccessful = true;
			errorMessage = "";
			parseParam();
			if (!parseSuccessful) {
				System.out.println(errorMessage);
			}
		}
		catch (Exception e) {
			parseSuccessful = false;
			errorMessage += e.getMessage()+"\n";
		}
		return parseSuccessful;
	}
	
	private void parseParam() {
		String off = engine.getAppProperty("offline");
		if (off != null && ("yes".equals(off)||"on".equals(off)||"true".equals(off))) {
			offline = true;
			System.out.println("[警告] ==> "+"离线模式开启，此模式只用于测试，期间将不连接服务器");
			System.out.println("[帮助] ==> "+"如需关闭离线模式，请将jad文件中配置参数offline的值设置为\"false\"");
		}
		else {
			offline = false;
		}
		
		Configurations conf = Configurations.getInstance();
		System.out.println("telcomOperators:"+conf.getTelcomOperators());
		System.out.println("serviceProvider:"+conf.getServiceProvider());
		if (conf.isServiceProviderWinside()) {
			if (conf.isTelcomOperatorsTelcomgd()) {
				parseWinsidegdPlatParam();
			}
			else {
				parseWinsidePlatParam();
			}
		}
		else if (conf.isServiceProviderThe9()
				|| conf.isServiceProviderOhyeah()) {
			parseOhyeahPlatParam();
		}else if(conf.isServiceProviderDijoy()){
			parseDijoyPlatParam();
		}else if(conf.isServiceProviderShengYi()){
			parseShengYiPlatParam();
		}else if(conf.isServiceProviderShiXian()){
			parseShiXianPlatParam();
		}
		else {
			parseSuccessful = false;
			errorMessage += "[错误] ==> "+"未知的入口参数"+conf.getServiceProvider()+"\n";
		}
		
		String amounts = conf.getPrice();
		if (amounts == null || "".equals(amounts)) {
			amounts = getStringParam("price");
		}
		if (amounts != null && !"".equals(amounts)) {
			parseAmounts(amounts);
		}
	}
	
	private void parseAmounts(String amounts) {
		try {
			int prevPos = 0;
			int scanPos = 0;
			int amountCount = 1;
			if (!amounts.startsWith("/") && !amounts.endsWith("/") && amounts.indexOf("//")<0) {
				while (scanPos < amounts.length()) {
					if (amounts.charAt(scanPos) == '/') {
						++amountCount;
					}
					++scanPos;
				}
				rechargeAmounts = new int[amountCount];
				
				scanPos = 0;
				amountCount = 0;
				while (scanPos < amounts.length()) {
					if (amounts.charAt(scanPos) == '/') {
						rechargeAmounts[amountCount] = Integer.parseInt(amounts.substring(prevPos, scanPos));
						++amountCount;
						prevPos = scanPos+1;
					}
					++scanPos;
				}
				rechargeAmounts[amountCount] = Integer.parseInt(amounts.substring(prevPos));
			}
			else {
				parseSuccessful = false;
				errorMessage += "[错误] ==> "+"参数"+"\""+"price"+"\""+"格式错误"+"\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			parseSuccessful = false;
			errorMessage += "[错误] ==> "+"参数"+"\""+"price"+"\""+"格式错误"+"\n";
		}
	}
	
	private void parseOhyeahPlatParam() {
		server = getStringParam("server");
		userId = getStringParam("userId");
		accountName = getStringParam("accountName");
		userToken = getStringParam("userToken");
		appName = getStringParam("appName");
		buyURL = "";
		gameid = "";
		spid = "";
		checkKey = "";
	}
	
	private void parseDijoyPlatParam() {
		userId = getStringParam("UserID");
		dijoyHomeUrl = getStringParam("HomeUrl");
		server = getStringParam("loginUrl");
		accountName = getStringParam("LoginID");
        appName = Configurations.getInstance().getAppName();
        if (appName == null || "".equals(appName)) {
		    appName = getStringParam("appName");
        }
		dijoyAppID = getStringParam("AppID");
		dijoyReturnUrl = getStringParam("ReturnUrl");
		dijoyPlatformExt = getStringParamDijoy("PlatformExt"); //该参数可能为空
		buyURL = getStringParam("BuyService");
		userToken = "";
		spid = "";
		//checkKey = Configurations.getInstance().getDijoyPayKey();
        //if (checkKey == null || "".equals(checkKey)) {
    	checkKey = getStringParam("payKey");
        //}
	}
	
	private void parseShengYiPlatParam(){
		server = getStringParam("server");
		userId = getStringParam("userId");
		accountName = getStringParam("accountName");
		userToken = getStringParam("userToken");
		appName = getStringParam("appName");
		shengyiCPID = getStringParam("cpId");
		shengyiCPPassWord = getStringParam("cpPassWord");
		shengyiUserIdType = getStringParam("userIdType");
		shengyiProductId = getStringParam("shengyiProductID");
		buyURL = "";
		gameid = "";
		spid = "";
		checkKey = "";
	}
	
	private void parseShiXianPlatParam() {
		server = getStringParam("loginurl");
		userId = getStringParam("tvplat#numbercode");
		userToken = ""/*getStringParam("USER_TOKEN")*/;
		appName = getStringParam("appname");
		feeaccount = getStringParam("tvplat#feeaccount");
		returnurl = ""/*getStringParam("tvplat#returnurl")*/;
		dwjvl = getStringParam("tvplat#dwjvl");
		opcomkey = getStringParam("tvplat#opcomkey");
		paysubway = getStringParam("tvplat#paysubway");
		user_group_id = ""/*getStringParam("USER_GROUP_ID")*/;
		buyURL = ""/*getStringParam("rechargeurl")*/;
		gameid = ""/*getStringParam("tvplat#gameid")*/;
		spid = "";
		checkKey = "";
		accountName = userId;
	}
	
	private void parseWinsidegdPlatParam() {
		server = getStringParam("w_server");
		userId = getStringParam("userid");
		accountName = getStringParam("iptvname");
		spid = getStringParam("spid");
		gameid = getStringParam("gameid");
		appName = getStringParam("product");
		checkKey = getStringParam("checkKey");
		buyURL = getStringParam("buyURL");
		zyUserToken = getStringParam("zyUserToken");
		stbType = getStringParam("stbType");
		enterURL = getStringParam("enterURL");
		myDXScore = getStringParam("myDXScore");
		hosturl = getStringParam("hosturl");
		code = getStringParam("code");
		timeStmp = getStringParam("timeStmp");
		userToken = zyUserToken;
	}
	
	private void parseWinsidePlatParam() {
		server = getStringParam("loginurl");
		userId = getStringParam("userid");
		accountName = getStringParam("username");
		userToken = getStringParam("userToken");
		gameid = getStringParam("gameid");
		spid = getStringParam("spid");
		appName = getStringParam("product");
		checkKey = getStringParam("checkKey");
		buyURL = getStringParam("buyURL");
	}
	
	private String getStringParam(String paramName) {
		String paramValue = null;
		try {
			paramValue = engine.getAppProperty(paramName).trim();
			if ("".equals(paramValue)) {
				parseSuccessful = false;
				errorMessage += "[信息] ==> "+"获取参数"+"\""+paramName+"\""+"失败"+"\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			parseSuccessful = false;
			errorMessage += "[信息] ==> "+"获取参数"+"\""+paramName+"\""+"失败"+"\n";
		}
		return paramValue;
	}
	
	private String getStringParamDijoy(String paramName) {
		String paramValue = null;
		try {
			paramValue = engine.getAppProperty(paramName).trim();
		}
		catch (Exception e) {
			e.printStackTrace();
			parseSuccessful = false;
			errorMessage += "[信息] ==> "+"获取参数"+"\""+paramName+"\""+"失败"+"\n";
		}
		return paramValue;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isParseSuccess() {
		return parseSuccessful;
	}
}
