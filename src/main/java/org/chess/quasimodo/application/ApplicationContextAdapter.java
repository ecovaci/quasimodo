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

import org.chess.quasimodo.domain.logic.Game;
import org.chess.quasimodo.domain.logic.MoveSource;
import org.chess.quasimodo.engine.Engine;
import org.chess.quasimodo.gui.BoardPanel;
import org.chess.quasimodo.gui.EditHumanPlayerDialog;
import org.chess.quasimodo.gui.EnginePropertiesDialog;
import org.chess.quasimodo.gui.MainFrame;
import org.chess.quasimodo.gui.ManageEnginesDialog;
import org.chess.quasimodo.gui.ManageHumanPlayersDialog;
import org.chess.quasimodo.gui.NewEngineDialog;
import org.chess.quasimodo.gui.PromotionDialog;
import org.chess.quasimodo.gui.SetUpGameDialog;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component ("contextAdapter")
public class ApplicationContextAdapter implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Game createGame () {
		return applicationContext.getBean("game", Game.class);
	}
	
	public Engine createEngine () {
		return applicationContext.getBean("engine", Engine.class);
	}
	
	public MoveSource getBoardPanel() {
		return applicationContext.getBean("boardPanel", BoardPanel.class);
	}
	
	public SetUpGameDialog getSetUpGameView() {
		return applicationContext.getBean("setupGameView", SetUpGameDialog.class);
	}
	
	public ManageEnginesDialog getManageEnginesView () {
		return applicationContext.getBean("manageEnginesView", ManageEnginesDialog.class);
	}
	
	public NewEngineDialog getNewEngineView () {
		return applicationContext.getBean("newEngineView", NewEngineDialog.class);
	}
	
	public EnginePropertiesDialog getEnginePropertiesDialog () {
		return applicationContext.getBean("enginePropertiesDialog", EnginePropertiesDialog.class);
	}
	
	public ManageHumanPlayersDialog getManagePlayersView () {
		return applicationContext.getBean("managePlayersView", ManageHumanPlayersDialog.class);
	}
	
	public EditHumanPlayerDialog getNewPlayerDialog () {
		return applicationContext.getBean("newPlayerDialog", EditHumanPlayerDialog.class);
	}
	
	public PromotionDialog getWhitePromotionDialog () {
		return applicationContext.getBean("whitePromotionDialog", PromotionDialog.class);
	}
	
	public PromotionDialog getBlackPromotionDialog () {
		return applicationContext.getBean("blackPromotionDialog", PromotionDialog.class);
	}
	
	public MainFrame getMainFrame () {
		return applicationContext.getBean("frame", MainFrame.class);
	}
}
