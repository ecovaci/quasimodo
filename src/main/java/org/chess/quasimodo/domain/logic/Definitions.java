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
import java.util.List;

public interface Definitions {
    char WHITE_QUEEN   = 'Q';
    char WHITE_KING    = 'K';
    char WHITE_BISHOP  = 'B';
    char WHITE_KNIGHT  = 'N';
    char WHITE_ROOK    = 'R';
    char WHITE_PAWN    = 'P';
    
    char BLACK_QUEEN   = 'q';
    char BLACK_KING    = 'k';
    char BLACK_BISHOP  = 'b';
    char BLACK_KNIGHT  = 'n';
    char BLACK_ROOK    = 'r';
    char BLACK_PAWN    = 'p';
    
    /* a bitfield with the castle permissions. if 1 is set,
    white can still castle kingside. 2 is white queenside.
	4 is black kingside. 8 is black queenside. */
    byte NO_CASTLE = 0;
    byte WHITE_CASTLE_KING_SIDE = 1;
    byte WHITE_CASTLE_QUEEN_SIDE = 2;
    byte BLACK_CASTLE_KING_SIDE = 4;
    byte BLACK_CASTLE_QUEEN_SIDE = 8;
    
    String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    long E4_ZOBRIST_KEY = -9062197578030825066L;
    
    /* useful squares */
    byte  A1 =  56;
    byte  B1 =	57;
    byte  C1 =	58;
    byte  D1 =	59;
    byte  E1 =	60;
    byte  F1 =	61;
    byte  G1 =	62;
    byte  H1 =	63;
    byte  A8 =	0;
    byte  B8 =	1;
    byte  C8 =	2;
    byte  D8 =	3;
    byte  E8 =	4;
    byte  F8 =	5;
    byte  G8 =	6;
    byte  H8 =	7;
    byte  D4 =  35;
    byte  D5 =  27;
    byte  E4 =  36;
    byte  E5 =  28;
    
    
    /**
     * Extended board.
     */
    byte XBOARD [] = {
		 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		 -1,  0,  1,  2,  3,  4,  5,  6,  7, -1,
		 -1,  8,  9, 10, 11, 12, 13, 14, 15, -1,
		 -1, 16, 17, 18, 19, 20, 21, 22, 23, -1,
		 -1, 24, 25, 26, 27, 28, 29, 30, 31, -1,
		 -1, 32, 33, 34, 35, 36, 37, 38, 39, -1,
		 -1, 40, 41, 42, 43, 44, 45, 46, 47, -1,
		 -1, 48, 49, 50, 51, 52, 53, 54, 55, -1,
		 -1, 56, 57, 58, 59, 60, 61, 62, 63, -1,
		 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	};

    /**
     * Valid positions for extended board.
     */
	byte XVALID_POSITIONS [] = {
		21, 22, 23, 24, 25, 26, 27, 28,
		31, 32, 33, 34, 35, 36, 37, 38,
		41, 42, 43, 44, 45, 46, 47, 48,
		51, 52, 53, 54, 55, 56, 57, 58,
		61, 62, 63, 64, 65, 66, 67, 68,
		71, 72, 73, 74, 75, 76, 77, 78,
		81, 82, 83, 84, 85, 86, 87, 88,
		91, 92, 93, 94, 95, 96, 97, 98
	};
	
	/**
	 * Yet another board.
	 */
    int YBOARD [] = {
   		 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
   		 -1,  0,  1,  2,  3,  4,  5,  6,  7, -1,
   		 -1,  8,  9, 10, 11, 12, 13, 14, 15, -1,
   		 -1, 16, 17, 18, 19, 20, 21, 22, 23, -1,
   		 -1, 24, 25, 26, 27, 28, 29, 30, 31, -1,
   		 -1, 32, 33, 34, 35, 36, 37, 38, 39, -1,
   		 -1, 40, 41, 42, 43, 44, 45, 46, 47, -1,
   		 -1, 48, 49, 50, 51, 52, 53, 54, 55, -1,
   		 -1, 56, 57, 58, 59, 60, 61, 62, 63, -1,
   		 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
   	};

    /**
     * Valid positions for yet another board.
     */
   	int YVALID_POSITIONS [] = {
   		11, 12, 13, 14, 15, 16, 17, 18,
   		21, 22, 23, 24, 25, 26, 27, 28,
   		31, 32, 33, 34, 35, 36, 37, 38,
   		41, 42, 43, 44, 45, 46, 47, 48,
   		51, 52, 53, 54, 55, 56, 57, 58,
   		61, 62, 63, 64, 65, 66, 67, 68,
   		71, 72, 73, 74, 75, 76, 77, 78,
   		81, 82, 83, 84, 85, 86, 87, 88
   	};
	
	byte CASTLE_MASK[] = {
		 7, 15, 15, 15,  3, 15, 15, 11,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		13, 15, 15, 15, 12, 15, 15, 14
	};
	
	byte OFFSET_NUMBERS[] = {
		0, 8, 4, 4, 8, 8
	};
	
	byte OFFSET[][] = {
		{ 0, 0, 0, 0, 0, 0, 0, 0 },
		{ -21, -19, -12, -8, 8, 12, 19, 21 },
		{ -11, -9, 9, 11, 0, 0, 0, 0 },
		{ -10, -1, 1, 10, 0, 0, 0, 0 },
		{ -11, -10, -9, -1, 1, 9, 10, 11 },
		{ -11, -10, -9, -1, 1, 9, 10, 11 }
	};
	
