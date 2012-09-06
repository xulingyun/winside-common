package cn.ohyeah.stb.util;

/**
 * ��ײ���
 * @author xiaochen
 *
 */
public class Collision {
	
	/**
	 * @param x1	������x������
	 * @param y1	������y������
	 * @param w1	����������
	 * @param h1	�������߶�
	 * @param x2	��������x������
	 * @param y2	��������y������
	 * @param w2	������������
	 * @param h2	���������߶�
	 * @return true:������ײ�� false:δ������ײ
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
