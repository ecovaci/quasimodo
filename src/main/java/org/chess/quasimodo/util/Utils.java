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

import java.awt.Font;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import org.apache.commons.lang3.text.WordUtils;
import org.chess.quasimodo.domain.logic.Definitions;
import org.chess.quasimodo.pgn.domain.Opening;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;



public class Utils {
	public static final DecimalFormat TWO_DIGITS_FORMAT = new DecimalFormat("00");
	public static final DecimalFormat INTEGER_FORMAT = new  DecimalFormat("#");
	public static final String PV_PATTERN = "{0}\n     {1} Depth: {2} Nodes: {3} kN  Time: {4}\n";
	public static final String TIME_SEPARATOR = ":";
	public static final String UNKNOWN = "?";
	
	//TODO - relocated, Position or board
    public static String toNotation (int offset) {
    	return (char)((offset & 7) + 'a') + "" +  (8 - (offset >> 3)); 
    }
    
    public static char getField (int offset) {
    	return (char)((offset & 7) + 'a');
    }
    
    public static int getRank (int offset) {
    	return 8 - (offset >> 3);
    }
    
    public static int getOffset (char field, int rank) {
    	return (8 - rank) * 8 + (field - 'a');
    }
    
    public static int getOffset (String str, int start) {
    	return getOffset(str.charAt(start), Integer.parseInt(str.substring(start + 1, start + 2)));
    }
    
    public static int getOffset (String move) {
    	return getOffset(move, 0);
    }
    
    public static int getPolyglotOffset (int row, int file) {
    	return (7 - row) * 8  + file;
    }
    
    public static boolean sameField (int offset1, int offset2) {
    	return (offset1  & 7) == (offset2  & 7);
    }
    
    public static boolean sameField (int offset1, int offset2, int offset3) {
    	return sameField(offset1, offset2) && sameField(offset2, offset3);
    }
    
    public static boolean sameRank (int offset1, int offset2) {
    	return offset1 >> 3 == offset2 >> 3;
    }
    
    public static boolean sameRank (int offset1, int offset2, int offset3) {
    	return sameRank(offset1, offset2) && sameRank(offset2, offset3);
    }
    
    public static boolean onFirstDiag (int offset1, int offset2) {
    	return Math.abs(offset2 - offset1) >> 3 == (Math.abs(offset2 - offset1) & 7);
    }
    
    public static boolean onSecondDiag (int offset1, int offset2) {
    	return (offset1 % 7) == (offset2 % 7);
    }
    
    public static boolean onSecondDiag (int offset1, int offset2, int offset3) {
    	return onSecondDiag(offset1, offset2) && onSecondDiag(offset2, offset3);
    }
    
    public static boolean onFirstDiag (int offset1, int offset2, int offset3) {
    	return onFirstDiag(offset1, offset2) && onFirstDiag(offset2, offset3);
    }
    
    public static boolean between (int offset1, int offset2, int offset3) {
    	return ((offset1 < offset2 && offset2 < offset3) 
    			    || (offset3 < offset2 && offset2 < offset1)) 
    	    && (sameRank(offset1, offset2, offset3) 
    	    		|| sameField(offset1, offset2, offset3)
    	    		|| onFirstDiag(offset1, offset2, offset3)
    	    		|| onSecondDiag(offset1, offset2, offset3));
    }
    
    public static boolean isCentralSquare (int offset) {
    	return offset == Definitions.D4 || offset == Definitions.D5 
    	          || offset == Definitions.E4 || offset == Definitions.E5;
    }
    
    public static boolean northArea (int offset) {
    	return offset < 32;
    }
    
    public static boolean southArea (int offset) {
    	return offset > 31;
    }
    
    public static boolean queenSide (int offset) {
    	return (offset & 7) < 4;
    }
    
    public static boolean kingSide (int offset) {
    	return (offset & 7) > 3;
    }
    
    public static int decodeAttackers (int digest, int square) {
    	if (square == Definitions.D4) {
    		return digest >>> 24;
    	} else if (square == Definitions.E4) {
    		return (digest << 8) >>> 24;
    	} else if (square == Definitions.D5) {
    		return (digest << 16) >>> 24;
    	} else if (square == Definitions.E5) {
    		return (digest << 24) >>> 24;
    	}
    	return -1;
    }
    
    //**************************************************************************************************//
    
