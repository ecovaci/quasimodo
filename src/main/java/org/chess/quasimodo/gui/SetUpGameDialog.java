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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.domain.TimeControlModel;
import org.chess.quasimodo.domain.validation.SetUpGameValidator;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.message.MessageHandler;
import org.chess.quasimodo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.AbstractErrors;
import org.springframework.validation.Validator;


@Component ("setupGameView")
@Scope ("prototype")
public class SetUpGameDialog extends AbstractDialogForm<SetUpGameModel> {//FIXME - design
	/**
	 * 
	 */
	private static final long serialVersionUID = -6149006136870625629L;
	
	@Autowired
	EventPublisherAdapter eventPublisher;
	
	@Autowired
	QuasimodoContext context;
	
    @Autowired
    private MessageHandler messageHandler;
	
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblWhitePlayer;
	private JComboBox whitePlayerComboBox;
	private JButton btnOk;
	private JButton btnCancel;
	private JLabel lblBlackPlayer;
	private JComboBox blackPlayerComboBox;
	private JPanel panel_2;
	private JSeparator separator;
	private JLabel lblSelectTimeControls;
	private JComboBox typeComboBox;
	private JLabel lblDefaults;
	private JComboBox defaultsComboBox;
	private JPanel firstTimeControlPanel;
	private JLabel lblTime;
	private JSpinner hSpinner_1;
	private JLabel lblH;
	private JSpinner mSpinner_1;
	private JLabel lblMin;
	private JSpinner sSpinner_1;
	private JLabel lblSec;
	private JLabel lblMoves;
	private JSpinner movesSpinner_1;
	private JLabel lblGainPerMove;
	private JSpinner gainSpinner_1;
	private JPanel secondTimeControlPanel;
	private JLabel label;
	private JSpinner hSpinner_2;
	private JLabel label_1;
	private JSpinner mSpinner_2;
	private JLabel label_2;
	private JSpinner sSpinner_2;
	private JLabel label_3;
	private JLabel label_4;
	private JSpinner movesSpinner_2;
	private JLabel label_5;
	private JSpinner gainSpinner_2;
	private JPanel thirdTimeControlPanel;
	private JLabel label_6;
	private JSpinner hSpinner_3;
	private JLabel label_7;
	private JSpinner mSpinner_3;
	private JLabel label_8;
	private JSpinner sSpinner_3;
	private JLabel label_9;
	private JLabel label_11;
	private JSpinner gainSpinner_3;
	
	private JLabel labelFriendly;
	private JButton addToDefaultsButton;

	/**
	 * Create the dialog.
	 */
	public SetUpGameDialog() {
		initializeView();
	}
	
