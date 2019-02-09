package org.chess.quasimodo.errors;

@SuppressWarnings("serial")
public class InvalidPGNException extends BusinessException {

	public InvalidPGNException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPGNException(String message) {
		super(message);
	}

}
