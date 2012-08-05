package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.itvgame.model.GameAttainment;
import cn.ohyeah.itvgame.model.GameAttainmentDesc;
import cn.ohyeah.itvgame.model.GameRanking;
import cn.ohyeah.itvgame.protocol.Constant;

/**
 * 游戏成就（排行）服务类
 * @author maqian
 * @version 1.0
 */
public final class AttainmentService extends AbstractHttpService{
	public AttainmentService(String url) {
		super(url);
	}
	
	/**
	 * 保存游戏成就
	 * @param accountId
	 * @param productId
	 * @param attainment
	 * @throws ServiceException
	 */
	public void save(int accountId, String userId, int productId, GameAttainment attainment) {
		try {
			initHead(Constant.PROTOCOL_TAG_ATTAINMENT, Constant.ATTAINMENT_CMD_SAVE);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(userId);
			bufferDos.writeInt(productId);
			attainment.writeSaveRequestData(bufferDos);
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
	 * 读取游戏成就
	 * @param accountId
	 * @param productId
	 * @param attainmentId
	 * @return 返回值非空，则成功；返回null，失败
	 * @throws ServiceException
	 */
	public GameAttainment read(int accountId, int productId, int attainmentId, String orderCmd, 
			java.util.Date start, java.util.Date end) {
		try {
			GameAttainment attainment = null;
			initHead(Constant.PROTOCOL_TAG_ATTAINMENT, Constant.ATTAINMENT_CMD_READ);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeInt(accountId);
			bufferDos.writeInt(productId);
			bufferDos.writeInt(attainmentId);
			if (orderCmd != null) {
				bufferDos.writeUTF(orderCmd);
			}
			else {
				bufferDos.writeUTF("");
			}
			if (start != null && end != null) {
				bufferDos.writeLong(start.getTime());
				bufferDos.writeLong(end.getTime());
			}
			else {
				bufferDos.writeLong(0);
				bufferDos.writeLong(0);
			}
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	attainment = new GameAttainment();
		    	attainment.readReadResponseData(connectionDis);
		    }
		    return attainment;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 更新游戏成就
	 * @param accountId
	 * @param userId
	 * @param productId
	 * @param attainment
	 * @throws ServiceException
	 */
	public void update(int accountId, String userId, int productId, GameAttainment attainment) {
		try {
			initHead(Constant.PROTOCOL_TAG_ATTAINMENT, Constant.ATTAINMENT_CMD_UPDATE);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeInt(accountId);
			bufferDos.writeUTF(userId);
			bufferDos.writeInt(productId);
			attainment.writeUpdateRequestData(bufferDos);
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
	 * <pre>
	 * 查询玩家排行列表
	 * 若查询排名靠前的1~10条记录,offset=0, limit=10
	 * 若查询排名靠前的11-20条记录，offset=10， limit=10
	 * </pre>
	 * @param accountId
	 * @param productId
	 * @param offset
	 * @param length
	 * @return 返回非空，则成功；返回null，失败
	 * @throws ServiceException
	 */
	public GameRanking[] queryRankingList(int productId, String orderCmd, 
			java.util.Date start, java.util.Date end, int offset, int length) {
		try {
			GameRanking[] rankingList = null;
			initHead(Constant.PROTOCOL_TAG_ATTAINMENT, Constant.ATTAINMENT_CMD_QUERY_RANKING_LIST);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeInt(productId);
			if (orderCmd != null) {
				bufferDos.writeUTF(orderCmd);
			}
			else {
				bufferDos.writeUTF("");
			}
			if (start != null && end != null) {
				bufferDos.writeLong(start.getTime());
				bufferDos.writeLong(end.getTime());
			}
			else {
				bufferDos.writeLong(0);
				bufferDos.writeLong(0);
			}
			bufferDos.writeInt(offset);
			bufferDos.writeInt(length);
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	int num = connectionDis.readShort();
		    	if (num > 0) {
		    		rankingList = new GameRanking[num];
			    	for (int i = 0; i < num; ++i) {
			    		rankingList[i] = new GameRanking();
			    		rankingList[i].readQueryResponseData(connectionDis);
			    	}
		    	}
		    }
		    return rankingList;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 查询成就描述列表
	 * @param accountId
	 * @param productId
	 * @return
	 * @throws ServiceException
	 */
	public GameAttainmentDesc[] queryDescList(int accountId, int productId, String orderCmd, 
			java.util.Date start, java.util.Date end) {
		try {
			GameAttainmentDesc[] descList = null;
			initHead(Constant.PROTOCOL_TAG_ATTAINMENT, Constant.ATTAINMENT_CMD_QUERY_DESC_LIST);
			openBufferDataOutputStream();
			bufferDos.writeInt(headWrapper.getHead());
			bufferDos.writeInt(accountId);
			bufferDos.writeInt(productId);
			if (orderCmd != null) {
				bufferDos.writeUTF(orderCmd);
			}
			else {
				bufferDos.writeUTF("");
			}
			if (start != null && end != null) {
				bufferDos.writeLong(start.getTime());
				bufferDos.writeLong(end.getTime());
			}
			else {
				bufferDos.writeLong(0);
				bufferDos.writeLong(0);
			}
			byte[] data = bufferBaos.toByteArray();
			closeBufferDataOutputStream();
			
			writeData(data);
			checkHead();
		    if (readResult() == 0) {
		    	int num = connectionDis.readShort();
		    	if (num > 0) {
		    		descList = new GameAttainmentDesc[num];
			    	for (int i = 0; i < num; ++i) {
			    		descList[i] = new GameAttainmentDesc();
			    		descList[i].readQueryResponseData(connectionDis);
			    	}
		    	}
		    }
		    return descList;
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		finally {
			close();
		}
	}
}
