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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.chess.quasimodo.annotation.Design;
import org.chess.quasimodo.config.design.Designable;
import org.chess.quasimodo.config.design.Designer;
import org.chess.quasimodo.event.EngineOutputAware;
import org.chess.quasimodo.event.MoveTimeChangedAware;
import org.chess.quasimodo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class EnginePanel extends JPanel implements EngineOutputAware, MoveTimeChangedAware, Designable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4601159668233130376L;
	
	private JPanel      namePanel;
	private JPanel      evalPanel;
	private JPanel      depthPanel;
	private JPanel      nodePanel;
	private JPanel      timePanel;
	private JPanel      crtMovePanel;
	private JScrollPane scrollPane;
	private JTextArea   varTextArea;
	
	private JButton     btnAnalyse;
	private JLabel      nameLabel;
	private JLabel      crtMoveLabel;
	private JLabel      npsLabel;
	private JLabel      depthLabel;
	private JLabel      evalLabel;
	
	private BufferedImage backgroundImage;
	
	@Autowired
	private Designer designer;
	
	@Design (key="engine.output.font")
	public Font getEngineOutputFont() {
		return varTextArea.getFont();
	}

	@Design (key="engine.output.font")
	public void setEngineOutputFont(Font engineOutputFont) {
		varTextArea.setFont(engineOutputFont);
	}

	@Design (key="engine.label.font")
	public Font getEngineLabelFont() {
		return timeLabel.getFont();
	}

	@Design (key="engine.label.font")
	public void setEngineLabelFont(Font engineLabelFont) {
		timeLabel.setFont(engineLabelFont);
		depthLabel.setFont(engineLabelFont);
		npsLabel.setFont(engineLabelFont);
		evalLabel.setFont(engineLabelFont);
		crtMoveLabel.setFont(engineLabelFont);
		btnAnalyse.setFont(engineLabelFont);
	}

	private JPanel engineInfoPanel;
	
	@SuppressWarnings("serial")
	private JPanel getEngineInfoPanel () {
		if (engineInfoPanel == null) {
			engineInfoPanel = new TextureJPanel() {

				@Override
				protected BufferedImage getBackgroundImage() {
					return backgroundImage;
				}
				
			};
			engineInfoPanel.setOpaque(true);
			engineInfoPanel.setPreferredSize(new Dimension(80, 85));
			GroupLayout gl_engineInfoPanel = new GroupLayout(engineInfoPanel);
			gl_engineInfoPanel.setHorizontalGroup(
				gl_engineInfoPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_engineInfoPanel.createSequentialGroup()
						.addGap(10)
						.addGroup(gl_engineInfoPanel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_engineInfoPanel.createSequentialGroup()
								.addComponent(getNamePanel(), GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(getBtnAnalyse(), GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
								.addGap(48)
								.addComponent(getCrtMovePanel(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_engineInfoPanel.createSequentialGroup()
								.addComponent(getEvalPanel(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
								.addGap(5)
								.addComponent(getDepthPanel(), GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addGap(5)
								.addComponent(getNodePanel(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
								.addGap(4)
								.addComponent(getTimePanel(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))))
			);
			gl_engineInfoPanel.setVerticalGroup(
				gl_engineInfoPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_engineInfoPanel.createSequentialGroup()
						.addGap(11)
						.addGroup(gl_engineInfoPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(getNamePanel(), GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_engineInfoPanel.createSequentialGroup()
								.addGap(3)
								.addComponent(getBtnAnalyse(), GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_engineInfoPanel.createSequentialGroup()
								.addGap(4)
								.addComponent(getCrtMovePanel(), GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
						.addGap(10)
						.addGroup(gl_engineInfoPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(getEvalPanel(), GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addComponent(getDepthPanel(), GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addComponent(getNodePanel(), GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addComponent(getTimePanel(), GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
			);
			engineInfoPanel.setLayout(gl_engineInfoPanel);
		}
		return engineInfoPanel;
	}
	
	private JPanel getNamePanel () {
		if (namePanel == null) {
			namePanel = new JPanel();
			namePanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			namePanel.setLayout(new BorderLayout(0, 0));
			namePanel.add(getNameLabel(), BorderLayout.CENTER);
		}
		return namePanel;
	}
	
	public void setEngineName (String engineName) {
		getNameLabel().setText(engineName);
	}
	
	private JPanel getEvalPanel () {
		if (evalPanel == null) {
			evalPanel = new JPanel();
			evalPanel.setPreferredSize(new Dimension(40, 20));
			evalPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			evalPanel.setLayout(new BorderLayout(0, 0));
			evalPanel.add(getEvalLabel(), BorderLayout.CENTER);
		}
		return evalPanel;
	}
	
	private JPanel getDepthPanel () {
		if (depthPanel == null) {
			depthPanel = new JPanel();
			depthPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			depthPanel.setPreferredSize(new Dimension(10, 20));
			depthPanel.setLayout(new BorderLayout(0, 0));
			depthPanel.add(getDepthLabel(), BorderLayout.CENTER);
		}
		return depthPanel;
	}
	
	private JPanel getNodePanel () {
		if (nodePanel == null) {
			nodePanel = new JPanel();
			nodePanel.setPreferredSize(new Dimension(10, 20));
			nodePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			nodePanel.setLayout(new BorderLayout(0, 0));
			nodePanel.add(getNpsLabel(), BorderLayout.CENTER);
		}
		return nodePanel;
	}
	
	private JPanel getTimePanel () {
		if (timePanel == null) {
			timePanel = new JPanel();
			timePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			timePanel.setPreferredSize(new Dimension(10, 20));
			timePanel.setLayout(new BorderLayout(0, 0));
			timePanel.add(getTimeLabel(), BorderLayout.CENTER);
		}
		return timePanel;
	}
	
	private JButton getBtnAnalyse () {
		if (btnAnalyse == null) {
			btnAnalyse = new JButton("Analyse");
			btnAnalyse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}
			});
			btnAnalyse.setMargin(new Insets(0, 0, 0, 0));
		}
		return btnAnalyse;
	}
	
	private JPanel getCrtMovePanel () {
		if (crtMovePanel == null) {
			crtMovePanel = new JPanel();
			crtMovePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			crtMovePanel.setPreferredSize(new Dimension(10, 20));
			crtMovePanel.setLayout(new BorderLayout(0, 0));
			crtMovePanel.add(getCrtMoveLabel());
		}
		return crtMovePanel;
	}
	
	private JTextArea getLinesPane () {
		if (varTextArea == null) {
			varTextArea = new JTextArea();
			varTextArea.setEditable(false);
			varTextArea.setLineWrap(false);
			varTextArea.setRequestFocusEnabled(false);
			varTextArea.setFocusable(false);
		}
		return varTextArea;
	}
	
	private JScrollPane getScrollPane () {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getLinesPane());
			scrollPane.setPreferredSize(new Dimension (100, 50));
		}
		return scrollPane;
	}
	
	@PostConstruct
	public void initialize () {
		try {
			backgroundImage = ImageIO.read(Thread.currentThread().getContextClassLoader()
				      .getResource("images/marble04.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setLayout(new BorderLayout(0, 0));
		this.add(getEngineInfoPanel(), BorderLayout.NORTH);
		add(getScrollPane(), BorderLayout.CENTER);
		injectDesign();
	}

	public void clearEngineOutput () {
		varTextArea.setText(null);
	}
	
	private JLabel timeLabel;
	
	@Override
	public void setCurrentMove(String currentMove) {
		crtMoveLabel.setText(currentMove);
	}

	@Override
	public void setDepth(int depth) {
		depthLabel.setText("Depth " + depth);
		
	}

	@Override
	public void setNps(int knps) {
		npsLabel.setText(knps / 1000 + " kN/s");
		
	}
	
	private JLabel getNameLabel() {
		if (nameLabel == null) {
			nameLabel = new JLabel();
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			nameLabel.setOpaque(true);
			nameLabel.setBackground(new Color(240, 248, 255));
		}
		return nameLabel;
	}
	
	private JLabel getCrtMoveLabel() {
		if (crtMoveLabel == null) {
			crtMoveLabel = new JLabel();
			crtMoveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return crtMoveLabel;
	}
	private JLabel getNpsLabel() {
		if (npsLabel == null) {
			npsLabel = new JLabel();
			npsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return npsLabel;
	}
	private JLabel getDepthLabel() {
		if (depthLabel == null) {
			depthLabel = new JLabel();
			depthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return depthLabel;
	}
	private JLabel getEvalLabel() {
		if (evalLabel == null) {
			evalLabel = new JLabel();
			evalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return evalLabel;
	}

	@Override
	public void setScore(String score) {
		evalLabel.setText(score);
	}

	@Override
	public void setPV(String variation, String score,
			String depth, String nodes, String time) {
		varTextArea.insert(Utils.formatPV(variation, score, depth, nodes, time), 0);
	}
	
	private JLabel getTimeLabel() {
		if (timeLabel == null) {
			timeLabel = new JLabel();
			timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return timeLabel;
	}

	@Override
	public void setMoveTime(long moveTime) {
		timeLabel.setText(Utils.formatSeconds(moveTime));
	}

	@Override
	public void refreshDesign() {
		repaint();
	}
	
	@Override
	public void injectDesign() {
		designer.injectDesign(this);
	}

}
