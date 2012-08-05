package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.asyn.AsynRequest;
import cn.ohyeah.stb.asyn.AsynResponse;
import cn.ohyeah.stb.asyn.IAsynService;
import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.net.AbstractNetClient;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 服务抽象类
 * @author maqian
 * @version 2.0
 */
public class AbstractService {
	protected static FrameDecoder decoder;
	protected static IAsynService asynServ;
	
	private String server;
	private int connType;
	
	protected AbstractService(String server) {
		this(server, AbstractNetClient.CONN_TYPE_SHORT);
	}
	
	protected AbstractService(String server, int connType) {
		this.server = server;
		this.connType = connType;;
	}
	
	public static final void registerAsynService(IAsynService asynService) {
		asynServ = asynService;
	}
	
	public static final void registerFrameDecoder(FrameDecoder frameDecoder){
		decoder = frameDecoder;
	}
	
	protected final HeadWrapper buildHead(int version, int tag, int cmd) {
		return new HeadWrapper.Builder().version(version).tag(tag).command(cmd).build();
	}
	
	protected final void writeHead(ByteBuffer req, HeadWrapper head) {
		req.writeInt(head.getHead());
		req.skipWriter(4);
	}
	
	protected final AsynResult asynRequest(ByteBuffer req, HeadWrapper head) 
		throws InterruptedException {
		AsynRequest asynRequest = new AsynRequest(server, connType, req);
		AsynResponse asynResponse = asynServ.asynRequest(asynRequest);
		return new AsynResult(asynResponse, decoder, head);
	}
	

}
