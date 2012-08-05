package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.itvgame.model.Authorization;
import cn.ohyeah.itvgame.model.LoginInfo;
import cn.ohyeah.itvgame.model.SubscribeProperties;
import cn.ohyeah.itvgame.protocol.Constant;

/**
 * 账户服务类
 * @author maqian
 * @version 1.0
 */
public final class AccountService extends AbstractHttpService {

	public AccountService(String url) {
		super(url);
	}

	/**
	 * 查询授权信息
	 * @param accountId
	 * @param productId
	 * @return
	 * @throws ServiceException
	 */
	public Authorization getAuthorization(int accountId, int productId) {
		try {
			Authorization auth = null;
			initHead(Constant.PROTOCOL_TAG_ACCOUNT, Constant.ACCOUNT_CMD_QUERY_AUTHORIZATION);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeInt(accountId);
			bufferDos.writeInt(productId);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	auth = new Authorization();
		    	auth.readAuthorization(connectionDis);
		    }
		    return auth;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 查询订购属性信息
	 * @param accountId
	 * @param productId
	 * @return
	 * @throws ServiceException
	 */
	public SubscribeProperties getSubscribeProperties(String buyURL, int accountId, 
			String accountName, String userToken, int productId, String checkKey) {
		try {
			SubscribeProperties subProps = null;
			initHead(Constant.PROTOCOL_TAG_ACCOUNT, Constant.ACCOUNT_CMD_QUERY_SUB_PROPS);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeUTF(checkKey);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	subProps = new SubscribeProperties();
		    	subProps.readSubscribeProperties(connectionDis);
		    }
		    return subProps;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 完成用户登录并返回登录信息
	 * @param userId
	 * @param accountName
	 * @param userToken
	 * @param appName
	 * @return
	 * @throws ServiceException
	 */
	public LoginInfo userLogin(String buyURL, String userId, String accountName, String userToken, String appName, String checkKey) {
		try {
			LoginInfo info = null;
			initHead(Constant.PROTOCOL_TAG_ACCOUNT, Constant.ACCOUNT_CMD_USER_LOGIN);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeUTF(userId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeUTF(appName);
			bufferDos.writeUTF(checkKey);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	info = new LoginInfo();
		    	info.readLoginInfo(connectionDis);
		    }
		    return info;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
}
