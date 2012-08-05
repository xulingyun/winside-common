package cn.ohyeah.stb.util;

/**
 * 阻塞队列，用于构建生产者-消费者模型
 * @author maqian
 * @version 1.0
 */
public class BlockingArrayQueue {
	private Object[] queue;
	private short writerIndex;
	private short readerIndex;
	private short size;
	volatile private short len;
	
	public BlockingArrayQueue() {
		this(32);
	}
	
	public BlockingArrayQueue(int size) {
		this.queue = new Object[size];
		this.size = (short)size;
	}
	
	/**
	 * 加入队列前，判断元素是否为null
	 * @param obj
	 */
	private void checkNull(Object obj) {
		if (obj == null) {
			throw new NullPointerException("队列元素不能为null");
		}
	}
	
	/**
	 * 将元素添加到队列尾部
	 * @param obj
	 */
	private void enqueue(Object obj) {
		queue[writerIndex] = obj;
		if (writerIndex < size-1) {
			++writerIndex;
		}
		else {
			writerIndex = 0;
		}
		++len;
	}
	
	/**
	 * 取出队列头部元素
	 * @return
	 */
	private Object dequeue() {
		Object obj = queue[readerIndex];
		queue[readerIndex] = null;
		if (readerIndex < size-1) {
			++readerIndex;
		}
		else {
			readerIndex = 0;
		}
		--len;
		return obj;
	}
	
	/**
	 * <pre>
	 * 添加非空元素到队列中
	 * 如果队列未满，将元素添加到队列尾部，并立即返回
	 * 如果队列已满，阻塞等待，直到对列未满
	 * </pre>
	 * @param obj
	 * @throws InterruptedException
	 */
	public void put(Object obj) throws InterruptedException {
		checkNull(obj);
		synchronized (this) {
			while (len >= size) {
				wait();
			}
			enqueue(obj);
			notifyAll();
		}
	}
	
	/**
	 * <pre>
	 * 添加非空元素到队列中
	 * 如果队列未满，将元素添加到队列尾部，并立即返回true
	 * 如果队列已满，立即返回false
	 * </pre>
	 * @param obj
	 * @return
	 */
	public boolean offer(Object obj) {
		checkNull(obj);
		synchronized (this) {
			if (len < size) {
				enqueue(obj);
				notifyAll();
				return true;
			}
			return false;
		}
	}
	
	/**
	 * <pre>
	 * 添加非空元素到队列中
	 * 如果队列未满，将元素添加到队列尾部，并立即返回true
	 * 如果队列已满，阻塞等待timout毫秒，如果队列未满，则将元素添加到队列尾部，并返回true，反之，返回false
	 * </pre>
	 * @param obj
	 * @param timeout
	 * @return
	 * @throws InterruptedException
	 */
	public boolean offer(Object obj, int timeout) throws InterruptedException {
		checkNull(obj);
		synchronized (this) {
			if (len >= size) {
				wait(timeout);
			}
			return offer(obj);
		}
	}
	
	/**
	 * <pre>
	 * 从队列中取出元素
	 * 如果队列非空，立即返回队列头部元素
	 * 如果队列为空，阻塞等待，直到队列非空
	 * </pre>
	 * @return
	 * @throws InterruptedException
	 */
	public Object take() throws InterruptedException {
		synchronized (this) {
			while (len <= 0) {
				wait();
			}
			Object obj = dequeue();
			notifyAll();
			return obj;
		}
	}
	
	/**
	 * <pre>
	 * 从队列中取出元素
	 * 如果队列非空，立即返回队列头部元素
	 * 如果队列为空，立即返回null
	 * </pre>
	 * @return
	 */
	public Object poll() {
		synchronized (this) {
			if (len > 0) {
				Object obj = dequeue();
				notifyAll();
				return obj;
			}
			return null;
		}
	}
	
	/**
	 * <pre>
	 * 从队列中取出元素
	 * 如果队列非空，立即返回队列头部元素
	 * 如果队列为空，阻塞等待timout毫秒，如果队列非空，则返回队列头部元素，反之，返回null
	 * </pre>
	 * @param timeout
	 * @return
	 * @throws InterruptedException
	 */
	public Object poll(int timeout) throws InterruptedException {
		synchronized (this) {
			if (len <= 0) {
				wait(timeout);
			}
			return poll();
		}
	}
	
	/**
	 * 清空队列
	 */
	public void clear() {
		synchronized (this) {
			for (int i = 0; i < queue.length; ++i) {
				queue[i] = null;
			}
			writerIndex = 0;
			readerIndex = 0;
			len = 0;
		}
	}
	
	/**
	 * 队列中元素个数
	 * @return
	 */
	public int length() {
		return len;
	}
	
	/**
	 * 队列中空位个数
	 * @return
	 */
	public int available() {
		return size-len;
	}
	
}
