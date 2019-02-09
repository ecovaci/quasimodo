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
package org.chess.quasimodo.application;

import org.chess.quasimodo.domain.logic.Game;
import org.springframework.stereotype.Component;


@Component ("context")
public class QuasimodoContext {
	
	private boolean ignoreUserInput;
	
	private Game currentGame;
	
	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	public boolean isIgnoreUserInput() {
		return ignoreUserInput;
	}

	public void setIgnoreUserInput(boolean ignoreUserInput) {
		this.ignoreUserInput = ignoreUserInput;
	}
	
	public boolean existCurrentGame () {
		return this.currentGame != null;
	}
	
	public boolean hasActiveGame () {
		return this.currentGame != null &&  !this.currentGame.isOver();
	}
	
}