    public static String addRoundBrackets (String word)  {
    	return "(" + word + ")";
    }
    
    public static String toString (Rectangle rectangle) {
    	Assert.notNull(rectangle, "null rectangle");
    	return rectangle.x + ";" + rectangle.y + ";" + rectangle.width + ";" + rectangle.height;
    }
    
    public static String toStringUnix (Rectangle rectangle) {
    	Assert.notNull(rectangle, "null rectangle");
    	int xpoz = rectangle.x > 0 ? rectangle.x :  0;
    	int ypoz = rectangle.y > 0 ? rectangle.y :  0;
    	return xpoz + ";" + ypoz + ";" + rectangle.width + ";" + rectangle.height;
    }
    
    public static Rectangle toRectangle(String s) {
    	Assert.notNull(s, "null s");
    	String[] splited = s.split(",");
    	return new Rectangle(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]),
    			Integer.parseInt(splited[2]), Integer.parseInt(splited[3]));
    }
    
	public static String formatSeconds (long seconds) {
		long minutes = seconds / 60;
		long hours =  minutes / 60;
		return hours + TIME_SEPARATOR + TWO_DIGITS_FORMAT.format(minutes % 60) 
		    + TIME_SEPARATOR + TWO_DIGITS_FORMAT.format( seconds % 60);
	}
	
	public static String formatMiliSeconds (long miliseconds) {
		long seconds = miliseconds / 1000;
		long minutes =  seconds / 60;
		return TWO_DIGITS_FORMAT.format(minutes) 
		    + TIME_SEPARATOR + TWO_DIGITS_FORMAT.format(seconds % 60)
		    + TIME_SEPARATOR + miliseconds % 1000;
	}
	
	public static String formatPV (String variation, String score,
			String depth, String nodes, String time) {
		return MessageFormat.format(PV_PATTERN, 
			                          variation, 
						              score, 
						              depth, 
						              isPositiveInteger(nodes) ? (Long.parseLong(nodes) / 1000) : UNKNOWN,
						              isPositiveInteger(time) ? formatMiliSeconds(Long.parseLong(time)): UNKNOWN);
	}
	
	public static int toInteger (Object object) {
		Assert.notNull(object, "Null object");
		Assert.isTrue(object.toString().matches("[-+]?\\d+"), "Not an integer");
		return Integer.parseInt(object.toString());
	}
	
	public static boolean isPositiveInteger (String str) {
		return str != null && str.matches("\\d+");
	}
	
	 /**
     * Creates a string from the arguments separated by spaces.
     * @param strings The string to be concatenated. 
     * @return The concatenated arguments (if any) with spaces in between, or null when no arguments.
     */
    public static String toSentence  (String ... strings) {
    	StringBuffer buffer = new StringBuffer();
    	if (strings != null) {
    		for (String s:strings) {
    			buffer.append(" " + s);
    		}
    		return buffer.toString().trim();
    	}
    	return null;
    }
	
    public static JLabel createWrappedLabel (int rowLength, String ... lines) {
    	return createWrappedLabel(null, rowLength, lines);
    }
    
    public static JLabel createWrappedLabel (Font font, int rowLength, String ... lines) {
    	JLabel label = new JLabel();
    	StringBuffer buffer = new StringBuffer();
    	for (String text:lines) {
	    	buffer.append(WordUtils.wrap(text, rowLength, "<br>", false));
	    	buffer.append("<br>");
        }
    	if (font != null) {
    	    label.setFont(font);
    	}
    	label.setText(HTMLFormat.toHTMLDocument(buffer.toString()));
    	return label;
    }
    
    public static String formatOpeningMessage (Opening opening) {
    	return opening.getCode() + ":" + opening.getName() 
    	   + (StringUtils.hasLength(opening.getVariation()) ? ":" + opening.getVariation() : "");
    }
    
	public static String stripRegex (String text, Pattern pattern, String replacement) {
		int lenght;
		String result = text;
		do {
			lenght = result.length();
			result = pattern.matcher(result).replaceAll(replacement);
		} while (lenght > result.length()) ;
		return result;
	}
	
	public static void deleteSequence (final StringBuffer text, int start, int end) {
		String content = text.toString();
		text.setLength(0);
		text.append(content.substring(0, start));
		if (end + 1 < content.length()) {
		    text.append( content.substring(end + 1, content.length()));
		}
	}
}
