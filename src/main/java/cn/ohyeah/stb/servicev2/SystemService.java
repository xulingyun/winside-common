package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 系统服务类
 * 
 * @author maqian
 * @version 2.0
 */
public class SystemService extends AbstractService {
	public SystemService(String server, int connType) {
		super(server, connType);
	}

	/**
	 * <pre>
	 * 获取系统时间
	 * asynResult.getResult() ==> java.util.Date
	 * </pre>
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult getSystemTime()
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_SYS_SERV,
					Constant.SYS_SERV_CMD_SYN_TIME);
			ByteBuffer req = new ByteBuffer(16);
			writeHead(req, head);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 掌世界广东平台,将游戏加入大厅收藏夹
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param hosturl
	 * @param accountId
	 * @param userId
	 * @param accountName
	 * @param productId
	 * @param gameid
	 * @param spid
	 * @param code
	 * @param timeStmp
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult addFavoritegd(String hosturl,
			int accountId, String userId, String accountName, int productId,
			String gameid, String spid, String code, String timeStmp)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_SYS_SERV,
					Constant.SYS_SERV_CMD_ADD_FAVORITEGD);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(hosturl);
			req.writeInt(accountId);
			req.writeUTF(userId);
			req.writeUTF(accountName);
			req.writeInt(productId);
			req.writeUTF(gameid);
			req.writeUTF(spid);
			req.writeUTF(code);
			req.writeUTF(timeStmp);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
