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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.chess.quasimodo.domain.logic.FormView;
import org.chess.quasimodo.gui.model.PromotionDialogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.AbstractErrors;


public class PromotionDialog implements FormView<PromotionDialogModel> {
	
	@Autowired
	private BoardPanel boardPanel;
	
	private JOptionPane optionPane;
	private JDialog dialog;
	private ButtonGroup buttonGroup;
	
	private PromotionDialogModel model; 
	
	private  ImageIcon wnImg;
	private  ImageIcon bnImg;
	private  ImageIcon wbImg;
	private  ImageIcon bbImg;
	private  ImageIcon wrImg;
	private  ImageIcon brImg;
	private  ImageIcon wqImg;
	private  ImageIcon bqImg;
	
	public static final String ACTION_QUEEN  = "queen";
	public static final String ACTION_ROOK   = "rook";
	public static final String ACTION_BISHOP = "bishop";
	public static final String ACTION_KNIGHT = "knight";
	
	private final int imgSize = 40;
	
	private char getPiece() {
		char piece = 0;
		if(optionPane.getValue() != null) {
			String selection = buttonGroup.getSelection().getActionCommand();
			if (ACTION_QUEEN.equals(selection)) {
				if (model.isWhite()) {
					piece = 'Q';
				} else {
					piece = 'q';
				}
			} else if (ACTION_ROOK.equals(selection)) {
				if (model.isWhite()) {
					piece = 'R';
				} else {
					piece = 'r';
				}
			} else if (ACTION_BISHOP.equals(selection)) {
				if (model.isWhite()) {
					piece = 'B';
				} else {
					piece = 'b';
				}
			} else if (ACTION_KNIGHT.equals(selection)) {
				if (model.isWhite()) {
					piece = 'N';
				} else {
					piece = 'n';
				}
			} else {
				//no other action is possible
			}
			return piece;
		} else {
			throw new IllegalStateException("No selection made");
		}
	}

	public void showView () {
		dialog.setVisible(true);
	}
	
	private JOptionPane getOptionPane () {
		if (optionPane == null)  {
			optionPane = new JOptionPane("", JOptionPane.PLAIN_MESSAGE);
			optionPane.setPreferredSize(new Dimension(80, 48));
		}
		return optionPane;
	}
	
	private JDialog getDialog () {
		if (dialog == null)  {
			optionPane = getOptionPane ();
			dialog = optionPane.createDialog("Promotion");
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setTitle("Promotion piece");
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.setBounds(100, 100, 330,170);
			dialog.getContentPane().setLayout(new BorderLayout(5, 5));
			dialog.add(optionPane, BorderLayout.SOUTH);
		}
		return dialog;
	}
	
	private JRadioButton queenRadioButton;
	
	private JRadioButton getQueenRadioButton () {
		if (queenRadioButton == null) {
			queenRadioButton = new JRadioButton();
			queenRadioButton.setPreferredSize(new Dimension(45,28));
			queenRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
			queenRadioButton.setActionCommand(ACTION_QUEEN );
			queenRadioButton.setSelected(true);
		}
		return queenRadioButton;
	}
	
	private JRadioButton rookRadioButton;
	
	private JRadioButton getRookRadioButton () {
		if (rookRadioButton == null) {
			rookRadioButton = new JRadioButton();
			rookRadioButton.setPreferredSize(new Dimension(45,18));
			rookRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
			rookRadioButton.setActionCommand(ACTION_ROOK);
		}
		return rookRadioButton;
	}
	
	private JRadioButton bishopRadioButton;
	
	private JRadioButton getBishopRadioButton () {
		if (bishopRadioButton == null) {
			bishopRadioButton = new JRadioButton();
			bishopRadioButton.setPreferredSize(new Dimension(45,18));
			bishopRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
			bishopRadioButton.setActionCommand(ACTION_BISHOP);
		}
		return bishopRadioButton;
	}
	
	private JRadioButton knightRadioButton;
	
	private JRadioButton getKnightRadioButton () {
		if (knightRadioButton == null) {
			knightRadioButton = new JRadioButton();
			knightRadioButton.setPreferredSize(new Dimension(45,18));
			knightRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
			knightRadioButton.setActionCommand(ACTION_KNIGHT);
		}
		return knightRadioButton;
	}
	
