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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.domain.validation.EditPlayerValidator;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.AbstractErrors;
import org.springframework.validation.Validator;


@SuppressWarnings("serial")
@Component("newPlayerDialog")
public class EditHumanPlayerDialog extends AbstractDialogForm<HumanPlayerModel> {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JTextField fNameTextField;
	private JTextField lNameTextField;
	private JTextField countryTextField;
	private JLabel lblElo;
	private JTextField eloTextField;

    @Autowired
    private MessageHandler messageHandler;
	
	@Autowired
    private EventPublisherAdapter publisherAdapter; 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditHumanPlayerDialog dialog = new EditHumanPlayerDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditHumanPlayerDialog() {
		Font tahoma = new Font("Tahoma", Font.PLAIN, 12);//FIXME - design
		UIManager.put("Label.font", tahoma);
		UIManager.put("TextField.font",tahoma);
		UIManager.put("ComboBox.font", tahoma);
	    UIManager.put("Button.font", tahoma);
		
		setModal(true);
		setTitle("Human Player");
		setBounds(100, 100, 349, 236);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblFirstName());
		contentPanel.add(getLblLastName());
		{
			JLabel lblTown = new JLabel("Country:");
			lblTown.setBounds(10, 86, 86, 20);
			contentPanel.add(lblTown);
		}
		contentPanel.add(getFNameTextField());
		contentPanel.add(getLNameTextField());
		contentPanel.add(getTownTextField());
		contentPanel.add(getLblElo());
		contentPanel.add(getEloTextField());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setPreferredSize(new Dimension(10, 50));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				okButton.setMinimumSize(new Dimension(67, 23));
				okButton.setMaximumSize(new Dimension(67, 23));
				okButton.setPreferredSize(new Dimension(67, 23));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				cancelButton.setActionCommand("Cancel");
				cancelButton.setMinimumSize(new Dimension(67, 23));
				cancelButton.setMaximumSize(new Dimension(67, 23));
				cancelButton.setPreferredSize(new Dimension(67, 23));
				cancelButton.setMargin(new Insets(0, 0, 0, 0));
				buttonPane.add(cancelButton);
			}
		}
		setLocationRelativeTo(null);
	}
	private JLabel getLblFirstName() {
		if (lblFirstName == null) {
			lblFirstName = new JLabel("First Name:");
			lblFirstName.setBounds(10, 24, 86, 20);
		}
		return lblFirstName;
	}
	private JLabel getLblLastName() {
		if (lblLastName == null) {
			lblLastName = new JLabel("Last Name:");
			lblLastName.setBounds(10, 55, 86, 20);
		}
		return lblLastName;
	}
	private JTextField getFNameTextField() {
		if (fNameTextField == null) {
			fNameTextField = new JTextField();
			fNameTextField.setBounds(106, 23, 209, 20);
			fNameTextField.setColumns(10);
		}
		return fNameTextField;
	}
	private JTextField getLNameTextField() {
		if (lNameTextField == null) {
			lNameTextField = new JTextField();
			lNameTextField.setBounds(106, 55, 209, 20);
			lNameTextField.setColumns(10);
		}
		return lNameTextField;
	}
	private JTextField getTownTextField() {
		if (countryTextField == null) {
			countryTextField = new JTextField();
			countryTextField.setBounds(106, 86, 209, 20);
			countryTextField.setColumns(10);
		}
		return countryTextField;
	}
	
	private JLabel getLblElo() {
		if (lblElo == null) {
			lblElo = new JLabel("ELO:");
			lblElo.setBounds(10, 117, 58, 20);
		}
		return lblElo;
	}
	
	private JTextField getEloTextField() {
		if (eloTextField == null) {
			eloTextField = new JTextField();
			eloTextField.setBounds(105, 117, 71, 20);
			eloTextField.setColumns(10);
		}
		return eloTextField;
	}
	
	protected void okButtonActionPerformed(ActionEvent e) {
		updateModel();
		AbstractErrors result = validateModel();
		if (result.hasErrors()) {
			messageHandler.showErrorMessages(this, result);
		} else {
			publisherAdapter.publishCommandEvent(e.getSource(), this, CommandEvent.Command.NEW_PLAYER);
			dispose();
			
		}
	}
	
	protected void cancelButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	@Override
	public void updateModel() {
	    model.setFirstName(fNameTextField.getText());
		model.setLastName(lNameTextField.getText());
		model.setCountry(countryTextField.getText());
		model.setElo(eloTextField.getText());
	}

	@Override
	public void commit() {
		fNameTextField.setText(model.getFirstName());
		lNameTextField.setText(model.getLastName());
		countryTextField.setText(model.getCountry());
		eloTextField.setText(model.getElo());
	}

	@Override
	protected Validator getValidator() {
		return new EditPlayerValidator();
	}
}
