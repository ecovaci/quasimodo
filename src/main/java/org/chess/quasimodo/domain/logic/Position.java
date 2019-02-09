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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.chess.quasimodo.book.BookEntry;
import org.chess.quasimodo.book.PolyglotRandom;
import org.chess.quasimodo.errors.InvalidFENException;
import org.chess.quasimodo.gui.model.PositionDialogModel;
import org.chess.quasimodo.util.Utils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public class Position implements Definitions, Cloneable {
	
	public static final int NO_EN_PASSANT = -1;
	public static final int DEFAULT_MOVE_COUNTER = 1;
	
	private Piece[]       pieces = new Piece[64];
	private ChessColor    colorToMove = ChessColor.WHITE;
	
	private int           fullMoveCounter;
	private byte          fifty;
	private byte          castle;
	private int           ep;
	
	public Position() {
		initialize();
	}
	
	/**
	 * @param pieces
	 * @param colorToMove
	 * @param previous
	 * @param fullMoveCounter
	 * @param fifty
	 * @param castle
	 * @param ep
	 */
	public Position(PositionDialogModel model) {
		this.pieces = model.getPieces();
		this.colorToMove = model.getColorToMove();
		this.fullMoveCounter = model.getFullMoveCounter();
		this.castle = model.getCastle();
		this.ep = StringUtils.hasLength(model.getEp()) ? 
				model.getEp().charAt(0) - 'a' : NO_EN_PASSANT;
	}

	private void initialize () {
		colorToMove = ChessColor.WHITE;
		fullMoveCounter = 1;
		fifty = 0;
		castle = NO_CASTLE;
		ep = NO_EN_PASSANT;
	}
	
	public Piece[] getPieces() {
		return pieces;
	}
	
	protected void clearPieces () {
		Arrays.fill(pieces, null);
	}
	
	public boolean isWhiteToMove () {
		return colorToMove.isWhite();
	}
	
	public boolean isBlackToMove () {
		return colorToMove.isBlack();
	}

	public void reset () {
		clearPieces();
		initialize();
	}
	
	public boolean isInitial () {
		return fullMoveCounter == 1 && colorToMove == ChessColor.WHITE;
	}
	
	public void load (String fen) throws InvalidFENException {
		try {
			String[] splited_fen = fen.split("\\s");
			String[] ranks = splited_fen[0].split("/");
			Validate.isTrue(ranks.length == 8, "Invalid fen - rank's number found: " + ranks.length);
			int f;
			for (int r = 0;r < ranks.length;r++) {
				f = -1;
				for (char code:ranks[r].toCharArray()) {
					if (Character.isLetter(code)) {//letter case
						f++;
						Assert.isTrue(f < 8, "Invalid fen - file's index out of range: " + f);
						pieces[8 * r + f] = new Piece (code);
					} else {//digit case
						f += Character.getNumericValue(code);
					}
				}
			}
			
			if (splited_fen.length > 1) {
				if (splited_fen[1].charAt(0) == ChessColor.BLACK.getCode()) {
				    colorToMove = ChessColor.BLACK;
				} else if (splited_fen[1].charAt(0) == ChessColor.WHITE.getCode()) {
					colorToMove = ChessColor.WHITE;
				} else {
					throw new InvalidFENException ("Invalid fen - color to move: " + splited_fen[1].charAt(0));
				}
				if (splited_fen.length > 2) {
					parseCastle(splited_fen[2]);
					if (splited_fen.length > 3) {
						if (!"-".equals(splited_fen[3])) {
				        	ep = 8 * (8 - Character.getNumericValue(splited_fen[3].charAt(1)))
				        	     + splited_fen[3].charAt(0) - 'a';
						}
						if (splited_fen.length > 4) {
				        	fifty = Byte.parseByte(splited_fen[4]);
				        	if (splited_fen.length > 5) {//means 6
				    			fullMoveCounter = Integer.parseInt(splited_fen[5]);
				    		}
						} 
					} 
				}
			}
		} catch (Exception e) {
			throw new InvalidFENException("Invalid FEN", e);
		}
	}
	
	private void parseCastle (String str) {
		if (str.indexOf(WHITE_KING) > -1) {
			castle |= WHITE_CASTLE_KING_SIDE;
		}
		if (str.indexOf(WHITE_QUEEN) > -1) {
			castle |= WHITE_CASTLE_QUEEN_SIDE;
		}
		if (str.indexOf(BLACK_KING) > -1) {
			castle |= BLACK_CASTLE_KING_SIDE;
		}
		if (str.indexOf(BLACK_QUEEN) > -1) {
			castle |= BLACK_CASTLE_QUEEN_SIDE;
		}
	}

	public ChessColor colorToMove() {
		return colorToMove;
	}
	
	public ChessColor opponent() {
		return colorToMove.opposite();
	}

	public int getFullMoveCounter() {
		return fullMoveCounter;
	}

	protected byte getFifty() {
		return fifty;
	}

	protected byte getCastle() {
		return castle;
	}

	protected int getEnpassantSquare() {
		return ep;
	}

	protected boolean isEnPassant () {
		return this.ep != -1;
	}
	
	/**
	 * Generates pseudo-legal moves.
	 * @return
	 */
	protected List<Move> generateMoves () {
		List<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < 64; i++) {
			if (Piece.belongsTo(pieces[i], colorToMove)) {
				if (pieces[i].isPawn()) {
					if (colorToMove == ChessColor.WHITE) {
						if (i % 8 != 0 && Piece.isBlack(pieces[i - 9])) {
							if (i >> 3 == 1) {
								moves.add(new Move(i, i - 9, Move.PAWN_MOVE | Move.CAPTURE | Move.PROMOTE));
							} else {
							    moves.add(new Move(i, i - 9, Move.PAWN_MOVE | Move.CAPTURE));
							}
						}
						if (i % 8 != 7 && Piece.isBlack(pieces[i - 7])) {
							if (i >> 3 == 1) {
								moves.add(new Move(i, i - 7, Move.PAWN_MOVE | Move.CAPTURE | Move.PROMOTE));
							} else {
							    moves.add(new Move(i, i - 7, Move.PAWN_MOVE | Move.CAPTURE));
							}
						}
						if (pieces[i - 8] == null) {
							if (i >> 3 == 1) {
								moves.add(new Move(i, i - 8, Move.PAWN_MOVE | Move.PROMOTE));
							} else {
							    moves.add(new Move(i, i - 8, Move.PAWN_MOVE));
							}
							if (i >= 48 && pieces[i - 16] == null) {
								moves.add(new Move(i, i - 16, Move.PAWN_MOVE | Move.PUSH_PAWN_TWO_SQUARES));
							}
						}
					} else {
						if (i % 8 != 0 && Piece.isWhite(pieces[i + 7])) {
							if (i >> 3 == 6) {
								moves.add(new Move(i, i + 7, Move.PAWN_MOVE | Move.CAPTURE | Move.PROMOTE));
							} else {
						        moves.add(new Move(i, i + 7, Move.PAWN_MOVE | Move.CAPTURE));
							}
						}
						if (i % 8 != 7 && Piece.isWhite(pieces[i + 9])) {
							if (i >> 3 == 6) {
								moves.add(new Move(i, i + 9, Move.PAWN_MOVE | Move.CAPTURE | Move.PROMOTE));
							} else {
							    moves.add(new Move(i, i + 9, Move.PAWN_MOVE | Move.CAPTURE));
							}
						}
						if (pieces[i + 8] == null) {
							if (i >> 3 == 6) {
								moves.add(new Move(i, i + 8, Move.PAWN_MOVE | Move.PROMOTE));
							} else {
							    moves.add(new Move(i, i + 8, Move.PAWN_MOVE ));
							}
							if (i <= 15 && pieces[i + 16] == null){
								moves.add(new Move(i, i + 16, Move.PAWN_MOVE | Move.PUSH_PAWN_TWO_SQUARES));
							}
						}
					}
				} else {
					for (int j = 0; j < pieces[i].getOffset_number(); j++) {
						for (int n = i;;) {
							n = XBOARD[XVALID_POSITIONS[n] + pieces[i].getOffset()[j]];
							if (n == -1) {
								break;
							}
							if (pieces[n] != null) {
								if (pieces[n].getColor() == colorToMove.opposite()){
									moves.add(new Move(i, n, Move.CAPTURE));
								}
								break;
							}
							moves.add(new Move(i, n, Move.NORMAL_MOVE));
							if (!pieces[i].isSlide()) {
								break;
							}
						}
					}
				}
			}
		}
		/* generate castle moves */
		if (colorToMove == ChessColor.WHITE) {
			if ((castle & WHITE_CASTLE_KING_SIDE) != NO_CASTLE) {
				moves.add(new Move(E1, G1, Move.CASTLE));
			}
			if ((castle & WHITE_CASTLE_QUEEN_SIDE) != NO_CASTLE) {
				moves.add(new Move(E1, C1, Move.CASTLE));
			}
		}
		else {
			if ((castle & BLACK_CASTLE_KING_SIDE) != NO_CASTLE) {
				moves.add(new Move(E8, G8, Move.CASTLE));
			}
			if ((castle & BLACK_CASTLE_QUEEN_SIDE) != NO_CASTLE) {
				moves.add(new Move(E8, C8, Move.CASTLE));
			}
		}
		
		/* generate en passant moves */
		if (this.isEnPassant()) {
			if (colorToMove == ChessColor.WHITE) {
				if (ep % 8 != 0 && Piece.isWhite(pieces[ep + 7]) && Piece.isPawn(pieces[ep + 7])) {
					moves.add(new Move(ep + 7, ep, Move.PAWN_MOVE | Move.CAPTURE | Move.EN_PASSANT_CAPTURE));
				}
				if (ep % 8 != 7 && Piece.isWhite(pieces[ep + 9]) && Piece.isPawn(pieces[ep + 9])){
					moves.add(new Move(ep + 9, ep, Move.PAWN_MOVE | Move.CAPTURE | Move.EN_PASSANT_CAPTURE));
				}
			}
			else {
				if (ep % 8 != 0 && Piece.isBlack(pieces[ep - 9]) && Piece.isPawn(pieces[ep - 9])){
					moves.add(new Move(ep - 9, ep, Move.PAWN_MOVE | Move.CAPTURE | Move.EN_PASSANT_CAPTURE));
				}
				if (ep % 8 != 7 && Piece.isBlack(pieces[ep - 7]) && Piece.isPawn(pieces[ep - 7])){
					moves.add(new Move(ep - 7, ep, Move.PAWN_MOVE | Move.CAPTURE | Move.EN_PASSANT_CAPTURE));
				}
			}
		}
		return moves;
	}
	
	public boolean inCheck (ChessColor player) {
		for (int i = 0;i < pieces.length;i++) {
			if (pieces[i] != null && pieces[i].isKing()&& pieces[i].belongsTo(player)) {
				return attack(i, player.opposite());
			}
		}
		//TODO - throw exception: king not found!
		return true;
		
	}
	
	public boolean attack(int square, ChessColor player) {
		for (int i = 0; i < 64; i++) {
			if (Piece.belongsTo(pieces[i], player)) {
				if (pieces[i].isPawn()) {
					if (player == ChessColor.WHITE) {
						if (i % 8 != 0 && i - 9 == square)
							return true;
						if (i % 8 != 7 && i - 7 == square)
							return true;
					} else {
						if (i % 8 != 0 && i + 7 == square)
							return true;
						if (i % 8 != 7 && i + 9 == square)
							return true;
					}
				}
				else {
					for (int j = 0; j < pieces[i].getOffset_number(); j++) {
						for (int n = i;;) {
							n = Definitions.XBOARD[Definitions.XVALID_POSITIONS[n] + pieces[i].getOffset()[j]];
							if (n == -1) {
								break;
							}
							if (n == square) {
								return true;
							}
							if (pieces[n] != null) {
								break;
							}
							if (!pieces[i].isSlide()) {
								break;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean isValid (Move move) {
		return this.clone().makeMove(move);
	}
	
	public Move getLegalMove (int from, int to) {
		for (Move move : legalMoves ()) {
			if (move.from == from && move.to == to) {
				return move;
			}
		}
		return null;
	}
	
	public Move getPseudoLegalMove (int from, int to) {
		for (Move move : generateMoves ()) {
			if (move.from == from && move.to == to) {
				return move;
			}
		}
		return null;
	}
	
	public Move getPseudoLegalMove (String uciMove) {
		Move move = getPseudoLegalMove (Utils.getOffset(uciMove, 0), Utils.getOffset(uciMove, 2));
		if (move != null && uciMove.length() == 5 //FIXME - it is correct 5?
				) {
			char pcode = isWhiteToMove() ? Character.toUpperCase(uciMove.charAt(4)) 
					: Character.toLowerCase(uciMove.charAt(4)); 
			move.setPromotedPiece(new Piece (pcode));
		}
		return move;
	}
	
	public List<Move> legalMoves () {
		List<Move> moves = generateMoves();
		for (Iterator<Move> itr = moves.iterator();itr.hasNext();) {
			if (!isValid(itr.next())) {
				itr.remove();
			}
		}
		return moves;
	}
	
	public boolean makeMove (Move move) {
		/* test to see if a castle move is legal and move the rook
		   (the king is moved with the usual move code later) */
		if (move.isCastle()) {
			int from = -1, to = -1;
			if (this.inCheck(colorToMove)) {
				return false;
			}
			switch (move.to) {
				case 62:
					if (pieces[F1] != null || pieces[G1] != null ||
							this.attack(F1, colorToMove.opposite()) || this.attack(G1, colorToMove.opposite())) {
						return false;
					}
					from = H1;
					to = F1;
					break;
				case 58:
					if (pieces[B1] != null || pieces[C1] != null || pieces[D1] != null ||
							this.attack(C1, colorToMove.opposite()) || this.attack(D1, colorToMove.opposite()))
						return false;
					from = A1;
					to = D1;
					break;
				case 6:
					if (pieces[F8] != null || pieces[G8] != null ||
							this.attack(F8, colorToMove.opposite()) || this.attack(G8, colorToMove.opposite()))
						return false;
					from = H8;
					to = F8;
					break;
				case 2:
					if (pieces[B8] != null || pieces[C8] != null || pieces[D8] != null ||
							this.attack(C8, colorToMove.opposite()) || this.attack(D8, colorToMove.opposite()))
						return false;
					from = A8;
					to = D8;
					break;
			}
			pieces[to] = pieces[from];
			pieces[from] = null;
		}
		
		/* update castle flag */
		castle &= CASTLE_MASK[move.from] & CASTLE_MASK[move.to];
		
		/* update en passant flag */
		if (move.isTwoSquaresPawnPush()) {
			if (colorToMove == ChessColor.WHITE) {
				this.ep = move.to + 8;
			} else {
				this.ep = move.to - 8;
			}
		} else {
			this.ep = -1;
		}
		
		/* update fifty-move-rule flag */
		if (move.isCancelFifty()) {
			fifty = 0;
		} else {
			fifty++;
		}
		
		/* move the piece */
		if (move.isPromote()) {
			pieces[move.to] = move.getPromotedPiece();
		} else {
			pieces[move.to] = pieces[move.from];
		}
		
		/* clear to space where the piece came from */
		pieces[move.from] = null;

		/* erase the pawn if this is an en passant move */
		if (move.isEPCapture()) {
			if (colorToMove == ChessColor.WHITE) {
				pieces[move.to + 8] = null;
			} else {
				pieces[move.to - 8] = null;
			}
		}
		
		/* increment move's counter only for black colorToMove*/
		if (colorToMove == ChessColor.BLACK) {
		    fullMoveCounter++;
		}
		
		/* now give turn to the opponent */
		colorToMove = colorToMove.opposite();
		
		/* the final test for legality: see whether this move leaves the king in check */
		if (this.inCheck(colorToMove.opposite())) {
			return false;
		}
		return true;
	}
	
	protected String makePseudoMove (String uciMove) {
		StringBuffer buffer = new StringBuffer();
		
		//******************** offsets *****************************//
		int from = Utils.getOffset(uciMove, 0);
		int to   = Utils.getOffset(uciMove, 2);
		//**********************************************************//
		
		buffer.append(pieces[from].toSAN() + uciMove.substring(0, 2));
		if (pieces[to] == null) { /*normal move*/
		    buffer.append("-");
		} else {                  /*capture*/
			buffer.append(CAPTURE);
		}
		buffer.append(uciMove.substring(2, 4));
		
		//******************* make the  move ***********************//
		//check for castle
        if (pieces[from].isKing() && Math.abs(from - to) == 2) {
			if (from == E1 && to == G1) {        /*white short castle*/
				pieces[F1] = pieces[H1];
				pieces[H1] = null;
			} else if (from == E8 && to == G8) { /*black short castle*/
				pieces[F8] = pieces[H8];
				pieces[H8] = null;
			} else if (from == E1 && to == C1) { /*white long castle*/
				pieces[D1] = pieces[A1];
				pieces[A1] = null;
			} else {                             /*black long castle*/
				pieces[D8] = pieces[A8];
				pieces[A8] = null;
			}
		}
        
		pieces[to] = pieces[from];
		pieces[from] = null;
		
		//check for promotion
		if (uciMove.length() == 5) {
			buffer.append(PROMOTE + Character.toUpperCase(uciMove.charAt(4)));
			if (colorToMove.isWhite()) {
				pieces[to] = new Piece(Character.toUpperCase(uciMove.charAt(4)));
			} else {
				pieces[to] = new Piece(uciMove.charAt(4));
			}
		}
		colorToMove = colorToMove.opposite();
		//**********************************************************//
		
		if (colorToMove == ChessColor.BLACK) {
		    fullMoveCounter++;
		}
		//mark a possible check
		if (inCheck(colorToMove)) {
			buffer.append(CHECK);
		}
		return buffer.toString();
	}
	
	public boolean isMate () {
		return inCheck(colorToMove) && legalMoves().isEmpty();
	}
	
	public long zobristHashKey () {
		long key = 0;
		int piece_offset;
		for (int i = 0;i < pieces.length;i++) {
			if (pieces[i] != null) {
				piece_offset = 64 * pieces[i].getZobristValue() + 8 * (Utils.getRank(i) - 1) +  i % 8; 
				key ^= PolyglotRandom.RANDOM64[piece_offset];
			}
		}
		//castle
		if ((castle & WHITE_CASTLE_KING_SIDE) != NO_CASTLE) {
			key ^= PolyglotRandom.RANDOM64[768 + 0];
		}
		if ((castle & WHITE_CASTLE_QUEEN_SIDE) != NO_CASTLE) {
			key ^= PolyglotRandom.RANDOM64[768 + 1];
		}
		if ((castle & BLACK_CASTLE_KING_SIDE) != NO_CASTLE) {
			key ^= PolyglotRandom.RANDOM64[768 + 2];
		}
		if ((castle & BLACK_CASTLE_QUEEN_SIDE) != NO_CASTLE) {
			key ^= PolyglotRandom.RANDOM64[768 + 3];
		}
		//en passant
		if (ep > -1) {
			if (colorToMove == ChessColor.WHITE) {
				if ((ep % 8 != 0 && Piece.isWhite(pieces[ep + 7]) && Piece.isPawn(pieces[ep + 7]))
						|| ep % 8 != 7 && Piece.isWhite(pieces[ep + 9]) && Piece.isPawn(pieces[ep + 9])) {
					key ^= PolyglotRandom.RANDOM64[772 + ep % 8];
				}
			}
			else {
				if ((ep % 8 != 0 && Piece.isBlack(pieces[ep - 9]) && Piece.isPawn(pieces[ep - 9]))
						|| ep % 8 != 7 && Piece.isBlack(pieces[ep - 7]) && Piece.isPawn(pieces[ep - 7])){
					key ^= PolyglotRandom.RANDOM64[772 + ep % 8];
				}
			}
		}
		
		if (colorToMove.isWhite()) {
			key ^= PolyglotRandom.RANDOM64[780];
		}
		return key;
	}
	
    public void print () {
		System.out.println("[Player to move: " + colorToMove + "]");
		System.out.println("[Full move counter: " + fullMoveCounter+ "]");
		System.out.println("[Fifty: " + fifty+ "]");
		System.out.println("[Castle: " + castle+ "]");
		System.out.println("[EP: " + ep+ "]");
		String borderLine = "*-------------------------------*";
		for (int i = 0;i < pieces.length;i++) {
			if (i%8 == 0) {
				if (i != 0) {
					System.out.println ('|');
				}
				System.out.println(borderLine);
			}
			System.out.print("| " + (pieces[i] != null ? pieces[i].getCode():" ")  + " ");
		}
		System.out.println ('|');
	    System.out.println(borderLine);
	}
	
    public String toSAN (Move move) {
    	String san = this.toPseudoSAN(move);
		if (this.inCheck(colorToMove)) {
			if (this.isMate()) {
				san += Definitions.MATE;
			} else {
			    san += Definitions.CHECK;
			}
		}
		return san;
    }
    
    public String toSAN (String uciMove) {
    	return toSAN(getPseudoLegalMove (uciMove));
    }
    
    public String toPseudoSAN (Move move) {
    	Assert.notNull(move, "move cannot be null");
    	StringBuffer buffer = new StringBuffer();;
		int from = move.from;
		int to = move.to;
		if (move.isCastle()) {
			if (to == G8 || to == G1 ) {
				buffer.append(CASTLE_SHORT);
			} else {
				buffer.append(CASTLE_LONG);
			}
		} else if (move.isPromote()) {
			if (move.isCapture()) {
				buffer.append(Utils.getField(from) + CAPTURE);
			}
			buffer.append(Utils.toNotation(to));
			buffer.append(PROMOTE + move.getPromotedPiece().toSAN());
		} else {
			buffer.append(toNormalMovePseudoSAN(move));
		}
		return buffer.toString();
	}
    
    public String toPseudoSAN (String uciMove) {
    	 return toPseudoSAN(getPseudoLegalMove(uciMove));
    }
    
    public boolean makeSANMove (String sanMove) {
    	if (sanMove.endsWith("#")) { //ignore chess or mate
    		sanMove = sanMove.replace("#","");
    	} 
    	if (sanMove.endsWith("+")) { //ignore chess or mate
    		sanMove = sanMove.replace("+","");
    	} 
		//******************** offsets *****************************//
		int from = -1;
		int to   = -1;
		//**********************************************************//
		
		//check for castle
        if (CASTLE_SHORT.equals(sanMove)) {
			if (colorToMove.isWhite()) { /*white short castle*/
				from = E1;
				to   = G1;
			} else {                     /*black short castle*/
				from = E8;
				to   = G8;
			} 
		} else if (CASTLE_LONG.equals(sanMove)) {
			if (colorToMove.isWhite()) { /*white long castle*/
				from = E1;
				to   = C1;
			} else {                    /*black long castle*/
				from = E8;
				to   = C8;
			}
		}
        
        if (from > 0) {//castle case
        	return makeMove (getPseudoLegalMove(from, to));
        }
		
		//check for promotion
        int equalIndex = sanMove.indexOf('=');
        Piece promotedPiece = null;
		if (equalIndex > -1) {
			if (colorToMove.isWhite()) {
				promotedPiece = new Piece(sanMove.charAt(equalIndex + 1));
			} else {
				promotedPiece = new Piece(Character.toLowerCase(sanMove.charAt(equalIndex + 1)));
			}
			sanMove = sanMove.substring(0, equalIndex);
		}
		to = Utils.getOffset(sanMove, sanMove.length() - 2);
		List<Move> moves = generateMoves();
        int captureIndex = sanMove.indexOf('x');
        sanMove = sanMove.replace("x", "");
		for (Move move:moves) {
			if (move.to == to) {
				if (sanMove.length() == 2) { //pawn push move, not capture (might be promotion)
					if (Piece.isPawn(pieces[move.from])) {
						move.setPromotedPiece(promotedPiece);
						return makeMove (move);
					}
				} else if (sanMove.length() == 3) { //(not pawn move, might be capture) or (pawn capture move)
					if (Piece.isValidSANCode(sanMove.charAt(0))) { //not pawn move, might be capture
						if (sanMove.charAt(0) == Character.toUpperCase(pieces[move.from].getCode())
								&& isValid(move)) {
							return makeMove (move);
						} 
				    } else if (captureIndex > -1) { //pawn capture move
						if (Piece.isPawn(pieces[move.from]) 
								&& (Utils.getField(move.from) == sanMove.charAt(0))) {
				    		move.setPromotedPiece(promotedPiece);
							return makeMove (move);
				    	}
					}
				} else if (sanMove.charAt(0) == Character.toUpperCase(pieces[move.from].getCode())) {//ambigous move
					if (sanMove.length() == 4) {
						if ((Character.isLetter(sanMove.charAt(1))                 //field case
								&& Utils.getField(move.from) == sanMove.charAt(1))
								|| (Character.isDigit(sanMove.charAt(1))           //rank case
										&& Utils.getRank(move.from) == Character.getNumericValue(sanMove.charAt(1)))) {
							return makeMove (move);
						}
					} else if (sanMove.length() == 5) {
						if (move.from == Utils.getOffset(sanMove.substring(1,3))) {
							return makeMove (move);
						}
					}
				}
			} 
		}
		
		return false;
    }
    
    private String toNormalMovePseudoSAN (Move move) {
    	boolean ambiguous      = false;
		boolean differentField = true;
		boolean differentRank  = true;
		for (Move legalMove : legalMoves()) {
			if (move.isAmbiguous(legalMove, pieces)) {
				ambiguous = true;
				if (Utils.sameField(legalMove.from, move.from)) {
					differentField = false;
				} else if (Utils.sameRank(legalMove.from, move.from)) {
					differentRank = false;
				}
			}
		}
		StringBuffer notation = new StringBuffer();
		notation.append(pieces[move.from].toSAN());
		if (ambiguous) {
			if (differentField) {
				notation.append(Utils.getField(move.from));
			} else if (differentRank) {
				notation.append(Utils.getRank(move.from));
			} else {
				notation.append(Utils.getField(move.from));
				notation.append(Utils.getRank(move.from));
			}
        }
		if (move.isCapture()) {
			if (move.isPawnMove()) {
				notation.append(Utils.getField(move.from));
			}
			notation.append(CAPTURE);
		}
		notation.append(Utils.toNotation(move.to));
		return notation.toString();
    }
    
    public String exportFEN () {
    	StringBuffer buffer = new StringBuffer();
    	int gap = 0;
    	for (int i = 0;i < pieces.length;i++) {
			if (pieces[i] != null) {
				if (gap > 0) {
    				buffer.append(gap);
    			}
				gap = 0;
				buffer.append(pieces[i].getCode());
			} else {
				gap ++;
			}
			if (i % 8 == 7) {
    			if (gap > 0) {
    				buffer.append(gap);
    			}
    			gap = 0;
    			if (i < pieces.length - 1) {
    			    buffer.append("/");
    			}
    		}
		}
    	buffer.append(" ");
    	buffer.append(colorToMove.code);
    	buffer.append(" ");
    	if (castle != NO_CASTLE) {
    		if ((castle & WHITE_CASTLE_KING_SIDE) != 0) {
    			buffer.append("K");
    		}
    		if ((castle & WHITE_CASTLE_QUEEN_SIDE) != 0) {
    			buffer.append("Q");
    		}
    		if ((castle & BLACK_CASTLE_KING_SIDE) != 0) {
    			buffer.append("k");
    		}
    		if ((castle & BLACK_CASTLE_QUEEN_SIDE) != 0) {
    			buffer.append("q");
    		}
    	} else {
    		buffer.append("-");
    	}
    	buffer.append(" ");
    	if (ep > -1) {
    		buffer.append(Utils.toNotation(ep));
    	} else {
    		buffer.append("-");
    	}
    	
    	buffer.append(" ");
    	buffer.append(fifty);
    	buffer.append(" ");
    	buffer.append(fullMoveCounter);
    	return buffer.toString();
    }

	public String toUCINotation (BookEntry bookEntry) {
		int from = Utils.getPolyglotOffset(bookEntry.getFrom_row(), bookEntry.getFrom_file());
		int to = Utils.getPolyglotOffset(bookEntry.getTo_row(), bookEntry.getTo_file());
		if (pieces[from].isKing()) {
			if (to - from == 3) {/*short castle*/
				to = to - 1;
			} else if (from - to == 4) {/*long castle*/
				to = to + 2;
			}
		}
		StringBuffer buffer = new StringBuffer();;
		buffer.append(Utils.toNotation(from));
		buffer.append(Utils.toNotation(to));
		int promotedPiece = bookEntry.getPromotedPiece();
		if (promotedPiece > 0 ) {
			if (promotedPiece == 1) {
				buffer.append('k');
			} else if (promotedPiece == 2) {
				buffer.append('b');
			} else if (promotedPiece == 3) {
				buffer.append('r');
			} else if (promotedPiece == 4) {
				buffer.append('q');
			}
		}
		return buffer.toString();
	}
    
    
    
    public Game.Status computeGameStatus () {
    	Game.Status status = Game.Status.UNDECIDED;
    	if (fifty > 99) {
    		status = Game.Status.DRAW_BY_FIFTY;
    	} else {
	    	List<Move> moves = legalMoves();
	    	if (moves.isEmpty()) {
	    		if (inCheck(colorToMove)) {
	    			status =  Game.Status.MATE;
	    		} else {
	    			status =  Game.Status.DRAW_BY_STALEMATE;
	    		}
	    	} else if (drawByMaterial()) {
	    		status =  Game.Status.DRAW_BY_MATERIAL;
	    	}
    	}
		return status;
	}
    
    public int countPieces () {
    	int counter = 0;
    	for (Piece piece:pieces) {
    		if (piece != null) {
    			counter++;
    		}
    	}
    	return counter;
    }
    
    public List<Piece> nonKingPieces () {
    	List<Piece> nonKingPieces = new ArrayList<Piece>();
    	for (Piece piece:pieces) {
    		if (piece != null && !piece.isKing()) {
    			nonKingPieces.add(piece);
    		}
    	}
    	return nonKingPieces;
    }
    
    public boolean drawByMaterial () {
    	List<Piece> nonKingPieces = nonKingPieces();
    	switch (nonKingPieces.size()) {
    	case 0:
    		return true;
    	case 1:
    		return nonKingPieces.get(0).isLight();
    	case 2:
    		if (nonKingPieces.get(0).opposite(nonKingPieces.get(1)) 
    				&& nonKingPieces.get(0).isBishop() && nonKingPieces.get(1).isBishop()) {
    			List<Integer> bishopOffsets = getPieceOffsets (Piece.Type.BISHOP);
    			return Board.squareColor(bishopOffsets.get(0)).equals(Board.squareColor(bishopOffsets.get(1))) ;
    		}
    	default:
    		return false;	
		}
    }
    
    public List<Integer> getPieceOffsets (Piece.Type pieceType) {
    	List<Integer> pieceOffsets = new ArrayList<Integer>();
    	for (int i = 0;i < 64;i++) {
    		if (pieces[i] != null && pieces[i].getType() == pieceType) {
    			pieceOffsets.add(i);
    		}
    	}
    	return pieceOffsets;
    }
    
    public String makeUCIMove (Move move) {
		String san = toPseudoSAN(move);
		makeMove(move);
		if (inCheck(colorToMove)) {
			if (isMate()) {
				san += Definitions.MATE;
			} else {
			    san += Definitions.CHECK;
			}
		}
		return san;
	}
    
    public String makeUCIMove (String uciMove) {
		return makeUCIMove(getPseudoLegalMove(uciMove));
	}
    
    public PositionDialogModel getModel () {
    	PositionDialogModel model = new PositionDialogModel();
    	model.setCastle(castle);
    	model.setColorToMove(colorToMove);
    	model.setEp(ep > NO_EN_PASSANT ? "" + (char)(ep + 'a') : null);
    	model.setFullMoveCounter(fullMoveCounter);
    	model.setPieces(pieces);
    	return model;
    }
    
	@Override
	public Position clone() {
		Position clone = new Position();
		clone.pieces = clonePieces();
		clone.colorToMove = colorToMove.duplicate();
		clone.fullMoveCounter = fullMoveCounter;
		clone.fifty = fifty;
		clone.castle = castle;
		clone.ep = ep;
		return clone;
	}

	public Piece[] clonePieces () {
		Piece[] cloned = new Piece[64];
		for (int i = 0;i < pieces.length;i++) {
			if (pieces[i] != null) {
			    cloned[i]= pieces[i].clone();
			}
		}
		return cloned;
	}
	
	//----------------------- Tactics ------------------------//
	
	//------------------ PIN 
	/**
	 * Find pins for a given color that make the pin.
	 * @param color The color of the attacker.
	 * @return  The list of found pins.
	 */
	public List<Pin> findPins (ChessColor color) {
		List<Position.Pin> pins = new ArrayList<Position.Pin>();
		for (int offset =0;offset < pieces.length;offset++) {
			if (pieces[offset].getColor() == color 
					&& pieces[offset].isSlide()) {
				pins.addAll(findPins (offset));
			}
		}
		return pins;
	}
	
	/**
	 * Find pin for a given piece (as attacker).
	 * @param offset The attacker's position.
	 * @return The list of found pins.
	 */
	public List<Pin> findPins (int offset) {
		List<Pin> pins = new ArrayList<Pin>();
		Direction[] directions;
		if (pieces[offset].isRook()) {
			directions = STRAIGHT_DIRECTIONS;
		} else if (pieces[offset].isBishop()) {
			directions = CROSS_DIRECTIONS;
		} else if (pieces[offset].isQueen())  {
			directions = ALL_DIRECTIONS;
		} else {
			throw new IllegalArgumentException("Cannot pin but with a sliding piece");
		}
		Integer[] twoPieces;
		for (Direction dir:directions) {
			twoPieces = nextTwoPieceOffsets(offset, pieces[offset].getColor().opposite(), dir);
			if (twoPieces != null && twoPieces.length == 2 && twoPieces[1] != null) {
				if (pieces[twoPieces[1]].isKing() 
						|| pieces[offset].getValue() < pieces[twoPieces[1]].getValue()) {
					pins.add(new Pin(offset, twoPieces[0], twoPieces[1]));
				}
			}
		}
		return pins;
	}
	
	private Integer[] nextTwoPieceOffsets (int offset, ChessColor color, Direction dir) {
		Integer[] nextTwo = null;
		int i = 0;
		int j;
		while ((j = YVALID_POSITIONS[offset] + (++i) * (dir.x + 10 * dir.y)) < 100 && YBOARD[j] != -1) {
			if (pieces[YBOARD[j]] != null 
					&& pieces[YBOARD[j]].getColor() == color) {
				if (nextTwo == null) {
					nextTwo = new Integer[2];
					nextTwo[0] = YBOARD[j];
				} else if (nextTwo[1] == null) {
					nextTwo[1] = YBOARD[j];
				} else {
					break;
				}
			}
		}
		return nextTwo;
	}
	
	/**
	 * Find forks for a given attacking piece.
	 * The attacked pieces may be well protected though.
	 * @param offset The attacker's position.
	 * @return
	 */
	public Fork findFork (int offset) {
		Fork fork = null;
		if (pieces[offset] != null) {
			List<Integer> legalyAttacked = legalyAttackedPieceSquares(offset);
			if (legalyAttacked.size() > 1) {
				fork = new Fork();
				fork.attacker = offset;
				fork.attacked = legalyAttacked;
			}
		}
		return fork;
	}
	
	/**
	 * Find all the pieces of a given player attacking a given square.<br>
	 * <b>OBS.</b> No check for move validity is made!
	 * @param targetOffset The square under attack.
	 * @param color The color of the attacker.
	 * @return The list of attacking pieces.
	 */
	public List<Integer> attackingSquare(int targetOffset, ChessColor color) {
		List<Integer> attackingList = new ArrayList<Integer>();
		for (int i = 0; i < 64; i++) {
			if (Piece.belongsTo(pieces[i], color)) {
				if (pieces[i].isPawn()) {
					if (color == ChessColor.WHITE) {
						if ((i % 8 != 0 && i - 9 == targetOffset) || (i % 8 != 7 && i - 7 == targetOffset)) {
							attackingList.add(i);
						}
					} else if ((i % 8 != 0 && i + 7 == targetOffset) || (i % 8 != 7 && i + 9 == targetOffset)) {
						attackingList.add(i);
					}
				}
				else {
					for (int j = 0; j < pieces[i].getOffset_number(); j++) {
						for (int n = i;;) {
							n = Definitions.XBOARD[Definitions.XVALID_POSITIONS[n] + pieces[i].getOffset()[j]];
							if (n == -1) {
								break;
							}
							if (n == targetOffset) {
								attackingList.add(i);
							}
							if (pieces[n] != null) {
								break;
							}
							if (!pieces[i].isSlide()) {
								break;
							}
						}
					}
				}
			}
		}
		return attackingList;
	}
	
	/**
	 * Find all the pieces attacked from a given square.<br>
	 * <b>OBS. No check for move validity is made!</b>
	 * @param offset The attacker's square.
	 * @return The list of attacked not empty squares.
	 */
	public List<Integer> attackedNotEmptySquares(int offset) {
		return attackedSquares(offset, new PieceOffsetRule());
	}
	
	public List<Integer> legalyAttackedPieceSquares(int offset) {
		List<Integer> attacked =  attackedNotEmptySquares(offset);
		for (Iterator<Integer> itr = attacked.iterator();itr.hasNext();) {
			if (getLegalMove(offset, itr.next()) == null) {
				itr.remove();
			}
		}
		return attacked;
	}
	
	private List<Integer> filterLegalMoves (int from, List<Integer> toList) {
		for (Iterator<Integer> itr = toList.iterator();itr.hasNext();) {
			if (getLegalMove(from, itr.next()) == null) {
				itr.remove();
			}
		}
		return toList;
	}
	
	private List<Integer> filterLegalMoves (List<Integer> fromList, int to) {
		for (Iterator<Integer> itr = fromList.iterator();itr.hasNext();) {
			if (getLegalMove(itr.next(), to) == null) {
				itr.remove();
			}
		}
		return fromList;
	}
	
	/**
	 * Find all the squares (empty or occupied by the opponent's pieces or else, 
	 * depending on the {@link OffsetRule} implementation) attacked from a given square.<br>
	 * <b>OBS.</b> No check for move validity is performed!
	 * @param offset The attacker's square.
	 * @param offsetRule The rule for adding squares to the resulting list.
	 * @return The list of attacked squares.
	 */
	public List<Integer> attackedSquares(int offset, OffsetRule offsetRule) {
		Assert.notNull(pieces[offset], "No piece found at: " + offset);
		List<Integer> attackedList = new ArrayList<Integer>();
		if (pieces[offset].isPawn()) {
			if (pieces[offset].getColor() == ChessColor.WHITE) {
				if (offset % 8 != 0) {
					offsetRule.addOffset(offset - 9, attackedList, pieces[offset].getColor().opposite());
				} else if (offset % 8 != 7) {
					offsetRule.addOffset(offset - 7, attackedList, pieces[offset].getColor().opposite());
				}
			} else if (offset % 8 != 0 ) {
				offsetRule.addOffset(offset + 7, attackedList, pieces[offset].getColor().opposite());
			} else if (offset % 8 != 7) {
				offsetRule.addOffset(offset + 9, attackedList, pieces[offset].getColor().opposite());
			}
		} else {
			for (int j = 0; j < pieces[offset].getOffset_number(); j++) {
				for (int n = offset;;) {
					n = Definitions.XBOARD[Definitions.XVALID_POSITIONS[n] + pieces[offset].getOffset()[j]];
					if (n == -1) {
						break;
					}
					offsetRule.addOffset(n, attackedList, pieces[offset].getColor().opposite());
					if (pieces[n] != null) {
						break;
					}
					if (!pieces[offset].isSlide()) {
						break;
					}
				}
			}
		}
		return attackedList;
	}
	
	/**
	 * Find all the squares - empty or occupied by the opponent's pieces -
	 * attacked from a given square.<br>
	 * <b>OBS.</b> No check for move validity is performed!
	 * @param offset The attacker's square.
	 * @return The list of attacked squares.
	 */
	public List<Integer> attackedEmptyOrEnemySquares (int offset) {
		return attackedSquares (offset, new SimpleOffsetUtil());
	}
	
	/**
	 * Find all the squares - empty or occupied by the opponent's pieces -
	 * legaly attacked from a given square.<br>
	 * @param offset The attacker's square.
	 * @return The list of attacked squares.
	 */
	public List<Integer> legalyAttackedEmptyOrEnemySquares (int offset) {
		return filterLegalMoves(offset, attackedEmptyOrEnemySquares (offset));
	}
	
	/**
	 * Find all the squares attacked from a given square.<br>
	 * <b>OBS.</b> No check for move validity is performed!
	 * @param offset The attacker's square.
	 * @return The list of attacked squares.
	 */
	public List<Integer> attackedSquares (int offset) {
		return attackedSquares (offset, new NoOffsetRule());
	}
	
	/**
	 * Find all the squares legaly attacked from a given square.<br>
	 * @param offset The attacker's square.
	 * @return The list of attacked squares.
	 */
	public List<Integer> legalyAttackedSquares (int offset) {
		return filterLegalMoves(offset, attackedSquares (offset));
	}
	
	//--------------------------------------
	
	//----------------------- Pawn structure
	
    public List<Integer> isolaniPawns (ChessColor color) {
    	List<Integer> isolaniPawns = new ArrayList<Integer>();
    	boolean isIsolani;
    	for (int i = 0;i < pieces.length;i++) {
    		isIsolani = true;
    		if (Piece.isPawn(pieces[i]) && pieces[i].getColor() == color) {
	        	for (int j = 0;j < pieces.length;j++) {
	        		if (i != j && Piece.isPawn(pieces[j])
	        				&& pieces[j].getColor() == color 
	        				&& Math.abs(i & 7 - j & 7) == 1 ) {
	        			isIsolani = false;
	        			break;
	        		}
	        	}
    		}
    		if (isIsolani) {
    			isolaniPawns.add(i);
    		}
    	}
    	return isolaniPawns;
    }
	
    public List<HangingPawns> hangingPawns (ChessColor color) {
    	List<HangingPawns> hangingPawns = new ArrayList<Position.HangingPawns>();
    	int f = 0;
    	while (f < 7) {
    		if (isHalfOpenFile(f, color.opposite()) 
    				&& isHalfOpenFile(f + 1, color.opposite()) 
    				&& (f == 0 || isHalfOpenFile(f, color))
    				&& (f == 6 || isHalfOpenFile(f + 2, color))) {
    			List<Integer> pawns1 = pawnsOnFile(f, color);
    			if (pawns1.size() == 1) {
    				List<Integer> pawns2 = pawnsOnFile(f + 1, color);
    				if (pawns2.size() == 1) {
    					if (pawns1.get(0) >> 3 == pawns2.get(0) >> 3) {
    						hangingPawns.add(new HangingPawns(pawns1.get(0), pawns2.get(0)));
    					}
    				}
    			}
    		}
    		f = f + 2;
    	}
    	return hangingPawns;
    }
    
    public boolean existPawnOnFile (int file, ChessColor color) {//file 0 -> 7
    	return !pawnsOnFile(file, color).isEmpty();
    }
    
    public List<Integer> pawnsOnFile (int file, ChessColor color) {
    	List<Integer> pawnList = new ArrayList<Integer>();
    	for (int i = 0;i < pieces.length;i++) {
    		if ((i & 7) == file && Piece.isPawn(pieces[i]) 
    				&& pieces[i].belongsTo(color)) {
    			pawnList.add(i);
    		}
    	}
    	return pawnList;
    }
    
    public boolean isHalfOpenFile (int file, ChessColor color) {
    	return !existPawnOnFile(file, color);
    }
    
    public boolean isOpenFile (int file) {
    	for (int i = 0;i < pieces.length;i++) {
    		if ((i & 7) == file && Piece.isPawn(pieces[i])) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public List<Integer> passedPawns (ChessColor color) {
    	List<Integer> passedPawns = new ArrayList<Integer>();
    	boolean passed;
    	int start;
    	int end;
    	for (int i = 0;i < pieces.length;i++) {
    		if (Piece.isPawn(pieces[i]) && pieces[i].belongsTo(color)) {
    			passed = true;
    			start = color.isWhite() ? 0 : i + 7;
    			end = color.isWhite() ? i - 6 : pieces.length;
    			for (int j = start;j < end;j++) {
					if (Piece.isPawn(pieces[j])) {
						if (((i & 7) == (j & 7)) 
								|| (pieces[j].belongsTo(color.opposite()) 
								       && Math.abs((i & 7) - (j & 7)) == 1)) {
							passed = false;
							break;
						} 
					}
				}
    			if (passed) {
    				passedPawns.add(i);
    			}
    		}
    	}
    	return passedPawns;
    }
    
	//--------------------------------------
	
    //---------------------------- Material balance
    
    public int materialValue (ChessColor color) {
    	int sum = 0;
    	for (int i = 0;i < pieces.length;i++) {
    		if (pieces[i].belongsTo(color)) {
    			sum += pieces[i].getValue();
    		}
    	}
    	return sum;
    }
    
    /**
     * Gets the material balance. If positive, it is in white favor.
     * @return The sum of pieces value  pondered by color sign (+1 for white, -1 for black).
     */
    public int materialBalance () {
    	int sum = 0;
    	for (int i = 0;i < pieces.length;i++) {
    		sum += pieces[i].getValue() * pieces[i].getColor().sign() ;
    	}
    	return sum;
    }
    
    //-------------------------------------------
	
    //------------------------------- King Safety
    
    public int kingPawnShelter (ChessColor color) {
    	int kingOffset = getKingOffset(color);
    	Assert.isTrue(kingOffset > 0, color.asString() + " king is missing!");
    	byte pawnOnRank0 = 0;
    	byte pawnOnRank1 = 0;
    	for (int i = 0;i < pieces.length;i++) {
    		if (Piece.isPawn(pieces[i]) && pieces[i].belongsTo(color)) {
    			if (Math.abs((kingOffset & 7) - (i & 7)) < 2) {
    				if (i >> 3 == ((kingOffset >> 3) - color.sign())) {
    					pawnOnRank0++;
    				} else if (i >> 3 == ((kingOffset >> 3) - 2 * color.sign())) {
    					pawnOnRank1++;
    				}
    			}
    		}
    	}
    	return pawnOnRank1 + 4 * pawnOnRank0;
    }
    
    public int getKingOffset (ChessColor color) {
    	for (int i = 0;i < pieces.length;i++) {
    		if (Piece.isKing(pieces[i]) && pieces[i].belongsTo(color)) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    //---------------------------------------------
    
    //--------------------------------- Center control
    
    public int countAttackersOnCenter (ChessColor color) {
    	return countAttackers(color, D4) + countAttackers(color, E4)
    	   + countAttackers(color, D5) + countAttackers(color, E5);
    }
    
    public int countAttackers (ChessColor color, int square) {
    	return filterLegalMoves(attackingSquare(square, color), square).size();
    }
    
    //------------------------------------------------
    
    //-------------------------------- Mobility
    
    public int mobility (ChessColor color, Piece.Type pieceType) {
    	int sum = 0;
    	for (int i = 0;i < pieces.length;i++) {
    		if (pieces[i] != null && pieces[i].getType() == pieceType 
    				&& pieces[i].belongsTo(color)) {
    			sum += attackedEmptyOrEnemySquares(i).size();
    		}
    	}
    	return sum;
    }
    
    public int mobility (int offset) {
    	return attackedEmptyOrEnemySquares(offset).size();
    }
    
    //-----------------------------------------
    
    //-------------------------------- Space
    
    public int space (ChessColor color) {
    	int sum = 0;
    	for (int i = 0;i < pieces.length;i++) {
    		if (pieces[i] != null && pieces[i].belongsTo(color)) {
    			for(Iterator<Integer> itr = legalyAttackedSquares(i).iterator();itr.hasNext();) {
    				if ((color.isWhite() && i < 32) 
    						|| (color.isBlack() && i > 31)) {
    					sum ++;
    				}
    			}
    		}
    	}
    	return sum;
    }
    
    //-----------------------------------------
    
    @Override
	public String toString() {
		return "Position [castle=" + castle + ", colorToMove=" + colorToMove
				+ ", ep=" + ep + ", fifty=" + fifty + ", fullMoveCounter="
				+ fullMoveCounter + ", pieces=" + Arrays.toString(pieces) + "]";
	}
	
	public static interface OffsetRule {
		void addOffset (int offset,List<Integer> list,ChessColor color);
	}
	
	private class PieceOffsetRule implements OffsetRule {
		@Override
		public void addOffset(int offset, List<Integer> list, ChessColor color) {
			if (pieces[offset] != null && pieces[offset].getColor() == color) {
				list.add(offset);
			}
		}
	}
	
	/**
	 * Empty square or occupied by opponent's pieces.
	 * @author Eugen Covaci
	 */
	private class SimpleOffsetUtil implements OffsetRule {
		@Override
		public void addOffset(int offset, List<Integer> list, ChessColor color) {
			if (pieces[offset] == null || pieces[offset].getColor() == color) {
				list.add(offset);
			}
		}
	}
	
	private class NoOffsetRule implements OffsetRule {
		@Override
		public void addOffset(int offset, List<Integer> list, ChessColor color) {
			list.add(offset);
		}
	}
}
