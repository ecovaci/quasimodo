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
package org.chess.quasimodo.domain.logic;

import java.text.MessageFormat;

import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.eco.OpeningScout;
import org.chess.quasimodo.engine.AbstractEngine;
import org.chess.quasimodo.engine.Engine;
import org.chess.quasimodo.engine.EngineRepository;
import org.chess.quasimodo.errors.BusinessException;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.event.GameStatusAware;
import org.chess.quasimodo.gui.BoardPanel;
import org.chess.quasimodo.gui.EnginePanel;
import org.chess.quasimodo.gui.MainFrame;
import org.chess.quasimodo.message.Message;
import org.chess.quasimodo.message.MessageHandler;
import org.chess.quasimodo.pgn.domain.Opening;
import org.chess.quasimodo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Incapsulates the logique behind of a chess game.
 * @author Eugen Covaci
 */
@Scope ("prototype")
@Component ("game")
public class Game extends BasicForm<SetUpGameModel> implements GameStatusAware {
	
	private final Logger log = LoggerFactory.getLogger(Game.class);
	
	/**
	 * The white player. 
	 */
	private Player whitePlayer;
	
	/**
	 * The black player.
	 */
	private Player blackPlayer;
	
	/**
	 * The board owned by this game.
	 */
	private Board board;
	
	/**
	 * The current status of the game.
	 */
	private Status status = Status.UNDECIDED;
	
	@Autowired
	private EngineRepository engineRepository;
	
	@Autowired
	private MainFrame frame;
	
	@Autowired
	@Qualifier ("boardPanel")
	private BoardPanel boardView;
	
	@Autowired
	private EnginePanel engineView;
	
	@Autowired
	private EnginePanel extraEngineView;
	
	@Autowired
	private Clock  clock;
	
	@Autowired
	private Notation notation;
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private OpeningScout openingScout;
	
	public Game() {
		board = new Board();
	}

	/**
	 * Register a player for this game.
	 * @param player The player.
	 * @param color The playing color.
	 */
	protected void registerPlayer (Player player, ChessColor color) {
    	if (color == ChessColor.WHITE) {
    		whitePlayer = player;
    	} else {
    		blackPlayer = player;
    	}
    }
	
	public void subscribePlayer (Player player) throws EngineException {
		if (player.isUser()) {
			player.subscribeTo(boardView);
		} else {
			Engine engine = engineRepository.createEngine();
			player.subscribeTo(engine);
		}
	}
	
	private void registerEngineListeners () {
		if (isEngineVSEngine()) {
		    engineView.setEngineName(whitePlayer.getModel().toString());
		    ((AbstractEngine)(whitePlayer.getMoveSource())).registerEngineOutputListener(engineView);
		    extraEngineView.setEngineName(blackPlayer.getModel().toString());
		    ((AbstractEngine)(blackPlayer.getMoveSource())).registerEngineOutputListener(extraEngineView);
		    frame.showExtraEnginePanel();
		} else if (isUserVSEngine ()) {
			engineView.setEngineName(blackPlayer.getModel().toString());
			((AbstractEngine)(blackPlayer.getMoveSource())).registerEngineOutputListener(engineView);
		} else if (isEngineVSUser()) {
			engineView.setEngineName(whitePlayer.getModel().toString());
			((AbstractEngine)(whitePlayer.getMoveSource())).registerEngineOutputListener(engineView);
		} 
	}
	
	public void start () {
		registerEngineListeners();
		//notify the player to make the next move
		playerToMove().myTurn();
		//start recording time
		clock.setUpForGame();
	}
	
	public boolean isEngineVSEngine () {
		return !whitePlayer.isUser() && !blackPlayer.isUser();
	}
	
	public boolean isUserVSEngine () {
		return whitePlayer.isUser() && !blackPlayer.isUser();
	}
	
	public boolean isEngineVSUser () {
		return !whitePlayer.isUser() && blackPlayer.isUser();
	}
	
