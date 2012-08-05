package cn.ohyeah.stb.servicev2;

import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.modelv2.Authorization;
import cn.ohyeah.stb.modelv2.GameAttainment;
import cn.ohyeah.stb.modelv2.GameAttainmentDesc;
import cn.ohyeah.stb.modelv2.GameRanking;
import cn.ohyeah.stb.modelv2.GameRecord;
import cn.ohyeah.stb.modelv2.GameRecordDesc;
import cn.ohyeah.stb.modelv2.LoginInfo;
import cn.ohyeah.stb.modelv2.OwnProp;
import cn.ohyeah.stb.modelv2.Prop;
import cn.ohyeah.stb.modelv2.PurchaseRecord;
import cn.ohyeah.stb.modelv2.SubscribeProperties;
import cn.ohyeah.stb.modelv2.SubscribeRecord;
import cn.ohyeah.stb.protocolv2.Constant;
import cn.ohyeah.stb.protocolv2.HeadWrapper;

public class FrameDecoder {
	
	protected Object decodeOtherTag(ByteBuffer data, HeadWrapper head) {
		String msg = "无效的协议标识, tag="+head.getTag();
		throw new ServiceException(msg);
	}
	
	protected Object decodeOtherCmd(ByteBuffer data, HeadWrapper head) {
		String msg = "无效的协议命令, cmd="+head.getCommand();
		throw new ServiceException(msg);
	}
	
	protected Object decodeOtherUserdata(ByteBuffer data, HeadWrapper head) {
		String msg = "无效的用户数据, userdata="+head.getUserdata();
		throw new ServiceException(msg);
	}
	
