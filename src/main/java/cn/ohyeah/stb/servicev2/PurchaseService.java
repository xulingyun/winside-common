package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 游戏计费服务类
 * 
 * @author maqian
 * @version 2.0
 */
public class PurchaseService extends AbstractService {

	public PurchaseService(String server, int connType) {
		super(server, connType);
	}

	/**
	 * <pre>
	 * 购买道具, 需要消耗游戏币, 道具类游戏使用
	 * asynResult.getResult() ==> int(余额)
	 * </pre>
	 * 
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param propId
	 * @param propCount
	 * @param remark
	 * @param checkKey
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult purchaseProp(String buyURL,
			int accountId, String accountName, String userToken, int productId,
			int propId, int propCount, String remark, String checkKey)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_PURCHASE,
					Constant.PURCHASE_CMD_PURCHASE_PROP);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeInt(accountId);
			req.writeUTF(accountName);
			req.writeUTF(userToken);
			req.writeInt(productId);
			req.writeInt(propId);
			req.writeInt(propCount);
			req.writeUTF(remark);
			req.writeUTF(checkKey);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 消费游戏币, 道具类游戏使用
	 * asynResult.getResult() ==> int(余额)
	 * </pre>
	 * 
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param amount
	 * @param remark
	 * @param checkKey
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult expend(String buyURL,
			int accountId, String accountName, String userToken, int productId,
			int amount, String remark, String checkKey)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_PURCHASE,
					Constant.PURCHASE_CMD_EXPEND);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeInt(accountId);
			req.writeUTF(accountName);
			req.writeUTF(userToken);
			req.writeInt(productId);
			req.writeInt(amount);
			req.writeUTF(remark);
			req.writeUTF(checkKey);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 查询消费记录
	 * asynResult.getResult() ==> PurchaseRecord[]
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param offset
	 * @param length
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult queryPurchasePropRecord(
			int accountId, int productId, int offset, int length)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_PURCHASE,
					Constant.PURCHASE_CMD_QUERY_PURCHASE_RECORD);
			ByteBuffer req = new ByteBuffer(32);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			req.writeInt(offset);
			req.writeInt(length);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
