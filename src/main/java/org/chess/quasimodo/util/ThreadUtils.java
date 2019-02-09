package org.chess.quasimodo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);
	
	/**
	 * Quietly sleeps the current thread.(ignoring any error)
	 * @param timemiliseconds  the length of time to sleep in milliseconds.
	 */
    public static void waitFor (long timemiliseconds) {
    	try {
			Thread.sleep(timemiliseconds);
		} catch (InterruptedException e) {
			logger.error("Cannot interrupt current thread", e);
		}
    }
}
