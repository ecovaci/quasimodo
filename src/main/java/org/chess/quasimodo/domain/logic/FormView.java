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

import org.springframework.validation.AbstractErrors;

/**
 * This class is an abstraction of a view (meaning any Swing/AWT component).
 * @author Eugen Covaci, created on Jan 21, 2011
 *
 * @param <M> The underlying model.
 */
public interface FormView<M> extends Form<M> {
	
	/**
     * The underlying model is synchronized with the view.
     */
    void updateModel ();
    
    /**
     * The form is synchronized with the underlying model.
     */
    void commit ();
    
    /**
     * Makes this view visible.
     */
    void showView ();

    /**
     * Validates the underlying model.
     * @return The errors (if any).
     */
	AbstractErrors validateModel();
	
	void dispose ();
}
