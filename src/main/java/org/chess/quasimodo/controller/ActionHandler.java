package org.chess.quasimodo.controller;

import org.chess.quasimodo.event.CommandEvent;

public interface ActionHandler {

	public abstract void handleNewGame();

	public abstract void handleAdHocNewGame();

	public abstract void handleNewEngine(CommandEvent event);
	
	public abstract void handleSaveEngineOptions(CommandEvent event);

	public abstract void handleNewBook(CommandEvent event);

	public abstract void handleSetUpGame(CommandEvent event);

	public abstract void handleSetUpGameShow();

	public abstract void handleSetUpPosition(CommandEvent event);

	public abstract void handleManageEngines();

	public abstract void handleDeleteEngine(CommandEvent event);

	public abstract void handleManagePlayers();

	public abstract void handleNewPlayer(CommandEvent event);

	public abstract void handleAddPlayer();

	public abstract void handleEditPlayer(CommandEvent event);

	public abstract void handleDeletePlayer(CommandEvent event);

	public abstract void handleAbortGame();

	public abstract void handleNextMove();

	public abstract void handlePreviousMove();

	public abstract void handleGoToMove(CommandEvent event);

}