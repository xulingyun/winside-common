package cn.ohyeah.stb.res;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import cn.ohyeah.itvgame.model.OwnProp;
import cn.ohyeah.stb.game.EngineService;
import cn.ohyeah.stb.game.ServiceWrapper;

/**
 * 游戏道具管理器
 * @author maqian
 * @version 1.0
 */
public class GamePropsManager {
	private static GamePropsManager instance;
	private static EngineService service;
	private Hashtable propClasses;
	private Hashtable propidRange;
	private GameProp[] propsList;
	private short[] propsLimit;
	private short[] propsCount;
	private short[] propsChange;
	private boolean supportPropsLimit;
	private String errorMessage;
	
	public static void registerService(EngineService engineService) {
		service = engineService;
		instance = new GamePropsManager();
	}
	
	public static GamePropsManager getInstance() {
		return instance;
	}
	
	public void registerGamePropClass(Class c) {
		if (propClasses == null) {
			propClasses = new Hashtable(4);
		}
		propClasses.put(c.getName().toUpperCase(), c);
	}
	
	public void registerGamePropIdRange(Class c, int startId, int endId) {
		if (propidRange == null) {
			propidRange = new Hashtable(4);
		}
		propidRange.put(c.getName().toUpperCase(), new int[]{startId, endId});
	}
	
