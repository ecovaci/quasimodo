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

public enum ChessColor {
	 WHITE('w'), BLACK('b');
	 char code;

	/**
	 * @param code
	 */
	private ChessColor(char code) {
		this.code = code;
	}

	public char getCode() {
		return code;
	}
	
	public ChessColor opposite () {
		if (this == WHITE) {
			return BLACK;
		} else {
		    return WHITE;
		}
	}
	
	public ChessColor duplicate () {
		if (this == WHITE) {
			return WHITE;
		} else {
		    return BLACK;
		}
	}
	
	public boolean isWhite () {
		return this == WHITE;
	}
	
	public boolean isBlack () {
		return this == BLACK;
	}
	
	public int sign () {
		if (this == WHITE) {
			return 1;
		}
		return -1;
	}
	
	public String asString () {
		if (isWhite()) {
			return "White";
		} else {
			return "Black";
		}
	}
}
