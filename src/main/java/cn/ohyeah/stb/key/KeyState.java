package cn.ohyeah.stb.key;

/**
 * 按键状态
 * @author maqian
 * @version 1.0
 */
public class KeyState {
	//private Object keyLock = new Object();
	private int keyStates;
	private int currKeyCode;
	private char ch;
	private boolean supportKeyReleased;
	private boolean isDoubleClick;
	private boolean isTripleClick;
	private boolean hasPersistMoveEvent;
	
	public KeyState(){}
	
	/**
	 * 获取当前按键对应的字符
	 * @return
	 */
	public char getKeyChar() {
		return ch;
	}
	
	/**
	 * 得到当前击键的键值
	 * @return
	 */
	public int getCurrentKeyCode() {
		return currKeyCode;
	}
	
	/**
	 * 向按键缓存中添加键值
	 * @param keyCode
	 */
	public void set(int keyCode) {
		//synchronized (keyLock) {
			keyStates |= keyCode;
		//}
		if (currKeyCode ==  keyCode) {
			if (isDoubleClick) {
				isTripleClick = true;
			}
			else {
				isDoubleClick = true;
			}
		}
		else {
			isDoubleClick = false;
			isTripleClick = false;
		}
		currKeyCode = keyCode;
	}
	
	/**
	 * 从按键缓存中移除键值
	 * @param keyCode
	 */
	public void remove(int keyCode) {
		//synchronized (keyLock) {
			keyStates &= ~keyCode;
		//}
	}
	
	/**
	 * 清空按键缓存
	 */
	public void clear() {
		//synchronized (keyLock) {
			keyStates = 0;
		//}
	}
	
	/**
	 * 判断按键缓存中是否包含键
	 * @param keyCode
	 * @return
	 */
	public boolean contains(int keyCode) {
		//synchronized (keyLock) {
			return (keyStates&keyCode)!=0;
		//}
	}
	
	/**
	 * 判断按键缓存中是否存在任意键值
	 * @return
	 */
	public boolean containsAnyKey() {
		//synchronized (keyLock) {
			return keyStates!=0;
		//}
	}
	
	public boolean containsAnyKeyAndRemove() {
		//synchronized (keyLock) {
			if (containsAnyKey()) {
				clear();
				return true;
			}
			return false;
		//}
	}
	
	/**
	 * 判断按键缓存中是否包含键值，并移除<br/>
	 * 如果包含键，则移除该键，并返回true;
	 * 如果不包含键，直接返回false
	 * @param keyCode
	 * @return
	 */
	public boolean containsAndRemove(int keyCode) {
		//synchronized (keyLock) {
			if (contains(keyCode)) {
				remove(keyCode);
				return true;
			}
			return false;
		//}
	}
	
	/**
	 * 判断是否连续两次击键
	 * @param keyCode
	 * @return
	 */
	public boolean isDoubleClick(int keyCode) {
		return (currKeyCode==keyCode)&&isDoubleClick;
	}
	
	/**
	 * 判断是否连续三次击键
	 * @param keyCode
	 * @return
	 */
	public boolean isTripleClick(int keyCode) {
		return (currKeyCode==keyCode)&&isTripleClick;
	}
	
	/**
	 * 是否支持按键释放事件
	 * @return
	 */
	public boolean isSupportKeyReleased() {
		return supportKeyReleased;
	}
	
