package cn.ohyeah.stb.game;

import cn.ohyeah.itvgame.service.AccountService;

/**
 * 服务包装类，对服务类做了简单的包装，
 * 为了保证线程安全，请先创建新的对象再调用
 * @author maqian
 * @version 1.0
 */
public final class ServiceWrapper {
	private static final String OFFLINE_MSG = "离线模式不支持此操作";
	private static boolean offline;
	
	private String server;
	private IEngine engine;
	private ParamManager pm;
	private EngineService engineService;
	
	private int result;
	private String message;
	
	public ServiceWrapper(IEngine engine, String server) {
		this.engine = engine;
		this.engineService = engine.getEngineService();
		this.pm = engineService.getParamManager();
		this.server = server;
		offline = pm.offline;
	}
	
	
	public boolean userLogin(){
		AccountService as = new AccountService(server);
		as.cmdLogin(pm.userId, pm.accountName, pm.appName);
		message = as.getMessage();
		return as.isSuccess();
	}
	
}
