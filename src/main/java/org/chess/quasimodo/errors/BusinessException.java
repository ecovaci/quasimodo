package org.chess.quasimodo.errors;

public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4053042783266154925L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message + "\n Cause: " + cause != null ? cause.getMessage() : "N/A");
	}
	
}
