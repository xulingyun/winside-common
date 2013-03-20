package cn.ohyeah.itvgame.service;

import java.io.IOException;

import cn.ohyeah.stb.util.ConvertUtil;

public class PropService extends AbstractHttpService {

	public PropService(String url) {
		super(url);
	}

	/**
	 * 保存增值道具数据
	 * @param userid
	 * @param username
	 * @param product
	 * @param data 增值道具相关数据(65535字节)
	 */
	public void savePropItem(String userid, String username, String product, String data){
		String sendCmd = null;
		serviceLocation += addr_saveItem;
		
		sendCmd = "userid=" + HURLEncoder.encode(userid) + 
				  "&username="+ HURLEncoder.encode(username) + 
				  "&product="+ HURLEncoder.encode(product) +
				  "&datas="+ HURLEncoder.encode(data);

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
	 * 加载增值道具数据
	 * @param userid
	 * @param username
	 * @param product
	 * @return 道具字符串(保存时自定义的格式)
	 */
	public String loadPropItem(String userid, String username, String product){
		String sendCmd = null;
		serviceLocation += addr_loadItem;
		
		sendCmd = "userid=" + HURLEncoder.encode(userid) + 
				  "&username="+ HURLEncoder.encode(username) + 
				  "&product="+ HURLEncoder.encode(product);

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