    class HangingPawns {
    	public int offset1;
    	public int offset2;
    	
    	public HangingPawns(int offset1, int offset2) {
			this.offset1 = offset1;
			this.offset2 = offset2;
		}
    }
	
	class Pin {
		public Integer attacker;
		public Integer pinned;
		public Integer valuable;
		
		public Pin(Integer attacker, Integer pinned, Integer valuable) {
			this.attacker = attacker;
			this.pinned = pinned;
			this.valuable = valuable;
		}

		@Override
		public String toString() {
			return "Pin [attacker=" + attacker + ", pinned=" + pinned
					+ ", valuable=" + valuable + "]";
		}
		
	}
	
	class Fork {
		public Integer attacker;
		public List<Integer> attacked = new ArrayList<Integer>();
		
		@Override
		public String toString() {
			return "Fork [attacker=" + attacker + ", attacked=" + attacked
					+ "]";
		}
	}
	
	class Direction {
		public byte x;
		public byte y;
		
		public Direction(byte x, byte y) {
			this.x = x;
			this.y = y;
		}
	}
	
	Direction UP = new Direction((byte)0, (byte)-1);
	Direction DOWN = new Direction((byte)0, (byte)1);
	Direction RIGHT = new Direction((byte)1, (byte)0);
	Direction LEFT = new Direction((byte)-1, (byte)0);
	Direction UP_LEFT = new Direction((byte)-1, (byte)-1);
	Direction DOWN_LEFT = new Direction((byte)-1, (byte)1);
	Direction UP_RIGHT = new Direction((byte)1, (byte)-1);
	Direction DOWN_RIGHT = new Direction((byte)1, (byte)1);
	
	Direction[] ALL_DIRECTIONS = {UP, DOWN, RIGHT, LEFT, UP_LEFT, DOWN_LEFT, UP_RIGHT, DOWN_RIGHT};
	Direction[] STRAIGHT_DIRECTIONS = {UP, DOWN, RIGHT, LEFT};
	Direction[] CROSS_DIRECTIONS = {UP_LEFT, DOWN_LEFT, UP_RIGHT, DOWN_RIGHT};
	
	String WKING   = "k";
	String BKING   = "l";
	String WQUEEN  = "q";
	String BQUEEN  = "w";
	String WROOK   = "r";
	String BROOK   = "t";
	String WBISHOP = "b";
	String BBISHOP = "v";
	String WKNIGHT = "n";
	String BKNIGHT = "m";
	String WPAWN   = "p";
	String BPAWN   = "o";
	
	String CASTLE_SHORT  = "O-O";
	String CASTLE_LONG   = "O-O-O";
	String CHECK         = "+";
	String MATE         = "#";
	String PROMOTE       = "=";
	String CAPTURE       = "x";
	
	String WHITE_WINS = "1-0";
	String BLACK_WINS = "0-1";
	String DRAW = "1/2-1/2";
	String UNDECIDED = "*";
	
	String WHITE_DECISIVE_ADVANTAGE = "+-";
	String BLACK_DECISIVE_ADVANTAGE = "-+";
	String WHITE_MODERATE_ADVANTAGE = "+/-";
	String BLACK_MODERATE_ADVANTAGE = "-/+";
	String WHITE_SLIGHT_ADVANTAGE = "+/=";
	String BLACK_SLIGHT_ADVANTAGE = "-/=";
	String EQUALITY = "=";
	
	String IRREGULAR_OPENING_CODE = "A00";
	
	byte THREE_PAWNS        = 6;
	byte TWO_PAWNS_AND_HALF = 5;
	byte TWO_PAWNS          = 4;
	byte ONE_PAWNS_TWO_HALF = 3;
	byte ONE_PAWNS_ONE_HALF = 2;
	byte ONE_PAWN           = 1;
	byte NO_PAWN            = 0;
	
	enum Evaluation {
		GOOD_MOVE (1, "good move", "!"),
		POOR_MOVE (2,  "poor move", "?"),
		VERY_GOOD_MOVE (3 , "very good move", "!!"),
		VERY_POOR_MOVE (4 , "very poor move", "??" ),
		SPECULATIVE_MOVE (5, "speculative move", "!?" ),
		QUESTIONABLE_MOVE (6, "questionable move", "?!"),
		FORCED_MOVE (7, "forced move"),
		DRAWISH_POSITION (10, "drawish position", "="),
		UNCLEAR_POSITION (13, "unclear position", "" + (char)8734),
		WHITE_SLIGHT_ADVANTAGE (14, "White has a slight advantage", "+/-"),
		BLACK_SLIGHT_ADVANTAGE (15, "Black has a slight advantage", "-/+"),
		WHITE_MODERATE_ADVANTAGE (16, "White has a moderate advantage", "" + (char)177),
		BLACK_MODERATE_ADVANTAGE (17, "Black has a moderate advantage", "" + (char)8723 ),
		WHITE_DECISIVE_ADVANTAGE (18, "White has a decisive advantage", "+-"),
		BLACK_DECISIVE_ADVANTAGE (19, "Black has a decisive advantage", "-+");
		
		private int glyph;
		private String description;
		private String symbol;
		
		public int getGlyph() {
			return glyph;
		}

		public String getDescription() {
			return description;
		}

		public String getSymbol() {
			return symbol;
		}

		Evaluation(int glyph, String description) {
			this.glyph = glyph;
			this.description = description;
		}
		
		Evaluation(int glyph, String description, String symbol) {
			this.glyph = glyph;
			this.description = description;
			this.symbol = symbol;
		}
		
		
	}
}
