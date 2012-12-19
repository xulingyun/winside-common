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
		if(Configurations.getInstance().isTelcomOperatorsTianweiSZ()){
			return new StateRechargeWinsideTW(engine).recharge();
		}if(Configurations.getInstance().isTelcomOperatorsTelcomhn()
				&& Configurations.getInstance().isServiceProviderWinside()){
			return new StateRechargehn(engine).recharge();
		}else{
			return new StateRecharge(engine).recharge();
		}
	}

}
