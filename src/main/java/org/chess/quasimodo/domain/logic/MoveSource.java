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

import org.chess.quasimodo.errors.IllegalOperationException;
import org.chess.quasimodo.event.PositionChangedAware;

/**
 * It provides moves to a binded player.
 * No more than one player can be binded at same time.
 * @author Eugen Covaci
 */
public interface MoveSource extends PositionChangedAware {
	/**
	 * Register player to receive moves. May throw {@link IllegalOperationException}
     * if a binded player already exists.
	 * @param player The player to be registered.
	 */
    void bindPlayer (Player player);
    
    /**
     * It unregisters binded player. May throw {@link IllegalOperationException}
     * if removing is not allowed.
     */
    void removePlayer ();
    
    /**
     * It is this move source turn now.
     */
	void myTurn ();
	
	/**
	 * Check to see whether a player is currently binded.
	 * @return
	 */
	boolean isPlayerBinded();
}
