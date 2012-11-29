package cn.ohyeah.stb.game;

import cn.ohyeah.stb.game.ServiceWrapper;

/**
 * 向服务器发送心跳包类(掌世界天威地区有该功能)
 * @author xiaochen
 *
 */
public class OnlineThread implements Runnable {

	private IEngine engine;
	public long t1, t2;
	private int period=600;	//每隔600秒发送一次心跳包
	public OnlineThread(IEngine engine){
		this.engine = engine;
	}
	
	public void run() {
		while(true){
			t2 = System.currentTimeMillis()/1000;
			if((t2-t1)>period){
				System.out.println("向服务器发送心跳包");
				t1 = System.currentTimeMillis()/1000;
				ServiceWrapper sw = engine.getServiceWrapper();
				sw.sendHeartbeatPacket();
			}
		}
	}

}
