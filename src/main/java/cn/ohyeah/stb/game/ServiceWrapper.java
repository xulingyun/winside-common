package cn.ohyeah.stb.game;

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
	private ParamManager paramManager;
	private EngineService engineService;
	
	private int result;
	private String message;
	
	public ServiceWrapper(IEngine engine, String server) {
		this.engine = engine;
		this.engineService = engine.getEngineService();
		this.paramManager = engineService.getParamManager();
		this.server = server;
		offline = paramManager.offline;
	}
	
	
}