	public boolean isUserVSUser () {
		return whitePlayer.isUser() && blackPlayer.isUser();
	}
	
	public boolean whiteToMove () {
		return board.isWhiteToMove();
	}
	
	public boolean isSoundRequired () {
		return whitePlayer.isUser() || blackPlayer.isUser();
	}
	
	public Player playerToMove () {
		if (whiteToMove()) {
			return whitePlayer;
		} else {
			return blackPlayer;
		}
	}
	
	public Player playerToWait () {
		if (whiteToMove()) {
			return blackPlayer;
		} else {
			return whitePlayer;
		}
	}
	
	public Player getWhitePlayer() {
		return whitePlayer;
	}

	public Player getBlackPlayer() {
		return blackPlayer;
	}

	public Status getStatus() {
		return status;
	}
	
	public boolean isOver () {
		return status.isGameOver();
	}

	public Notation getNotation() {
		return notation;
	}

	protected synchronized boolean makeMove (final Move move) {
		if (status.isGameOver()) {
			return true;
		}
		boolean result = board.makeMove(move);
		if (result) {
			if (playerToWait().isUser()) {
				boardView.piecesChanged(board.getPieces());
			} else {
				boardView.playMove(move);
			}
			notation.afterMove(playerToWait().getColor(), board.getPlyNumber(), board.getLastMoveSAN());
			onGameStatusChanged(board.getGameStatus());
			clock.onMove(getPlyNumber());
			
			if (openingScout.isActive()) {
				new SearchOpeningThread(board.getSANMoveList()).start();
			}
			
			if (status.isGameOver()) {
				showGameResult(status);
			} else {
				playerToMove().myTurn();
			}
		}
		return result;
	}
	
	private void showGameResult (Status status) {
		String message;
		if (status.isPureDraw()) {
			message = Message.DRAW;
		} else if (status.isPureMate()) {
			message = MessageFormat.format(Message.WIN, playerToWait().getColor().asString());
		} else if (status.isTimeout()) {
			message = MessageFormat.format(Message.TIMEOUT, playerToWait().getColor().asString());
		} else {
			message = "Result: " + status.getDescription();
		}
		
		messageHandler.showInfoMessage("Game over!", message);
	}
	
	private void switchUserPlayersOnBoard () {
		if (board.isWhiteToMove()) {
		    switchPlayerOnBoard (blackPlayer, whitePlayer);
		} else {
			switchPlayerOnBoard (whitePlayer, blackPlayer);
		}
	}
	
	private void switchPlayerOnBoard (Player toGo, Player toStay) {
		toGo.unsubscribe();
		toStay.subscribeTo(boardView);
	}
	
	protected Move requestMove (int from, int to) {
		return board.getLegalMove(from, to);
	}
	
	public String getCurrentPosition() {
		return board.exportFEN();
	}
	
	public Piece[] getPieces () {
		return board.getPieces();
	}
	
	private void firePositionChangeEvent () {
		whitePlayer.getMoveSource().positionChanged();
		blackPlayer.getMoveSource().positionChanged();
	}
	
	public int getPlyNumber () {
		return board.getPlyNumber();
	}
	
	public int getMovesNumber () {
		return board.getMovesNumber();
	}
	
	public void replacePosition (Position position) {
		notation.reset();
		board.replacePosition(position);
		boardView.piecesChanged(board.getPieces());
		firePositionChangeEvent();
		playerToMove().myTurn();
	}

	public Clock getClock() {
		return clock;
	}
	
	public String getTitle () {
		StringBuffer title = new StringBuffer();
		title.append(whitePlayer.getModel());
		title.append(" - ");
		title.append(blackPlayer.getModel());
		return title.toString();
	}
	
	public String getGameInfo () {
		return getModel().getTimeDescription();
	}
	
	public void resetView () {
		boardView.reset();
		notation.reset();
		frame.clearStatusMessage();
	}
	
