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
package org.chess.quasimodo.engine.uci;

import java.io.IOException;

public class BasicEngineHandler {
    private Process process;
    private WaitToFinish waitingThread;
    
    public BasicEngineHandler() {
    	waitingThread = new WaitToFinish();
    	waitingThread.setDaemon(true);
	}
    
	/**
	 * Starts the engine with given parameters.
	 * @param cmdarray The parameters 
	 * @return The corresponding plugin to talk to the engine.
	 * @throws IOException 
	 */
	protected EnginePlugin startEngine(String[] cmdarray) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		process = runtime.exec(cmdarray);
		waitingThread.start();
		return new EnginePlugin(process, Thread.NORM_PRIORITY);
	}
	
	/**
     * Forces engine's process to stop. Does nothing if the engine is already stopped.
     */
	public void killEngine() {
		if (process != null) {
		    process.destroy();
		}
	}
	
	/**
	 * Check to see if engine's process is active.
	 * @return <code>true</code> only if the engine's process is up and running.
	 */
	public boolean isAlive () {
		return waitingThread.isAlive();
	}
	
	//This thread will live as long as 
	//the engine's process will be alive.
	private class WaitToFinish extends Thread {
		@Override
		public void run() {
			if (process != null) {
			    try {
					process.waitFor();
				} catch (InterruptedException e) {
					//ignore exception
				}
			}
		}
	}
}
