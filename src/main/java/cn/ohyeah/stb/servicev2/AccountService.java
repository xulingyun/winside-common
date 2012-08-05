package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 账户服务类
 * 
 * @author maqian
 * @version 2.0
 */
public class AccountService extends AbstractService {

	public AccountService(String server, int connType) {
		super(server, connType);
	}

	/**
	 * <pre>
	 * 查询授权信息
	 * asynResult.getResult() ==> Authorization
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult getAuthorization(int accountId, int productId)
			throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ACCOUNT,
					Constant.ACCOUNT_CMD_QUERY_AUTHORIZATION);
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
	 * 查询订购属性信息
	 * asynResult.getResult() ==> SubscribeProperties
	 * </pre>
	 * 
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param checkKey
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult getSubscribeProperties(String buyURL,
			int accountId, String accountName, String userToken, int productId,
			String checkKey) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ACCOUNT,
					Constant.ACCOUNT_CMD_QUERY_SUB_PROPS);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeInt(accountId);
			req.writeUTF(accountName);
			req.writeUTF(userToken);
			req.writeInt(productId);
			req.writeUTF(checkKey);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 用户登录并返回登录信息
	 * asynResult.getResult() ==> LoginInfo
	 * </pre>
	 * 
	 * @param buyURL
	 * @param userId
	 * @param accountName
	 * @param userToken
	 * @param appName
	 * @param checkKey
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult userLogin(String buyURL, String userId,
			String accountName, String userToken, String appName,
			String checkKey) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_ACCOUNT,
					Constant.ACCOUNT_CMD_USER_LOGIN);
			ByteBuffer req = new ByteBuffer(128);
			writeHead(req, head);
			req.writeUTF(buyURL);
			req.writeUTF(userId);
			req.writeUTF(accountName);
			req.writeUTF(userToken);
			req.writeUTF(appName);
			req.writeUTF(checkKey);
			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
