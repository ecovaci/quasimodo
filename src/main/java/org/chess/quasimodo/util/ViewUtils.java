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
package org.chess.quasimodo.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;

import org.springframework.util.Assert;

/*import uic.widgets.ColorSelection;
import uic.widgets.FontSelection;
import uic.widgets.StandardDialog;
import uic.widgets.calendar.UICDatePicker;*/

public class ViewUtils {
/*	*//**
	 * Opens a dialog with font chooser capabilities.<br>
	 * <b>WARNING:</b> This method has to be called from within
	 * event-dispatching thread.
	 * 
	 * @param owner
	 *            The chooser's dialog owner (might be null).
	 * @param initialFont
	 *            The font to be proposed first (might be null).
	 * @return The user's selected font or null if no selection has been made.
	 *//*
	public static Font showFontChooser(Window owner, Font initialFont) {
		FontSelection fontSelection = new FontSelection();
		fontSelection.setFont(initialFont);
		StandardDialog dialog = new StandardDialog(owner, "Select Font", true,
				StandardDialog.OK_CANCEL);
		dialog.setComponent(fontSelection);
		if (dialog.show() == StandardDialog.BUTTON_OK) {
			return fontSelection.getFont();
		} else {
			return null;
		}
	}

	*//**
	 * Opens a dialog with color chooser capabilities.<br>
	 * <b>WARNING:</b> This method has to be called from within
	 * event-dispatching thread.
	 * 
	 * @param owner
	 *            The chooser's dialog owner (might be null).
	 * @param initialColor
	 *            The color to be proposed first (might be null).
	 * @return The user's selected color or null if no selection has been made.
	 *//*
	public static Color showColorChooser(Window owner, Color initialColor) {
		ColorSelection colorSelection = new ColorSelection();
		colorSelection.setColor(initialColor);
		StandardDialog dialog = new StandardDialog(owner, "Select Color", true,
				StandardDialog.OK_CANCEL);
		dialog.setComponent(colorSelection);
		if (dialog.show() == StandardDialog.BUTTON_OK) {
			return colorSelection.getColor();
		} else {
			return null;
		}
	}*/

/*	public static Date showDateChooser(Window owner, Date initialDate,
			boolean modal) {
		return showDateChooser(owner, initialDate, modal, null, new Dimension (
				330, 250));
	}
*/
/*	public static Date showDateChooser(Window owner, Date initialDate,
			boolean modal, Point location, Dimension size) {
		JDialog frame = new JDialog(owner);
		frame.setModal(modal);
		if (location != null) {
			frame.setLocation(location);
		} else {
			frame.setLocationRelativeTo(null);
		}

		frame.setSize(size);
		UICDatePicker datePicker = new UICDatePicker();
		Calendar calendar = Calendar.getInstance();
		if (initialDate != null) {
			calendar.setTime(initialDate);
			datePicker.setYear(calendar.get(Calendar.YEAR));
			datePicker.setMonth(calendar.get(Calendar.MONTH));
			datePicker.setDate(calendar.get(Calendar.DAY_OF_MONTH));
		}
		frame.getContentPane().add(datePicker);
		frame.setVisible(true);
		
		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDate());
		return calendar.getTime();
	}*/

	public static Component getComponent (Container container, String name) {
		Assert.notNull(name, "Name cannot be null");
		for (Component component:container.getComponents()) {
			if (name.equals(component.getName())) {
				return component;
			}
		}
		return null;
	}
}
