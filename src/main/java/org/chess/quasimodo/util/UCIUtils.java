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
package org.chess.quasimodo.util;

import java.text.DecimalFormat;

import org.springframework.util.Assert;

public class UCIUtils {
	public static final String OPTION_PATTERN = "setoption name {0} value {1}";
	public static final String POSITION_PATTERN_2 = "position {0} {1}";
	public static final String POSITION_PATTERN_3 = "position {0} {1} {2}";
	public static final String GO_INFINITE_PATTERN = "go infinite {0} {1}";
	
	//--------------------------------- Go command section
	public static final String GO_PATTERN_1  = "go {0}";
	public static final String GO_PATTERN_2  = "go {0} {1}";
	public static final String GO_PATTERN_3  = "go {0} {1} {2}";
	public static final String GO_PATTERN_4  = "go {0} {1} {2} {3}";
	public static final String GO_PATTERN_8  = "go {0} {1} {2} {3} {4} {5} {6} {7}";
	public static final String GO_PATTERN_10 = "go {0} {1} {2} {3} {4} {5} {6} {7} {8} {9}";
	//-----------------------------------------------------
	
	public static final DecimalFormat SCORE_FORMAT = new DecimalFormat("0.00");
	
  /*  public static String format (String source, Object ... params) {
        return MessageFormat.format(source, params);
    }*/
    
    public static String formatScore (int cpscore) {
    	double pScore = cpscore/100.00;
    	if (pScore > 0) {
    		SCORE_FORMAT.setPositivePrefix("+");
    	} else {
    		SCORE_FORMAT.setPositivePrefix("");
    	}
   	    return SCORE_FORMAT.format(cpscore/100.00);
    }
    
    public static String getSimpleFEN (String fen) {
    	String [] splited = fen.split("\\s");
    	Assert.isTrue(splited.length == 6, "Invalid FEN, has [" + splited.length + "], instead of 6");
    	return splited[0] + " " + splited[1]+ " " + splited[2] + " " + splited[3];  
    }
    
    public static void main(String[] args) {
		System.out.println(formatScore(Integer.parseInt("-10")));
	}
}
