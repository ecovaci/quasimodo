/*******************************************************************************
 * Quasimodo - a chess interface for playing and analyzing chess games.
 * Copyright (C) 2011 Eugen Covaci.
 * All rights reserved.
 *  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 ******************************************************************************/
package org.chess.quasimodo.concurrent;

import org.springframework.util.ErrorHandler;

/**
 * 
 * @author Eugen Covaci
 */
public abstract class ErrorHandlingThread extends Thread implements ErrorHandlingRunnable {
    /**
     * Handler for execution's errors.(May be null)
     */
    private ErrorHandler errorHandler;
    
    public ErrorHandlingThread() {
		super();
	}

	public ErrorHandlingThread(String name) {
		super(name);
	}

	public ErrorHandlingThread(ThreadGroup group, String name) {
		super(group, name);
	}

	public ErrorHandlingThread(ErrorHandler errorHandler) {
		super();
		this.setErrorHandler(errorHandler);
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
    public void run() {
		try {
			doWork();
		} catch (Exception e) {
			//Logger.getLogger(getClass()).error(e, e);
			if (errorHandler != null) {
				try {
					errorHandler.handleError(e);
				} catch (Exception e1) {
					//Logger.getLogger(getClass()).warn("Method contract breaked, shouldn't have any error there", e1);
				}
			}
		}
    }
}
