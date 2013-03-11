package cn.ohyeah.stb.game;

/**
 * 充值服务类
 * @author xiaochen
 *
 */
public class Recharge {
	
	IEngine engine;
	
	public Recharge(IEngine engine){
		this.engine = engine;
	}
	
	public int recharge(){
		System.out.println("telcom:"+Configurations.getInstance().getTelcomOperators());
		if(Configurations.getInstance().isTelcomOperatorsTianweiSZ()){
			return new StateRechargeWinsideTW(engine).recharge();    //掌世界天威
		}else if(Configurations.getInstance().isTelcomOperatorsTelcomhn()
				&& Configurations.getInstance().isServiceProviderWinside()){
			return new StateRechargehn(engine).recharge();			 //掌世界湖南
		}else if(Configurations.getInstance().isTelcomOperatorsTelcomhn()
				&& Configurations.getInstance().isServiceProviderShengYi()){
			return new StateRechargeShengyihn(engine).recharge();    //盛翼湖南
		}else{
			return new StateRecharge(engine).recharge();
		}
	}

}
