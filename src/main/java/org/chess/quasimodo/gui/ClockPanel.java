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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.chess.quasimodo.config.design.Designable;
import org.chess.quasimodo.config.design.Designer;
import org.chess.quasimodo.domain.logic.ChessColor;
import org.chess.quasimodo.event.ClockChangedAware;
import org.chess.quasimodo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component ("clockView")
public class ClockPanel extends JPanel implements ClockChangedAware, Designable {//TODO - needs refactoring

	/**
	 * Serial IdOptions.
	 */
	private static final long serialVersionUID = 103632822782087418L;

	private JLabel whiteClockLabel;
	private JLabel blackClockLabel;
	
	@Autowired
	private Designer designer;
	
	public ClockPanel () {
		initialize();
		setOpaque(true);
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.gui.ClockChangedAware#setWhiteClockTime(long, long)
	 */
	public void setClockTime (long whiteElapsedTime,long blackElapsedTime) {
		whiteClockLabel.setText(Utils.formatSeconds(whiteElapsedTime));
		blackClockLabel.setText(Utils.formatSeconds(blackElapsedTime));
	}
	
    private JLabel playerNamesLabel;
	
	private JLabel getPlayerNamesLabel () {
		if (playerNamesLabel == null) {
			playerNamesLabel = new JLabel("New game");
			playerNamesLabel.setFont(new Font("Arial", Font.BOLD, 15));
			playerNamesLabel.setPreferredSize(new Dimension(250, 30));
			playerNamesLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			playerNamesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return playerNamesLabel;
	}
	
	public void setGameTitle (String gameTitle) {
		getPlayerNamesLabel().setText(gameTitle);
	}
	
	private JLabel gameInfoLabel;
	
	private JLabel getGameInfoLabel() {
		if (gameInfoLabel == null) {
			gameInfoLabel = new JLabel();
			gameInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
			gameInfoLabel.setPreferredSize(new Dimension(250, 25));
			gameInfoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			gameInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return gameInfoLabel;
	}

	public void setGameInfo (String gameInfo) {
		getGameInfoLabel().setText(gameInfo);
	}
	
	private JLabel getWhiteClockLabel () {
		if (whiteClockLabel == null) {
			whiteClockLabel = new JLabel("0:00:00");
			whiteClockLabel.setOpaque(true);
			whiteClockLabel.setForeground(new Color(253, 255, 172));
			whiteClockLabel.setBackground(Color.BLACK);
			whiteClockLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
			whiteClockLabel.setBorder(new BevelBorder(BevelBorder.LOWERED,Color.WHITE,Color.WHITE));
			whiteClockLabel.setFont(new Font("Arial", Font.BOLD, 26));
			whiteClockLabel.setHorizontalAlignment(SwingConstants.CENTER);
			whiteClockLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			whiteClockLabel.setPreferredSize(new Dimension(180, 40));
		}
		return whiteClockLabel;
	}
	
	private JLabel getBlackClockLabel () {
		if (blackClockLabel == null) {
			blackClockLabel = new JLabel("0:00:00");
			blackClockLabel.setOpaque(true);
			blackClockLabel.setBackground(Color.BLACK);
			blackClockLabel.setForeground(new Color(253, 255, 172));
			blackClockLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
			blackClockLabel.setBorder(new BevelBorder(BevelBorder.LOWERED,Color.WHITE,Color.WHITE));
			blackClockLabel.setFont(new Font("Arial", Font.BOLD, 26));
			blackClockLabel.setHorizontalAlignment(SwingConstants.CENTER);
			blackClockLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			blackClockLabel.setPreferredSize(new Dimension(180, 40));
		}
		return blackClockLabel;
	}
	
	protected void initialize () {
		this.setBorder(null);
		this.setPreferredSize(new Dimension(250, 120));
		GridBagLayout gbl_clockPanel = new GridBagLayout();
		this.setLayout(gbl_clockPanel);
		
		GridBagConstraints gbc_whiteClockLabel = new GridBagConstraints();
		gbc_whiteClockLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_whiteClockLabel.insets = new Insets(0, 0, 5, 5);
		gbc_whiteClockLabel.gridx = 0;
		gbc_whiteClockLabel.gridy = 0;
		this.add(getWhiteClockLabel(), gbc_whiteClockLabel);
		
		GridBagConstraints gbc_blackClockLabel = new GridBagConstraints();
		gbc_blackClockLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_blackClockLabel.insets = new Insets(0, 0, 5, 0);
		gbc_blackClockLabel.gridx = 1;
		gbc_blackClockLabel.gridy = 0;
		this.add(getBlackClockLabel(), gbc_blackClockLabel);
		
		GridBagConstraints gbc_playerNamesLabel = new GridBagConstraints();
		gbc_playerNamesLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_playerNamesLabel.gridwidth = 2;
		gbc_playerNamesLabel.insets = new Insets(0, 0, 5, 0);
		gbc_playerNamesLabel.gridx = 0;
		gbc_playerNamesLabel.gridy = 1;
		this.add(getPlayerNamesLabel(), gbc_playerNamesLabel);
		
		GridBagConstraints gbc_gameInfoLabel = new GridBagConstraints();
		gbc_gameInfoLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_gameInfoLabel.gridwidth = 2;
		gbc_gameInfoLabel.insets = new Insets(0, 0, 5, 0);
		gbc_gameInfoLabel.gridx = 0;
		gbc_gameInfoLabel.gridy = 2;
		this.add(getGameInfoLabel(), gbc_gameInfoLabel);
	}

	@Override
	public void injectDesign() {
		designer.injectDesign(this);
	}

	@Override
	public void refreshDesign() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeOver(ChessColor colorToMove) {
		if (colorToMove.isWhite()) {
			setTimeOver (whiteClockLabel);
		} else {
			setTimeOver (blackClockLabel);
		}
		
	}
	
	private void setTimeOver (JLabel label) {
		label.setForeground(Color.RED);
		label.setText("TIME");
	}
}