	public Object decode(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getTag()) {
		case Constant.PROTOCOL_TAG_RECORD: 
			result = decodeTagRecord(data, head);
			break;
		case Constant.PROTOCOL_TAG_ATTAINMENT:
			result = decodeTagAttainment(data, head);
			break;
		case Constant.PROTOCOL_TAG_PROP: 
			result = decodeTagProp(data, head);
			break;
		case Constant.PROTOCOL_TAG_SUBSCRIBE:
			result = decodeTagSubscribe(data, head);
			break;
		case Constant.PROTOCOL_TAG_PURCHASE: 
			result = decodeTagPurchase(data, head);
			break;
		case Constant.PROTOCOL_TAG_ACCOUNT:
			result = decodeTagAccount(data, head);
			break;
		case Constant.PROTOCOL_TAG_SYS_SERV:
			result = decodeTagSysServ(data, head);
			break;
		default: 
			result = decodeOtherTag(data, head);
		}
		return result;
	}
	
	private Object decodeTagRecord(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getCommand()) {
		case Constant.RECORD_CMD_SAVE: 
			result = decodeTagRecordCmdSave(data, head);
			break;
		case Constant.RECORD_CMD_READ: 
			result = decodeTagRecordCmdRead(data, head);
			break;
		case Constant.RECORD_CMD_QUERY_DESC_LIST: 
			result = decodeTagRecordCmdQueryDescList(data, head);
			break;
		case Constant.RECORD_CMD_UPDATE: 
			result = decodeTagRecordCmdUpdate(data, head);
			break;
		default: 
			result = decodeOtherCmd(data, head);
		}
		return result;
	}
	
	private Object decodeTagRecordCmdSave(ByteBuffer data, HeadWrapper head) {
		return null;
	}
	
	private Object decodeTagRecordCmdRead(ByteBuffer data, HeadWrapper head) {
		if (data.length() > 0) {
			GameRecord record = new GameRecord();
	    	record.readReadResponseData(data);
			return record;
		}
		return null;
	}
	
	private Object decodeTagRecordCmdQueryDescList(ByteBuffer data, HeadWrapper head) {
		GameRecordDesc[] descList = null;
		int num = data.readShort();
    	if (num > 0) {
	    	descList = new GameRecordDesc[num];
	    	for (int i = 0; i < num; ++i) {
	    		descList[i] = new GameRecordDesc();
	    		descList[i].readQueryResponseData(data);
	    	}
    	}
		return descList;
	}
	
	private Object decodeTagRecordCmdUpdate(ByteBuffer data, HeadWrapper head) {
		return null;
	}
	
	public Object decodeTagAttainment(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getCommand()) {
		case Constant.ATTAINMENT_CMD_SAVE: 
			result = decodeTagAttainmentCmdSave(data, head);
			break;
		case Constant.ATTAINMENT_CMD_READ: 
			result = decodeTagAttainmentCmdRead(data, head);
			break;
		case Constant.ATTAINMENT_CMD_UPDATE: 
			result = decodeTagAttainmentCmdUpdate(data, head);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_DESC_LIST: 
			result = decodeTagAttainmentCmdQueryDescList(data, head);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_RANKING_LIST: 
			result = decodeTagAttainmentCmdQueryRankingList(data, head);
			break;
		default: 
			result = decodeOtherCmd(data, head);
		}
		return result;
	}
	private Object decodeTagAttainmentCmdQueryRankingList(ByteBuffer data,
			HeadWrapper head) {
		GameRanking[] rankingList = null;
		int num = data.readShort();
    	if (num > 0) {
    		rankingList = new GameRanking[num];
	    	for (int i = 0; i < num; ++i) {
	    		rankingList[i] = new GameRanking();
	    		rankingList[i].readQueryResponseData(data);
	    	}
    	}
		return rankingList;
	}

	private Object decodeTagAttainmentCmdQueryDescList(ByteBuffer data,
			HeadWrapper head) {
		GameAttainmentDesc[] descList = null;
		int num = data.readShort();
    	if (num > 0) {
    		descList = new GameAttainmentDesc[num];
	    	for (int i = 0; i < num; ++i) {
	    		descList[i] = new GameAttainmentDesc();
	    		descList[i].readQueryResponseData(data);
	    	}
    	}
		return descList;
	}

	private Object decodeTagAttainmentCmdUpdate(ByteBuffer data,
			HeadWrapper head) {
		return null;
	}

	private Object decodeTagAttainmentCmdRead(ByteBuffer data, HeadWrapper head) {
		if (data.length() > 0) {
			GameAttainment attainment = new GameAttainment();
	    	attainment.readReadResponseData(data);
			return attainment;
		}
		return null;
	}

	private Object decodeTagAttainmentCmdSave(ByteBuffer data, HeadWrapper head) {
		return null;
	}

	public Object decodeTagProp(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getCommand()) {
		case Constant.PROP_CMD_QUERY_PROP_LIST: 
			result = decodeTagPropCmdQueryPropList(data, head);
			break;
		case Constant.PROP_CMD_QUERY_OWN_PROP_LIST: 
			result = decodeTagPropCmdQueryOwnPropList(data, head);
			break;
		case Constant.PROP_CMD_USE_PROPS:
			result = decodeTagPropCmdUseProps(data, head);
			break;
		case Constant.PROP_CMD_SYN_PROPS:
			result = decodeTagPropCmdSynProps(data, head);
			break;
		default: 
			result = decodeOtherCmd(data, head);
		}
		return result;
	}
	private Object decodeTagPropCmdSynProps(ByteBuffer data, HeadWrapper head) {
		return null;
	}

	private Object decodeTagPropCmdUseProps(ByteBuffer data, HeadWrapper head) {
		return null;
	}

	private Object decodeTagPropCmdQueryOwnPropList(ByteBuffer data,
			HeadWrapper head) {
		OwnProp[] propList = null;
		int num = data.readShort();
    	if (num > 0) {
    		propList = new OwnProp[num];
	    	for (int i = 0; i < num; ++i) {
	    		propList[i] = new OwnProp();
	    		propList[i].readQueryResponseData(data);
	    	}
    	}
    	return propList;
	}

	private Object decodeTagPropCmdQueryPropList(ByteBuffer data,
			HeadWrapper head) {
		Prop[] propList = null;
		int num = data.readShort();
    	if (num > 0) {
    		propList = new Prop[num];
	    	for (int i = 0; i < num; ++i) {
	    		propList[i] = new Prop();
	    		propList[i].readQueryResponseData(data);
	    	}
    	}
    	return propList;
	}

	public Object decodeTagSubscribe(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getCommand()) {
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE: 
			result = decodeTagSubscribeCmdSubscribe(data, head);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE: 
			result = decodeTagSubscribeCmdRecharge(data, head);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_BALANCE:
			result = decodeTagSubscribeCmdQueryBalance(data, head);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD:
			result = decodeTagSubscribeCmdQuerySubscribeRecord(data, head);
			break;
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT:
			result = decodeTagSubscribeCmdSubscribeProduct(data, head);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_WINSIDEGD:
			result = decodeTagSubscribeCmdRechargeWinsidegd(data, head);
			break;
		default: 
			result = decodeOtherCmd(data, head);
		}
		return result;
	}
	private Object decodeTagSubscribeCmdRechargeWinsidegd(ByteBuffer data,
			HeadWrapper head) {
		return new Integer(data.readInt());
	}

	private Object decodeTagSubscribeCmdSubscribeProduct(ByteBuffer data,
			HeadWrapper head) {
		return null;
	}

	private Object decodeTagSubscribeCmdQuerySubscribeRecord(ByteBuffer data,
			HeadWrapper head) {
		SubscribeRecord[] srList = null;
		int num = data.readShort();
    	if (num > 0) {
    		srList = new SubscribeRecord[num];
	    	for (int i = 0; i < num; ++i) {
	    		srList[i] = new SubscribeRecord();
	    		srList[i].readQueryResponseData(data);
	    	}
    	}
    	return srList;
	}

	private Object decodeTagSubscribeCmdQueryBalance(ByteBuffer data,
			HeadWrapper head) {
		return new Integer(data.readInt());
	}

	private Object decodeTagSubscribeCmdRecharge(ByteBuffer data,
			HeadWrapper head) {
		return new Integer(data.readInt());
	}

	private Object decodeTagSubscribeCmdSubscribe(ByteBuffer data,
			HeadWrapper head) {
		return null;
	}

	public Object decodeTagPurchase(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getCommand()) {
		case Constant.PURCHASE_CMD_PURCHASE_PROP: 
			result = decodeTagPurchaseCmdPurchaseProp(data, head);
			break;
		case Constant.PURCHASE_CMD_EXPEND: 
			result = decodeTagPurchaseCmdExpend(data, head);
			break;
		case Constant.PURCHASE_CMD_QUERY_PURCHASE_RECORD: 
			result = decodeTagPurchaseCmdQueryPurchaseRecord(data, head);
			break;
		default: 
			result = decodeOtherCmd(data, head);
		}
		return result;
	}
	private Object decodeTagPurchaseCmdQueryPurchaseRecord(ByteBuffer data,
			HeadWrapper head) {
		PurchaseRecord[] purchaseList = null;
		int num = data.readShort();
    	if (num > 0) {
    		purchaseList = new PurchaseRecord[num];
	    	for (int i = 0; i < num; ++i) {
	    		purchaseList[i] = new PurchaseRecord();
	    		purchaseList[i].readQueryResponseData(data);
	    	}
    	}
    	return purchaseList;
	}

	private Object decodeTagPurchaseCmdExpend(ByteBuffer data, HeadWrapper head) {
		return new Integer(data.readInt());
	}

	private Object decodeTagPurchaseCmdPurchaseProp(ByteBuffer data,
			HeadWrapper head) {
		return new Integer(data.readInt());
	}

	public Object decodeTagAccount(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getCommand()) {
		case Constant.ACCOUNT_CMD_QUERY_AUTHORIZATION: 
			result = decodeTagAccountCmdQueryAuthorization(data, head);
			break;
		case Constant.ACCOUNT_CMD_QUERY_SUB_PROPS:
			result = decodeTagAccountCmdQuerySubProps(data, head);
			break;
		case Constant.ACCOUNT_CMD_USER_LOGIN:
			result = decodeTagAccountCmdUserLogin(data, head);
			break;
		default: 
			result = decodeOtherCmd(data, head);
		}
		return result;
	}
	private Object decodeTagAccountCmdUserLogin(ByteBuffer data,
			HeadWrapper head) {
		LoginInfo info = new LoginInfo();
    	info.readLoginInfo(data);
    	return info;
	}

	private Object decodeTagAccountCmdQuerySubProps(ByteBuffer data,
			HeadWrapper head) {
		SubscribeProperties subProps = new SubscribeProperties();
    	subProps.readSubscribeProperties(data);
    	return subProps;
	}

	private Object decodeTagAccountCmdQueryAuthorization(ByteBuffer data,
			HeadWrapper head) {
		Authorization auth = new Authorization();
    	auth.readAuthorization(data);
    	return auth;
	}

	public Object decodeTagSysServ(ByteBuffer data, HeadWrapper head) {
		Object result = null;
		switch (head.getCommand()) {
		case Constant.SYS_SERV_CMD_SYN_TIME: 
			result = decodeTagSysServCmdSynTime(data, head);
			break;
		case Constant.SYS_SERV_CMD_ADD_FAVORITEGD:
			result = decodeTagSysServCmdAddFavoritegd(data, head);
			break;
		default: 
			result = decodeOtherCmd(data, head);
		}
		return result;
	}
	
	private Object decodeTagSysServCmdAddFavoritegd(ByteBuffer data,
			HeadWrapper head) {
		return null;
	}

	private Object decodeTagSysServCmdSynTime(ByteBuffer data, HeadWrapper head) {
		return new java.util.Date(data.readLong());
	}

}