	private ButtonGroup getButtonGroup () {
		if (buttonGroup == null) {
			buttonGroup = new ButtonGroup();
			buttonGroup.add(getQueenRadioButton());
			buttonGroup.add(getRookRadioButton());
			buttonGroup.add(getBishopRadioButton());
			buttonGroup.add(getKnightRadioButton());
		}
		return buttonGroup;
	}
	
	private JPanel rPanel;
	
	private JPanel getRPanel () {
		if (rPanel == null) {
			rPanel = new JPanel();
			rPanel.setPreferredSize(new Dimension(300, 20));
			rPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			rPanel.add(getQueenRadioButton());
			rPanel.add(getRookRadioButton());
			rPanel.add(getBishopRadioButton());
			rPanel.add(getKnightRadioButton());
		}
		return rPanel;
	}
	
	private JButton queenButton;
	
	private JButton getQueenButton () {
		if (queenButton  == null) {
			queenButton = new JButton("");
			queenButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					queenRadioButton.setSelected(true);
				}
			});
			queenButton.setPreferredSize(new Dimension(45, 45));
			if (model.isWhite()) {
			    queenButton.setIcon(wqImg);
			} else {
				queenButton.setIcon(bqImg);
			}
		}
		return queenButton;
	}
	
	private JButton rookButton;
	
	private JButton getRookButton () {
		if (rookButton == null) {
			rookButton = new JButton("");
			rookButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rookRadioButton.setSelected(true);
				}
			});
			rookButton.setPreferredSize(new Dimension(45, 45));
			if (model.isWhite()) {
				rookButton.setIcon(wrImg);
			} else {
				rookButton.setIcon(brImg);
			}
		}
		return rookButton;
	}
	
	private JButton bishopButton;
	
	private JButton getBishopButton () {
		if (bishopButton == null) {
			bishopButton = new JButton("");
			bishopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					bishopRadioButton.setSelected(true);
				}
			});
			bishopButton.setPreferredSize(new Dimension(45, 45));
			if (model.isWhite()) {
				bishopButton.setIcon(wbImg);
			} else {
				bishopButton.setIcon(bbImg);
			}
		}
		return bishopButton;
	}
	
	private JButton knightButton;
	private JButton getKnightButton () {
		if (knightButton == null) {
			knightButton = new JButton("");
			knightButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					knightRadioButton.setSelected(true);
				}
			});
			knightButton.setPreferredSize(new Dimension(45, 45));
			if (model.isWhite()) {
				knightButton.setIcon(wnImg);
			} else {
				knightButton.setIcon(bnImg);
			}
		}
		return knightButton;
	}
	
	private JPanel bPanel;
	private JPanel getBPanel () {
		if (bPanel == null) {
			bPanel = new JPanel();
			bPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			bPanel.setPreferredSize(new Dimension(200, 50));
			bPanel.add(getQueenButton());
			bPanel.add(getRookButton());
			bPanel.add(getBishopButton());
			bPanel.add(getKnightButton());
		}
		return bPanel;
	}
	
	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public PromotionDialog(PromotionDialogModel model) {
		this.model = model;
	}
	
	@PostConstruct
	public void initialize () {
		dialog = getDialog();
		loadPieceImages();
		getButtonGroup();
		dialog.getContentPane().add (getRPanel(), BorderLayout.CENTER);
		dialog.getContentPane().add (getBPanel(), BorderLayout.NORTH);
		dialog.setLocationRelativeTo(null);
	}
	
	private void loadPieceImages () {
		wnImg = new ImageIcon(boardPanel.getWnImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
		bnImg = new ImageIcon(boardPanel.getBnImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
		wbImg = new ImageIcon(boardPanel.getWbImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
		bbImg = new ImageIcon(boardPanel.getBbImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
		wrImg = new ImageIcon(boardPanel.getWrImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
		brImg = new ImageIcon(boardPanel.getBrImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
		wqImg = new ImageIcon(boardPanel.getWqImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
		bqImg = new ImageIcon(boardPanel.getBqImg()
				.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH));
	}

	@Override
	public void commit() {
		
	}

	@Override
	public PromotionDialogModel getModel() {
		return model;
	}

	@Override
	public void setModel(PromotionDialogModel model) {
		Assert.notNull(model, "Model cannot be null");
		this.model = model;
	}
	
	@Override
	public void updateModel() {
		model.setPiece(getPiece());
	}

	public AbstractErrors validateModel() {
		return null;
	}

	@Override
	public void dispose() {
		if (dialog != null) {
		    dialog.dispose();
		}
	}
}
