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

public interface Message {
    String ERROR_RESTORING_FRAME        = "Error restoring window state";
    String ERROR_FATAL                  = "A fatal error has occured with message: {0}, check the log file for details."; 
    String CHECK_LOG                    = "Check the log file for details.";
    String CLOSE_APP                    = "Application will be closed!";
    String ERROR_ENGINE                 = "There is something wrong with the engine";
    
    String WARNING_INVALID_EP           = "Invalid En passant";                           
    
    String ASK_PROCEED                  = "Still proceed?";
    
    String READ_LOG                     = "Check the application log for details.";
    
    String CLOSE_EXISTENT_GAME          = "A game is currently playing. Do you want to abort it?";
    
    /********* Draw messages ***********/
    
    /**
     * Draw by fifty rule message.
     */
    String DRAW_BY_FIFTY = "Draw by fifty rule";
    
    /**
     * Draw by insufficient material on board message.
     */
    String DRAW_BY_MATERIAL = "Draw by insufficient material on board";
    
    /**
     * Draw by stalemate message.
     */
    String DRAW_STALEMATE = "Draw by stalemate";
    
    /**
     * Draw by threefold repetition message.
     */
    String DRAW_THREEFOLD_REPETITION = "Draw by threefold repetition";
    
    /**
     * Draw by agreement message.
     */
    String DRAW_BY_AGREEMENT = "Draw by agreement";
    
    /**
     * Game aborted
     */
    String ABORTED = "Aborted";
    
    /***********************************/
    
    String WIN = "Result: {0} wins.";
    String RESIGN = "Result: {0} resigns.";
    String TIMEOUT = "Result: {0} wins by timeout.";
    String DRAW = "Result: Draw.";
}
