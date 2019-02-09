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
package org.chess.quasimodo.gui.model;

import java.util.Arrays;

import org.chess.quasimodo.domain.logic.ChessColor;
import org.chess.quasimodo.domain.logic.Piece;


public class PositionDialogModel {
    private Piece[]     pieces;
	private int         fullMoveCounter;
	private byte        castle;
	private String      ep;
	private ChessColor  colorToMove;
	
	public Piece[] getPieces() {
		return pieces;
	}
	
	public void setPieces(Piece[] pieces) {
		this.pieces = pieces;
	}
	
	public int getFullMoveCounter() {
		return fullMoveCounter;
	}
	
	public void setFullMoveCounter(int moveCounter) {
		this.fullMoveCounter = moveCounter;
	}
	
	public byte getCastle() {
		return castle;
	}
	
	public void setCastle(byte castle) {
		this.castle = castle;
	}
	
	public String getEp() {
		return ep;
	}
	
	public void setEp(String ep) {
		this.ep = ep;
	}
	
	public ChessColor getColorToMove() {
		return colorToMove;
	}
	
	public void setColorToMove(ChessColor colorToMove) {
		this.colorToMove = colorToMove;
	}
	
	@Override
	public String toString() {
		return "PositionDialogModel [castle=" + castle + ", colorToMove="
				+ colorToMove + ", ep=" + ep + ", fullMoveCounter=" + fullMoveCounter
				+ ", pieces=" + Arrays.toString(pieces) + "]";
	}
}
