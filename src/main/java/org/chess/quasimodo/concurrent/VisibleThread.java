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

import org.chess.quasimodo.gui.BusyDialog;
import org.springframework.util.ErrorHandler;


public abstract class VisibleThread extends Thread implements VisibleRunnable {
   
    /**
     * The dialog that will acompany the thread execution.
     */
    private BusyDialog dialog;
    
    /**
     * Handler for execution's errors.(May be null)
     */
    private ErrorHandler errorHandler;
    
    /**
     * Default constructor.
     */
    public VisibleThread() {
		super();
		dialog = new BusyDialog();
	}
    
    /**
     * Constructor.
     * @param message The message to be shown while thread is running. 
     */
    public VisibleThread (String message) {
    	this(message, null);
    	
	}
    
    /**
     * Contructor.
     * @param message The message to be shown while thread is running.
     * @param errorHandler The error handler.
     */
    public VisibleThread (ErrorHandler errorHandler) {
    	this(null, errorHandler);
	}
    
    /**
     * Contructor.
     * @param message The message to be shown while thread is running.
     * @param errorHandler The error handler.
     */
    public VisibleThread (String message, ErrorHandler errorHandler) {
    	this();
    	dialog.setMessage(message);
    	this.errorHandler = errorHandler;
	}
    
	@Override
    public void run() {
		dialog.setVisible(true);
		try {
			doWork();
			dialog.setVisible(false);
		} catch (Exception e) {
			dialog.setVisible(false);
			//Logger.getLogger(getClass()).error(e, e);
			if (errorHandler != null) {
				try {
					errorHandler.handleError(e);
				} catch (Exception e1) {
					//Logger.getLogger(getClass()).error("Method contract breaked, shouldn't have any error here", e1);
				}
			}
		}
    }
    
    /* (non-Javadoc)
	 * @see org.chess.quasimodo.annotation.VisibleRunnable#doWork()
	 */
    @Override
	public abstract void doWork () throws Exception ;
    
    /* (non-Javadoc)
	 * @see org.chess.quasimodo.annotation.VisibleRunnable#setMessage(java.lang.String)
	 */
    @Override
	public void setMessage (String message) { 
    	dialog.setMessage(message);
    }
    
	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
    
    /* (non-Javadoc)
	 * @see org.chess.quasimodo.annotation.VisibleRunnable#hide()
	 */
    @Override
	public void hide () {
    	dialog.dispose();
    }
}
