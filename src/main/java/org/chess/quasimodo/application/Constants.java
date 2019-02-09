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
package org.chess.quasimodo.application;

/**
 * Some useful constants.
 * @author Eugen Covaci
 *
 */
public interface Constants {
	//------------------------ TODO remove ----------------------------
	/**
	 * Application directory in user's home directory.
	 *//*
    String APP_DIRECTORY_NAME = ".quasimodo";
    
    *//**
     * Engines directory name.
     *//*
    String ENGINES_DIRECTORY_NAME = "engines";
    
    *//**
     * Players directory name.
     *//*
    String PLAYERS_DIRECTORY_NAME = "players";
    
    String ENGINES_DESCRIPTION_FILENAME = "engines.properties";*/
    
    String ECO_DATABASE_FILENAME = "eco_db.h2.db";
    
    String USER_PREFS_COMMENT = "Quasimodo - preserve window layout.";
    
    String FRAME_BOUNDS = "frame.bounds";
    String FRAME_MAXIMIZED = "frame.maximized";
    String FRAME_VERTICAL_SPLIT = "frame.verticalSplit.location";
    String FRAME_HORIZONTAL_SPLIT = "frame.horizontalSplit.location";

    //-------------------------------------------------------------------
    
    int NORMAL_EXIT_CODE         = 0;
    int ERR_CODE_STARTUP_APP     = 1;
    int ERR_CODE_STARTUP_SPRING  = 2;
    int ERR_CODE_ECO             = 3;
    int ERR_CODE_STARTUP_GUI     = 4;
    int ERR_CODE_RUNTIME         = 5;
}
