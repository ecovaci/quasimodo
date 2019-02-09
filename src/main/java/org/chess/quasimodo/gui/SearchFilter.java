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
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.springframework.stereotype.Component;

@Component ("searchFilter")
public class SearchFilter extends DisposableDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7822140458356090021L;
	
	private JTextField wNameTextField;
	private JTextField wELOTextField;
	private JTextField bNameTextField;
	private JTextField bELOTextField;
	private JTextField eventTextField;
	private JTextField dateTextField;
	private JTextField openingTextField;
	private JTextField resultTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchFilter dialog = new SearchFilter();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public SearchFilter() {
		setTitle("Search Filter");
		setBounds(100, 100, 632, 411);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel filterPanel = new JPanel();
		scrollPane.setViewportView(filterPanel);
		filterPanel.setLayout(null);
		
		JPanel whitePanel = new JPanel();
		whitePanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "White player", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		whitePanel.setBounds(22, 31, 278, 111);
		filterPanel.add(whitePanel);
		whitePanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 30, 70, 14);
		whitePanel.add(lblName);
		
		wNameTextField = new JTextField();
		wNameTextField.setBounds(90, 27, 160, 20);
		whitePanel.add(wNameTextField);
		wNameTextField.setColumns(10);
		
		JLabel lblElo = new JLabel("ELO:");
		lblElo.setBounds(10, 69, 70, 14);
		whitePanel.add(lblElo);
		
		wELOTextField = new JTextField();
		wELOTextField.setBounds(90, 66, 160, 20);
		whitePanel.add(wELOTextField);
		wELOTextField.setColumns(10);
		
		JPanel blackPanel = new JPanel();
		blackPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Black player", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		blackPanel.setBounds(310, 31, 278, 111);
		filterPanel.add(blackPanel);
		blackPanel.setLayout(null);
		
		JLabel lblName_1 = new JLabel("Name:");
		lblName_1.setBounds(10, 28, 67, 14);
		blackPanel.add(lblName_1);
		
		bNameTextField = new JTextField();
		bNameTextField.setBounds(87, 25, 166, 20);
		blackPanel.add(bNameTextField);
		bNameTextField.setColumns(10);
		
		JLabel lblElo_1 = new JLabel("ELO:");
		lblElo_1.setBounds(10, 69, 67, 14);
		blackPanel.add(lblElo_1);
		
		bELOTextField = new JTextField();
		bELOTextField.setBounds(87, 66, 166, 20);
		blackPanel.add(bELOTextField);
		bELOTextField.setColumns(10);
		
		JLabel lblEvent = new JLabel("Event:");
		lblEvent.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEvent.setBounds(155, 173, 95, 14);
		filterPanel.add(lblEvent);
		
		eventTextField = new JTextField();
		eventTextField.setBounds(276, 170, 198, 20);
		filterPanel.add(eventTextField);
		eventTextField.setColumns(10);
		
		JLabel lblDate = new JLabel("Date:");
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(201, 204, 46, 14);
		filterPanel.add(lblDate);
		
		dateTextField = new JTextField();
		dateTextField.setBounds(276, 201, 108, 20);
		filterPanel.add(dateTextField);
		dateTextField.setColumns(10);
		
		JLabel lblOpening = new JLabel("Opening:");
		lblOpening.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOpening.setBounds(179, 235, 68, 14);
		filterPanel.add(lblOpening);
		
		openingTextField = new JTextField();
		openingTextField.setBounds(276, 232, 46, 20);
		filterPanel.add(openingTextField);
		openingTextField.setColumns(10);
		
		JLabel lblResult = new JLabel("Result:");
		lblResult.setHorizontalAlignment(SwingConstants.RIGHT);
		lblResult.setBounds(192, 266, 58, 14);
		filterPanel.add(lblResult);
		
		resultTextField = new JTextField();
		resultTextField.setBounds(276, 263, 46, 20);
		filterPanel.add(resultTextField);
		resultTextField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(194, 327, 115, 23);
		filterPanel.add(btnSearch);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(319, 327, 100, 23);
		filterPanel.add(btnCancel);
		
		JCheckBox chckbxIgnoreColor = new JCheckBox("Ignore color");
		chckbxIgnoreColor.setBounds(22, 164, 97, 23);
		filterPanel.add(chckbxIgnoreColor);
		
		JLabel lblddmmyyyy = new JLabel("(dd/mm/yyyy)");
		lblddmmyyyy.setBounds(394, 204, 75, 14);
		filterPanel.add(lblddmmyyyy);
	}
}
