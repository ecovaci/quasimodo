/**
 * 
 */
package org.chess.quasimodo.errors;

/**
 * @author Eugen Covaci
 *
 */
public class PGNGenerateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6183325926249378032L;

	/**
	 * 
	 */
	public PGNGenerateException() {
	}

	/**
	 * @param message
	 */
	public PGNGenerateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public PGNGenerateException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PGNGenerateException(String message, Throwable cause) {
		super(message, cause);
	}

}
