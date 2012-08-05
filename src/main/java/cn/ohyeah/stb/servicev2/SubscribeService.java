package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 订购服务类
 * 
 * @author maqian
 * @version 2.0
 */
public class SubscribeService extends AbstractService {

	public SubscribeService(String server, int connType) {
		super(server, connType);
	}

	/**
	 * <pre>
	 * 游戏内订购产品
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param subscribeType
	 * @param payType
	 * @param remark
	 * @param checkKey
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult subscribeProduct(
			String buyURL, int accountId, String accountName, String userToken,
			int productId, String subscribeType, int payType, String remark,
			String checkKey) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_SUBSCRIBE,
					Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeInt(accountId);
			req.writeUTF(accountName);
			req.writeUTF(userToken);
			req.writeInt(productId);
			req.writeUTF(subscribeType);
			req.writeInt(payType);
			req.writeUTF(remark);
			req.writeUTF(checkKey);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 游戏内充值
	 * asynResult.getResult() ==> int(余额)
	 * </pre>
	 * 
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param amount
	 * @param ratio
	 * @param payType
	 * @param remark
	 * @param checkKey
	 * @param spid
	 * @param password
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult recharge(String buyURL,
			int accountId, String accountName, String userToken, int productId,
			int amount, int ratio, int payType, String remark, String checkKey,
			String spid, String password) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_SUBSCRIBE,
					Constant.SUBSCRIBE_CMD_RECHARGE);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeInt(accountId);
			req.writeUTF(accountName);
			req.writeUTF(userToken);
			req.writeInt(productId);
			req.writeInt(amount);
			req.writeInt(ratio);
			req.writeInt(payType);
			req.writeUTF(remark);
			req.writeUTF(checkKey);
			req.writeUTF(spid);
			if (password == null) {
				req.writeUTF("");
			} else {
				req.writeUTF(password);
			}

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 游戏内掌世界广东充值接口
	 * asynResult.getResult() ==> int(余额)
	 * </pre>
	 * 
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param amount
	 * @param ratio
	 * @param payType
	 * @param remark
	 * @param checkKey
	 * @param spid
	 * @param gameid
	 * @param enterURL
	 * @param stbType
	 * @param password
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult rechargeWinsidegd(
			String buyURL, int accountId, String accountName, String userToken,
			int productId, int amount, int ratio, int payType, String remark,
			String checkKey, String spid, String gameid, String enterURL,
			String stbType, String password) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_SUBSCRIBE,
					Constant.SUBSCRIBE_CMD_RECHARGE_WINSIDEGD);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeInt(accountId);
			req.writeUTF(accountName);
			req.writeUTF(userToken);
			req.writeInt(productId);
			req.writeInt(amount);
			req.writeInt(ratio);
			req.writeInt(payType);
			req.writeUTF(remark);
			req.writeUTF(checkKey);
			req.writeUTF(spid);
			req.writeUTF(gameid);
			req.writeUTF(enterURL);
			req.writeUTF(stbType);
			if (password == null) {
				req.writeUTF("");
			} else {
				req.writeUTF(password);
			}

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 查询充值记录
	 * asynResult.getResult() ==> SubscribeRecord[]
	 * </pre>
	 * 
	 * @param userId
	 * @param productId
	 * @param offset
	 * @param length
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult querySubscribeRecord(
			String userId, int productId, int offset, int length)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_SUBSCRIBE,
					Constant.SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD);
			ByteBuffer req = new ByteBuffer(32);
			writeHead(req, head);
			req.writeUTF(userId);
			req.writeInt(productId);
			req.writeInt(offset);
			req.writeInt(length);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 查询账户余额
	 * asynResult.getResult() ==> int(余额)
	 * </pre>
	 * 
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param productId
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult queryBalance(String buyURL,
			int accountId, String accountName, int productId)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_SUBSCRIBE,
					Constant.SUBSCRIBE_CMD_QUERY_BALANCE);
			ByteBuffer req = new ByteBuffer(64);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeInt(accountId);
			req.writeUTF(accountName);
			req.writeInt(productId);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
