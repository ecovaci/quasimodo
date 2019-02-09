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
package org.chess.quasimodo.event;

import java.util.HashMap;
import java.util.Map;

import org.chess.quasimodo.domain.logic.Form;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;


/**
 * Incapsulates a command issued by the GUI.
 * It is up to logical module to decide whether
 * this command will be executed and how the answer will look like.
 * @author Eugen Covaci
 */
public class CommandEvent extends ApplicationEvent {

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = -858817319516385617L;

    private final Map<String, Object> parameters = new HashMap<String, Object>();
	
	/**
	 * The view that issued this event.
	 */
	private Form<?> form;
	
	/**
	 * The command.
	 */
	private Command command;
	
	/**
	 * Constructor.
	 * @param sender The specific component that issued this event(never null).
	 * @param form The view that issued this event.
	 * @param command The command.
	 */
	public CommandEvent(Object sender, Form<?> form, Command command) {
		super(sender);
		Assert.notNull(command, "null command");
		this.form = form;
		this.command = command;
	}
	
	/**
	 * Constructor.
	 * @param sender The specific component that issued this event(never null).
	 * @param command The command.
	 */
	public CommandEvent(Object sender, Command command) {
		this(sender, null, command);
	}
	
	/**
	 * Getter for the view.
	 * @return The view.
	 */
	public Form<?> getForm() {
		return form;
	}

	/**
	 * Getter for the command.
	 * @return The command.
	 */
	public Command getCommand() {
		return command;
	}

	public void addParameter (String name, Object value) {
		Assert.notNull(value, "Parameter name cannot be null");
		parameters.put(name, value);
	}
	
	public Object getParameter (String name) {
		return parameters.get(name);
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}
 
	@Override
	public String toString() {
		return "CommandEvent [command=" + command + "]";
	}
	
	/**
	 * All the available commands.
	 * @author Eugen Covaci
	 */
	public enum Command {
		MANAGE_ENGINES ("manageEngines"), 
		NEW_ENGINE ("newEngine"), 
		SAVE_ENGINE_OPTIONS ("saveEngineOptions"),
		DELETE_ENGINE ("deleteEngine"),
		CHANGE_ENGINE ("changeEngine"), 
		NEW_BOOK ("newBook"),
		MANAGE_PLAYERS ("managePlayers"),
		NEW_PLAYER ("newPlayer"),
		ADD_PLAYER ("addPlayer"),
		EDIT_PLAYER ("editPlayer"),
		DELETE_PLAYER ("deletePlayer"),
		NEW_GAME ("newGame"), 
		NEW_DEFAULT_GAME ("newAdHocGame"),
		SET_UP_GAME_SHOW ("setUpGameShow"),
		SET_UP_GAME_SAVE ("setUpGame"), 
		SET_UP_POSITION ("setUpPosition"),
		GO_TO_MOVE ("goToMove"),
		PREVIOUS_MOVE ("previousMove"),
		NEXT_MOVE ("nextMove"),
		START ("start"), 
		ABORT_GAME ("stop"), 
		PAUSE ("pause"), 
		RESUME ("resume"),
		TAKEBACK ("takeback"), 
		FORCE_MOVE ("forcemove"), 
		ANALIZE ("analyse"), 
		STOP_ANALYSE ("stopAnalyse"),
		WHITE_RESIGN ("whiteResign"), 
		BLACK_RESIGN ("blackResign"), 
		WHITE_REQUEST_DRAW ("whiteRequestDraw"), 
		BLACK_REQUEST_DRAW ("blackRequestDraw");
		
		/**
		 * The command's name.
		 */
		private String name;
		
		private Command(String name) {
			this.name = name;
		}

		/**
		 * Getter for the command's name.
		 * @return The command's name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Check to see if the game has ended somehow.
		 * @return <code>true</code> only if the game is stopped or one of the players resigned.
		 */
		public boolean isGameOver () {
			return this == ABORT_GAME || this == WHITE_RESIGN || this == BLACK_RESIGN;
		}
		
		/**
		 * Check to see if one of the players requested draw.
		 * @return <code>true</code> only if one of the players requested draw.
		 */
		public boolean isDrawRequested () {
			return this == WHITE_REQUEST_DRAW || this == BLACK_REQUEST_DRAW;
		}
		
	}
}
