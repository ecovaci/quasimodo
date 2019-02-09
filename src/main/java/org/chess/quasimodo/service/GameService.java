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
package org.chess.quasimodo.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.domain.logic.ChessColor;
import org.chess.quasimodo.domain.logic.Game;
import org.chess.quasimodo.domain.logic.Notation;
import org.chess.quasimodo.domain.logic.Player;
import org.chess.quasimodo.domain.logic.Position;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.gui.BoardPanel;
import org.chess.quasimodo.gui.model.PositionDialogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service ("gameService")
public class GameService {
	private Log logger = LogFactory.getLog(GameService.class);
	
	@Autowired
	private BoardPanel boardPanel;
	
	@Autowired
	private ApplicationContextAdapter contextAdapter;
	
	@Autowired
	private QuasimodoContext context;
	
	@Autowired
	private Config config;
	
	/**
	 * Creates and starts a new game.
	 */
	public synchronized Game newGame() throws EngineException {
		logger.info("New game is about to be created");
		Game game = contextAdapter.createGame();
		SetUpGameModel model = config.getSelectedGameModel();
		game.setModel(model);
		Player whitePlayer = createPlayer(model.getWhitePlayerModel());
		whitePlayer.challenge(game, ChessColor.WHITE);
		game.subscribePlayer(whitePlayer);
		Player blackPlayer = createPlayer(model.getBlackPlayerModel());
		blackPlayer.challenge(game, ChessColor.BLACK);
		game.subscribePlayer(blackPlayer);
		return game;
	}

	public synchronized void setUpGame(SetUpGameModel model) {
		config.setSelectedGameModel(model);
	}
	
	private Player createPlayer (final PlayerModel playerModel) {
		Player player = new Player () {
			@Override
			public boolean isUser() {
				return playerModel instanceof HumanPlayerModel;
			}
		};
		player.setModel(playerModel);
		return player;
	}
	
	public synchronized void setUpPosition (PositionDialogModel model) {
		//TODO - implementation
	}
	
	public void goToMove (int index) {
		Game currentGame = context.getCurrentGame();
		Position position = new Position();
		position.load(currentGame.getPositionAt(index));
		boardPanel.piecesChanged(position.getPieces());
		boardPanel.positionChanged();
		currentGame.getNotation().jumpToMoveAt(index - 1);
	}
	
	public void goToPreviousMove () {
		Notation notation = context.getCurrentGame().getNotation();
		if (notation.getCurrentMoveIndex() > -1) {
			goToMove(notation.getCurrentMoveIndex());
		}
	}
	
	public void goToNextMove () {
		Notation notation = context.getCurrentGame().getNotation();
		if (notation.getCurrentMoveIndex() < notation.getMoveNumber() - 1) {
			goToMove(notation.getCurrentMoveIndex() + 2);
		}
	}
}
