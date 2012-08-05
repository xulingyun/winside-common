package cn.ohyeah.stb.asyn;

/**
 * 异步响应对象
 * @author maqian
 * @version 1.0
 */
public class AsynResponse {
	private static final String RESULT_NOT_REALIZE = "result not realized";
	
	private AsynRequest request;
	volatile private Object data;
	volatile private int percent; 
	volatile private boolean complete;
	volatile private Exception exception;
	
	public AsynResponse(AsynRequest request) {
		this.request = request;
	}
	
	public AsynRequest getRequest() {
		return this.request;
	}
	
	public void setRequest(AsynRequest request) {
		this.request = request;
	}
	
	private void checkException() {
		if (exception != null) {
			throw new RuntimeException(exception.getMessage());
		}
	}
	
	private void checkComplete() {
		if (!complete) {
			throw new RuntimeException(RESULT_NOT_REALIZE);
		}
	}
	
	public Object getData() {
		checkComplete();
		checkException();
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	/**
	 * 请求处理完成百分比，值为(0~100)
	 * @return
	 */
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	/**
	 * 判断结果是否已经有效
	 * @return
	 */
	public boolean isComplete() {
		checkException();
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	/**
	 * 阻塞方式获取异步服务响应
	 * @return
	 * @throws InterruptedException 
	 */
	public Object get() throws InterruptedException {
		synchronized (this) {
			while (!complete) {
				wait(); 
				checkException();
			}
			return data;
		}
	}
	
	public void signal() {
		synchronized (this) {
			notifyAll();
		}
	}
}
