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

import org.chess.quasimodo.domain.logic.ChessColor;

/**
 * To be implemented by classes listening for clock events.
 * @author Eugen Covaci
 *
 */
public interface ClockChangedAware {
	
	/**
	 * Called when clock changes.
	 * @param whiteElapsedTime White elapsed time (seconds).
	 * @param blackElapsedTime Black elapsed time (seconds).
	 */
	void setClockTime(long whiteElapsedTime, long blackElapsedTime);
	
	/**
	 * Called when game time is over.
	 * @param colorToMove The color to move.
	 */
	void timeOver (ChessColor colorToMove);
}
