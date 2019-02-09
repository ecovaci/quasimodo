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

import org.chess.quasimodo.domain.SetUpGameModel;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class SetUpGameValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return SetUpGameModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SetUpGameModel model = (SetUpGameModel)target;
		if (model.isFriendly()) {//nothing to validate, just return
			return;
		}
		TimeControlValidator  timeControlValidator = new TimeControlValidator();
		if (model.isThreeTimeControls()) {
		    ValidationUtils.invokeValidator(timeControlValidator, model.getFirstTC(), errors);
		    ValidationUtils.invokeValidator(timeControlValidator, model.getSecondTC(), errors);
		}
		ValidationUtils.invokeValidator(timeControlValidator, model.getThirdTC(), errors);
		if (errors.hasErrors()) {
			return;
		}
		if (model.isThreeTimeControls()) {
			if (!model.getFirstTC().isValidTime()) {
				errors.reject("At least one time value of the first time control must be greater then zero!");
			} else if (!model.getFirstTC().isValidMoves()) {
				errors.reject("The moves counter of the first time control must be greater then zero!");
			} else if (model.getSecondTC().isValidTime()) {
				if (!model.getSecondTC().isValidMoves()) {
					errors.reject("The moves counter of the second time control must be greater then zero\n when it is assigned time for moves!");
				}
			} 
		} else {
			if (!model.getThirdTC().isValidTime()) {
				errors.reject("At least one time value of the time control must be greater then zero!");
			} 
		}
	}
}
