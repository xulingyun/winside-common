package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.itvgame.model.PurchaseRecord;
import cn.ohyeah.itvgame.model.SubscribePayType;
import cn.ohyeah.itvgame.protocol.Constant;

/**
 * 游戏计费服务类
 * @author maqian
 * @version 1.0
 */
public final class PurchaseService extends AbstractHttpService{
	public PurchaseService(String url) {
		super(url);
	}
	
	/**
	 * 购买道具，需要消耗元宝（金币）
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param propId
	 * @param propCount
	 * @param remark
	 * @return 返回非负则成功，否则失败
	 * @throws ServiceException
	 */
	public int purchaseProp(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int propId, int propCount, String remark, String checkKey) {
		try {
			int balance = -1;
			initHead(Constant.PROTOCOL_TAG_PURCHASE, Constant.PURCHASE_CMD_PURCHASE_PROP);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(propId);
			bufferDos.writeInt(propCount);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(checkKey);
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
	 * 消费金币,amount,消费金额，单位元宝（金币）
	 * @param buyURL
	 * @param accountId
	 * @param accountName
	 * @param userToken
	 * @param productId
	 * @param amount
	 * @param remark
	 * @return 返回非负，成功；否则，失败
	 * @throws ServiceException 
	 */
	public int expend(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount, String remark, String checkKey) {
		try {
			int balance = -1;
			initHead(Constant.PROTOCOL_TAG_PURCHASE, Constant.PURCHASE_CMD_EXPEND);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(amount);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(checkKey);
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
	
	public int expendDijoy(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount,int propId, String remark, String appId, String checkKey,
			String platformExt) {
		return expendDijoy(buyURL, accountId, accountName, userToken, productId, amount,  propId,
				SubscribePayType.PAY_TYPE_BILL, remark, appId, checkKey, platformExt);
	}
	
	public int expendDijoy(String buyURL, int accountId, String accountName, String userToken, 
			int productId, int amount, int propId, int payType, String remark, String appId, String checkKey,
			String platformExt) {
		try {
			int balance = -1;
			initHead(Constant.PROTOCOL_TAG_PURCHASE, Constant.PURCHASE_CMD_EXPEND_DIJOY);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyURL);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeUTF(userToken);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(amount);
			bufferDos.writeInt(propId);
			bufferDos.writeInt(payType);
			bufferDos.writeUTF(remark);
			bufferDos.writeUTF(appId);
			bufferDos.writeUTF(checkKey);
			bufferDos.writeUTF(platformExt);
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
	 * <pre>
	 * 查询消费记录
	 * 若查询最近的1~10条记录,offset=0, length=10
	 * 若查询最近的11-20条记录，offset=10, length=10
	 * </pre>
	 * @param accountId
	 * @param productId
	 * @param offset
	 * @param length
	 * @return 返回非空，则成功；否则，失败
	 * @throws ServiceException
	 */
	public PurchaseRecord[] queryPurchasePropRecord(int accountId, int productId, int offset, int length) {
		PurchaseRecord[] purchaseList = null;
		try {
			initHead(Constant.PROTOCOL_TAG_PURCHASE, Constant.PURCHASE_CMD_QUERY_PURCHASE_RECORD);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeInt(accountId);
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
		    		purchaseList = new PurchaseRecord[num];
			    	for (int i = 0; i < num; ++i) {
			    		purchaseList[i] = new PurchaseRecord();
			    		purchaseList[i].readQueryResponseData(connectionDis);
			    	}
		    	}
		    }
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
		return purchaseList;
	}
}
