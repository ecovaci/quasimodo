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

import org.chess.quasimodo.engine.model.IdOptions;


public class EngineModel extends PlayerModel {
	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = 4766227720457863894L;
	
    private String      pathname;
    private IdOptions   idOptions;

    private String      bookFilePath; 
    private Integer     bookMoveLimit;
    
	public String getPathname() {
		return pathname;
	}
	
	public void setPathname(String pathname) {
		this.pathname = pathname;
	}
	
	public IdOptions getIdOptions() {
		return idOptions;
	}
	
	public void setIdOptions(IdOptions idOptions) {
		this.idOptions = idOptions;
	}

	public Integer getBookMoveLimit() {
		return bookMoveLimit;
	}

	public void setBookMoveLimit(Integer bookMoveLimit) {
		this.bookMoveLimit = bookMoveLimit;
	}

	public String getBookFilePath() {
		return bookFilePath;
	}

	public void setBookFilePath(String bookFilePath) {
		this.bookFilePath = bookFilePath;
	}

	public void removeBook () {
		this.bookFilePath = null;
	}
	
	@Override
	public String toString() {
		return idOptions != null ? idOptions.getName() : null;
	}

	@Override
	public boolean isEngine() {
		return true;
	}
}