	public void pause () {
		//TODO - implementation
	}
	
	public void resume () {
		//TODO - implementation
	}
	
	public void stop (Status status) {
		if (!whitePlayer.isUser()) {
			closeEngine(whitePlayer);
		}
		if (!blackPlayer.isUser()) {
			closeEngine(blackPlayer);
		}
		clock.stop();
		notation.afterGameOver(playerToMove(), status);
	}
	
	@Override
	public synchronized void onGameStatusChanged(final Status status) {
		log.info("Process game status [" + status + "]");
		//check the current status
		if (this.status.isGameOver()) {
			throw new BusinessException("The game is over, nothing to do!");
		} 
		if (status.isGameOver()) {
			stop(status);
		} else {
			if (isUserVSUser()) {
				switchUserPlayersOnBoard ();
			} 
		}
		this.status = status;
	}
	
	private void closeEngine (Player player) {
		try {
			((AbstractEngine)player.getMoveSource()).remove();
		} catch (EngineException e) {
			e.printStackTrace();//FIXME - handle error
		}
	}
	
	public String getPositionAt (int fullMoveIndex) {
		return board.getPositionAt(fullMoveIndex);
	}
	
	private class SearchOpeningThread extends Thread {
		String moves;
		
		SearchOpeningThread(String moves) {
			super();
			this.moves = moves;
		}

		@Override
		public void run() {
			openingScout.search(moves);
			if (!openingScout.isActive()) {
				if (openingScout.hasOpening()) {
					messageHandler.showStatusMessage(Utils.formatOpeningMessage(openingScout.getOpening()));
				} else {
					messageHandler.showStatusMessage("Irregular opening:A00");
				}
			}
		}
	}
	
	public enum Status {
		UNDECIDED            (0),
		MATE                 (1),
		RESIGN               (4),
		TIMEOUT              (7),
		ABORT                (3,  Message.ABORTED),
		DRAW                 (2),
		DRAW_BY_REPETITION   (5,  Message.DRAW_THREEFOLD_REPETITION),
		DRAW_BY_FIFTY        (8,  Message.DRAW_BY_FIFTY),
		DRAW_BY_MATERIAL     (11, Message.DRAW_BY_MATERIAL),
		DRAW_BY_STALEMATE    (14, Message.DRAW_STALEMATE),
		DRAW_BY_AGREEMENT    (17, Message.DRAW_BY_AGREEMENT);
		
		private int code;
		private String description;
		
		/**
		 * @param code
		 * @param description
		 */
		private Status(int code, String description) {
			this.code = code;
			this.description = description;
		}
		/**
		 * @param code
		 */
		private Status(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}
		
		public String getDescription() {
			return description;
		}
		
		public boolean isMate () {
			return code % 3 == MATE.code;
		}
		
		public boolean isPureMate () {
			return code == MATE.code;
		}
		
		public boolean isTimeout () {
			return code == TIMEOUT.code;
		}
		
		public boolean isPureDraw () {
			return code == DRAW.code;
		}
		
		public boolean isDraw () {
			return code %  3 == DRAW.code;
		}
		
		public boolean isDrawByFifty () {
			return code == DRAW_BY_FIFTY.code;
		}
		
		public boolean isDrawByRepetition () {
			return code == DRAW_BY_REPETITION.code;
		}
		
		public boolean isDrawByStalemate () {
			return code == DRAW_BY_STALEMATE.code;
		}
		
		public boolean isDrawByMaterial () {
			return code == DRAW_BY_MATERIAL.code;
		}
		
		public boolean isResign() {
			return code == RESIGN.code;
		}
		
		public boolean isAbort() {
			return code == ABORT.code;
		}
		
		public boolean isGameOver() {
			return code != UNDECIDED.code;
		}
		
		public boolean isPureUndecided() {
			return code == UNDECIDED.code;
		}
		
		public boolean isUndecided() {
			return code % 3 == UNDECIDED.code;
		}
	}
}
