package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.itvgame.protocol.Constant;

/**
 * 系统服务类
 * @author maqian
 * @version 1.0
 */
public final class SystemService extends AbstractHttpService{
	public SystemService(String url) {
		super(url);
	}
	
	/**
	 * 获取系统时间
	 * @return
	 */
	public java.util.Date getSystemTime() {
		try {
			java.util.Date time = null;
			initHead(Constant.PROTOCOL_TAG_SYS_SERV, Constant.SYS_SERV_CMD_SYN_TIME);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	time = new java.util.Date(connectionDis.readLong());
		    }
		    return time;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 添加收藏（广东）
	 * @param hosturl
	 * @param accountId
	 * @param userId
	 * @param accountName
	 * @param productId
	 * @param gameid
	 * @param spid
	 * @param code
	 * @param timeStmp
	 */
	public void addFavoritegd(String hosturl, int accountId, String userId, String accountName, int productId, 
			String gameid, String spid, String code, String timeStmp) {
		try {
			initHead(Constant.PROTOCOL_TAG_SYS_SERV, Constant.SYS_SERV_CMD_ADD_FAVORITEGD);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(hosturl);
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(userId);
			bufferDos.writeUTF(accountName);
			bufferDos.writeInt(productId);
			bufferDos.writeUTF(gameid);
			bufferDos.writeUTF(spid);
			bufferDos.writeUTF(code);
			bufferDos.writeUTF(timeStmp);
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
	 * 掌世界进入充值页面
	 * @param buyUrl
	 * @param userId
	 */
	public void gotoRechargePage(String buyUrl, String userId){
		try {
			initHead(Constant.PROTOCOL_TAG_SYS_SERV, Constant.SYS_SERV_CMD_GOTO_RECHARGE_PAGE);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeUTF(buyUrl);
			bufferDos.writeUTF(userId);
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
}
