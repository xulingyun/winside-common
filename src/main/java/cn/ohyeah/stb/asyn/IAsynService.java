package cn.ohyeah.stb.asyn;

public interface IAsynService {
	abstract public AsynResponse asynRequest(AsynRequest req) throws InterruptedException;
}