	/**
	 * 判断按键状态中是否由移动事件
	 * @param keyCode
	 * @return
	 */
	public boolean containsMoveEvent(int keyCode) {
		if (contains(keyCode) || (keyCode == currKeyCode && hasPersistMoveEvent)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断按键状态中是否由移动事件并移除该状态
	 * @param keyCode
	 * @return
	 */
	public boolean containsMoveEventAndRemove(int keyCode) {
		if (containsAndRemove(keyCode) || (keyCode == currKeyCode && hasPersistMoveEvent)) {
			return true;
		}
		return false;
	}
	
	private void resetMoveEvent() {
		if (supportKeyReleased) {
			hasPersistMoveEvent = true;
		}
		else {
			if (isDoubleClick) {
				hasPersistMoveEvent = true;
			}
			else {
				hasPersistMoveEvent = false;
			}
		}
	}
	
	/**
	 * 按键点击处理函数
	 * @param keyCode
	 */
	public void keyPressed(int keyCode) {
		ch = 'V';
		switch(keyCode)
		{
		case IPTVKeyCode.KEY_UP:
		case IPTVKeyCode.KEY_UP_2:
			set(KeyCode.UP);
			ch = 'U';
			resetMoveEvent();
			break;
		case IPTVKeyCode.KEY_DOWN:
		case IPTVKeyCode.KEY_DOWN_2:
			set(KeyCode.DOWN);
			ch = 'D';
			resetMoveEvent();
			break;
		case IPTVKeyCode.KEY_LEFT:
		case IPTVKeyCode.KEY_LEFT_2:
			set(KeyCode.LEFT);
			ch = 'L';
			resetMoveEvent();
			break;
		case IPTVKeyCode.KEY_RIGHT:
		case IPTVKeyCode.KEY_RIGHT_2:
			set(KeyCode.RIGHT);
			ch = 'R';
			resetMoveEvent();
			break;
		case IPTVKeyCode.KEY_OK:
		case IPTVKeyCode.KEY_OK_2:
			set(KeyCode.OK);
			ch = 'O';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_BACK:
		case IPTVKeyCode.KEY_BACK_COSHIP:
			set(KeyCode.BACK);
			ch = 'B';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM0:
			set(KeyCode.NUM0);
			ch = '0';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM1:
			set(KeyCode.NUM1);
			ch = '1';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM2:
			set(KeyCode.NUM2);
			ch = '2';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM3:
			set(KeyCode.NUM3);
			ch = '3';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM4:
			set(KeyCode.NUM4);
			ch = '4';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM5:
			set(KeyCode.NUM5);
			ch = '5';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM6:
			set(KeyCode.NUM6);
			ch = '6';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM7:
			set(KeyCode.NUM7);
			ch = '7';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM8:
			set(KeyCode.NUM8);
			ch = '8';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_NUM9:
			set(KeyCode.NUM9);
			ch = '9';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_STAR:
			set(KeyCode.ASTERISK);
			ch = '*';
			hasPersistMoveEvent = false;
			break;
		case IPTVKeyCode.KEY_POUND:
			set(KeyCode.POUND);
			ch = '#';
			hasPersistMoveEvent = false;
			break;
		default: break;
		}
	}
	
	/**
	 * 按键释放事件处理函数
	 * @param keyCode
	 */
	public final void keyReleased(int keyCode) {
		if (!supportKeyReleased) {
			supportKeyReleased = true;
		}
		hasPersistMoveEvent = false;
		switch(keyCode)
		{
		case IPTVKeyCode.KEY_UP:
		case IPTVKeyCode.KEY_UP_2:
			remove(KeyCode.UP);
			break;
		case IPTVKeyCode.KEY_DOWN:
		case IPTVKeyCode.KEY_DOWN_2:
			remove(KeyCode.DOWN);
			break;
		case IPTVKeyCode.KEY_LEFT:
		case IPTVKeyCode.KEY_LEFT_2:
			remove(KeyCode.LEFT);
			break;
		case IPTVKeyCode.KEY_RIGHT:
		case IPTVKeyCode.KEY_RIGHT_2:
			remove(KeyCode.RIGHT);
			break;
		case IPTVKeyCode.KEY_OK:
		case IPTVKeyCode.KEY_OK_2:
			remove(KeyCode.OK);
			break;
		case IPTVKeyCode.KEY_BACK:
		case IPTVKeyCode.KEY_BACK_COSHIP:
			remove(KeyCode.BACK);
			break;
		case IPTVKeyCode.KEY_NUM0:
			remove(KeyCode.NUM0);
			break;
		case IPTVKeyCode.KEY_NUM1:
			remove(KeyCode.NUM1);
			break;
		case IPTVKeyCode.KEY_NUM2:
			remove(KeyCode.NUM2);
			break;
		case IPTVKeyCode.KEY_NUM3:
			remove(KeyCode.NUM3);
			break;
		case IPTVKeyCode.KEY_NUM4:
			remove(KeyCode.NUM4);
			break;
		case IPTVKeyCode.KEY_NUM5:
			remove(KeyCode.NUM5);
			break;
		case IPTVKeyCode.KEY_NUM6:
			remove(KeyCode.NUM6);
			break;
		case IPTVKeyCode.KEY_NUM7:
			remove(KeyCode.NUM7);
			break;
		case IPTVKeyCode.KEY_NUM8:
			remove(KeyCode.NUM8);
			break;
		case IPTVKeyCode.KEY_NUM9:
			remove(KeyCode.NUM9);
			break;
		case IPTVKeyCode.KEY_STAR:
			remove(KeyCode.ASTERISK);
			break;
		case IPTVKeyCode.KEY_POUND:
			remove(KeyCode.POUND);
			break;
		default: break;
		}
	}
}
