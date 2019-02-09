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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.AbstractErrors;


@SuppressWarnings("serial")
@Component ("bookView")
public class BookPanel extends JPanel {
    @Autowired
    private MessageHandler messageHandler;
	
	@Autowired
	private EventPublisherAdapter eventPublisher;
	
	private JButton btnOpenBookFile;
	
	@Autowired
	private Config config;
	
	@PostConstruct
	public void initialize () {
		if (config.hasBookfile()) {
			setLayout(new BorderLayout(0, 0));
			add(getBtnOpenBookFile(), BorderLayout.CENTER);
		} else {
			setLayout(new FlowLayout());
			add(getBtnOpenBookFile(), BorderLayout.CENTER);
		}
		
	}
	
	private JButton getBtnOpenBookFile() {
		if (btnOpenBookFile == null) {
			btnOpenBookFile = new JButton("Open openingBook file");
			btnOpenBookFile.setMargin(new Insets(0, 0, 0, 0));
			btnOpenBookFile.setPreferredSize(new Dimension(90, 23));
			btnOpenBookFile.setSize(new Dimension(90, 23));
			btnOpenBookFile.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					openBookFileAction ();
				}
			});
		}
		return btnOpenBookFile;
	}
	
	private void openBookFileAction () {
		SelectBookFile fileSelector = new SelectBookFile();
		fileSelector.setTitle("Select Book File");
		fileSelector.showView();
	}
	
	private class SelectBookFile extends SelectFileDialog {

		@Override
		protected void acceptActionPerformed(ActionEvent e) {
			super.acceptActionPerformed(e);
			AbstractErrors result = validateModel();
			if (result.hasErrors()) {
				messageHandler.showErrorMessages(this, result.getAllErrors().get(0).getCode());
			} else {
				eventPublisher.publishCommandEvent(e.getSource(), this, CommandEvent.Command.NEW_BOOK);
				dispose();
			}
		}
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame dialog = new JFrame();
					dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
					dialog.setSize(300, 300);
					dialog.getContentPane().add(new BookPanel());
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
