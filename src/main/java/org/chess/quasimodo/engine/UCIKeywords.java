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
package org.chess.quasimodo.engine;

public interface UCIKeywords {
	/**
	 * quit UCI keyword.
	 */
    String QUIT = "quit";
    
    /**
     * uci UCI keyword.
     */
    String UCI = "uci";
    
    /**
     * debug UCI keyword.
     */
    String DEBUG = "debug";
    
    /**
     * isready UCI keyword.
     */
    String ISREADY = "isready";
    
    /**
     * setoption UCI keyword.
     */
    String SETOPTION = "setoption";
    
    /**
     * register UCI keyword.
     */
    String REGISTER = "register";
    
    /**
     * ucinewgame UCI keyword.
     */
    String NEWGAME = "ucinewgame";
    
    /**
     * position UCI keyword.
     */
    String POSITION_FEN = "position";
    
    /**
     * fen UCI keyword.
     */
    String FEN  = "fen";
    
    /**
     * startpos UCI keyword.
     */
    String STARTPOS = "startpos";
    
    /**
     * moves UCI keyword.
     */
    String MOVES = "moves";
    
    /**
     * searchmoves UCI keyword.
     */
    String SEARCHMOVES  = "searchmoves";
    
    /**
     * go UCI keyword.
     */
    String  GO = "go";
    
    /**
     * go infinite command.
     */
    String  GO_INFINITE = "go infinite";
    
    /**
     * go ponder command.
     */
    String  GO_PONDER = "go ponder";
    
    /**
     * stop UCI keyword.
     */
    String  STOP = "stop";
    
    /**
     * ponderhit UCI keyword.
     */
    String  PONDERHIT = "ponderhit";
    
    /**
     * id UCI keyword.
     */
    String  ID = "id";
    
    /**
     * author UCI keyword.
     */
    String  AUTHOR = "author";
    
    /**
     * type UCI keyword.
     */
    String  TYPE = "type";
    
    /**
     * default UCI keyword.
     */
    String  DEFAULT = "default";
    
    /**
     * min UCI keyword.
     */
    String  MIN = "min";
    
    /**
     * max UCI keyword.
     */
    String  MAX = "max";
    
    /**
     * uciok UCI keyword.
     */
    String  UCIOK = "uciok";
    
    /**
     * readyok UCI keyword.
     */
    String  READYOK = "readyok";
    
    /**
     * bestmove UCI keyword.
     */
    String  BESTMOVE = "bestmove";
    
    /**
     * copyprotection UCI keyword.
     */
    String  COPYPROTECTION = "copyprotection";
    
    /**
     * registration UCI keyword.
     */
    String  REGISTRATION = "registration";
    
    /**
     * info UCI keyword.
     */
    String  INFO = "info";
    
    /**
     * option UCI keyword.
     */
    String  OPTION = "option";
    
    /**
     * name UCI keyword.
     */
    String  NAME = "name";
    
    /**
     * value UCI keyword.
     */
    String  VALUE = "value";
    
    /**
     * depth UCI keyword.
     */
    String  DEPTH = "depth";
    
    /**
     * seldepth UCI keyword.
     */
    String  SELDEPTH = "seldepth";
    
    /**
     * pv UCI keyword.
     */
	String  PV = "pv";  
	
    /**
     * hashfull UCI keyword.
     */
	String  HASHFULL = "hashfull"; 
	
    /**
     * nps UCI keyword.
     */
	String  NPS = "nps"; 
	
    /**
     * time UCI keyword.
     */
	String  TIME = "time"; 
	
    /**
     * nodes UCI keyword.
     */
	String  NODES = "nodes"; 
	
    /**
     * tbhits UCI keyword.
     */
	String  TBHITS = "tbhits";
	
    /**
     * sbhits UCI keyword.
     */
	String  SBHITS = "sbhits";
	
    /**
     * cpuload UCI keyword.
     */
	String  CPULOAD = "cpuload";
	
    /**
     * currmove UCI keyword.
     */
	String  CURRMOVE = "currmove";
	
    /**
     * currmovenumber UCI keyword.
     */
	String  CURRMOVENUMBER = "currmovenumber";
	
    /**
     * currline UCI keyword.
     */
	String  CURRLINE = "currline";
	
    /**
     * string UCI keyword.
     */
	String  STRING = "string";
	
    /**
     * multipv UCI keyword.
     */
	String  MULTIPV = "multipv";
	
    /**
     * refutation UCI keyword.
     */
	String  REFUTATION = "refutation";
	
    /**
     * score UCI keyword.
     */
	String  SCORE = "score";
	
    /**
     * cp UCI keyword.
     */
	String  CP = "cp"; 
	
    /**
     * mate UCI keyword.
     */
	String  MATE = "mate"; 
	
    /**
     * lowerbound UCI keyword.
     */
	String  LOWERBOUND = "lowerbound";
	
    /**
     * upperbound UCI keyword.
     */
	String  UPPERBOUND = "upperbound";
	
    /**
     * movestogo UCI keyword.
     */
	String  MOVESTOGO = "movestogo";
	
    /**
     * movetime UCI keyword.
     */
	String  MOVETIME = "movetime";
	
    /**
     * btime UCI keyword.
     */
	String  BTIME = "btime"; 
	
    /**
     * wtime UCI keyword.
     */
	String  WTIME = "wtime"; 
	
    /**
     * winc UCI keyword.
     */
	String  WINC = "winc"; 
	
    /**
     * binc UCI keyword.
     */
	String  BINC = "binc";
	
}
