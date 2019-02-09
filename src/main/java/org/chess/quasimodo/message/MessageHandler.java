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
package org.chess.quasimodo.message;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;

import org.chess.quasimodo.annotation.Design;
import org.chess.quasimodo.config.design.Designable;
import org.chess.quasimodo.config.design.Designer;
import org.chess.quasimodo.gui.MainFrame;
import org.chess.quasimodo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.AbstractErrors;
import org.springframework.validation.ObjectError;


@org.springframework.stereotype.Component("messageHandler")
public class MessageHandler implements Designable {
	public static final int ERROR_TRACE_DEPTH = 10;
	
	@Autowired
	private MainFrame frame;
	
	private Font font;
	
	private Integer rowLength = 200;
	
	@Design (key="message.font")
	public Font getFont() {
		return font;
	}

	@Design (key="message.font")
	public void setFont(Font font) {
		this.font = font;
	}

	@Design(key="message.row.length")
	public Integer getRowLength() {
		return rowLength;
	}

	@Design(key="message.row.length")
	public void setRowLength(Integer rowLength) {
		this.rowLength = rowLength;
	}

	@Autowired
	private Designer designer;
	
	@PostConstruct
	public void injectDesign () {
		designer.injectDesign(this);
	}

	public void showErrorMessages (String ... messages) {
    	showMessage(null, "Error", JOptionPane.ERROR_MESSAGE, messages);
    }
	
	public void showErrorFullTrace (String message, Throwable t) {
		showErrorFullTrace(null, message, t);
    }
	
	public void showErrorFullTrace (Throwable t) {
		showErrorFullTrace(null, null, t);
    }
	
	public void showErrorFullTrace (Component parent,String message, Throwable t) {
		List<String> messages = new ArrayList<String>();
		if (StringUtils.hasLength(message)) {
		    messages.add(message);
		}
		Throwable cause = t;
		while (cause != null) {
			if (cause.getMessage() != null) {
			    messages.add(cause.getMessage());
			}
			cause = cause.getCause();
		}
    	showMessage(parent, "Error", JOptionPane.ERROR_MESSAGE, messages.toArray(new String[messages.size()]));
    }
	
	public void showErrorMessage (Throwable t) {
		showErrorMessage(null, null, t);
    }
	
	public void showErrorMessage (String message, Throwable t) {
		showErrorMessage(null, message, t);
    }
	
	public void showErrorMessage (Component parent,String message, Throwable t) {
    	showMessage(parent, "Error", JOptionPane.ERROR_MESSAGE, message, "Cause: " + (StringUtils.hasLength(t.getMessage()) ? t.getMessage() : "N/A"));
    }
	
    public void showErrorMessages (Component parent,String ... messages) {
    	showMessage(parent, "Error", JOptionPane.ERROR_MESSAGE, messages);
    }
    
    public void showErrorMessages (Component parent,AbstractErrors result) {
    	System.out.println("message.row.length " + rowLength);
    	String[] messages = new String[result.getErrorCount()];
    	int i = 0;
    	for (ObjectError error : result.getAllErrors()) {
    		messages[i++] = error.getCode();
    	}
    	showErrorMessages(parent, messages);
    }
    
    public void showInfoMessage (String ... messages) {
    	showInfoMessage(null, messages);
    }
    
    public void showInfoMessage (Component parent,String ... messages) {
    	showMessage(parent, "Info", JOptionPane.INFORMATION_MESSAGE, messages);
    }
    
    public void showMessage (Component parent, String title, int type, String ... messages) {
    	JOptionPane.showMessageDialog(parent != null ? parent : JOptionPane.getRootFrame(), 
    			Utils.createWrappedLabel(font, rowLength, messages), title, type);
    }
    
    public int showConfirm (Component parent, String title, int type, String ... messages) {
    	return JOptionPane.showConfirmDialog(parent != null ? parent : JOptionPane.getRootFrame(), 
    			Utils.createWrappedLabel(font, rowLength, messages), title, type);
    }
    
    public int showConfirm (String ... messages) {
    	return showConfirm(null, "Confirmation", JOptionPane.ERROR_MESSAGE, messages);
    }
    
    public int showConfirm (Component parent,String ... messages) {
    	return showConfirm(parent, "Confirmation", JOptionPane.ERROR_MESSAGE, messages);
    }
    
    public int showWarningConfirm (Component parent,String ... messages) {
    	return showConfirm(parent, "Warning", JOptionPane.WARNING_MESSAGE, messages);
    }
    
    public void showWarningMessage (String ... messages) {
    	showMessage(null, "Warning", JOptionPane.WARNING_MESSAGE, messages);
    }
    
    public void showWarningMessage (Component parent,String ... messages) {
    	showMessage(parent, "Warning", JOptionPane.WARNING_MESSAGE, messages);
    }

    public static void showErrorTrace (String message, Throwable t) {
		List<String> messages = new ArrayList<String>();
		messages.add(message);
		Throwable cause = t;
		int depth = 0;
		while (cause != null) {
			if (StringUtils.hasLength(cause.getMessage())) {
				if ( ++depth > ERROR_TRACE_DEPTH ) {
					messages.add("To see the entire error trace, check the application's log file.");
					break;
				}
			    messages.add("Cause: " + cause.getMessage());
			   
			}
			cause = cause.getCause();
		}
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), 
				Utils.createWrappedLabel(200, 
						messages.toArray(new String[messages.size()])),
				                       "Fatal Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showStatusMessage (String message) {
    	frame.printStatusMessage(message);
    }
    
	@Override
	public void refreshDesign() {
		//does nothing here
	}
    
}
