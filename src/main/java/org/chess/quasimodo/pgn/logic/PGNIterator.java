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
package org.chess.quasimodo.pgn.logic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

import org.chess.quasimodo.domain.logic.Definitions;
import org.chess.quasimodo.errors.InvalidPGNException;
import org.chess.quasimodo.pgn.domain.PGNGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PGNIterator implements Iterator<PGNGame> {
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	private final Logger logger = LoggerFactory.getLogger(PGNIterator.class);
	
	private RandomAccessFile reader;
	
	private long lineCounter;
	
	protected PGNIterator(RandomAccessFile reader) {
		this.reader = reader;
	}

	@Override
	public boolean hasNext() {
		boolean result = false;;
		try {
			long position = reader.getFilePointer();
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!"".equals(line)) {
					if (line.startsWith("[")) {
						result = true;
						break;
					}
				}
			}
			reader.seek(position);
			return result;
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read the stream", e);
		}
	}

	@Override
	public PGNGame next() {
		try {
			StringBuffer moveBuffer = new StringBuffer();
			PGNGame game = new PGNGame();
			logger.debug("Start parsing new PGN game at: " + lineCounter + " line");
			boolean foundMoves = false;
			String line;
			while ((line = reader.readLine()) != null) {
				logger.debug("PGN line: " + line);
				lineCounter++;
				line = line.trim();
				
				if (line.startsWith("[")) {
					if (!line.endsWith("]")) {
						throw new InvalidPGNException ("Missing right square bracket on line [" + lineCounter + "]");
					}
					game.processTag(line);
				} else {
					if ("".equals(line)) {
						if (foundMoves) {
							break;
						} else {
							continue;
						}
					} else if (!foundMoves){
						foundMoves = true;
					}
					if (line.indexOf('%') > 0) {
						//ignore the rest of this line, it is just text
						line = line.substring(0, line.indexOf('%'));
					}
					moveBuffer.append(line);
					if (!line.endsWith(".")) {
						moveBuffer.append(" ");
					}
					if (line.endsWith(Definitions.WHITE_WINS) || line.endsWith(Definitions.BLACK_WINS) 
							|| line.endsWith(Definitions.DRAW) || line.endsWith(Definitions.UNDECIDED)) {
						break;
					} 
				}
			}
			game.loadTextContent(moveBuffer.toString());
			game.parse ();
			return game;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void remove() {
		throw new IllegalStateException("Cannot remove a PGN game from file");
	}

}
