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

import org.chess.quasimodo.book.BookEntry;
import org.chess.quasimodo.engine.model.Info;
import org.chess.quasimodo.errors.InvalidFENException;
import org.chess.quasimodo.util.UCIUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;



@Scope ("prototype")
@Component ("engineBoard")
public class EngineBoard implements Definitions {
	static int THRESHOLD_SLIGHT    = 34;//TODO - config
	static int THRESHOLD_MODERATE  = 69;
	static int THRESHOLD_DECISIVE  = 150;
	
    private Position position;
    
    public EngineBoard() throws InvalidFENException {
    	setPosition(Definitions.INITIAL_FEN);
	}
    
    public void setPosition (String fen) throws InvalidFENException {
    	(position = new Position()).load(fen);
    }
    
    public String toSANMove (String uciMove) {
    	return position.toPseudoSAN(uciMove);
    }
    
    public String formatCurrentMove (String uciMove) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(position.getFullMoveCounter() + ".");
    	if (position.isBlackToMove()) {
    		buffer.append(".. ");
    	}
    	buffer.append(position.toPseudoSAN(uciMove));
    	return buffer.toString();
    }
    
	public String parseVariation(Info info) {
		StringBuffer moves = new StringBuffer();
		//working on a clone
		Position workingPos = this.position.clone();
		if (workingPos.isBlackToMove()) {
			moves.append(workingPos.getFullMoveCounter() + "... ");
		}
		for (String uciMove : info.pv.split("\\s")) {
			if (workingPos.isWhiteToMove()) {
				moves.append(workingPos.getFullMoveCounter() + ".");
			}
			moves.append(workingPos.makeUCIMove(uciMove));
			//leave space between moves
			moves.append(" ");
		}
		return moves.toString().trim();
	}
	
	public String parseExtraInfo(Info info) {
		StringBuffer extraInfo = new StringBuffer();
		extraInfo.append(parseScore(info, false));
		return extraInfo.toString();
	}
	
	public String parseScore (Info info, boolean fullDesc) {
		StringBuffer score = new StringBuffer();
		Integer mate = parseMate(info.mate);
		if (mate != null) {
			if (mate > 0) {
				score.append(Definitions.WHITE_DECISIVE_ADVANTAGE);
			} else {
				score.append(Definitions.BLACK_DECISIVE_ADVANTAGE);
			}
			score.append(" (" + Definitions.MATE + Math.abs(mate) + ") ");
		} else {
			Integer cp = parseCp (info.cp);
			if (cp != null) {
				if (fullDesc) {
					if (cp > THRESHOLD_DECISIVE) {
						score.append(Definitions.WHITE_DECISIVE_ADVANTAGE);
					} else if (cp > THRESHOLD_MODERATE) {
						score.append(Definitions.WHITE_MODERATE_ADVANTAGE);
					} else if (cp > THRESHOLD_SLIGHT) {
						score.append(Definitions.WHITE_SLIGHT_ADVANTAGE);
					} else if (cp < -THRESHOLD_DECISIVE) {
						score.append(Definitions.BLACK_DECISIVE_ADVANTAGE);
					} else if (cp < -THRESHOLD_MODERATE)  {
						score.append(Definitions.BLACK_MODERATE_ADVANTAGE);
					} else if (cp < -THRESHOLD_SLIGHT)  {
						score.append(Definitions.BLACK_SLIGHT_ADVANTAGE);
					} else {
						score.append(Definitions.EQUALITY);
					}
				}
				score.append(" (" + UCIUtils.formatScore(cp) + ") ");
			} 
		}
		return score.toString();
	}
	
	public Integer parseCp (String strCp) {
		Integer cp = null;
		if (StringUtils.hasLength(strCp)) {
		    cp = (position.isWhiteToMove() ? 1 : -1) * Integer.parseInt(strCp);
		} 
		return cp;
	}
	
	public Integer parseMate (String strMate) {
		Integer mate = null;
		if (StringUtils.hasLength(strMate)) {
		    mate = (position.isWhiteToMove() ? 1 : -1) * Integer.parseInt(strMate);
		} 
		return mate;
	}
	
	public long zobristHashKey () {
		Assert.notNull(position, "Position is not set");
		return position.zobristHashKey();
	}
	
	public String toUCINotation (BookEntry bookEntry) {
		Assert.notNull(position, "Position is not set");
		return position.toUCINotation(bookEntry);
	}
}
