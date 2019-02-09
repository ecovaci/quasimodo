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

import org.chess.quasimodo.util.Utils;

public class Move implements Definitions {
	public static final byte NORMAL_MOVE = 0;
	public static final byte CAPTURE = 1;
	public static final byte CASTLE = 2;
	public static final byte EN_PASSANT_CAPTURE = 4;
	public static final byte PUSH_PAWN_TWO_SQUARES = 8;
	public static final byte PAWN_MOVE = 16;
	public static final byte PROMOTE = 32;
	
	public int from;
	public int to;
	private Piece promotedPiece;
	private int type;
	
	
	/**
	 * @param from
	 * @param to
	 * @param type
	 */
	public Move(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	/**
	 * @param from
	 * @param to
	 * @param type
	 */
	public Move(int from, int to, int type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}
	
	public Piece getPromotedPiece() {
		return promotedPiece;
	}
	
	public void setPromotedPiece(Piece promotedPiece) {
		this.promotedPiece = promotedPiece;
	}
	
	public int getType() {
		return type;
	}
	
	protected void setType(int type) {
		this.type = type;
	}

	public boolean isTwoSquaresPawnPush () {
		return (type & PUSH_PAWN_TWO_SQUARES) != 0;
	}
	
	public boolean isCancelFifty () {
		return (type & (CAPTURE | PAWN_MOVE)) != 0;
	}
	
	public boolean isPromote () {
		return (type & PROMOTE) != 0;
	}
	
	public boolean isEPCapture() {
		return (type & EN_PASSANT_CAPTURE) != 0;
	}
	
	public boolean isCapture() {
		return (type & EN_PASSANT_CAPTURE) != 0 || (type & CAPTURE) != 0;
	}
	
	public boolean isCastle () {
		return (type & CASTLE) != 0;
	}
	
	public boolean isPawnMove () {
		return (type & PAWN_MOVE) != 0;
	}
	
	public boolean isAmbiguous (Move move,Piece[] pieces) {
		return !pieces[this.from].isPawn() 
		   && this.to == move.to && this.from != move.from 
		   && pieces[this.from].equals(pieces[move.from]);
	}
	
	public String toUCI () {
    	StringBuffer buffer = new StringBuffer();;
		buffer.append(Utils.toNotation(from));
		buffer.append(Utils.toNotation(to));
		if(isPromote()) {
			buffer.append(Character.toLowerCase(promotedPiece.getCode()));
		} 
		return buffer.toString();
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + from;
		result = prime * result + to;
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (from != other.from)
			return false;
		if (to != other.to)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Move [from=" + from + ", promotedPiece=" + promotedPiece
				+ ", to=" + to + ", type=" + type + "]";
	}
}
