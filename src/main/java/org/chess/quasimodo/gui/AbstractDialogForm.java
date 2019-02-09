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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.chess.quasimodo.domain.logic.FormView;
import org.springframework.validation.AbstractErrors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * An abstract, closable on ESC key {@link JDialog}, following the {@link FormView} structure.
 * @author Eugen Covaci
 * @param <M> The model.
 */
@SuppressWarnings("serial")
public abstract class AbstractDialogForm<M> extends JDialog  implements FormView<M> {
    protected M model; 
	
    public AbstractDialogForm() {
    	setRootPane(new DisposeRootPane());
	}

    public AbstractDialogForm(Dialog owner, boolean modal) {
		super(owner, modal);
		setRootPane(new DisposeRootPane());
	}

    public AbstractDialogForm(Frame owner, boolean modal) {
		super(owner, modal);
		setRootPane(new DisposeRootPane());
	}

	@Override
	public M getModel() {
		return model;
	}

	@Override
	public void setModel(M model) {
		this.model = model;
	}
	
	@Override
	public void updateModel() {}

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

	protected abstract Validator getValidator();
	
	//Class used to bind ESC key press event
	//to dialog disposal.
	private class DisposeRootPane extends JRootPane {

		public DisposeRootPane() {
		    this.registerKeyboardAction(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  onEscapeKeyEvent();
			  }
		    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
		
		public void onEscapeKeyEvent() {
			dispose();
		}
	}
}