	public void loadGameProps(String path) {
		InputStream is = service.getClass().getResourceAsStream(path);
		DataInputStream dis = new DataInputStream(is);
		try {
			String magic = dis.readUTF();
			if (!"props".equals(magic)) {
				throw new RuntimeException("数据文件标识错误");
			}
			short version = dis.readShort();
			if (version != 1)  {
				throw new RuntimeException("数据文件版本错误");
			}
			short props = dis.readShort();
			if (props <= 0) {
				throw new RuntimeException("道具数量无效");
			}
			short propKinds = dis.readShort();
			if (propKinds <= 0) {
				throw new RuntimeException("道具种类无效");
			}
			propsList = new GameProp[props];
			propsCount = new short[props];
			propsChange = new short[props];
			for (int i = 0; i < propKinds; ++i) {
				String name = dis.readUTF();
				Class c = (Class)propClasses.get(name);
				int num = dis.readShort();
				for (int j = 0; j < num; ++j) {
					GameProp prop = (GameProp)c.newInstance();
					prop.deserialize(dis);
					propsList[prop.getId()] = prop;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("加载道具信息失败，原因："+e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException("加载道具信息失败，原因："+e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("加载道具信息失败，原因："+e.getMessage());
		}
		finally {
			try {
				if (dis != null) {
					dis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setPropsLimit(short[] limit) {
		supportPropsLimit = true;
		propsLimit = limit;
	}
	
	public void setPropsLimit(short limit) {
		supportPropsLimit = true;
		propsLimit = new short[propsList.length];
		for (int i = 0; i < propsList.length; ++i) {
			propsLimit[i] = (short)limit;
		}
	}
	
	private void resetPropsChange() {
		for (int i = 0; i < propsChange.length; ++i) {
			propsChange[i] = 0;
		}
	}
	
	public boolean useProp(GameProp prop, int n) {
		return useProp(prop.getId(), n);
	}
	
	public boolean useProp(int propId, int n) {
		if (propsCount[propId] >= n) {
			propsCount[propId] -= n;
		}
		return false;
	}
	
	public int getPropCount(int propId) {
		return propsCount[propId];
	}
	
	public int getPropCount(GameProp prop) {
		return getPropCount(prop.getId());
	}
	
	public boolean judgeAddProp(int propId, int n) {
		boolean canAdd = true;
		if (supportPropsLimit && propsCount[propId]+n > propsLimit[propId]) {
			canAdd = false;
		}
		return canAdd;
	}
	
	public boolean judgeAddProp(GameProp prop, int n) {
		return judgeAddProp(prop.getId(), n);
	}
	
	private void addProp(int propId, int n, boolean change) {
		if (supportPropsLimit) {
			if (propsCount[propId]+n > propsLimit[propId]) {
				if (change) {
					propsChange[propId] += propsLimit[propId]-propsCount[propId];
				}
				propsCount[propId] = propsLimit[propId];
			}
			else {
				if (change) {
					propsChange[propId] += n;
				}
				propsCount[propId] += n;
			}
		}
		else {
			if (change) {
				propsChange[propId] += n;
			}
			propsCount[propId] += n;
		}
	}
	
	public void addProp(int propId, int n) {
		addProp(propId, n, true);
	}
	
	public void addProp(GameProp prop, int n) {
		addProp(prop.getId(), n);
	}
	
	private int getPropIdByServerPropId(int serverPropId) {
		int propId = -1;
		for (int i = 0; i < propsList.length; ++i) {
			if (propsList[i].getPropId() == serverPropId) {
				propId = i;
				break;
			}
		}
		return propId;
	}
	
	public int getPropSize(Class c) {
		int[] range = (int[])propidRange.get(c.getName().toUpperCase());
		return range[1]-range[0]+1;
	}
	
	public int getPropSize() {
		return propsList.length;
	}
	
	public GameProp[] getPropList(Class c) {
		GameProp[] props = null;;
		if (propClasses.size() > 1) { 
			int[] range = (int[])propidRange.get(c.getName().toUpperCase());
			props = new GameProp[range[1]-range[0]+1];
			System.arraycopy(propsList, range[0], props, 0, props.length);
		}
		else {
			props = propsList;
		}
		return props;
	}
	
	public GameProp[] getPropList() {
		return propsList;
	}

	public boolean pullProps() {
		boolean success = false;
		try {
			ServiceWrapper sw = service.getServiceWrapper();
			OwnProp[] ops = sw.queryOwnPropList();
			if (sw.isServiceSuccessful()) {
				success = true;
				if (ops!=null && ops.length>0) {
					for (int i = 0; i < ops.length; ++i) {
						int propId = getPropIdByServerPropId(ops[i].getPropId());
						propsCount[propId] = (short)ops[i].getCount();
					}
				}
			}
			else {
				success = false;
				errorMessage = sw.getServiceMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			success = false;
			errorMessage = e.getMessage();
		}
		return success;
	}
	
	public boolean pushProps() {
		boolean success = false;
		try {
			int[] propIds = getSynPropIds();
			if (propIds!=null && propIds.length>0) {
				int[] synPropIds = getSynServerPropIds(propIds);
				int[] synPropCounts = getSynPropCounts(propIds);
				ServiceWrapper sw = service.getServiceWrapper();
				sw.synProps(synPropIds, synPropCounts);
				if (sw.isServiceSuccessful()) {
					success = true;
					resetPropsChange();
				}
				else {
					success = false;
				}
			}
			else {
				success = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	private int[] getSynPropCounts(int[] propIds) {
		int[] counts = null;
		if (propIds!=null && propIds.length>0) {
			counts = new int[propIds.length];
			for (int i = 0; i < propIds.length; ++i) {
				counts[i] = propsCount[propIds[i]];
			}
		}
		return counts;
	}
	
	private int[] getSynServerPropIds(int[] propIds) {
		int[] srvPropIds = null;
		if (propIds!=null && propIds.length>0) {
			srvPropIds = new int[propIds.length];
			for (int i = 0; i < propIds.length; ++i) {
				srvPropIds[i] = propsList[propIds[i]].getPropId();
			}
		}
		return srvPropIds;
	}
	
	private int[] getSynPropIds() {
		int count = 0;
		for (int i = 0; i < propsChange.length; ++i) {
			if (propsChange[i] != 0) {
				++count;
			}
		}
		int[] propIds = null;
		if (count > 0) {
			propIds = new int[count];
			for (int i = 0; i < propsChange.length; ++i) {
				if (propsChange[i] != 0) {
					propIds[i] = i;
				}
			}
		}
		return propIds;
	}
	
	
	public boolean buyProp(int propId, int n) {
		boolean success = false;
		errorMessage = "";
		try {
			ServiceWrapper sw = service.getServiceWrapper();
			sw.purchaseProp(propId, n, "购买道具"+propsList[propId].getName());
			if (sw.isServiceSuccessful()) {
				success = true;
				addProp(propId, n, false);
			}
			else {
				success = false;
				errorMessage = sw.getServiceMessage();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			success = false;
			errorMessage = e.getMessage();
		}
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
