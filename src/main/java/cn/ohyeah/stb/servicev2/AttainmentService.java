package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.modelv2.GameAttainment;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 游戏成就（排行）服务类
 * 
 * @author maqian
 * @version 2.0
 */
public class AttainmentService extends AbstractService {

	public AttainmentService(String server, int connType) {
		super(server, connType);
	}

	/**
	 * <pre>
	 * 保存游戏成就
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param accountId
	 * @param userId
	 * @param productId
	 * @param attainment
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult save(int accountId,
			String userId, int productId, GameAttainment attainment)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ATTAINMENT,
					Constant.ATTAINMENT_CMD_SAVE);
			int bufLen = 64;
			if (attainment.getData() != null) {
				bufLen += attainment.getData().length;
			}
			ByteBuffer req = new ByteBuffer(bufLen);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeUTF(userId);
			req.writeInt(productId);
			attainment.writeSaveRequestData(req);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 读取游戏成就
	 * asynResult.getResult() ==> GameAttainment
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param attainmentId
	 * @param orderCmd
	 * @param start
	 * @param end
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult read(int accountId,
			int productId, int attainmentId, String orderCmd,
			java.util.Date start, java.util.Date end)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ATTAINMENT,
					Constant.ATTAINMENT_CMD_READ);
			ByteBuffer req = new ByteBuffer(64);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			req.writeInt(attainmentId);
			if (orderCmd != null) {
				req.writeUTF(orderCmd);
			} else {
				req.writeUTF("");
			}
			if (start != null && end != null) {
				req.writeLong(start.getTime());
				req.writeLong(end.getTime());
			} else {
				req.writeLong(0);
				req.writeLong(0);
			}

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 更新游戏成就
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param accountId
	 * @param userId
	 * @param productId
	 * @param attainment
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult update(int accountId,
			String userId, int productId, GameAttainment attainment)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ATTAINMENT,
					Constant.ATTAINMENT_CMD_UPDATE);
			int bufLen = 64;
			if (attainment.getData() != null) {
				bufLen += attainment.getData().length;
			}
			ByteBuffer req = new ByteBuffer(bufLen);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeUTF(userId);
			req.writeInt(productId);
			attainment.writeUpdateRequestData(req);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 查询玩家排行列表
	 * asynResult.getResult() ==> GameRanking[]
	 * </pre>
	 * 
	 * @param productId
	 * @param orderCmd
	 *            排名指令("asc"/"desc"), 默认为"desc"
	 * @param start
	 *            起始时间, 默认为所有时间段
	 * @param end
	 *            结束时间, 默认为所有时间段
	 * @param offset
	 *            排名列表起始位置, 从0开始
	 * @param length
	 *            排名列表长度
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult queryRankingList(
			int productId, String orderCmd, java.util.Date start,
			java.util.Date end, int offset, int length)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ATTAINMENT,
					Constant.ATTAINMENT_CMD_QUERY_RANKING_LIST);
			ByteBuffer req = new ByteBuffer(64);
			writeHead(req, head);
			req.writeInt(productId);
			if (orderCmd != null) {
				req.writeUTF(orderCmd);
			} else {
				req.writeUTF("");
			}
			if (start != null && end != null) {
				req.writeLong(start.getTime());
				req.writeLong(end.getTime());
			} else {
				req.writeLong(0);
				req.writeLong(0);
			}
			req.writeInt(offset);
			req.writeInt(length);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 查询成就描述列表
	 * asynResult.getResult() ==> GameAttainmentDesc[]
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param orderCmd
	 *            排名指令("asc"/"desc"), 默认为"desc"
	 * @param start
	 *            起始时间, 默认为所有时间段
	 * @param end
	 *            结束时间, 默认为所有时间段
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult queryDescList(int accountId,
			int productId, String orderCmd, java.util.Date start,
			java.util.Date end) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ATTAINMENT,
					Constant.ATTAINMENT_CMD_QUERY_DESC_LIST);
			ByteBuffer req = new ByteBuffer(64);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			if (orderCmd != null) {
				req.writeUTF(orderCmd);
			} else {
				req.writeUTF("");
			}
			if (start != null && end != null) {
				req.writeLong(start.getTime());
				req.writeLong(end.getTime());
			} else {
				req.writeLong(0);
				req.writeLong(0);
			}

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
