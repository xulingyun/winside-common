package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.itvgame.model.SubscribePayType;
import cn.ohyeah.itvgame.model.SubscribeRecord;
import cn.ohyeah.itvgame.protocol.Constant;

/**
 * 订购服务类
 * @author maqian
 * @version 1.0
 */
public final class SubscribeService extends AbstractHttpService{
	public SubscribeService(String url) {
		super(url);
	}
	
	/**
	 * 产品订购
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param amount
	 * @param purchaseId
	 * @throws ServiceException
	 */
	public void subscribe(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int purchaseId, String remark, String checkKey) {
		subscribe(buyURL, accountId, accountName, userToken, productId, purchaseId, SubscribePayType.PAY_TYPE_BILL, remark, checkKey);
	}
	
	public void subscribe(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int purchaseId, int payType, String remark, String checkKey) {
		try {
			initHead(Constant.PROTOCOL_TAG_SUBSCRIBE, Constant.SUBSCRIBE_CMD_SUBSCRIBE);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(purchaseId);
			bufferDos.writeInt(payType);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(checkKey);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
			readResult();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 游戏内订购产品
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param subscribeType
	 * @param remark
	 * @return
	 * @throws ServiceException
	 */
	public void subscribeProduct(String buyURL, int accountId, String accountName, String userToken, 
			int productId, String subscribeType, String remark, String checkKey) {
		subscribeProduct(buyURL, accountId, accountName, userToken, productId, subscribeType, SubscribePayType.PAY_TYPE_BILL, remark, checkKey);
	}
	
	public void subscribeProduct(String buyURL, int accountId, String accountName, String userToken, 
			int productId, String subscribeType, int payType, String remark, String checkKey) {
		try {
			initHead(Constant.PROTOCOL_TAG_SUBSCRIBE, Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeUTF(subscribeType);
			bufferDos.writeInt(payType);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(checkKey);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
			readResult();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}

	/**
	 * 游戏类充值
	 * rate表示充值兑换比例，如1RMB=10元宝，rate应设为10；
	 * 注意：rate<=0，表示按服务器配置的默认rate值
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param amount
	 * @param ratio
	 * @param remark
	 * @return 返回充值后的余额
	 * @throws ServiceException
	 */
	public int recharge(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount, int ratio, String remark, String checkKey, String spid, String password) {
		return recharge(buyURL, accountId, accountName, userToken, productId, amount, ratio, 
				SubscribePayType.PAY_TYPE_BILL, remark, checkKey, spid, password);
	}
	
	public int recharge(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount, int ratio, int payType, String remark, String checkKey, String spid, String password) {
		try {
			int balance = -1;
			initHead(Constant.PROTOCOL_TAG_SUBSCRIBE, Constant.SUBSCRIBE_CMD_RECHARGE);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(amount);
			bufferDos.writeInt(ratio);
			bufferDos.writeInt(payType);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(checkKey);
			bufferDos.writeUTF(spid);
			if (password == null) {
				bufferDos.writeUTF("");
			}
			else {
				bufferDos.writeUTF(password);
			}
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
			if (readResult() == 0) {
				balance = connectionDis.readInt();
			}
			return balance;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	/**
	 * winsidegd充值接口
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param amount
	 * @param ratio
	 * @param remark
	 * @param checkKey
	 * @param spid
	 * @param gameid
	 * @param enterURL
	 * @param zyUserToken
	 * @param stbType
	 * @return
	 */
	public int rechargeWinsidegd(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount, int ratio, String remark, String checkKey, String spid,
			String gameid, String enterURL, String stbType, String password) {
		return rechargeWinsidegd(buyURL, accountId, accountName, userToken, productId, amount, ratio, 
				SubscribePayType.PAY_TYPE_BILL, remark, checkKey, spid, gameid, enterURL, stbType, password);
	}
	
	public int rechargeWinsidegd(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount, int ratio, int payType, String remark, String checkKey, String spid,
			String gameid, String enterURL, String stbType, String password) {
		try {
			int balance = -1;
			initHead(Constant.PROTOCOL_TAG_SUBSCRIBE, Constant.SUBSCRIBE_CMD_RECHARGE_WINSIDEGD);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(amount);
			bufferDos.writeInt(ratio);
			bufferDos.writeInt(payType);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(checkKey);
			bufferDos.writeUTF(spid);
			bufferDos.writeUTF(gameid);
			bufferDos.writeUTF(enterURL);
			bufferDos.writeUTF(stbType);
			if (password == null) {
				bufferDos.writeUTF("");
			}
			else {
				bufferDos.writeUTF(password);
			}
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
			if (readResult() == 0) {
				balance = connectionDis.readInt();
			}
			return balance;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	public int rechargeDijoy(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount, String remark, String checkKey, String appId, 
			String platformExt, String appExt) {
		return rechargeDijoy(buyURL, accountId, accountName, userToken, productId, amount,  
				SubscribePayType.PAY_TYPE_BILL, remark, checkKey, appId, platformExt, appExt);
	}
	
	public int rechargeDijoy(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount,int payType, String remark, String checkKey, String appId, 
			String platformExt, String appExt) {
		try {
			int balance = -1;
			initHead(Constant.PROTOCOL_TAG_SUBSCRIBE, Constant.SUBSCRIBE_CMD_RECHARGE_DIJOY);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(amount);
			bufferDos.writeInt(payType);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(checkKey);
			bufferDos.writeUTF(appId);
			bufferDos.writeUTF(platformExt);
			bufferDos.writeUTF(appExt);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
			if (readResult() == 0) {
				balance = connectionDis.readInt();
			}
			return balance;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	
	public void gotoOrderPageWinsidefj(String buyURL, String userId) {
		
	}
	
	/**
	 * <pre>
	 * 查询充值记录
	 * 若查询最近的1~10条记录,offset=0, length=10
	 * 若查询最近的11-20条记录，offset=10， length=10
	 * </pre>
	 * @param userId
	 * @param productId
	 * @param offset
	 * @param length
	 * @return 返回非空，成功；否则，失败
	 * @throws ServiceException 
	 */
	public SubscribeRecord[] querySubscribeRecord(String userId, int productId, int offset, int length) {
		try {
			SubscribeRecord[] srList = null;
			initHead(Constant.PROTOCOL_TAG_SUBSCRIBE, Constant.SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(userId);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(offset);
			bufferDos.writeInt(length);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	int num = connectionDis.readShort();
		    	if (num > 0) {
		    		srList = new SubscribeRecord[num];
			    	for (int i = 0; i < num; ++i) {
			    		srList[i] = new SubscribeRecord();
			    		srList[i].readQueryResponseData(connectionDis);
			    	}
		    	}
		    }
		    return srList;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	
	/**
	 * 查询账户余额
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param productId
	 * @return 若成功，返回余额，若失败，无意义
	 * @throws ServiceException
	 */
	public int queryBalance(String buyURL, int accountId, String accountName, int productId) {
		try {
			int balance = -10000;
			initHead(Constant.PROTOCOL_TAG_SUBSCRIBE, Constant.SUBSCRIBE_CMD_QUERY_BALANCE);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeInt(productId);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	balance = connectionDis.readInt();
		    }
		    return balance;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
}
