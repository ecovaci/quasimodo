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
package org.chess.quasimodo.errors;


@SuppressWarnings("serial")
public class FatalException extends Exception {
	
	/**
	 * 
	 */
	public FatalException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FatalException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public FatalException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FatalException(Throwable cause) {
		super(cause);
	}
}