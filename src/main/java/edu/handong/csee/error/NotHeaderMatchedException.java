package edu.handong.csee.error;

public class NotHeaderMatchedException extends Exception{
	public NotHeaderMatchedException() {
		super("Header is not matched");
	}
	
	public NotHeaderMatchedException(String message) {
		super(message);
	}
}
