package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.asyn.AsynResponse;
import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

/**
 * <pre>
 * 异步服务结果类, 非线程安全
 * V2版的service返回异步结果, 异步的结果并非立即有效.
 * (1)使用非阻塞方式获取结果,需要先判断结果是否有效,再做其他处理:
 * AsynResult asynResult = SystemService.getSystemTime();
 * if (asynResult.isComplete()) {
 *     if (asynResult.isSuccessful()) {
 *         //结果有效,并且处理成功
 *         //成功后的处理...
 *         java.util.Date now = (java.util.Date)asynResult.getResult();
 *     }
 *     else {
 *         //结果有效,但是处理失败
 *         //失败后的处理...
 *     }
 * }
 * else {
 *     //结果仍然无效
 *     //可以去做其他事情...
 * }
 * 
 * (2)使用阻塞方式获取结果
 * AsynResult asynResult = SystemService.getSystemTime();
 * //下面的方法将会阻塞,直到返回结果,或者发生错误
 * java.util.Date now = (java.util.Date)asynResult.get();
 * 
 * </pre>
 * @author maqian
 * @version 1.0
 */
public class AsynResult {
	private static final String RESULT_NOT_REALIZE = "result not realized";
	
	private HeadWrapper head;
	private int errorCode;
	private String errorMessage;
	private AsynResponse asynResponse;
	private FrameDecoder decoder;
	private Object result;
	
	public AsynResult(AsynResponse rsp, FrameDecoder decoder, HeadWrapper reqHead) {
		this.asynResponse = rsp;
		this.decoder = decoder;
		this.head = reqHead;
		this.errorCode = -1;
	}
	
	private void checkComplete() {
		if (!isComplete()) {
			throw new RuntimeException(RESULT_NOT_REALIZE);
		}
	}
	
	/**
	 * 服务是否成功,isComplete函数返回true后,此结果才有意义
	 * @return
	 */
	public boolean isSuccessful() {
		checkComplete();
		return errorCode==0;
	}
	
	/**
	 * 服务错误消息,isComplete函数返回true后,此结果才有意义
	 * @return
	 */
	public String getErrorMessage() {
		checkComplete();
		return errorMessage;
	}

	/**
	 * 判断结果是否已经有效
	 * @return
	 */
	public boolean isComplete() {
		if (asynResponse != null) {
			return asynResponse.isComplete();
		}
		return true;
	}
	
	/**
	 * 处理完成百分比
	 * 网络服务中,指获取到的数据长度占数据总长度的百分比
	 * @return
	 */
	public int getPercent() {
		if (asynResponse != null) {
			return asynResponse.getPercent();
		}
		return 100;
	}
	
	/**
	 * 核验响应消息头部是否和请求一致
	 * @param rsp
	 */
	private void checkHead(ByteBuffer rsp) {
	    HeadWrapper rspHead = new HeadWrapper(rsp.readInt());
	    if (rspHead.getVersion() != head.getVersion()) {
	    	throw new ServiceException("协议版本不一致");
	    }
	    if (rspHead.getTag() != head.getTag()) {
	    	throw new ServiceException("协议标识不一致");
	    }
	    if (rspHead.getCommand() != head.getCommand()) {
	    	throw new ServiceException("协议命令不一致");
	    }
	    head = rspHead;
	}
	
	/**
	 * 读取服务结果
	 * @param rsp
	 * @return
	 */
	private boolean readErrorCode(ByteBuffer rsp) {
		//跳过len字段
		rsp.skipReader(4);
		errorCode = rsp.readInt();
		if (errorCode != 0) {
			errorMessage = rsp.readUTF();
		}
		return errorCode == 0;
	}
	
	/**
	 * 解码响应数据
	 * @param data
	 * @return
	 */
	private Object decodeResult(ByteBuffer data) {
		if (data != null) {
			checkHead(data);
			if (readErrorCode(data)) {
				return decoder.decode(data, head);
			}
		}
		return null;
	}
	
	/**
	 * 直接获取异步服务结果,isComplete函数返回true后,此结果才有意义
	 * @return
	 */
	public Object getResult() {
		checkComplete();
		if (asynResponse != null) {
			result = decodeResult((ByteBuffer)asynResponse.getData());
			asynResponse = null;
		}
		return result;
	}
	
	/**
	 * 阻塞方式获取异步服务结果
	 * @return
	 * @throws InterruptedException 
	 */
	public Object get() throws InterruptedException {
		if (asynResponse != null) {
			result = decodeResult((ByteBuffer)asynResponse.get());
			asynResponse = null;
		}
		return result;
	}
}
