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

import java.util.HashSet;
import java.util.Set;

import org.chess.quasimodo.engine.model.BestMove;
import org.chess.quasimodo.engine.model.Info;


public class EngineOutputParser implements UCIKeywords {
    private static final Set<String> uciInfoKeywords = new HashSet<String>(); 
    private static final Set<String> uciInfoKeywordsAll = new HashSet<String>();
	
    static {
    	uciInfoKeywords.add(DEPTH);
    	uciInfoKeywords.add(TIME);
    	uciInfoKeywords.add(NODES);
    	uciInfoKeywords.add(PV);
    	uciInfoKeywords.add(MULTIPV);
    	uciInfoKeywords.add(CP);
    	uciInfoKeywords.add(MATE);
    	uciInfoKeywords.add(HASHFULL);
    	uciInfoKeywords.add(CURRMOVE);
    	uciInfoKeywords.add(CURRMOVENUMBER);
    	uciInfoKeywords.add(NPS);
    	uciInfoKeywords.add(TBHITS);
    	uciInfoKeywords.add(STRING);
    	
    	uciInfoKeywordsAll.addAll(uciInfoKeywords);
    	
    	uciInfoKeywordsAll.add(CURRLINE);
    	uciInfoKeywordsAll.add(REFUTATION);
    	uciInfoKeywordsAll.add(SBHITS);
    }
    
    public Info parseInfoLine (String line) throws Exception {
    	Info info = null;
    	if (line.startsWith(UCIKeywords.INFO)) {
    		info = new Info();
    		String[] splited = line.split("\\s");
    		String item;
    		for (int i = 1;i < splited.length;i++) {
    			if (uciInfoKeywords.contains(splited[i])) {
    				if (PV.equals(splited[i])) {
    					item = "";
    					while (++i < splited.length) {
    				        if (uciInfoKeywordsAll.contains(splited[i])) {
    				        	i--;
    				        	break;
    				        }
    				        item += " " + splited[i];
    					}
    					
    					Info.class.getField(PV).set(info, item.trim());
    				} else if (STRING.equals(splited[i])) {
    					break;
    				} else {
    					Info.class.getField(splited[i]).set(info, splited[i + 1]);
    				}
    			} 
    		}
    	}
    	return info;
    }
    
    public BestMove parseBestMoveLine (String line) throws Exception {
    	BestMove bestMove = null;
    	if (line.startsWith(UCIKeywords.BESTMOVE)) {
    		bestMove = new BestMove();
    		String[] splited = line.split("\\s");
    		bestMove.move = splited[1];
    		if (splited.length == 4) {
    			bestMove.ponder = splited[3];	
    		}
    	}
    	return bestMove;
    }
    
}
