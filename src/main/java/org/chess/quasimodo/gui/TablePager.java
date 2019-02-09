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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.util.Assert;

@SuppressWarnings("serial")
public class TablePager extends JPanel {
	private JButton btnPrevious;
	private JButton btnNext;
    
	private ButtonGroup buttonGroup;
	
	private int firstPageIndex;
	private int pagesNumber;
	
	/**
	 * Create the panel.
	 */
	public TablePager(int totalRowNumber, int rowOnPage,int windowSize) {
		Assert.isTrue(totalRowNumber > 0, "totalRowNumber must be not null positive integer");
		Assert.isTrue(rowOnPage > 0, "rowOnPage must be not null positive integer");
		Assert.isTrue(windowSize > 0, "windowSize must be not null positive integer");
		
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		this.add(getPreviousButton());
		
		firstPageIndex = 1;
		pagesNumber = totalRowNumber / rowOnPage + (totalRowNumber % rowOnPage == 0 ? 0 : 1);
        
        buttonGroup = new ButtonGroup();
        for (int i = 0;i < Math.min(pagesNumber, windowSize);i++) {
            this.add(getPageButton(i + 1, buttonGroup));
        }
		this.add(getNextButton());
	}

	public int getFirstPageIndex() {
		return firstPageIndex;
	}

	public int getPagesNumber() {
		return pagesNumber;
	}

	private JButton getPreviousButton () {
		if (btnPrevious == null) {
			btnPrevious = new JButton("<");
			btnPrevious.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnPreviousActionPerformed(e);
				}
			});
			
			btnPrevious.setPreferredSize(new Dimension(20, 15));
			btnPrevious.setMargin(new Insets(0, 0, 0, 0));
		}
		return btnPrevious;
	}
	
	private JButton getNextButton () {
		if (btnNext == null) {
		    btnNext = new JButton(">");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnNextActionPerformed(e);
				}
			});
			btnNext.setPreferredSize(new Dimension(20, 15));
			btnNext.setMargin(new Insets(0, 0, 0, 0));
		}
		return btnNext;
	}
	
	private JToggleButton getPageButton (int pageIndex, ButtonGroup buttonGroup) {
		JToggleButton toggleButton = new JToggleButton("" + pageIndex);
		toggleButton.setMargin(new Insets(0, 0, 0, 0));
		toggleButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				toggleButtonStateChanged(e);
			}
		});
		if (pageIndex == 1) {
			toggleButton.setSelected(true);
		}
		buttonGroup.add(toggleButton);
		return toggleButton;
	}
	
	private void toggleButtonStateChanged(ChangeEvent e) {
		JToggleButton source = (JToggleButton)e.getSource();
		if (source.isSelected()) {
			if (source.getText().equals("1")) {
				btnPrevious.setVisible(false);
			}
			if (source.getText().equals("" + pagesNumber)) {
				btnNext.setVisible(false);
			} 
            if (!source.getText().equals("1") && !source.getText().equals("" + pagesNumber))  {
            	if (!btnPrevious.isVisible()) { 
				    btnPrevious.setVisible(true);
            	}
            	if (!btnNext.isVisible()) { 
				    btnNext.setVisible(true);
            	}
			}
		}
	}
	
	private void btnPreviousActionPerformed(ActionEvent e) {
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		AbstractButton previous = null;
		AbstractButton next;
		while (buttons.hasMoreElements()) {
			next = buttons.nextElement();
			if(next.isSelected()) {
				if (previous != null) {
				    previous.setSelected(true);
				} else if (!next.getText().equals("1")) {
					firstPageIndex --;
					reindexPages(next);
				}
				break;
			} else {
				previous = next;
			}
		}
	}
	
	private void btnNextActionPerformed(ActionEvent e) {
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		AbstractButton next;
		while (buttons.hasMoreElements()) {
			next = buttons.nextElement();
			if(next.isSelected()) {
				if (buttons.hasMoreElements()) {
					buttons.nextElement().setSelected(true);
				} else if (!next.getText().equals("" + pagesNumber)) {
					firstPageIndex ++;
					reindexPages(next);
				}
				break;
			}
		}
	}
	
	private void reindexPages (AbstractButton button) {
		button.setSelected(false);
		assignPageButtons();
		button.doClick();
	}
	
	private void assignPageButtons () {
		int index = firstPageIndex;
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		while (buttons.hasMoreElements()) {
			buttons.nextElement().setText("" + (index++));
		}
	}
	
}
