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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.gui.model.ManageModelList;
import org.chess.quasimodo.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;


@SuppressWarnings("serial")
@Component ("managePlayersView")
public class ManageHumanPlayersDialog extends AbstractDialogForm<ManageModelList<HumanPlayerModel>> {

	private JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JList playersList;
	private JButton btnAddPlayer;
	private JButton btnEditPlayer;
	private JButton btnDeletePlayer;
	
    @Autowired
    private MessageHandler messageHandler;
	
	@Autowired
    private EventPublisherAdapter publisherAdapter; 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ManageHumanPlayersDialog dialog = new ManageHumanPlayersDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ManageHumanPlayersDialog() {
		setModal(true);
		Font tahoma = new Font("Tahoma", Font.PLAIN, 12);
		UIManager.put("Button.font", tahoma);
		setTitle("Manage Human Players");
		setBounds(100, 100, 334, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getScrollPane());
		contentPanel.add(getBtnAddPlayer());
		contentPanel.add(getBtnEditPlayer());
		contentPanel.add(getBtnDeletePlayer());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setPreferredSize(new Dimension(10, 50));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.setPreferredSize(new Dimension(67, 23));
				cancelButton.setMargin(new Insets(0, 0, 0, 0));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setLocationRelativeTo(null);
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 10, 200, 200);
			scrollPane.setViewportView(getPlayersList());
		}
		return scrollPane;
	}
	private JList getPlayersList() {
		if (playersList == null) {
			playersList = new JList();
			playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			playersList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					playersListValueChanged(e);
				}
			});
			playersList.setModel(new CustomAbstractListModel());
		}
		return playersList;
	}
	private JButton getBtnAddPlayer() {
		if (btnAddPlayer == null) {
			btnAddPlayer = new JButton("Add player");
			btnAddPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnAddPlayerActionPerformed(e);
				}
			});
			btnAddPlayer.setMargin(new Insets(0, 0, 0, 0));
			btnAddPlayer.setBounds(220, 62, 91, 23);
		}
		return btnAddPlayer;
	}
	private JButton getBtnEditPlayer() {
		if (btnEditPlayer == null) {
			btnEditPlayer = new JButton("Edit player");
			btnEditPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnEditPlayerActionPerformed(e);
				}
			});
			btnEditPlayer.setMargin(new Insets(0, 0, 0, 0));
			btnEditPlayer.setBounds(220, 95, 91, 23);
		}
		return btnEditPlayer;
	}
	private JButton getBtnDeletePlayer() {
		if (btnDeletePlayer == null) {
			btnDeletePlayer = new JButton("Delete player");
			btnDeletePlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnDeletePlayerActionPerformed(e);
				}
			});
			btnDeletePlayer.setMargin(new Insets(0, 0, 0, 0));
			btnDeletePlayer.setBounds(220, 129, 91, 23);
		}
		return btnDeletePlayer;
	}
	
	protected void btnAddPlayerActionPerformed(ActionEvent e) {
		publisherAdapter.publishCommandEvent(e.getSource(), CommandEvent.Command.ADD_PLAYER);
	}
	
	protected void btnEditPlayerActionPerformed(ActionEvent e) {
		if (model.hasSelected()) {
			publisherAdapter.publishCommandEvent(e.getSource(), this, CommandEvent.Command.EDIT_PLAYER);
		} else {
			messageHandler.showInfoMessage(this, "No selected player!");
		}
	}
	
	protected void btnDeletePlayerActionPerformed(ActionEvent e) {
		if (model.hasSelected()) {
			if (model.size() == 1) {
				messageHandler.showErrorMessages(this, "At least one human player must exist!");
			} else if (messageHandler.showConfirm(this, 
					"Delete [" + model.getSelectedModel() + "] player?") == JOptionPane.OK_OPTION) {
			    publisherAdapter.publishCommandEvent(e.getSource(), this, CommandEvent.Command.DELETE_PLAYER);
			}
		} else {
			messageHandler.showInfoMessage(this, "No selected player!");
		}
	}
	
	protected void cancelButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	@Override
	public void commit() {
		playersList.setModel(new CustomAbstractListModel());
	}
	
	protected void playersListValueChanged(ListSelectionEvent e) {
		model.setSelectedModel((HumanPlayerModel)playersList.getSelectedValue());
	}

	@Override
	protected Validator getValidator() {
		return null;
	}
	
    private class CustomAbstractListModel extends AbstractListModel {
		
		public int getSize() {
			return model.size();
		}
		
		public Object getElementAt(int index) {
			return model.getEngineModelAt(index);
		}
	}
}
