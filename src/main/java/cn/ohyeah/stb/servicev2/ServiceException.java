package cn.ohyeah.stb.servicev2;

public class ServiceException extends RuntimeException {
	public ServiceException() {
	}

	public ServiceException(String s) {
		super(s);
	}
}
