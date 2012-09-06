package cn.ohyeah.stb.util;

/**
 * 碰撞检测
 * @author xiaochen
 *
 */
public class Collision {
	
	/**
	 * @param x1	攻击方x轴坐标
	 * @param y1	攻击方y轴坐标
	 * @param w1	攻击方宽度
	 * @param h1	攻击方高度
	 * @param x2	被攻击方x轴坐标
	 * @param y2	被攻击方y轴坐标
	 * @param w2	被攻击方宽度
	 * @param h2	被攻击方高度
	 * @return true:发生碰撞， false:未发生碰撞
	 */
	public static boolean checkCollision(int x1,int y1,int w1,int h1, int x2,int y2,int w2,int h2){
		if((x1 >= x2 && x1 <= (x2+w2)) && (y1 >= y2  && y1 <= (y2+h2))){
			return true;
		}else if(((x1+w1) >= x2 && (x1+w1) <= (x2+w2)) && (y1 >= y2  && y1 <= (y2+h2))){
			return true;
		}else if((x1 >= x2 && x1 <= (x2+w2)) && ((y1+h1) >= y2  && (y1+h1) <= (y2+h2))){
			return true;
		}else if(((x1+w1) >= x2 && (x1+w1) <= (x2+w2)) && ((y1+h1) >= y2  && (y1+h1) <= (y2+h2))){
			return true;
		}else {
			return false;
		}
	}

}
