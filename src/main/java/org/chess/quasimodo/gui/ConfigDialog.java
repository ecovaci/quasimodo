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
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.chess.quasimodo.util.ViewUtils;


public class ConfigDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2998145585318368949L;
	
	private JTabbedPane tabbedPane;
	private JPanel designPanel;
	private JLabel lblMenuFont;
	private JButton btnChangeFont;
	private JButton btnApply;
	private JButton btnDefaults;
	private JComboBox fontComboBox;
	private JLabel lblFont;
	private JLabel lblFontdescription;
	private JLabel lblPieces;
	private JComboBox piecesComboBox;
	private JButton btnChangeWhiteSquaresColor;
	private JButton btnChangeBlackSquaresColor;
	private JSlider speedSlider;
	private JLabel lblSpeed;
	private JCheckBox chckbxShowCoordonates;
	private JPanel fontPanel;
	private JPanel boardPanel;
	private JPanel automovePanel;
	private JCheckBox chckbxFontAntialising;
	private JButton btnClose;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigDialog dialog = new ConfigDialog();
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
	public ConfigDialog() {
		setResizable(false);
		setTitle("Settings");
		setBounds(100, 100, 462, 495);
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);

	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
			tabbedPane.addTab("Design", null, getDesignPanel(), null);
		}
		return tabbedPane;
	}
	private JPanel getDesignPanel() {
		if (designPanel == null) {
			designPanel = new JPanel();
			designPanel.setName("");
			designPanel.setLayout(null);
			designPanel.add(getBtnDefaults());
			designPanel.add(getBtnApply());
			designPanel.add(getFontPanel());
			designPanel.add(getBoardPanel());
			designPanel.add(getBtnClose());
		}
		return designPanel;
	}
	private JLabel getLblMenuFont() {
		if (lblMenuFont == null) {
			lblMenuFont = new JLabel("Item:");
			lblMenuFont.setBounds(20, 25, 71, 14);
			lblMenuFont.setFont(new Font("Tahoma", Font.BOLD, 11));
		}
		return lblMenuFont;
	}
	private JButton getBtnChangeFont() {
		if (btnChangeFont == null) {
			btnChangeFont = new JButton("Change");
			btnChangeFont.setBounds(361, 55, 57, 23);
			btnChangeFont.setMargin(new Insets(0, 0, 0, 0));
			btnChangeFont.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnChangeFont.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnChangeFontActionPerformed(e);
				}
			});
		}
		return btnChangeFont;
	}
	protected void btnChangeFontActionPerformed(ActionEvent e) {
		/*Font selectedFont = ViewUtils.showFontChooser(this, getFont());//FIXME - initial font
		if (selectedFont != null) {
			 String  strStyle;
	         if (selectedFont.isBold()) {
	              strStyle = selectedFont.isItalic() ? "Bold/Italic" : "Bold";
	         } else {
	              strStyle = selectedFont.isItalic() ? "Italic" : "Plain";
	         }
	         getLblFontdescription().setFont(selectedFont);
	         getLblFontdescription().setText(selectedFont.getName() 
		    		  + ", " + selectedFont.getSize() + ", " + strStyle);
		}*/
	}
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton("Apply");
			btnApply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnApplyActionPerformed(e);
				}
			});
			btnApply.setBounds(190, 397, 72, 23);
			btnApply.setFont(new Font("Tahoma", Font.PLAIN, 11));
		}
		return btnApply;
	}
	private JButton getBtnDefaults() {
		if (btnDefaults == null) {
			btnDefaults = new JButton("Defaults");
			btnDefaults.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnDefaultsActionPerformed(e);
				}
			});
			btnDefaults.setBounds(34, 397, 75, 23);
			btnDefaults.setFont(new Font("Tahoma", Font.PLAIN, 11));
		}
		return btnDefaults;
	}
	private JComboBox getFontComboBox() {
		if (fontComboBox == null) {
			fontComboBox = new JComboBox();
			fontComboBox.setBounds(106, 21, 170, 22);
			fontComboBox.setFont(new Font("Tahoma", Font.PLAIN, 11));
			fontComboBox.setModel(new DefaultComboBoxModel(new String[] {"Menu", "Notation", "Engine output", "General"}));
		}
		return fontComboBox;
	}
	private JLabel getLblFont() {
		if (lblFont == null) {
			lblFont = new JLabel("Font:");
			lblFont.setBounds(20, 59, 57, 14);
			lblFont.setFont(new Font("Tahoma", Font.BOLD, 11));
		}
		return lblFont;
	}
	
	private JLabel getLblFontdescription() {
		if (lblFontdescription == null) {
			lblFontdescription = new JLabel();
			lblFontdescription.setBounds(106, 54, 247, 24);
			lblFontdescription.setAutoscrolls(true);
			lblFontdescription.setAlignmentX(0.5f);
			lblFontdescription.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			lblFontdescription.setBackground(SystemColor.info);
			lblFontdescription.setOpaque(true);
			lblFontdescription.setForeground(SystemColor.desktop);
			lblFontdescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		}
		return lblFontdescription;
	}
	private JLabel getLblPieces() {
		if (lblPieces == null) {
			lblPieces = new JLabel("Pieces:");
			lblPieces.setBounds(22, 27, 57, 14);
		}
		return lblPieces;
	}
	private JComboBox getPiecesComboBox() {
		if (piecesComboBox == null) {
			piecesComboBox = new JComboBox();
			piecesComboBox.setBounds(104, 24, 187, 20);
		}
		return piecesComboBox;
	}
	private JButton getBtnChangeWhiteSquaresColor() {
		if (btnChangeWhiteSquaresColor == null) {
			btnChangeWhiteSquaresColor = new JButton("White squares color");
			btnChangeWhiteSquaresColor.setBounds(71, 64, 127, 23);
			btnChangeWhiteSquaresColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnChangeWhiteSquaresColoractionPerformed(e);
				}
			});
			btnChangeWhiteSquaresColor.setMargin(new Insets(0, 0, 0, 0));
			btnChangeWhiteSquaresColor.setFont(new Font("Tahoma", Font.PLAIN, 11));
		}
		return btnChangeWhiteSquaresColor;
	}
	private JButton getBtnChangeBlackSquaresColor() {
		if (btnChangeBlackSquaresColor == null) {
			btnChangeBlackSquaresColor = new JButton("Black squares color");
			btnChangeBlackSquaresColor.setBounds(213, 64, 138, 23);
			btnChangeBlackSquaresColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnChangeBlackSquaresColoractionPerformed(e);
				}
			});
			btnChangeBlackSquaresColor.setMargin(new Insets(0, 0, 0, 0));
			btnChangeBlackSquaresColor.setFont(new Font("Tahoma", Font.PLAIN, 11));
		}
		return btnChangeBlackSquaresColor;
	}
	private JSlider getSpeedSlider() {
		if (speedSlider == null) {
			speedSlider = new JSlider();
			speedSlider.setBounds(66, 23, 200, 23);
			speedSlider.setMinimum(5);
			speedSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					sliderStateChanged(e);
				}
			});
		}
		return speedSlider;
	}
	private JLabel getLblSpeed() {
		if (lblSpeed == null) {
			lblSpeed = new JLabel();
			lblSpeed.setBounds(114, 51, 99, 23);
			lblSpeed.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			lblSpeed.setForeground(SystemColor.desktop);
			lblSpeed.setHorizontalAlignment(SwingConstants.CENTER);
			lblSpeed.setOpaque(true);
			lblSpeed.setBackground(SystemColor.info);
		}
		return lblSpeed;
	}
	protected void sliderStateChanged(ChangeEvent e) {
		String speed;
		if (speedSlider.getValue() == 5) {
			speed = "Minimum";
		} else if (speedSlider.getValue() < 20) {
			speed = "Very slow";
		} else if (speedSlider.getValue() < 40) {
			speed = "Slow";
		} else if (speedSlider.getValue() < 60) {
			speed = "Normal";
		} else if (speedSlider.getValue() < 80) {
			speed = "Fast";
		}  else if (speedSlider.getValue() == 100) {
			speed = "Maximum";
		}  else {
			speed = "Very fast";
		}
		lblSpeed.setText(speed + " " + speedSlider.getValue() + "%");
	}
	protected void btnChangeWhiteSquaresColoractionPerformed(ActionEvent e) {
		/*Color selectedColor = ViewUtils.showColorChooser(this, Color.BLUE);//FIXME - initial color
		if (selectedColor != null) {
			((JButton)e.getSource()).setBackground(selectedColor);
		}*/
	}
	protected void btnChangeBlackSquaresColoractionPerformed(ActionEvent e) {
		/*Color selectedColor = ViewUtils.showColorChooser(this, Color.BLUE);//FIXME - initial color
		if (selectedColor != null) {
			((JButton)e.getSource()).setBackground(selectedColor);
		}*/
	}
	
	protected void btnDefaultsActionPerformed(ActionEvent e) {
		
	}
	
	protected void btnApplyActionPerformed(ActionEvent e) {
		
	}
	
	private JCheckBox getChckbxShowCoordonates() {
		if (chckbxShowCoordonates == null) {
			chckbxShowCoordonates = new JCheckBox("Show coordonates");
			chckbxShowCoordonates.setBounds(138, 101, 153, 23);
		}
		return chckbxShowCoordonates;
	}
	private JPanel getFontPanel() {
		if (fontPanel == null) {
			fontPanel = new JPanel();
			fontPanel.setBorder(new TitledBorder(null, "Fonts", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			fontPanel.setBounds(10, 11, 428, 121);
			fontPanel.setLayout(null);
			fontPanel.add(getLblFont());
			fontPanel.add(getLblFontdescription());
			fontPanel.add(getBtnChangeFont());
			fontPanel.add(getLblMenuFont());
			fontPanel.add(getFontComboBox());
			fontPanel.add(getChckbxFontAntialising());
		}
		return fontPanel;
	}
	private JPanel getBoardPanel() {
		if (boardPanel == null) {
			boardPanel = new JPanel();
			boardPanel.setBorder(new TitledBorder(null, "Board", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			boardPanel.setBounds(10, 140, 428, 242);
			boardPanel.setLayout(null);
			boardPanel.add(getPiecesComboBox());
			boardPanel.add(getLblPieces());
			boardPanel.add(getBtnChangeWhiteSquaresColor());
			boardPanel.add(getBtnChangeBlackSquaresColor());
			boardPanel.add(getChckbxShowCoordonates());
			boardPanel.add(getAutomovePanel());
		}
		return boardPanel;
	}
	private JPanel getAutomovePanel() {
		if (automovePanel == null) {
			automovePanel = new JPanel();
			automovePanel.setBorder(new TitledBorder(null, "Automove Speed", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			automovePanel.setBounds(49, 131, 343, 85);
			automovePanel.setLayout(null);
			automovePanel.add(getSpeedSlider());
			automovePanel.add(getLblSpeed());
		}
		return automovePanel;
	}
	private JCheckBox getChckbxFontAntialising() {
		if (chckbxFontAntialising == null) {
			chckbxFontAntialising = new JCheckBox("Font Antialising");
			chckbxFontAntialising.setBounds(102, 85, 153, 23);
		}
		return chckbxFontAntialising;
	}
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton("Close");
			btnClose.setMinimumSize(new Dimension(53, 23));
			btnClose.setMaximumSize(new Dimension(53, 23));
			btnClose.setPreferredSize(new Dimension(53, 23));
			btnClose.setBounds(335, 397, 75, 23);
		}
		return btnClose;
	}
}
