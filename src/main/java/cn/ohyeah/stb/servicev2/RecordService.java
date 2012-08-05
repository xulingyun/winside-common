package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.modelv2.GameRecord;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * 游戏记录服务类
 * 
 * @author maqian
 * @version 2.0
 */
public class RecordService extends AbstractService {

	public RecordService(String server, int connType) {
		super(server, connType);
	}

	/**
	 * <pre>
	 * 保存游戏记录
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param record
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult save(int accountId,
			int productId, GameRecord record) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_RECORD, Constant.RECORD_CMD_SAVE);
			int bufLen = 64;
			if (record.getData() != null) {
				bufLen += record.getData().length;
			}
			ByteBuffer req = new ByteBuffer(bufLen);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			record.writeSaveRequestData(req);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 更新游戏记录
	 * asynResult.getResult() ==> null
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param record
	 * @throws InterruptedException
	 */
	public final AsynResult update(int accountId,
			int productId, GameRecord record) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_RECORD, Constant.RECORD_CMD_UPDATE);
			int bufLen = 64;
			if (record.getData() != null) {
				bufLen += record.getData().length;
			}
			ByteBuffer req = new ByteBuffer(bufLen);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			record.writeSaveRequestData(req);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 读取游戏记录
	 * asynResult.getResult() ==> GameRecord
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @param recordId
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult read(int accountId,
			int productId, int recordId) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_RECORD, Constant.RECORD_CMD_READ);
			ByteBuffer req = new ByteBuffer(32);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);
			req.writeInt(recordId);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 读取游戏记录描述列表
	 * asynResult.getResult() ==> GameRecordDesc[]
	 * </pre>
	 * 
	 * @param accountId
	 * @param productId
	 * @return
	 * @throws InterruptedException
	 */
	public final AsynResult queryDescList(int accountId,
			int productId) throws InterruptedException {
		try {
			HeadWrapper head = buildHead(Constant.PROTOCOL_VERSION,
					Constant.PROTOCOL_TAG_RECORD,
					Constant.RECORD_CMD_QUERY_DESC_LIST);
			ByteBuffer req = new ByteBuffer(16);
			writeHead(req, head);
			req.writeInt(accountId);
			req.writeInt(productId);

			return asynRequest(req, head);
		} catch (RuntimeException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
