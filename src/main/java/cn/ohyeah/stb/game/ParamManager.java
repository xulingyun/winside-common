package cn.ohyeah.stb.game;

/**
 * 参数管理类，加载校验jad参数
 * @author maqian
 * @version 1.0
 */
final class ParamManager {
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
	
	String spid;			/*供应商ID，中游和掌世界充值时需要*/
	String gameid;			/*游戏ID，中游和掌世界充值时需要*/
	String buyURL;			/*查询元宝和扣除元宝服务器地址*/
	String checkKey;		/*MD5加密字符串*/
	
	String server;			/*游戏服务器地址*/
	String userId;			/*机顶盒ID*/
	String accountName;		/*用户昵称*/
	String userToken;		
	String product;		/*产品名称*/
	
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
	
	private void parseWinsidegdPlatParam() {
		server = getStringParam("w_server");
		userId = getStringParam("userid");
		accountName = getStringParam("iptvname");
		spid = getStringParam("spid");
		gameid = getStringParam("gameid");
		product = getStringParam("product");
		checkKey = getStringParam("checkKey");
		buyURL = getStringParam("buyURL");
		zyUserToken = getStringParam("zyUserToken");
		stbType = getStringParam("stbType");
		enterURL = getStringParam("enterURL");
		myDXScore = getStringParam("myDXScore");
		hosturl = getStringParam("hosturl");
		code = getStringParam("gameCode");
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
		product = getStringParam("product");
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
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isParseSuccess() {
		return parseSuccessful;
	}
}
