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

import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.util.Utils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class EditPlayerValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return HumanPlayerModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		HumanPlayerModel model = (HumanPlayerModel)target;
		if (!StringUtils.hasLength(model.getFirstName())) {
			errors.reject("First name cannot be empty");
		}
		if (StringUtils.hasLength(model.getElo()) 
				&& !Utils.isPositiveInteger(model.getElo())) {
			errors.reject("ELO must be positive integer");
		}
	}
}

