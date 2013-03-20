package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.stb.util.ConvertUtil;

public class RecordService extends AbstractHttpService {

	public RecordService(String url) {
		super(url);
	}

	/**
	 * 保存游戏记录
	 * @param userid
	 * @param username
	 * @param product
	 * @param index 档案序号，取值范围为1-6
	 * @param datas 游戏数据的内容(65535字节)
	 */
	public void saveRecord(String userid, String username, String product, int index, String datas){
		String sendCmd = null;
		serviceLocation += addr_saveRecord;
		
		sendCmd = "userid=" + HURLEncoder.encode(userid) + 
				  "&username="+ HURLEncoder.encode(username) + 
				  "&product="+ HURLEncoder.encode(product) +
				  "&index="+ index +
				  "&datas="+ HURLEncoder.encode(datas);

		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
			}else{
				result = -1;
				message = info[2];
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存排行积分
	 * @param userid
	 * @param username
	 * @param product
	 * @param score
	 */
	public void saveScore(String userid, String username, String product, int score){
		String sendCmd = null;
		serviceLocation += addr_saveScore;
		
		sendCmd = "userid=" + HURLEncoder.encode(userid) + 
				  "&username="+ HURLEncoder.encode(username) + 
				  "&product="+ HURLEncoder.encode(product) +
				  "&score="+ score;

		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
			}else{
				result = -1;
				message = info[2];
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载游戏记录
	 * @param userid
	 * @param username
	 * @param product
	 * @param index 档案序号，取值范围为1-6
	 * @return
	 */
	public String loadRecord(String userid, String username, String product, int index){
		String sendCmd = null;
		serviceLocation += addr_loadRecord;
		
		sendCmd = "userid=" + HURLEncoder.encode(userid) + 
				  "&username="+ HURLEncoder.encode(username) + 
				  "&product="+ HURLEncoder.encode(product) +
				  "&index="+ index;

		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
				return info[2];
			}else{
				result = -1;
				message = info[2];
				return null;
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询排行
	 * @param userid
	 * @param username
	 * @param product
	 * @param type 0为总排行，1为日排行，2为周排行，3为月排行
	 * @return userid1， username1， 分数， 排名|userid2， username2，分数,排名|...|myRank:自己的排名,自己最高分数
	 */
	public String queryRank(String userid, String username, String product, int type){
		String sendCmd = null;
		serviceLocation += addr_queryRank;
		
		sendCmd = "userid=" + HURLEncoder.encode(userid) + 
				  "&username="+ HURLEncoder.encode(username) + 
				  "&product="+ HURLEncoder.encode(product) +
				  "&type="+ type;

		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
				return info[2];
			}else{
				result = -1;
				message = info[2];
				return null;
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
			return null;
		}
	}
}
