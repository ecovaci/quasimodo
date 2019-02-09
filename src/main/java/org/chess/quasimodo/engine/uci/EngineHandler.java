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

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chess.quasimodo.engine.UCIKeywords;
import org.chess.quasimodo.engine.model.Option;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.event.DefaultEngineOutputListener;
import org.chess.quasimodo.event.EngineOutputListener;
import org.chess.quasimodo.event.PlayerReadyAware;
import org.chess.quasimodo.util.ThreadUtils;
import org.chess.quasimodo.util.UCIUtils;
import org.chess.quasimodo.util.Utils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public class EngineHandler extends BasicEngineHandler implements UCIKeywords {
	private Log logger = LogFactory.getLog(EngineHandler.class);
	
	private File engineFile;
	private EnginePlugin enginePlugin; 	
	
	private boolean quitReceived;
	
    /**
     * Constructor.
	 * @param pathname The engine's file path.
     * @throws EngineException 
	 */
	public EngineHandler(String pathname) throws EngineException {
		Assert.isTrue(StringUtils.hasLength(pathname), "Path cannot be empty!");
		logger.debug("Load engine, filepath [" + pathname + "]");
		engineFile = new File(pathname);
		if (!engineFile.exists()) {
			throw new EngineException("Engine file not found on [" + pathname + "]");
		} else if (!engineFile.canExecute()) {
			throw new EngineException("Don't have execution rights on [" + pathname + "]");
		}
	}
	
	/**
	 * Starts the engine process.
	 * @param params The parameters to be delivered to command line.
	 * @throws EngineException
	 */
	public void start (String ... params) throws EngineException {
		String[] cmdarray;
		if (params != null) {
			cmdarray = new String[params.length + 1];
			System.arraycopy(params, 0, cmdarray, 1, params.length);
		} else {
			cmdarray = new String[1];
		}
		cmdarray[0] = engineFile.getAbsolutePath();
		try {
			enginePlugin = startEngine(cmdarray);
		} catch (IOException e) {
			throw new EngineException(e);
		}
		logger.info ("Engine [" + engineFile.getName() + "] has been started");
    }
    
	/**
	 * Sends uci command and listen for the response.
	 * @param outputListener The listener to receive feedback.
	 * @throws EngineException
	 */
    public void uci (final EngineOutputListener outputListener) throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + UCI + "]");
    	} else {
	    	enginePlugin.submitCommandAndListenWaitResult(
	    			UCI, UCIOK, outputListener, null);
    	}
    }
    
    /**
     * Sends uci command.
     * @throws EngineException
     */
    public void uci() throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + UCI + "]");
    	} else {
	    	enginePlugin.submitCommandAndListenWaitResult(
	    			UCI, UCIOK, new DefaultEngineOutputListener(), null);
    	}
    }
	
    public void newGame () throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + NEWGAME + "]");
    	} else {
    	    enginePlugin.submitCommandWaitResult(NEWGAME);
    	}
    }
    
    public void positionFen (String fen) throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + FEN + "]");
    	} else {
	    	enginePlugin.submitCommandWaitResult(
	    			MessageFormat.format(UCIUtils.POSITION_PATTERN_2, FEN, fen));
    	}
    }
    
    public void positionStart (String moves) throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + STARTPOS + "]");
    	} else {
	    	enginePlugin.submitCommandWaitResult(
	    			MessageFormat.format(UCIUtils.POSITION_PATTERN_3, STARTPOS, MOVES, moves));
    	}
    }
    
    public void goSearchMoves(
    		String moves,final EngineOutputListener outputListener) throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + SEARCHMOVES + "]");
    	} else {
	    	enginePlugin.submitCommandAndListen(
	    			MessageFormat.format(UCIUtils.GO_PATTERN_2, SEARCHMOVES, moves), outputListener, null);
    	}
    }
    
    public void goPonder (EngineOutputListener outputListener) throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + GO_PONDER + "]");
    	} else {
    	    enginePlugin.submitCommandAndListen(GO_PONDER, outputListener, null);
    	}
    }
    
    public void goInfinite (final EngineOutputListener outputListener, boolean waitResult) throws EngineException {
        goInfinite(null, outputListener, null, waitResult);
    }
    
    public void goInfinite (String moves, final EngineOutputListener outputListener,
    		PlayerReadyAware commandAware, boolean waitResult) throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + GO_INFINITE + "]");
    	} else {
	    	String command;
	    	if (moves != null) {
	    		command = MessageFormat.format(UCIUtils.GO_INFINITE_PATTERN, SEARCHMOVES, moves); 
	    	} else {
	    		command = GO_INFINITE;
	    	}
	    	if (waitResult) {
	    		enginePlugin.submitCommandAndListenWaitResult(command, outputListener, commandAware);
	    	} else {
	    	    enginePlugin.submitCommandAndListen(command, outputListener, commandAware);
	    	}
    	}
    }
    
    public void goInfinite (int timems, final EngineOutputListener outputListener) {
    	goInfinite(timems, null, outputListener);
    }
    
    public void goInfinite (int timemilliseconds, String moves, 
    		final EngineOutputListener outputListener) {
    	new Timer().schedule(new StopTimerTask(), timemilliseconds);
    	goInfinite(moves, outputListener, null, true);
    }
    
    private class StopTimerTask extends TimerTask {
    	@Override
		public void run() {
			enginePlugin.sendCommandNow(STOP);
			
		}
    }
    
    public void go (int movestogo, long wtime, long btime, 
    		long winc, long binc, final EngineOutputListener outputListener, PlayerReadyAware commandAware) {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + MOVESTOGO + "]");
    	} else {
	    	enginePlugin.submitCommandAndListen(
	    			MessageFormat.format(UCIUtils.GO_PATTERN_10, MOVESTOGO, movestogo, WTIME, wtime,
	    					BTIME, btime, WINC, winc, BINC, binc), outputListener, commandAware);
    	}
    }
    
    
    public void go (long wtime, long btime, 
    		long winc, long binc, final EngineOutputListener outputListener, PlayerReadyAware commandAware) {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + GO + "]");
    	} else {
	    	enginePlugin.submitCommandAndListen(
	    			MessageFormat.format(UCIUtils.GO_PATTERN_8, WTIME, wtime,
	    					BTIME, btime, WINC, winc, BINC, binc), outputListener, commandAware);
    	}
    }
    
    public void go (long wtime, long btime, 
    		final EngineOutputListener outputListener,PlayerReadyAware commandAware) {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + GO + "]");
    	} else {
	    	enginePlugin.submitCommandAndListen(
	    			MessageFormat.format(UCIUtils.GO_PATTERN_4, WTIME, Long.toString(wtime),
	    					BTIME, Long.toString(btime)), outputListener, commandAware);
    	}
    }
    
    public void go (long movetime, final EngineOutputListener outputListener,
    		PlayerReadyAware commandAware) {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + GO + "]");
    	} else {
	    	enginePlugin.submitCommandAndListen(
	    			MessageFormat.format(UCIUtils.GO_PATTERN_2, MOVETIME, 
	    					Utils.INTEGER_FORMAT.format(movetime)), outputListener, commandAware);
    	}
    }
    
    public void goAndWaitResult (long movetime, final EngineOutputListener outputListener,
    		PlayerReadyAware commandAware) {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + GO + "]");
    	} else {
	    	enginePlugin.submitCommandAndListenWaitResult(
	    			MessageFormat.format(UCIUtils.GO_PATTERN_2, MOVETIME, 
	    					Utils.INTEGER_FORMAT.format(movetime)), outputListener, commandAware);
    	}
    }
    
    public void searchMate () {
    	
    }
    
    public void stopCommandAndListen () throws EngineException {
    	enginePlugin.requestStopRunningCommandAndListen();
		enginePlugin.sendCommandNow(ISREADY);
    }
    
    public boolean isRunning () throws EngineException {
    	return enginePlugin.isCommandAndListenRunning();
    }
    
    public void setOption (String name, String value) throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + SETOPTION + "]");
    	} else {
    	    enginePlugin.submitCommandWaitResult(MessageFormat.format(UCIUtils.OPTION_PATTERN, name, value));
    	}
    }
    
    public void setOption (Option option) throws EngineException {
    	setOption (option.name, option.value);
    }
    
    public void ponderhit () throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + PONDERHIT + "]");
    	} else {
    	    enginePlugin.sendCommandNow(PONDERHIT);
    	}
    }
    
    public void isReady () throws EngineException {
    	if (isQuitReceived()) {
    	    logger.info("Quit received, command ignored [" + ISREADY + "]");
    	} else {
    		enginePlugin.submitCommandAndListenWaitResult(
        			ISREADY, READYOK, new DefaultEngineOutputListener(), null);
    		//enginePlugin.sendCommandNow(ISREADY);
    	}
    }
    
    
    
    /**
     * It signals that the engine is about to quit.
     * @return <code>true</code> if the quit process has been started.
     */
    public boolean isQuitReceived () {
    	return this.quitReceived;
    }
    
    /**
     * Stops the current engine's activity.<br>
     * The engine remains loaded and ready for futher commands. 
     * @throws EngineException
     */
    public void stop () throws EngineException {
    	logger.info("Stop listening to the current command");
    	stopCommandAndListen();
    	//wait to finish
    	ThreadUtils.waitFor(500);
		logger.info("Send stop command for engine to cancel the current task");
    	enginePlugin.sendCommandNow(STOP);
    	//wait for the stop 
    	//command to finish
    	ThreadUtils.waitFor(500);
    }
    
	/**
	 * Close engine in a gracefully manner: if a command is currently running,
	 * then stop it; send quit command, wait a while and kill the engine process 
	 * if it is still active.
	 * @throws EngineException
	 */
    public void quit () throws EngineException {
    	logger.info("Start quit process ...");
    	if (enginePlugin.isCommandAndListenRunning()) {
    		stop();
    	}
    	quitReceived = true;
    	enginePlugin.sendCommandNow(QUIT);
    	//wait for the quit command to take effect
    	ThreadUtils.waitFor(500);
    	//kill it if it is still alive
		if (isAlive()) {
			logger.info("Engine's process is still alive, let's kill it");
			killEngine();
		}
		logger.info("Quit process ended.");
    }
    
    public String getEngineFilepath () {
    	return engineFile.getAbsolutePath();
    }
}
