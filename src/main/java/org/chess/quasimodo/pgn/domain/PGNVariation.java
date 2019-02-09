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
package org.chess.quasimodo.pgn.domain;

import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.chess.quasimodo.domain.logic.Definitions;
import org.chess.quasimodo.errors.InvalidPGNException;
import org.chess.quasimodo.errors.PGNParseException;
import org.chess.quasimodo.util.Utils;
import org.springframework.util.Assert;

public class PGNVariation implements PGNParsable, PGNEntity<PGNEntitySequence> {
	
	public static final Pattern PATTERN = Pattern.compile("\\([\\s|\\S&&[^\\(]&&[^\\)]]*\\)") ;
	
	public static final Pattern START_WITH = Pattern.compile("^\\([\\s|\\S&&[^\\(]&&[^\\)]]*\\)");
	
    private PGNEntitySequence content = new PGNEntitySequence();
    
    public PGNVariation(String textContent) {
    	Assert.notNull(textContent, "Variation's text cannot be null");
    	this.content.loadTextContent(textContent);
	}

	public PGNEntitySequence getContent() {
		return content;
	}
    
	public void parse () {
		content.fullParse();
	}

	@Override
	public String toString() {
		return "PGNVariation [content=" + content + "]";
	}

	@Override
	public String toPGN() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(").append(content.toPGN()).append(")");
		return buffer.toString();
	}
	
	public static String stripAll (String text) throws PGNParseException {
		return Utils.stripRegex(text, PATTERN, " ");
	}
	
	public static String parseNext (final StringBuffer text) {
		String variation = null;
		Pair<Integer, Integer> bounds = getNextVariationBounds(text.toString());
		if (bounds.getLeft() > -1) {
			variation = text.substring(bounds.getLeft() + 1,bounds.getRight());
			Utils.deleteSequence(text, bounds.getLeft(), bounds.getRight());
		}
		return variation;
	}
	
	public static Pair<Integer, Integer> getNextVariationBounds (String text) {
		int leftBracketIndex = text.indexOf('(');
		int rightBracketIndex = -1;
		if (leftBracketIndex > -1) {
			int innerRightCounter = 0;
			int innerLeftCounter = 0;
			for (int i = 0;i < text.substring(leftBracketIndex).length();i++) {
				if (text.charAt(leftBracketIndex + i) == ')') {
					innerRightCounter++;
					rightBracketIndex = i;
				} else if (text.charAt(leftBracketIndex + i) == '(') {
					innerLeftCounter++;
				}
				if (innerLeftCounter == innerRightCounter && innerLeftCounter > 0) {
					break;
				}
			}
			
			if (rightBracketIndex < 0) {
				throw new InvalidPGNException ("Missing right round bracket for left round bracket index <" + leftBracketIndex + "> in text <" + text + ">");
			} 
		}
		return new ImmutablePair<Integer, Integer>(leftBracketIndex, rightBracketIndex);
	}
	
	public static void main(String[] args) {
		System.out.println(Definitions.Evaluation.UNCLEAR_POSITION.getSymbol());
	}
}
