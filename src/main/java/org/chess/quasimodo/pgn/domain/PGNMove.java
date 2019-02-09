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


public class PGNMove implements PGNEntity<String> {
	public static final Pattern PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9\\=\\+#]+");
	
	public static final Pattern START = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\=\\+#]+");
	
	public static final Pattern MOVE_NUMBER = Pattern.compile("^\\d+(?=\\.)");
	
	private String move;
    
	public PGNMove(String pgnMove) {
		this.move = pgnMove;
	}

	@Override
	public String getContent() {
		return move;
	}

	@Override
	public String toString() {
		return "PGNMove [move=" + move + "]";
	}

	@Override
	public String toPGN() {
		return move;
	}
    public static void main(String[] args) {
		Matcher m = MOVE_NUMBER.matcher("123..xxx");
		if (m.find()) {
			System.out.println(m.group());
		}
	}
}
