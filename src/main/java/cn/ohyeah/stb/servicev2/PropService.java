package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 道具服务类
 * 
 * @author maqian
 * @version 2.0
 */
public class PropService extends AbstractService {

	public PropService(String server, int connType) {
		super(server, connType);
	}

	/**
	 * <pre>
	 * 查询游戏道具列表
	 * asynResult.getResult() ==> Prop[]
	 * </pre>
	 * 
	 * @param productId
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult queryPropList(int productId)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_PROP,
					Constant.PROP_CMD_QUERY_PROP_LIST);
			ByteBuffer req = new ByteBuffer(16);
			writeHead(req, head);
			req.writeInt(productId);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 查询玩家拥有的道具列表
	 * asynResult.getResult() ==> OwnProp[]
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult queryOwnPropList(
			int accountId, int productId) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_PROP,
					Constant.PROP_CMD_QUERY_OWN_PROP_LIST);
			ByteBuffer req = new ByteBuffer(16);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 使用道具
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param propIds
	 * @param nums
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult useProps(int accountId,
			int productId, int[] propIds, int[] nums)
			throws InterruptedException {
		if (propIds.length == 0 || nums.length == 0
				|| propIds.length != nums.length) {
			throw new ServiceException("propIds或者nums长度错误");
		}
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_PROP, Constant.PROP_CMD_USE_PROPS);
			int bufLen = 32 + (propIds.length << 3);
			ByteBuffer req = new ByteBuffer(bufLen);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			req.writeShort((short) propIds.length);
			for (int i = 0; i < propIds.length; ++i) {
				req.writeInt(propIds[i]);
				req.writeInt(nums[i]);
			}

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 同步道具
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param propIds
	 * @param counts
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult synProps(int accountId,
			int productId, int[] propIds, int[] counts)
			throws InterruptedException {
		if (propIds.length == 0 || counts.length == 0
				|| propIds.length != counts.length) {
			throw new ServiceException("propIds或者counts长度错误");
		}
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_PROP, Constant.PROP_CMD_SYN_PROPS);
			int bufLen = 32 + (propIds.length << 3);
			ByteBuffer req = new ByteBuffer(bufLen);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			req.writeShort((short) propIds.length);
			for (int i = 0; i < propIds.length; ++i) {
				req.writeInt(propIds[i]);
				req.writeInt(counts[i]);
			}

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
