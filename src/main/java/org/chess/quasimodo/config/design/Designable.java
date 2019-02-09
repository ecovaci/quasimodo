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
package org.chess.quasimodo.config.design;


/**
 * It marks a class as eligible for design configuration (i.e. set fonts, colors etc.).
 * @author Eugen Covaci
 * @see {@link org.chess.quasimodo.annotation.Design}
 */
public interface Designable {
	
	/**
	 * This method should initialize all the {@link org.chess.quasimodo.annotation.Design}
	 * annotated fields with the corresponding values from design configuration file.
	 */
	void injectDesign ();
	
	void refreshDesign ();
}
