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
package org.chess.quasimodo.controller;

import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chess.quasimodo.annotation.Action;
import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.concurrent.VisibleThread;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.domain.logic.Form;
import org.chess.quasimodo.domain.logic.FormView;
import org.chess.quasimodo.domain.logic.Game;
import org.chess.quasimodo.domain.logic.Game.Status;
import org.chess.quasimodo.errors.BusinessException;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.gui.AbstractDialogForm;
import org.chess.quasimodo.gui.ClockPanel;
import org.chess.quasimodo.gui.model.ManageModelList;
import org.chess.quasimodo.gui.model.PositionDialogModel;
import org.chess.quasimodo.gui.model.SelectFileDialogModel;
import org.chess.quasimodo.message.Message;
import org.chess.quasimodo.message.MessageHandler;
import org.chess.quasimodo.service.BookService;
import org.chess.quasimodo.service.EngineService;
import org.chess.quasimodo.service.GameService;
import org.chess.quasimodo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ErrorHandler;


@Controller
public class ActionController implements ActionHandler {
	private static final Log logger = LogFactory.getLog(ActionController.class);
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private EngineService engineService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ApplicationContextAdapter contextAdapter;
	
	@Autowired
	private ErrorHandler errorHandler;
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private QuasimodoContext context;
	
	@Autowired
	private ClockPanel clockView;
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleNewGame()
	 */
	@Override
	@Action (value=CommandEvent.Command.NEW_GAME)
	public synchronized void handleNewGame () {
		if(closePlayingGame ()) {
			new NewGameVisibleThread(errorHandler).start();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleAdHocNewGame()
	 */
	@Override
	@Action (value=CommandEvent.Command.NEW_DEFAULT_GAME)
	public synchronized void handleAdHocNewGame () {
		try {
			startNewGame (false);
		} catch (Exception e) {
			logger.error(e, e);
			errorHandler.handleError(e);
		}
	}
	
	private boolean closePlayingGame () {
		if(context.hasActiveGame()) {
			if (messageHandler.showConfirm(Message.CLOSE_EXISTENT_GAME) != JOptionPane.OK_OPTION) {
				return false;
			}
		}
		return true;
	}
	
	private void startNewGame (boolean resetView) throws EngineException {
		Game game = gameService.newGame ();
		context.setCurrentGame(game);
		clockView.setGameTitle(game.getTitle());
		clockView.setGameInfo(game.getGameInfo());
		if (resetView) {
		    game.resetView();
		}
		//now start the game
		game.start();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleNewEngine(org.chess.quasimodo.domain.logic.Form)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Action (CommandEvent.Command.NEW_ENGINE)
	public synchronized void handleNewEngine (CommandEvent event) {
		Form<SelectFileDialogModel> form = (Form<SelectFileDialogModel>)event.getForm();
		Assert.notNull(form, "New engine form null is not allowed");
		engineService.registerNewEngine(form.getModel());
		showManageEnginesView();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleNewBook(org.chess.quasimodo.domain.logic.Form)
	 */
	@Override
	@Action (CommandEvent.Command.NEW_BOOK)
	public synchronized void handleNewBook (CommandEvent event) {
		@SuppressWarnings("unchecked")
		Form<SelectFileDialogModel> form = (Form<SelectFileDialogModel>)event.getForm();
		Assert.notNull(form, "New openingBook form null is not allowed");
		if (bookService.validateOpeningBook(form.getModel().getPathname()) 
				|| messageHandler.showConfirm("Doesn't seem to be a valid opening openingBook.", 
				"Proceed anyway?") == JOptionPane.OK_OPTION) {
			bookService.registerOpeningBook(form.getModel().getPathname());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Action (CommandEvent.Command.SAVE_ENGINE_OPTIONS)
	public synchronized void handleSaveEngineOptions(CommandEvent event) {
		Form<EngineModel> form = (Form<EngineModel>)event.getForm();
		Assert.notNull(form, "New openingBook form null is not allowed");
		engineService.saveEngineOptions(form.getModel());
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleSetUpGame(org.chess.quasimodo.domain.logic.Form)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Action (CommandEvent.Command.SET_UP_GAME_SAVE)
	public synchronized void handleSetUpGame (CommandEvent event) {
		Form<SetUpGameModel> form = (Form<SetUpGameModel>)event.getForm();
		logger.info("handle setUpGame command");
		Assert.notNull(form, "New game form null is not allowed");
		gameService.setUpGame (((Form<SetUpGameModel>)form).getModel());
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleSetUpGameShow()
	 */
	@Override
	@Action (CommandEvent.Command.SET_UP_GAME_SHOW)
	public synchronized void handleSetUpGameShow () {
		if (context.hasActiveGame()) {
			throw new BusinessException("Cannot setup game while playing!");
		}
		AbstractDialogForm<SetUpGameModel>  form = contextAdapter.getSetUpGameView(); 
		SetUpGameModel model = new SetUpGameModel();
		model.addAllPlayerModels(engineService.getRegisteredEngines());
		model.addAllPlayerModels(playerService.getRegisteredPlayers());
		form.setModel(model);
		form.commit();
		form.showView();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleSetUpPosition(org.chess.quasimodo.domain.logic.Form)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Action (CommandEvent.Command.SET_UP_POSITION)
	public synchronized void handleSetUpPosition (CommandEvent event) {
		Form<PositionDialogModel> form = (Form<PositionDialogModel>)event.getForm();
		logger.info("handle setUpPosition command");
		Assert.notNull(form, "New position form null is not allowed");
		gameService.setUpPosition(form.getModel());
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleManageEngines()
	 */
	@Override
	@Action (CommandEvent.Command.MANAGE_ENGINES)
	public synchronized void handleManageEngines () {
		showManageEnginesView();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleDeleteEngine(org.chess.quasimodo.domain.logic.Form)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Action (CommandEvent.Command.DELETE_ENGINE)
	public synchronized void handleDeleteEngine (CommandEvent event) {
		ManageModelList<EngineModel> form = (ManageModelList<EngineModel>)event.getForm().getModel();
		engineService.deleteEngine(form.getSelectedModel());
		showManageEnginesView();		
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleManagePlayers()
	 */
	@Override
	@Action (CommandEvent.Command.MANAGE_PLAYERS)
	public synchronized void handleManagePlayers () {
		showManagePlayersView();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleNewPlayer(org.chess.quasimodo.domain.logic.Form)
	 */
	@Override
	@Action (CommandEvent.Command.NEW_PLAYER)
	public synchronized void handleNewPlayer (CommandEvent event) {
		Assert.notNull(event.getForm(), "New player form null is not allowed");
		playerService.registerNewPlayer((HumanPlayerModel)(event.getForm()).getModel());
		showManagePlayersView();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleAddPlayer()
	 */
	@Override
	@Action (CommandEvent.Command.ADD_PLAYER)
	public synchronized void handleAddPlayer () {
		showPlayerForm(new HumanPlayerModel());
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleEditPlayer(org.chess.quasimodo.domain.logic.Form)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Action (CommandEvent.Command.EDIT_PLAYER)
	public synchronized void handleEditPlayer (CommandEvent event) {
		Assert.notNull(event.getForm(), "Edit player form null is not allowed");
		HumanPlayerModel playerModel = playerService.loadPlayerModel(
				((ManageModelList<HumanPlayerModel>)event.getForm().getModel()).getSelectedModel().getId());
		showPlayerForm(playerModel);
	}
	
	private void showPlayerForm (HumanPlayerModel playerModel) {
		FormView<HumanPlayerModel> playerForm = contextAdapter.getNewPlayerDialog();
		playerForm.setModel(playerModel);
		playerForm.commit();
		playerForm.showView();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleDeletePlayer(org.chess.quasimodo.domain.logic.Form)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Action (CommandEvent.Command.DELETE_PLAYER)
	public synchronized void handleDeletePlayer (CommandEvent event) {
		Assert.notNull(event.getForm(), "New engine form null is not allowed");
		playerService.deletePlayer(((ManageModelList<HumanPlayerModel>)(event.getForm().getModel())).getSelectedModel());
		showManagePlayersView();
	}
	
	private void showManageEnginesView () {
		showManageEntitiesView (contextAdapter.getManageEnginesView(), engineService.getRegisteredEngines());
	}
	
	private void showManagePlayersView () {
		showManageEntitiesView (contextAdapter.getManagePlayersView(), playerService.getRegisteredPlayers());
	}
	
	private <T> void showManageEntitiesView (FormView<ManageModelList<T>> form, List<T> modelList) {
		ManageModelList<T> model = new ManageModelList<T>();
		model.setModelList(modelList);
		form.setModel(model);
		form.commit();
		form.showView();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.controller.ActionHandler#handleAbortGame()
	 */
	@Override
	@Action (value=CommandEvent.Command.ABORT_GAME)
	public synchronized void handleAbortGame () {
		if (context.existCurrentGame()) {
			 new AbortVisibleThread("Aborting game ...", errorHandler).start();
		} else {
			errorHandler.handleError(new BusinessException("There is no current game, nothing to abort"));
		}
	}
	
	@Override
	@Action (value=CommandEvent.Command.GO_TO_MOVE)
	public synchronized void handleGoToMove (CommandEvent event) {
		gameService.goToMove(Integer.parseInt(event.getParameter("move").toString()));
	}
	
	@Override
	@Action (value=CommandEvent.Command.PREVIOUS_MOVE)
	public synchronized void handlePreviousMove () {
		gameService.goToPreviousMove();
	}
	
	@Override
	@Action (value=CommandEvent.Command.NEXT_MOVE)
	public synchronized void handleNextMove () {
		gameService.goToNextMove();
	}
	
	private class NewGameVisibleThread extends VisibleThread {
		
		NewGameVisibleThread(ErrorHandler errorHandler) {
			super(errorHandler);
		}

		@Override
		public void doWork() throws Exception {
			if(context.hasActiveGame()) {
				this.setMessage("Abort game ...");
				context.getCurrentGame().onGameStatusChanged(Status.ABORT);
			}
			this.setMessage("Starting game ...");
			startNewGame (true);
		}
	}
	
	private class AbortVisibleThread extends VisibleThread {
		
		AbortVisibleThread(String message, ErrorHandler errorHandler) {
			super(message, errorHandler);
		}

		@Override
		public void doWork() throws Exception {
			context.getCurrentGame().onGameStatusChanged(Status.ABORT);
		}
	}
}
