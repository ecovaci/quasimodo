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

import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.util.Utils;


public abstract class Player extends BasicForm<PlayerModel> {
    private Game game;
	private ChessColor  color;
	private MoveSource moveSource;
	
	public void challenge (Game game, ChessColor color) {
		game.registerPlayer(this, color);
		this.game = game;
    	this.color = color;
    }

	public ChessColor getColor() {
		return color;
	}

	public Move requestMove (int from, int to) {//without promoted piece !!!
		System.out.println("requestMove: from " + from + " to " + to);
		return game.requestMove(from, to);
	}
    
    public boolean makeMove (String uciMove) {
    	Move move = requestMove(Utils.getOffset(uciMove, 0), Utils.getOffset(uciMove, 2));
    	if (uciMove.length() == 5) {
			move.setPromotedPiece (
					new Piece (color.isWhite() ? Character.toUpperCase(uciMove.charAt(4))  
						: Character.toLowerCase(uciMove.charAt(4))));
    	}
    	return game.makeMove(move);
    }
	
    public boolean makeMove (Move move) {
    	return game.makeMove(move);
    }
    
    public boolean isWhite () {
    	return color.isWhite();
    }
    
    public void subscribeTo (final MoveSource source) {
    	this.moveSource = source;
    	source.bindPlayer(this);
    }
    
    public void unsubscribe () {
    	if (moveSource != null) {
    		moveSource.removePlayer();
    	    moveSource = null;
    	}
    }
    
    protected void myTurn () {
    	if (moveSource != null) {
    	    moveSource.myTurn();
    	}
    }
    
	public MoveSource getMoveSource() {
		return moveSource;
	}
	
	@Override
	public String toString() {
		return "Player [color=" + color + "]";
	}

	public abstract boolean isUser ();
}
