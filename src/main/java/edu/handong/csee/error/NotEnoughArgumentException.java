package edu.handong.csee.error;

public class NotEnoughArgumentException extends Exception {
	public NotEnoughArgumentException() {
		super("No CLI argument Exception! Please put a file path and save path");
	}
	
	public NotEnoughArgumentException(String message) {
		super(message);
	}
}
