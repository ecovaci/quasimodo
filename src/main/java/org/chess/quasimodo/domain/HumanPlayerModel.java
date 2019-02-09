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

import org.springframework.util.StringUtils;

public class HumanPlayerModel extends PlayerModel {
	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = -1675439063242599139L;
	
	private String    firstName;
	private String    lastName;
	private String    country;
    private String    elo;
    
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getElo() {
		return elo;
	}
	
	public void setElo(String elo) {
		this.elo = elo;
	}

	@Override
	public String toString() {
		return this.firstName + (StringUtils.hasLength(this.lastName) ? ", " + this.lastName : "");
	}

	@Override
	public boolean isEngine() {
		return false;
	}

}
