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

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class DisposableDialog extends JDialog {

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = -2239127484974679000L;

	public DisposableDialog() {
		init();
	}
	
	public DisposableDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		init();
	}

	public DisposableDialog(Frame owner, boolean modal) {
		super(owner, modal);
		init();
	}
	
	private void init () {
		setRootPane(new DisposeRootPane());
	}
	
	public void close () {
		this.dispose();
	}
	
	private class DisposeRootPane extends JRootPane {

		private static final long serialVersionUID = 8080241642222796250L;

		public DisposeRootPane() {
		    this.registerKeyboardAction(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  onEscapeKeyEvent();
			  }
		    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
		
		public void onEscapeKeyEvent() {
			close();
		}
	}
}
