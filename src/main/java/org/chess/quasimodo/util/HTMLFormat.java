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

import java.text.MessageFormat;


public class HTMLFormat {
	public static final String PARAGRAPH_PATTERN   = "<span style=\"vertical-align:top;font-family:{1};font-size:{2}pt;font-weight:bold\">{0}</span>";
	public static final String HTML_PATTERN        = "<html>{0}</html>";
	public static final String FONT_PATTERN        = "<font face=\"{1}\" color=\"{2}\">{0}</font>";
	public static final String HYPERLINK_PATTERN   = "<a href=\"gopher:{0}\" style=\"text-decoration:none;font-size:{2}pt;font-weight:{3}\">{1}</a>";
    
    public static String toHTMLDocument (String content) {
    	return MessageFormat.format(HTML_PATTERN, content != null ? content : "") ;
    }
   
    public static String toHTMLFontElement(String content, String fontName, String color) {
    	return MessageFormat.format(FONT_PATTERN, content != null ? content : "", fontName, color) ;
    }
    
    /**
     * Builds an HTML hyperlink for reprezenting moves on notation panel.
     * @param counter Move counter.
     * @param content The text displayed by the hyperlink.
     * @param fontSize font-size style.
     * @param fontWeight font-weight style (between 100-900; 400=normal,700=bold).
     * @return The HTML formatted hyperlink element.
     */
    public static String toHTMLHyperlinkElement(int counter, String content, int fontSize, int fontWeight) {
    	return MessageFormat.format(HYPERLINK_PATTERN, counter, content, fontSize, fontWeight) ;
    }
    
   
}
