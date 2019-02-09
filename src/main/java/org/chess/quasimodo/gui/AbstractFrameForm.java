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
package org.chess.quasimodo.gui;

import java.util.HashMap;

import javax.swing.JFrame;

import org.chess.quasimodo.domain.logic.FormView;
import org.springframework.validation.AbstractErrors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Un abstract {@link JFrame}, following {@link FormView} structure.
 * @author Eugen Covaci, created on Jun 17, 2011.
 * @param <M>
 */
@SuppressWarnings("serial")
public abstract class AbstractFrameForm<M> extends JFrame implements FormView<M> {
    private M model; 
	
	@Override
	public M getModel() {
		return model;
	}

	@Override
	public void updateModel() {}

	@Override
	public void setModel(M model) {
		this.model = model;
	}

	@Override
	public void commit() {}

	@Override
	public void showView() {
		setVisible(true);
	}

	@Override
	public AbstractErrors validateModel() {
		MapBindingResult result = new MapBindingResult(new HashMap<String, Object>(), "model");
		ValidationUtils.invokeValidator(getValidator(), getModel(), result);
		return result;
	}

	/**
	 * Getter for the validator used to validate the model.
	 * @return The validator used to validate the model.
	 */
	protected abstract Validator getValidator();
}
