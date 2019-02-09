package org.chess.quasimodo.errors;

@SuppressWarnings("serial")
public class IllegalOperationException extends AppException {

	public IllegalOperationException() {
		super();
	}

	public IllegalOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalOperationException(String message) {
		super(message);
	}

	public IllegalOperationException(Throwable cause) {
		super(cause);
	}

}