	private void initializeView() {
		setTitle("Set Up Game");
		setModal(true);
		setSize(627, 566);
		getContentPane().add(getPanel(), BorderLayout.NORTH);
		getContentPane().add(getPanel_1(), BorderLayout.SOUTH);
		getContentPane().add(getPanel_2(), BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}
	
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setPreferredSize(new Dimension(10, 50));
			panel.setLayout(null);
			panel.add(getLblWhitePlayer());
			panel.add(getWhitePlayerComboBox());
		}
		return panel;
	}
	
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setPreferredSize(new Dimension(10, 100));
			panel_1.setLayout(null);
			panel_1.add(getBtnOk());
			panel_1.add(getBtnCancel());
			panel_1.add(getLblBlackPlayer());
			panel_1.add(getBlackPlayerComboBox());
		}
		return panel_1;
	}
	
	private JLabel getLblWhitePlayer() {
		if (lblWhitePlayer == null) {
			lblWhitePlayer = new JLabel("White Player: ");
			lblWhitePlayer.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblWhitePlayer.setBounds(126, 11, 98, 28);
		}
		return lblWhitePlayer;
	}
	
	private JComboBox getWhitePlayerComboBox() {
		if (whitePlayerComboBox == null) {
			whitePlayerComboBox = new JComboBox();
			whitePlayerComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
			whitePlayerComboBox.setPreferredSize(new Dimension(29, 20));
			whitePlayerComboBox.setBounds(234, 14, 181, 22);
		}
		return whitePlayerComboBox;
	}
	
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("OK");
			btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btnOkActionPerformed(e);
				}
			});
			btnOk.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnOk.setBounds(220, 55, 91, 23);
		}
		return btnOk;
	}
	
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_btnCancelActionPerformed(e);
				}
			});
			btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnCancel.setBounds(321, 55, 91, 23);
		}
		return btnCancel;
	}
	
	private JLabel getLblBlackPlayer() {
		if (lblBlackPlayer == null) {
			lblBlackPlayer = new JLabel("Black Player: ");
			lblBlackPlayer.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblBlackPlayer.setBounds(126, 12, 104, 24);
		}
		return lblBlackPlayer;
	}
	
	private JComboBox getBlackPlayerComboBox() {
		if (blackPlayerComboBox == null) {
			blackPlayerComboBox = new JComboBox();
			blackPlayerComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
			blackPlayerComboBox.setPreferredSize(new Dimension(29, 20));
			blackPlayerComboBox.setBounds(235, 13, 177, 22);
		}
		return blackPlayerComboBox;
	}
	
	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			panel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel_2.setBorder(new TitledBorder(null, "Time Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_2.setLayout(null);
			panel_2.add(getSeparator());
			panel_2.add(getLblSelectTimeControls());
			panel_2.add(getTypeComboBox());
			panel_2.add(getLblDefaults());
			panel_2.add(getDefaultsComboBox());
			panel_2.add(getFirstTimeControlPanel());
			panel_2.add(getSecondTimeControlPanel());
			panel_2.add(getThirdTimeControlPanel());
			panel_2.add(getAddToDefaultsButton());
		}
		return panel_2;
	}
	
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			separator.setBounds(289, 24, 2, 349);
		}
		return separator;
	}
	
	private JLabel getLblSelectTimeControls() {
		if (lblSelectTimeControls == null) {
			lblSelectTimeControls = new JLabel("Select time control type:");
			lblSelectTimeControls.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblSelectTimeControls.setBounds(10, 35, 164, 24);
		}
		return lblSelectTimeControls;
	}
	
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			typeComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_typeComboBoxActionPerformed(e);
				}
			});
			typeComboBox.setModel(new DefaultComboBoxModel(new String[] {"One time control", "Three time controls", "Friendly"}));
			typeComboBox.setSelectedIndex(0);
			typeComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
			typeComboBox.setBounds(10, 57, 167, 22);
		}
		return typeComboBox;
	}
	
	private JLabel getLblDefaults() {
		if (lblDefaults == null) {
			lblDefaults = new JLabel("Defaults");
			lblDefaults.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblDefaults.setBounds(10, 101, 104, 15);
		}
		return lblDefaults;
	}
	
	private JComboBox getDefaultsComboBox() {
		if (defaultsComboBox == null) {
			defaultsComboBox = new JComboBox();
			defaultsComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_defaultsComboBox_actionPerformed(e);
				}
			});
			defaultsComboBox.setBounds(10, 115, 214, 22);
		}
		return defaultsComboBox;
	}
	
	private JPanel getFirstTimeControlPanel() {
		if (firstTimeControlPanel == null) {
			firstTimeControlPanel = new JPanel();
			firstTimeControlPanel.setBorder(new TitledBorder(null, "First time control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			firstTimeControlPanel.setBounds(301, 24, 305, 114);
			firstTimeControlPanel.setLayout(null);
			firstTimeControlPanel.add(getLblTime());
			firstTimeControlPanel.add(getHSpinner_1());
			firstTimeControlPanel.add(getLblH());
			firstTimeControlPanel.add(getMSpinner_1());
			firstTimeControlPanel.add(getLblMin());
			firstTimeControlPanel.add(getSSpinner_1());
			firstTimeControlPanel.add(getLblSec());
			firstTimeControlPanel.add(getLblMoves());
			firstTimeControlPanel.add(getMovesSpinner_1());
			firstTimeControlPanel.add(getLblGainPerMove());
			firstTimeControlPanel.add(getGainSpinner_1());
		}
		return firstTimeControlPanel;
	}
	
	private JLabel getLblTime() {
		if (lblTime == null) {
			lblTime = new JLabel("Time");
			lblTime.setBounds(10, 21, 31, 14);
			lblTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblTime;
	}
	
	private JSpinner getHSpinner_1() {
		if (hSpinner_1 == null) {
			hSpinner_1 = new JSpinner();
			hSpinner_1.setBounds(61, 20, 48, 18);
		}
		return hSpinner_1;
	}
	
	private JLabel getLblH() {
		if (lblH == null) {
			lblH = new JLabel("h");
			lblH.setBounds(115, 21, 17, 14);
			lblH.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblH;
	}
	
	private JSpinner getMSpinner_1() {
		if (mSpinner_1 == null) {
			mSpinner_1 = new JSpinner();
			mSpinner_1.setBounds(131, 20, 48, 18);
		}
		return mSpinner_1;
	}
	
	private JLabel getLblMin() {
		if (lblMin == null) {
			lblMin = new JLabel("min");
			lblMin.setBounds(189, 21, 19, 14);
			lblMin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblMin;
	}
	
	private JSpinner getSSpinner_1() {
		if (sSpinner_1 == null) {
			sSpinner_1 = new JSpinner();
			sSpinner_1.setBounds(213, 20, 48, 18);
		}
		return sSpinner_1;
	}
	
	private JLabel getLblSec() {
		if (lblSec == null) {
			lblSec = new JLabel("sec");
			lblSec.setBounds(271, 21, 23, 14);
			lblSec.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblSec;
	}
	
	private JLabel getLblMoves() {
		if (lblMoves == null) {
			lblMoves = new JLabel("Moves");
			lblMoves.setBounds(10, 52, 39, 18);
			lblMoves.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblMoves;
	}
	
	private JSpinner getMovesSpinner_1() {
		if (movesSpinner_1 == null) {
			movesSpinner_1 = new JSpinner();
			movesSpinner_1.setBounds(61, 53, 48, 18);
		}
		return movesSpinner_1;
	}
	
	private JLabel getLblGainPerMove() {
		if (lblGainPerMove == null) {
			lblGainPerMove = new JLabel("Gain per move");
			lblGainPerMove.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblGainPerMove.setBounds(10, 81, 99, 22);
		}
		return lblGainPerMove;
	}
	
	private JSpinner getGainSpinner_1() {
		if (gainSpinner_1 == null) {
			gainSpinner_1 = new JSpinner();
			gainSpinner_1.setBounds(131, 84, 48, 18);
		}
		return gainSpinner_1;
	}
	
	private JPanel getSecondTimeControlPanel() {
		if (secondTimeControlPanel == null) {
			secondTimeControlPanel = new JPanel();
			secondTimeControlPanel.setBorder(new TitledBorder(null, "Second time control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			secondTimeControlPanel.setLayout(null);
			secondTimeControlPanel.setBounds(301, 143, 305, 114);
			secondTimeControlPanel.add(getLabel());
			secondTimeControlPanel.add(getHSpinner_2());
			secondTimeControlPanel.add(getLabel_1());
			secondTimeControlPanel.add(getMSpinner_2());
			secondTimeControlPanel.add(getLabel_2());
			secondTimeControlPanel.add(getSSpinner_2());
			secondTimeControlPanel.add(getLabel_3());
			secondTimeControlPanel.add(getLabel_4());
			secondTimeControlPanel.add(getMovesSpinner_2());
			secondTimeControlPanel.add(getLabel_5());
			secondTimeControlPanel.add(getGainSpinner_2());
		}
		return secondTimeControlPanel;
	}
	
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Time");
			label.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label.setBounds(10, 21, 31, 14);
		}
		return label;
	}
	
	private JSpinner getHSpinner_2() {
		if (hSpinner_2 == null) {
			hSpinner_2 = new JSpinner();
			hSpinner_2.setBounds(61, 20, 48, 18);
		}
		return hSpinner_2;
	}
	
	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("h");
			label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_1.setBounds(115, 21, 17, 14);
		}
		return label_1;
	}
	
	private JSpinner getMSpinner_2() {
		if (mSpinner_2 == null) {
			mSpinner_2 = new JSpinner();
			mSpinner_2.setBounds(131, 20, 48, 18);
		}
		return mSpinner_2;
	}
	
	private JLabel getLabel_2() {
		if (label_2 == null) {
			label_2 = new JLabel("min");
			label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_2.setBounds(189, 21, 19, 14);
		}
		return label_2;
	}
	
	private JSpinner getSSpinner_2() {
		if (sSpinner_2 == null) {
			sSpinner_2 = new JSpinner();
			sSpinner_2.setBounds(213, 20, 48, 18);
		}
		return sSpinner_2;
	}
	
	private JLabel getLabel_3() {
		if (label_3 == null) {
			label_3 = new JLabel("sec");
			label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_3.setBounds(271, 21, 23, 14);
		}
		return label_3;
	}
	
	private JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel("Moves");
			label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_4.setBounds(10, 52, 39, 18);
		}
		return label_4;
	}
	
	private JSpinner getMovesSpinner_2() {
		if (movesSpinner_2 == null) {
			movesSpinner_2 = new JSpinner();
			movesSpinner_2.setBounds(61, 53, 48, 18);
		}
		return movesSpinner_2;
	}
	
	private JLabel getLabel_5() {
		if (label_5 == null) {
			label_5 = new JLabel("Gain per move");
			label_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_5.setBounds(10, 81, 99, 22);
		}
		return label_5;
	}
	
	private JSpinner getGainSpinner_2() {
		if (gainSpinner_2 == null) {
			gainSpinner_2 = new JSpinner();
			gainSpinner_2.setBounds(131, 84, 48, 18);
		}
		return gainSpinner_2;
	}
	
	private JPanel getThirdTimeControlPanel() {
		if (thirdTimeControlPanel == null) {
			thirdTimeControlPanel = new JPanel();
			thirdTimeControlPanel.setBorder(new TitledBorder(null, "Third time control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			thirdTimeControlPanel.setLayout(null);
			thirdTimeControlPanel.setBounds(301, 262, 305, 80);
			thirdTimeControlPanel.add(getLabel_6());
			thirdTimeControlPanel.add(getHSpinner_3());
			thirdTimeControlPanel.add(getLabel_7());
			thirdTimeControlPanel.add(getMSpinner_3());
			thirdTimeControlPanel.add(getLabel_8());
			thirdTimeControlPanel.add(getSSpinner_3());
			thirdTimeControlPanel.add(getLabel_9());
			thirdTimeControlPanel.add(getLabel_11());
			thirdTimeControlPanel.add(getGainSpinner_3());
		}
		return thirdTimeControlPanel;
	}
	
	private JLabel getLabel_6() {
		if (label_6 == null) {
			label_6 = new JLabel("Time");
			label_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_6.setBounds(10, 21, 31, 14);
		}
		return label_6;
	}
	
	private JSpinner getHSpinner_3() {
		if (hSpinner_3 == null) {
			hSpinner_3 = new JSpinner();
			hSpinner_3.setBounds(61, 20, 48, 18);
		}
		return hSpinner_3;
	}
	
	private JLabel getLabel_7() {
		if (label_7 == null) {
			label_7 = new JLabel("h");
			label_7.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_7.setBounds(115, 21, 17, 14);
		}
		return label_7;
	}
	
	private JSpinner getMSpinner_3() {
		if (mSpinner_3 == null) {
			mSpinner_3 = new JSpinner();
			mSpinner_3.setBounds(131, 20, 48, 18);
		}
		return mSpinner_3;
	}
	
	private JLabel getLabel_8() {
		if (label_8 == null) {
			label_8 = new JLabel("min");
			label_8.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_8.setBounds(189, 21, 19, 14);
		}
		return label_8;
	}
	
	private JSpinner getSSpinner_3() {
		if (sSpinner_3 == null) {
			sSpinner_3 = new JSpinner();
			sSpinner_3.setBounds(213, 20, 48, 18);
		}
		return sSpinner_3;
	}
	
	private JLabel getLabel_9() {
		if (label_9 == null) {
			label_9 = new JLabel("sec");
			label_9.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_9.setBounds(271, 21, 23, 14);
		}
		return label_9;
	}
	
	private JLabel getLabel_11() {
		if (label_11 == null) {
			label_11 = new JLabel("Gain per move");
			label_11.setFont(new Font("Tahoma", Font.PLAIN, 12));
			label_11.setBounds(10, 49, 99, 22);
		}
		return label_11;
	}
	
	private JSpinner getGainSpinner_3() {
		if (gainSpinner_3 == null) {
			gainSpinner_3 = new JSpinner();
			gainSpinner_3.setBounds(131, 52, 48, 18);
		}
		return gainSpinner_3;
	}
	
	protected void do_btnOkActionPerformed(ActionEvent e) {
		updateModel();
		AbstractErrors result = validateModel();
		if (result.hasErrors()) {
			messageHandler.showErrorMessages(result.getAllErrors().get(0).getCode());
		} else {
			eventPublisher.publishCommandEvent(e.getSource(), this, CommandEvent.Command.SET_UP_GAME_SAVE);
			dispose();
		}
	}
	
	protected void do_btnCancelActionPerformed(ActionEvent e) {
		dispose();
	}
	
	protected void do_typeComboBoxActionPerformed(ActionEvent e) {
		switch (typeComboBox.getSelectedIndex()) {
		case 0:
			getPanel_2().remove(getLabelFriendly());
		    getSecondTimeControlPanel().setVisible(false);
		    getFirstTimeControlPanel().setVisible(false);
		    getAddToDefaultsButton().setVisible(true);
		    ((TitledBorder)getThirdTimeControlPanel().getBorder()).setTitle("Single time control");
		    getThirdTimeControlPanel().setBounds(301, 24, 305, 114);
		    getThirdTimeControlPanel().setVisible(true);
			break;
		case 1:
			getPanel_2().remove(getLabelFriendly());
			((TitledBorder)getThirdTimeControlPanel().getBorder()).setTitle("Third time control");
			getThirdTimeControlPanel().setBounds(301, 262, 305, 80);
			getThirdTimeControlPanel().setVisible(true);
			getSecondTimeControlPanel().setVisible(true);
			getFirstTimeControlPanel().setVisible(true);
			getAddToDefaultsButton().setVisible(true);
			break;
		case 2:
			getFirstTimeControlPanel().setVisible(false);
			getSecondTimeControlPanel().setVisible(false);
			getThirdTimeControlPanel().setVisible(false);
			getPanel_2().add(getLabelFriendly());
			getAddToDefaultsButton().setVisible(false);
			break;
		}
	}
	
	private JLabel getLabelFriendly () {
		if (labelFriendly == null) {
			labelFriendly = Utils.createWrappedLabel(new Font("Tahoma", Font.PLAIN, 12), 50, 
					"If the player is engine type then will think a fixed time per move (few seconds), else there is no time limit.");
			labelFriendly.setBorder(new TitledBorder("No time limit"));
			labelFriendly.setBounds(301, 24, 305, 114);
		}
		return labelFriendly;
	}
	
	protected void do_defaultsComboBox_actionPerformed(ActionEvent e) {
		
	}
	private JButton getAddToDefaultsButton() {
		if (addToDefaultsButton == null) {
			addToDefaultsButton = new JButton("Add to defaults");
			addToDefaultsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					do_addToDefaultsbutton_actionPerformed(e);
				}
			});
			addToDefaultsButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
			addToDefaultsButton.setBounds(375, 353, 140, 23);
		}
		return addToDefaultsButton;
	}
	protected void do_addToDefaultsbutton_actionPerformed(ActionEvent e) {
		
	}

	@Override
	public void commit() {
        whitePlayerComboBox.removeAllItems();
        blackPlayerComboBox.removeAllItems();
    	whitePlayerComboBox.setModel(new DefaultComboBoxModel(model.getPlayerModels().toArray()));
    	blackPlayerComboBox.setModel(new DefaultComboBoxModel(model.getPlayerModels().toArray()));
    	//FIXME - other settings
	}

	@Override
	public void updateModel () {
		model.setFriendly(typeComboBox.getSelectedIndex() == 2);
		model.setOneTimeControl(typeComboBox.getSelectedIndex() == 0);
		if (model.isThreeTimeControls()) {
			TimeControlModel firstTC = new TimeControlModel();
			firstTC.setHours(Utils.toInteger(hSpinner_1.getValue()));
			firstTC.setMin(Utils.toInteger(mSpinner_1.getValue()));
			firstTC.setSec(Utils.toInteger(sSpinner_1.getValue()));
			firstTC.setGainPerMove(Utils.toInteger(gainSpinner_1.getValue()));
			firstTC.setMoves(Utils.toInteger(movesSpinner_1.getValue()));
		    model.setFirstTC(firstTC);
		    
		    TimeControlModel secondTC = new TimeControlModel();
		    secondTC.setHours(Utils.toInteger(hSpinner_2.getValue()));
		    secondTC.setMin(Utils.toInteger(mSpinner_2.getValue()));
		    secondTC.setSec(Utils.toInteger(sSpinner_2.getValue()));
		    secondTC.setGainPerMove(Utils.toInteger(gainSpinner_2.getValue()));
		    secondTC.setMoves(Utils.toInteger(movesSpinner_2.getValue()));
		    model.setSecondTC(secondTC);
		} 
		if (model.hasTimeControl()) {
		    TimeControlModel thirdTC = new TimeControlModel();
		    thirdTC.setHours(Utils.toInteger(hSpinner_3.getValue()));
		    thirdTC.setMin(Utils.toInteger(mSpinner_3.getValue()));
		    thirdTC.setSec(Utils.toInteger(sSpinner_3.getValue()));
		    thirdTC.setGainPerMove(Utils.toInteger(gainSpinner_3.getValue()));
		    model.setThirdTC(thirdTC);
		}
	    model.setWhitePlayerModel((PlayerModel)whitePlayerComboBox.getSelectedItem());
	    model.setBlackPlayerModel((PlayerModel)blackPlayerComboBox.getSelectedItem());
	}

	@Override
	protected Validator getValidator() {
		return new SetUpGameValidator();
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetUpGameDialog dialog = new SetUpGameDialog();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
