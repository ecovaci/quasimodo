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
package org.chess.quasimodo.engine;


import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.book.BookEntry;
import org.chess.quasimodo.concurrent.ErrorHandlingThread;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.domain.logic.Clock;
import org.chess.quasimodo.domain.logic.EngineBoard;
import org.chess.quasimodo.domain.logic.MoveSource;
import org.chess.quasimodo.domain.logic.Player;
import org.chess.quasimodo.engine.model.BestMove;
import org.chess.quasimodo.engine.model.Info;
import org.chess.quasimodo.errors.BusinessException;
import org.chess.quasimodo.errors.DefaultErrorHandler;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.errors.IllegalOperationException;
import org.chess.quasimodo.event.AbstractOutputListener;
import org.chess.quasimodo.event.EngineOutputAware;
import org.chess.quasimodo.gui.EnginePanel;
import org.chess.quasimodo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
import org.springframework.util.StringUtils;



@Scope ("prototype")
@Component ("engine")
public class Engine extends AbstractEngine implements MoveSource, UCIKeywords {
	private final Logger logger = LoggerFactory.getLogger(Engine.class);
	
	@Autowired
	protected Config config;
	
	@Autowired
	private DefaultErrorHandler errorHandler;
	
	@Autowired
	private  QuasimodoContext context;
	
    @Autowired
    private EngineBoard engineBoard;
	
    private EngineOutputAware engineOutputAware;
    
	private AbstractOutputListener outputListener = new DefaultOutputListener();
	
	private Player player;
    
    @Override
	public void bindPlayer(Player player) {
		if (this.player != null) {
			throw new IllegalOperationException("Another engine player is already binded");
		}
		try {
			bootstrap((EngineModel)player.getModel());
		} catch (EngineException e) {
			logger.error("Error on bootstraping engine [" + getEngineHandler().getEngineFilepath() + "]", e);
			throw new RuntimeException (e);
		}
		this.player = player;
	}
	
	@Override
	public void removePlayer() {
		if(player != null) {
			throw new IllegalOperationException("Once binded, an engine player cannot be removed");
		}
	}

	@Override
	public void myTurn() {
		logger.info("Engine's turn");
		context.setIgnoreUserInput(true);
		((EnginePanel)engineOutputAware).clearEngineOutput();
		new ThinkingThread(errorHandler).start();
	}
	
	private void think () throws EngineException {
		try {
			// init the engine board with the current fen
			String fen = context.getCurrentGame().getCurrentPosition();
			engineBoard.setPosition(fen);
			
			Clock clock = getGameClock();
            // check for book moves
			if (hasOpeningBook()) {
				logger.debug("Position fen: " + fen);
				long zobristKey = engineBoard.zobristHashKey();
				logger.debug("Position zobristKey: " + Long.toHexString(zobristKey));
				BookEntry bookEntry = getOpeningBook().randomBookEntry(zobristKey);
				if (bookEntry != null) {
					String bookUCIMove = engineBoard.toUCINotation(bookEntry);
					logger.debug("Book move found: " + bookUCIMove);
					clock.onPlayerReady();
					player.makeMove(bookUCIMove);
					return;
				}
			}
			getEngineHandler().positionFen(fen);
			SetUpGameModel gameModel = config.getSelectedGameModel();
			if (gameModel.isFriendly()) {
				getEngineHandler().go(config.getDefaultEngineThinkTime(), outputListener, clock);
			} else if (gameModel.isOneTimeControl()) {
				getEngineHandler().go(clock.getWhiteRemainingTime(), clock.getBlackRemainingTime(), 
						outputListener, clock);
			} else if (gameModel.isThreeTimeControls()) {
				//TODO - implementation
			}
		} catch (Exception e) {
			throw new EngineException("Error while thinking thrown by [" 
					+ getEngineHandler().getEngineFilepath() + "] engine", e);
		}
	}
	
	private Clock getGameClock() {
		if (context.existCurrentGame()) {
			return context.getCurrentGame().getClock();
		} 
		throw new BusinessException ("Game not started!");
	}
	
	@Override
	public void positionChanged() {
		logger.info("Engine::onPositionChanged()");
	}
	
	public boolean isPlayerBinded() {
		return player != null;
	}

	public void registerEngineOutputListener(EngineOutputAware engineOutputAware) {
		this.engineOutputAware = engineOutputAware;
	}
	
	private class ThinkingThread extends ErrorHandlingThread {
        
		ThinkingThread(ErrorHandler errorHandler) {
			super(errorHandler);
		}

		@Override
		public void doWork() throws Exception {
			think();
		}
	}
	
	private class DefaultOutputListener extends AbstractOutputListener {
		@Override
		public void onReceiveBestMove(BestMove bestMove) throws EngineException {
			getEngineHandler().stopCommandAndListen();
			player.makeMove(bestMove.move);
		}

		@Override
		public void onReceiveInfo(Info info) {
			if (StringUtils.hasLength(info.pv)) {
				engineOutputAware.setPV(engineBoard.parseVariation(info),
						engineBoard.parseScore(info, false), info.depth, info.nodes, info.time);
			} 
			String score = engineBoard.parseScore(info, true);
			if (StringUtils.hasLength(score)) {
			    engineOutputAware.setScore(score);
			}
			if (StringUtils.hasLength(info.currmove)) {
				engineOutputAware.setCurrentMove(engineBoard.formatCurrentMove(info.currmove));
			}
			if (Utils.isPositiveInteger(info.nps)) {
				engineOutputAware.setNps(Integer.parseInt(info.nps));
			}
			if (Utils.isPositiveInteger(info.depth)) {
				engineOutputAware.setDepth(Integer.parseInt(info.depth));
			}
		}
	}

}
