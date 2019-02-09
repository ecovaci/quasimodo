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
package org.chess.quasimodo.engine.model;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class Option implements Serializable {
	
    /**
	 * Serial Id.
	 */
	private static final long serialVersionUID = -1902544146242311386L;
	
	public static final String TYPE_CHECK    = "check";
	public static final String TYPE_STRING   = "string";
	public static final String TYPE_SPIN     = "spin";
	public static final String TYPE_BUTTON   = "button";
	public static final String TYPE_COMBO    = "combo";
	
	public static final String MULTIPV       = "MultiPV";
	public static final String ANALYSE_MODE  = "UCI_AnalyseMode";
	
	
	public String name;
    public String type;
    public String value;
    public String defaultValue;
    public String min;
    public String max;
    public String varLine = "";
    
    public boolean isTypeCheck () {
    	return TYPE_CHECK.equalsIgnoreCase(type);
    }
    
    public boolean isTypeString () {
    	return TYPE_STRING.equalsIgnoreCase(type);
    }
    
    public boolean isTypeButton () {
    	return TYPE_BUTTON.equalsIgnoreCase(type);
    }
    
    public boolean isTypeSpin () {
    	return TYPE_SPIN.equalsIgnoreCase(type);
    }
    
    public boolean isTypeCombo () {
    	return TYPE_COMBO.equalsIgnoreCase(type);
    }

    public boolean isModified () {
    	if (defaultValue == null) {
    		return StringUtils.hasLength(value);
    	} 
    	return !defaultValue.equals(value);
    }
    
	@Override
	public String toString() {
		return "Option [name=" + name + ", type=" + type + ", value=" + value
				+ ", defaultValue=" + defaultValue + ", min=" + min + ", max="
				+ max + ", varLine=" + varLine + "]";
	}
}
