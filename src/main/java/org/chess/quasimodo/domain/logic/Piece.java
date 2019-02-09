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

import org.springframework.util.StringUtils;

public class Piece implements Definitions, Cloneable {
	public enum Type {
		PAWN	(100, false, OFFSET_NUMBERS[0], OFFSET[0]),
		KNIGHT	(300, false, OFFSET_NUMBERS[1], OFFSET[1]),
		BISHOP	(300, true,  OFFSET_NUMBERS[2], OFFSET[2]),
		ROOK	(500, true,  OFFSET_NUMBERS[3], OFFSET[3]),
		QUEEN	(900, true,  OFFSET_NUMBERS[4], OFFSET[4]),
		KING	(  0, false, OFFSET_NUMBERS[5], OFFSET[5]);
		 
		private int     value;
		private boolean slide;
        private byte    offset_number;
        private byte[]  offset; 
         
	    Type (int value, boolean slide, byte offset_number, byte[] offset) {
			this.value = value;
			this.slide = slide;
			this.offset_number = offset_number;
			this.offset = offset;
		}
	}
	
	private char code;
	private byte zobristValue;
	private Type type;

	/**
	 * @param code
	 */
	public Piece(char code) {
		if ('p' == code || 'P' == code) {
			type = Type.PAWN;
			if ('p' == code) {
				zobristValue = 0;
			} else {
				zobristValue = 1;
			}
		} else if ('n' == code || 'N' == code) {
			type = Type.KNIGHT;
			if ('n' == code) {
				zobristValue = 2;
			} else {
				zobristValue = 3;
			}
		} else if ('b' == code || 'B' == code) {
			type = Type.BISHOP;
			if ('b' == code) {
				zobristValue = 4;
			} else {
				zobristValue = 5;
			}
		} else if ('r' == code || 'R' == code) {
			type = Type.ROOK;
			if ('r' == code) {
				zobristValue = 6;
			} else {
				zobristValue = 7;
			}
		} else if ('q' == code || 'Q' == code) {
			type = Type.QUEEN;
			if ('q' == code) {
				zobristValue = 8;
			} else {
				zobristValue = 9;
			}
		} else if ('k' == code || 'K' == code) {
			type = Type.KING;
			if ('k' == code) {
				zobristValue = 10;
			} else {
				zobristValue = 11;
			}
		} else {
			throw new IllegalArgumentException("Invalid piece code: " + code);
		}
		
		this.code = code;
	}
	
	public static Piece getPieceBySymbol (String letter) {
		if (StringUtils.hasLength(letter)) {
			char symbol = letter.charAt(0);
			switch (symbol) {
			case 'p':
				return new Piece ('P');
			case 'o':
				return new Piece ('p');
			case 'n':
				return new Piece ('N');
			case 'm':
				return new Piece ('n');
			case 'b':
				return new Piece ('B');
			case 'v':
				return new Piece ('b');
			case 'r':
				return new Piece ('R');
			case 't':
				return new Piece ('r');
			case 'q':
				return new Piece ('Q');
			case 'w':
				return new Piece ('q');
			case 'k':
				return new Piece ('K');
			case 'l':
				return new Piece ('k');
			default:
				return null;	
			}
		} 
		return null;
	}
	
	public Type getType() {
		return type;
	}

	public char getCode() {
		return code;
	}

	public ChessColor getColor () {
		if (Character.isUpperCase(code)){
			return ChessColor.WHITE;
		}
		return ChessColor.BLACK;
	}
	
	public boolean isWhite () {
		return Character.isUpperCase(code);
	}
	
	public boolean isBlack () {
		return Character.isLowerCase(code);
	}
	
	public boolean isSlide() {
		return type.slide;
	}
	
	 public int getValue() {
		 return type.value;
	 }


	protected byte getOffset_number() {
		return type.offset_number;
	}

	protected byte[] getOffset() {
		return type.offset;
	}
	
	public boolean isPawn () {
		return type == Type.PAWN;
	}
	
	public boolean isKing () {
		return type == Type.KING;
	}
	
	public boolean belongsTo (ChessColor color) {
		return getColor() == color;
	}
	
	public boolean opposite (Piece piece) {
		return getColor() != piece.getColor();
	}
	
	public boolean sameType (Piece piece) {
		return type == piece.type;
	}
	
	public byte getZobristValue() {
		return zobristValue;
	}

	public boolean isLight() {
		return type == Type.KNIGHT || type == Type.BISHOP;
	}
	
	public boolean isBishop () {
		return type == Type.BISHOP;
	}
	
	public boolean isRook () {
		return type == Type.ROOK;
	}
	
	public boolean isQueen() {
		return type == Type.QUEEN;
	}
	
	public static boolean isPawn (Piece piece) {
		return piece != null && piece.type == Type.PAWN;
	}
	
	public static boolean isKing (Piece piece) {
		return piece != null && piece.type == Type.KING;
	}
	
	public static boolean isRook (Piece piece) {
		return piece != null && piece.type == Type.ROOK;
	}
	
	public static boolean isWhite(Piece piece) {
    	return piece != null && piece.isWhite();
    }
	
	public static boolean isBlack(Piece piece) {
    	return piece != null && piece.isBlack();
    }
	
	public static boolean belongsTo (Piece piece, ChessColor player) {
    	return piece != null && piece.getColor() == player;
    }
	
	public static boolean isValidSANCode (char c) {
		return c == 'N' || c == 'B' || c == 'R' || c == 'Q' || c == 'K';
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Piece other = (Piece) obj;
		if (code != other.code)
			return false;
		return true;
	}

	@Override
	protected Piece clone() {
		return new Piece(code);
	}

	public String toSAN () {
		String san = "";
		if (!isPawn()) {
		   san += Character.toUpperCase(code);
		} 
		return san;
	}
	
	public String toLongNotation () {
		String san = "";
		if (!isPawn()) {
		   san += Character.toLowerCase(code);
		} 
		return san;
	}
	
	@Override
	public String toString() {
		return "Piece [code=" + code + ", type="
				+ type + ", zobristValue=" + zobristValue + "]";
	}
	
}
