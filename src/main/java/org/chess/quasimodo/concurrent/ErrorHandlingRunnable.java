package org.chess.quasimodo.concurrent;

import org.springframework.util.ErrorHandler;

public interface ErrorHandlingRunnable {
	
	/**
	 * The equivalent of {@link Thread#run()} method.
	 * @throws Exception
	 * @see Thread#run()
	 */
	void doWork() throws Exception;

	/**
	 * Setter for execution's error handler. This method must be called
	 * before the execution starts.
	 * @param errorHandler Execution's error handler.(May be null)
	 * @see ErrorHandler
	 */
	void setErrorHandler(ErrorHandler errorHandler);
}
