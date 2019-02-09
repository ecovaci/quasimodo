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

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.domain.logic.FormView;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.gui.model.ManageModelList;
import org.chess.quasimodo.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;


@Component ("manageEnginesView")
public class ManageEnginesDialog extends AbstractDialogForm<ManageModelList<EngineModel>>{
	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = -7743505542659460090L;
	
	private JScrollPane scrollPane;
	
	private JList engineList;
	
	private JButton btnAddEngine;
	
	private JButton btnDeleteEngine;
	
	private JButton btnProperties;
	
	private JButton btnCancel;
	
    @Autowired
	private ApplicationContextAdapter contextAdapter;
    
    @Autowired
    private EventPublisherAdapter publisherAdapter; 
	
    @Autowired
    private MessageHandler messageHandler;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Font tahoma = new Font("Tahoma", Font.PLAIN, 12);
					UIManager.put("Menu.font", tahoma);
					UIManager.put("MenuItem.font", tahoma);
					UIManager.put("Label.font", tahoma);
					UIManager.put("TextField.font",tahoma);
					UIManager.put("ComboBox.font", tahoma);
					UIManager.put("OptionPane.font", tahoma);
				    UIManager.put("Button.font", tahoma);
					
					
					ManageEnginesDialog dialog = new ManageEnginesDialog();
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
	public ManageEnginesDialog() {
		Font tahoma = new Font("Tahoma", Font.PLAIN, 12);
		UIManager.put("Menu.font", tahoma);
		UIManager.put("MenuItem.font", tahoma);
		UIManager.put("Label.font", tahoma);
		UIManager.put("TextField.font",tahoma);
		UIManager.put("ComboBox.font", tahoma);
		UIManager.put("OptionPane.font", tahoma);
	    UIManager.put("Button.font", tahoma);
		initialize();
	}
	private void initialize() {
		setModal(true);
		setResizable(false);
		setTitle("Manage Engines");
		setBounds(100, 100, 347, 371);
		getContentPane().setLayout(null);
		getContentPane().add(getScrollPane());
		getContentPane().add(getBtnAddEngine());
		getContentPane().add(getBtnDeleteEngine());
		getContentPane().add(getBtnProperties());
		getContentPane().add(getBtnCancel());
		setLocationRelativeTo(null);
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 11, 196, 267);
			scrollPane.setViewportView(getEngineList());
		}
		return scrollPane;
	}
	private JList getEngineList() {
		if (engineList == null) {
			engineList = new JList();
			engineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			engineList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					engineListvalueChanged(e);
				}
			});
			engineList.setModel(new CustomAbstractListModel());
		}
		return engineList;
	}
	private JButton getBtnAddEngine() {
		if (btnAddEngine == null) {
			btnAddEngine = new JButton("Add engine");
			btnAddEngine.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnAddEngineActionPerformed(e);
				}
			});
			btnAddEngine.setMargin(new Insets(0, 0, 0, 0));
			btnAddEngine.setBounds(216, 100, 113, 23);
		}
		return btnAddEngine;
	}
	private JButton getBtnDeleteEngine() {
		if (btnDeleteEngine == null) {
			btnDeleteEngine = new JButton("Delete engine");
			btnDeleteEngine.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnDeleteEngineActionPerformed(e);
				}
			});
			btnDeleteEngine.setMargin(new Insets(0, 0, 0, 0));
			btnDeleteEngine.setBounds(216, 134, 113, 23);
		}
		return btnDeleteEngine;
	}
	private JButton getBtnProperties() {
		if (btnProperties == null) {
			btnProperties = new JButton("Properties");
			btnProperties.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnPropertiesActionPerformed(e);
				}
			});
			btnProperties.setMargin(new Insets(0, 0, 0, 0));
			btnProperties.setBounds(216, 168, 113, 23);
		}
		return btnProperties;
	}
	
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Close");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnCancelActionPerformed(e);
				}
			});
			btnCancel.setBounds(139, 298, 67, 23);
		}
		return btnCancel;
	}
	
	protected void engineListvalueChanged(ListSelectionEvent e) {
		model.setSelectedModel((EngineModel)engineList.getSelectedValue());
	}
	
	protected void btnCancelActionPerformed(ActionEvent e) {
		dispose();
	}
	
	protected void btnPropertiesActionPerformed(ActionEvent e) {
		if (model.hasSelected()) {
			FormView<EngineModel> enginePropertiesForm = contextAdapter.getEnginePropertiesDialog();
			enginePropertiesForm.setModel(model.getSelectedModel());
			enginePropertiesForm.commit();
			enginePropertiesForm.showView();
		} else {
			messageHandler.showInfoMessage(this, "No engine selected!");
		}
	}
	
	protected void btnDeleteEngineActionPerformed(ActionEvent e) {
		if (model.hasSelected()) {
			if (messageHandler.showConfirm(this, 
			"Delete [" + model.getSelectedModel().getIdOptions().getName() + "] engine?") == JOptionPane.OK_OPTION){
			    publisherAdapter.publishCommandEvent(e.getSource(), this, CommandEvent.Command.DELETE_ENGINE);
			}
		} else {
			messageHandler.showInfoMessage(this, "No engine selected!");
		}
	}
	
	protected void btnAddEngineActionPerformed(ActionEvent e) {
		contextAdapter.getNewEngineView().showView();
	}
	
	@Override
	public void commit() {
		engineList.setModel(new CustomAbstractListModel());
	}
	
	@Override
	protected Validator getValidator() {
		return null;
	}
	
	private class CustomAbstractListModel extends AbstractListModel {
		private static final long serialVersionUID = -6096524403538306255L;
		
		public int getSize() {
			return model.size();
		}
		
		public Object getElementAt(int index) {
			return model.getEngineModelAt(index);
		}
	}
}
