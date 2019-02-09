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

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.chess.quasimodo.errors.InvalidFENException;
import org.chess.quasimodo.util.UCIUtils;
import org.springframework.util.Assert;


public class Board {
	/**
	 * The current position on the board.
	 */
	private Position position;
	
	/**
	 * Not the game history, but the history of the moves played after 
	 * the initial position has been loaded.
	 */
	private final Vector<Move> moveHistory = new Vector<Move>(); 
	
	
	private final Vector<String> fenHistory = new Vector<String>();
	
	
	/**
	 * @throws InvalidFENException 
	 * 
	 */
	public Board(String fen) throws InvalidFENException {
		(position = new Position()).load(fen);
		fenHistory.add(fen);
	}

	/**
	 * @throws InvalidFENException 
	 * 
	 */
	public Board() throws InvalidFENException {
		this(Definitions.INITIAL_FEN);
	}
	
	public static Piece[] getInitialPieces () {
		Position position = new Position();
		position.load(Definitions.INITIAL_FEN);
		return position.getPieces();
	}
	
	public List<Move> getMoves() {
		return Collections.unmodifiableList(moveHistory);
	}

	public synchronized boolean makeMove (Move move) {
		//test move's pseudo-legality	
		boolean result = position.generateMoves().contains(move);
		if (result) {
			//test move's legality working 
			//on a cloned position
			Position workingPosition = position.clone();			
			//attempt to make the move 
			//on cloned position
	        result = workingPosition.makeMove(move);
	        //if legal move, 
	        //we take the new position 
	        //as the current one
			if (result) {
				position = workingPosition;
				moveHistory.add(move);
				fenHistory.add(position.exportFEN());
			} 
		}
		return result;
	}
	
	public String getLastMoveSAN () {
		Position previous = new Position();
		previous.load(fenHistory.get(fenHistory.size() - 2));
		return previous.toSAN(moveHistory.lastElement());
	}
	
	public int getMovesNumber () {
		return moveHistory.size();
	}
	
	public int getPlyNumber () {
		return position.getFullMoveCounter();
	}
	
	public String getSANMoveList () {
		Position pos = new Position();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0;i < moveHistory.size();i++) {
			if (i % 2 == 0) {
				buffer.append ((i / 2 + 1) + ". ");
			}
			pos.load(fenHistory.get(i));
			buffer.append(pos.toSAN(moveHistory.get(i)) + " ");
			pos.reset();
		}
		return buffer.toString().trim();
	}

	public boolean isWhiteToMove() {
		return position.isWhiteToMove();
	}
	
	public ChessColor colorToMove() {
		return position.colorToMove();
	}
	
	public boolean isValidMove (Move move) {
		return position.isValid(move);
	}
	
	public Game.Status getGameStatus () {
		if (isThreefoldRepetition()) {
			return Game.Status.DRAW_BY_REPETITION;
		}
		return position.computeGameStatus();
	}
	
    public static ChessColor squareColor (int offset) {
    	return offset % 2 == 0 ? ChessColor.WHITE : ChessColor.BLACK;
    }
	
	/**
	 * Getter for pieces.
	 * @return A clone of the actual pieces, to prevent unauthorized modifications.
	 */
	public Piece[] getPieces() {
		return position.clonePieces();
	}
	
	public Move getLegalMove (int from, int to) {
		return position.getLegalMove(from, to);
	}
	
	public int getLocalFullMoveNumber () {
		return (moveHistory.size() / 2) + moveHistory.size() % 2;
	}
	
	public String exportFEN () {
		return position.exportFEN();
	}
	
	public synchronized void replacePosition (Position position) {
		moveHistory.clear();
		this.position = position;
	}
	
	public String getPositionAt (int ply) {
		Assert.isTrue(ply >= 0 && ply < fenHistory.size(), 
				"Invalid move index [" + ply + "] ,must be between 0 and " + fenHistory.size());
	    return fenHistory.get(ply);
	}
	
	public boolean isThreefoldRepetition () {
		int counter = 0;
		String currentFenSimple = UCIUtils.getSimpleFEN(fenHistory.lastElement());
		for (String fen : fenHistory) {
			if (currentFenSimple.equals(UCIUtils.getSimpleFEN(fen))) {
				if (++counter > 2) {
					return true;
				}
			}
		}
		return false;
	}
	
}
