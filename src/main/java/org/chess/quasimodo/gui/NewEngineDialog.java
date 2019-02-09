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

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.AbstractErrors;


@SuppressWarnings("serial")
@Component ("newEngineView")
@Scope ("prototype")
public class NewEngineDialog extends SelectFileDialog {
    @Autowired
    private MessageHandler messageHandler;
	
	@Autowired
	private EventPublisherAdapter eventPublisher;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewEngineDialog dialog = new NewEngineDialog();
			dialog.setTitle("Select Engine");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void acceptActionPerformed(ActionEvent e) {
		super.acceptActionPerformed(e);
		AbstractErrors result = validateModel();
		if (result.hasErrors()) {
			messageHandler.showErrorMessages(this, result.getAllErrors().get(0).getCode());
		} else {
			eventPublisher.publishCommandEvent(e.getSource(), this, CommandEvent.Command.NEW_ENGINE);
			dispose();
		}
	}
}
