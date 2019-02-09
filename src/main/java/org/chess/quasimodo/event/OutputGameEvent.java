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
package org.chess.quasimodo.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;


public class OutputGameEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8683070162257350837L;
	
	public enum Type {
		MATE_WHITE_WINS, MATE_BLACK_WINS, 
		DRAW_BY_AGREEMENT, DRAW_BY_REPETITION, DRAW_BY_FIFTY_RULE, UNDECIDED;
		
		public boolean isGameOver () {
			return this == UNDECIDED || isMate() || isDraw(); 
		}
		
		public boolean isMate () {
			return this == MATE_BLACK_WINS || this == MATE_WHITE_WINS;
		}
		
		public boolean isDraw () {
			return this == DRAW_BY_AGREEMENT || this == DRAW_BY_FIFTY_RULE 
			|| this == DRAW_BY_REPETITION;
		}
	}
	
	private volatile Type eventType;
	
	public OutputGameEvent(Object source,Type eventType) {
		super(source);
		Assert.notNull(eventType, "null eventType");
		this.eventType = eventType;
	}

	public Type getEventType() {
		return eventType;
	}
	
}
