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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.chess.quasimodo.util.Utils;
import org.springframework.util.Assert;

public class PGNComment implements PGNEntity<String> {
	
	public static final Pattern PATTERN = Pattern.compile("\\{.*?\\}");
	
	public static final Pattern START_WITH = Pattern.compile("^\\{.*?\\}");
	
    private String comment;

	public PGNComment(String comment) {
		Assert.notNull(comment, "Comment cannot be null");
		this.comment = comment;
	}
	
	@Override
	public String getContent() {
		return comment;
	}

	@Override
	public String toString() {
		return "PGNComment [comment=" + comment + "]";
	}

	@Override
	public String toPGN() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{").append(comment).append("}");
		return buffer.toString();
	}
	
	public static String stripAll (String text) {
	    return PATTERN.matcher(text).replaceAll(" ");
	}
	
	public static String parseNext(final StringBuffer text) {
		String comment = null;
		Matcher matcher = PATTERN.matcher(text);
		if (matcher.find()) {
			comment = text.substring(matcher.start() + 1, matcher.end() - 1);
			Utils.deleteSequence(text, matcher.start(), matcher.end());
		}
		return comment;
	}
}
