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
package org.chess.quasimodo.book;


public class BookEntry  {
	
    private int from_row;
    private int from_file;
    private int to_row;
    private int to_file;
    private int promotedPiece;
    
    private int weight;

	public int getFrom_row() {
		return from_row;
	}

	public void setFrom_row(int fromRow) {
		from_row = fromRow;
	}

	public int getFrom_file() {
		return from_file;
	}

	public void setFrom_file(int fromFile) {
		from_file = fromFile;
	}

	public int getTo_row() {
		return to_row;
	}

	public void setTo_row(int toRow) {
		to_row = toRow;
	}

	public int getTo_file() {
		return to_file;
	}

	public void setTo_file(int toFile) {
		to_file = toFile;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getPromotedPiece() {
		return promotedPiece;
	}

	public void setPromotedPiece(int promotedPiece) {
		this.promotedPiece = promotedPiece;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + from_file;
		result = prime * result + from_row;
		result = prime * result + promotedPiece;
		result = prime * result + to_file;
		result = prime * result + to_row;
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookEntry other = (BookEntry) obj;
		if (from_file != other.from_file)
			return false;
		if (from_row != other.from_row)
			return false;
		if (promotedPiece != other.promotedPiece)
			return false;
		if (to_file != other.to_file)
			return false;
		if (to_row != other.to_row)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BookEntry [from_file=" + from_file + ", from_row=" + from_row
				+ ", promotedPiece=" + promotedPiece + ", to_file=" + to_file
				+ ", to_row=" + to_row + ", weight=" + weight + "]";
	}

/*	@Override
	public int compareTo(BookEntry entry) {
		if (this.weight < entry.weight) {
			return -1;
		} else if (this.weight > entry.weight) {
			return 1;
		}
		return 0;
	}*/

}
