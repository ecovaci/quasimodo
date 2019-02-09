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
package org.chess.quasimodo.engine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdOptions implements Serializable {
	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = -1839350830357086641L;
	
	public static final String     NAME = "name";
	public static final String     AUTHOR = "author";
	
	private List<Option>           options = new ArrayList<Option>() ;
	private Map<String, String>    idMap = new HashMap<String, String> () ;
	
	public void addOption (Option option) {
		options.add(option);
	}
	
	public void addId (String name, String value) {
		idMap.put(name, value);
	}

	public List<Option> getOptions() {
		return options;
	}

	public Map<String, String> getIdMap() {
		return idMap;
	}
	
	public String getName () {
		return idMap.get(NAME);
	}
	
	public String getAuthor () {
		return idMap.get(AUTHOR);
	}

	@Override
	public String toString() {
		return "IdOptions [options=" + options + ", idMap=" + idMap + "]";
	}
}
