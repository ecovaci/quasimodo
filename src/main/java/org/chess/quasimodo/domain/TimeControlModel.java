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
package org.chess.quasimodo.domain;
public class TimeControlModel {
	private int hours;
	private int min;
	private int sec;
	private int moves;
	private int gainPerMove;
	
	public int getHours() {
		return hours;
	}
	
	public void setHours(int hours) {
		this.hours = hours;
	}
	
	public int getMin() {
		return min;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public int getSec() {
		return sec;
	}
	
	public void setSec(int sec) {
		this.sec = sec;
	}
	
	public int getMoveNumber() {
		return moves;
	}
	public void setMoves(int moves) {
		this.moves = moves;
	}
	
	public int getGainPerMove() {
		return gainPerMove;
	}
	
	public void setGainPerMove(int gainPerMove) {
		this.gainPerMove = gainPerMove;
	}

	public long getTotalTime () {
		return sec + min * 60 + hours * 3600;
	}
	
	public long getIncrementedTotalTime () {
		return getTotalTime() + this.gainPerMove;
	}
	
	public boolean hasPositiveValues () {
		return hours >= 0 && min >= 0 && sec >= 0 && moves >= 0 && gainPerMove >=0;
	}
	
	public boolean isValidTime () {
		return hours > 0 || min > 0 || sec > 0;
	}
	
	public boolean isValidMoves () {
		return moves > 0;
	}
	
	public String asText () {
		StringBuffer text = new StringBuffer();
		text.append((hours * 60 + min)  + "'");
		if (sec != 0 || gainPerMove != 0) {
			text.append("+");
		    text.append(sec + "''");
		}
		if (gainPerMove != 0) {
			text.append("+");
			text.append(gainPerMove + "''");
		}
		return text.toString();
	}
	
	@Override
	public String toString() {
		return "TimeControlModel [gainPerMove=" + gainPerMove + ", hours="
				+ hours + ", min=" + min + ", moves=" + moves + ", sec="
				+ sec + "]";
	}
}
