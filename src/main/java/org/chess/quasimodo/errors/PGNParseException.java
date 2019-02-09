package org.chess.quasimodo.errors;

public class PGNParseException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2924923304497903089L;

	public PGNParseException() {
		super();
	}

	public PGNParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PGNParseException(String message) {
		super(message);
	}

	public PGNParseException(Throwable cause) {
		super(cause);
	}

}
