package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.stb.util.ConvertUtil;
import cn.ohyeah.stb.util.MD5Encrypt;

public class ConsumeService extends AbstractHttpService {

	public ConsumeService(String url) {
		super(url);
	}
	
	/**
	 * 元宝查询接口
	 * @param userid
	 * @return 成功则返回元宝数, 失败返回-1
	 */
	public int queryCoin(String userid){
		String sendCmd = null;
		serviceLocation += addr_query_coins;
		
		sendCmd = "userid=" + HURLEncoder.encode(userid);

		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
				return Integer.parseInt(info[2]);
			}else{
				result = -1;
				message = info[2];
				return -1;
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 元宝扣除接口
	 * @param userid
	 * @param username
	 * @param checkKey checkKey为接口提供方指定
	 * @param product 产品的英文标识
	 * @param contents 扣费内容，指具体购买的道具名称
	 * @param amount contents的数量，仅用于统计
	 * @param coins 欲扣除的元宝总数
	 */
	public int consumeCoin(String userid, String username, String checkKey,
			String product, String contents, int amount, int coins){
		
		String sendCmd = null;
		serviceLocation += addr_consume_coins;
		String checkcode = userid 
						+ "|" + username
						+ "|" + product
						+ "|" + contents
						+ "|" + amount
						+ "|" + coins
						+ "|" +checkKey;
		checkcode = MD5Encrypt.toMD5(checkcode).toLowerCase();
		System.out.println("consume checkcode:"+checkcode);
		
		sendCmd = "userid=" + HURLEncoder.encode(userid)+
				  "&username=" + HURLEncoder.encode(username)+	
				  "&product=" + HURLEncoder.encode(product)+	
				  "&contents=" + HURLEncoder.encode(contents)+	
				  "&amount=" + HURLEncoder.encode(String.valueOf(amount))+	
				  "&coins=" + HURLEncoder.encode(String.valueOf(coins))+	
				  "&checkcode=" + HURLEncoder.encode(String.valueOf(checkcode));	
		
		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
				return Integer.parseInt(info[2]); 
			}else{
				result = Integer.parseInt(info[1]);
				message = info[2];
				return -1;
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 通用充值接口
	 * @param userid
	 * @param username
	 * @param money
	 * @param spid
	 * @param product
	 * @param userToken
	 * @param checkKey
	 * @param password
	 */
	public int recharge(String userid, String username, int money, String spid, 
			String product, String userToken, String checkKey, String password){
		String sendCmd = null;
		serviceLocation += addr_order_coins;
		String checkcode = userid 
						   + "|" + spid
						   + "|" + product
						   + "|" + checkKey
						   + "|" + money;
		checkcode = MD5Encrypt.toMD5(checkcode).toLowerCase();
		System.out.println("recharge checkcode:"+checkcode);
		
		sendCmd = "userid=" + HURLEncoder.encode(userid)+
		          "&username=" + HURLEncoder.encode(username)+	
		          "&money=" + HURLEncoder.encode(String.valueOf(money))+	
		          "&spid=" + HURLEncoder.encode(spid)+	
		          "&product=" + HURLEncoder.encode(product)+	
		          "&userToken=" + HURLEncoder.encode(userToken)+	
		          "&checkCode=" + HURLEncoder.encode(checkcode);	
		
		if (password!=null && !password.equals("")) {
			sendCmd += "&passwd=" + HURLEncoder.encode(password);
		}
		
		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
				return Integer.parseInt(info[2]); 
			}else{
				result = Integer.parseInt(info[1]);
				message = info[2];
				return -1;
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 广东充值接口
	 * @param userid
	 * @param username
	 * @param spid
	 * @param stbType
	 * @param product
	 * @param money
	 * @param gameid
	 * @param enterURL
	 * @param zyUserToken
	 * @param checkKey
	 * @param payType
	 * @return
	 */
	public int rechargeGd(String userid, String username, String spid, String stbType, String product, int money, String gameid,
			String enterURL, String zyUserToken, String checkKey, int payType){
		String sendCmd = null;
		serviceLocation += addr_order_coins;
		String checkcode = userid 
						   + "|" + spid
						   + "|" + product
						   + "|" + checkKey
						   + "|" + money;
		checkcode = MD5Encrypt.toMD5(checkcode).toLowerCase();
		
		sendCmd = "userid=" + HURLEncoder.encode(userid)+
		          "&username=" + HURLEncoder.encode(username)+	
		          "&spid=" + HURLEncoder.encode(spid)+	
		          "&stbType=" + HURLEncoder.encode(stbType)+	
		          "&product=" + HURLEncoder.encode(product)+
		          "&money=" + HURLEncoder.encode(String.valueOf(money))+	
		          "&gameid=" + HURLEncoder.encode(gameid)+	
		          "&enterURL=" + HURLEncoder.encode(enterURL)+	
		          "&zyUserToken=" + HURLEncoder.encode(zyUserToken)+	
		          "&payType=" + HURLEncoder.encode(String.valueOf(payType))+	
				  "&checkCode=" + HURLEncoder.encode(checkcode);	
		
		try {
			String str = postViaHttpConnection(serviceLocation, sendCmd);
			String info[] = ConvertUtil.split(str, "#");
			if(info[1].equals("0")){
				result = 0;
				return Integer.parseInt(info[2]); 
			}else{
				result = Integer.parseInt(info[1]);
				message = info[2];
				return -1;
			}
		} catch (IOException e) {
			result = -1;
			message = e.getMessage();
			e.printStackTrace();
			return -1;
		}
	}
}
