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
package org.chess.quasimodo.domain.validation;

import org.chess.quasimodo.domain.TimeControlModel;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



public class TimeControlValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TimeControlModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TimeControlModel timeControl = (TimeControlModel)target;
		if (!timeControl.hasPositiveValues()) {
			errors.reject("All values must be at least zero.");
		} 
	}

}