package cn.ohyeah.stb.util;

/**
 * 碰撞检测
 * @author xiaochen
 *
 */
public class Collision {
	
	/**
	 * 此方法用于小物体碰撞大物体的方形规则碰撞检测
	 * @param x1	小物体x轴坐标
	 * @param y1	小物体y轴坐标
	 * @param w1	小物体宽度
	 * @param h1	小物体高度
	 * @param x2	大物体x轴坐标
	 * @param y2	大物体y轴坐标
	 * @param w2	大物体宽度
	 * @param h2	大物体高度
	 * @return true:发生碰撞， false:未发生碰撞
	 */
	public static boolean checkSquareCollision(int x1,int y1,int w1,int h1, int x2,int y2,int w2,int h2){
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

	/**
	 * 此方法用于小物体碰撞大物体的圆形规则碰撞检测
	 * @param x1	小物体x轴坐标
	 * @param y1	小物体y轴坐标
	 * @param w1	小物体宽度
	 * @param h1	小物体高度
	 * @param x2	大物体x轴坐标
	 * @param y2	大物体y轴坐标
	 * @param w2	大物体宽度
	 * @param h2	大物体高度
	 * @return true:发生碰撞， false:未发生碰撞
	 */
	public static boolean checkCircularCollision(int x1,int y1,int w1,int h1, int x2,int y2,int w2,int h2){
		int r1 = w1/2, cX1 = x1+r1, cY1 = y1+h1/2;
		int r2 = w2/2, cX2 = x2+r2, cY2 = y2+h2/2;
		if(((cX2-cX1)*(cX2-cX1)+(cY2-cY1)*(cY2-cY1)) <= (r1+r2)*(r1+r2))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 此方法用于小物体碰撞大物体的圆形碰方形规则
	 * @param x1	小物体x轴坐标
	 * @param y1	小物体y轴坐标
	 * @param w1	小物体宽度
	 * @param h1	小物体高度
	 * @param x2	大物体x轴坐标
	 * @param y2	大物体y轴坐标
	 * @param w2	大物体宽度
	 * @param h2	大物体高度
	 * @return true:发生碰撞， false:未发生碰撞
	 */
	public static boolean checkCircularToSquareCollision(int x1,int y1,int w1,int h1, int x2,int y2,int w2,int h2){
		return false;
	}
	
	/**
	 * 此方法用于小物体碰撞大物体的方形碰圆形规则
	 * @param x1	小物体x轴坐标
	 * @param y1	小物体y轴坐标
	 * @param w1	小物体宽度
	 * @param h1	小物体高度
	 * @param x2	大物体x轴坐标
	 * @param y2	大物体y轴坐标
	 * @param w2	大物体宽度
	 * @param h2	大物体高度
	 * @return true:发生碰撞， false:未发生碰撞
	 */
	public static boolean checkSquareToCircularCollision(int x1,int y1,int w1,int h1, int x2,int y2,int w2,int h2){
		int r1;
		if(w1<h1){
			r1 = w1/2;
		}else{
			r1 = h1/2;
		}
		int cX1 = x1+r1, cY1 = y1+h1/2;
		int r2 = w2/2, cX2 = x2+r2, cY2 = y2+h2/2;
		if(((cX2-cX1)*(cX2-cX1)+(cY2-cY1)*(cY2-cY1)) <= (r1+r2)*(r1+r2))
		{
			return true;
		}
		return false;
	}
}
