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
package org.chess.quasimodo.config;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.service.EngineService;
import org.chess.quasimodo.service.PlayerService;
import org.chess.quasimodo.util.LocalIOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


@Component ("config")
@PropertySource(value = "file:${user.home}/.quasimodo/config.properties", name = "userProperties")
public class Config {

	@Value("${game.ponderMode}")
	private boolean ponderMode;
	
	@Value("${game.autoflag}")
	private boolean autoflag;

	@Value("${openingBook.filepath}")
	private Resource bookFile;
	
	@Value("file:${user.home}/.quasimodo/engines")
	private Resource enginesDirectory;
	
	@Value("file:${user.home}/.quasimodo/players")
	private Resource playersDirectory;
	
	@Value("file:${user.home}/.quasimodo/user.prefs")
	private Resource userPrefs;
	
	@Value("file:${user.home}/.quasimodo/database/eco.h2.db")
	private Resource ecoFile;
	
	@Value("file:${user.home}/.quasimodo/config.properties")
	private Resource configFile;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private EngineService engineService;
	
	private int defaultEngineThinkTime = 5000;
	
	private SetUpGameModel gameModel;
	
	/**
	 * Get selected game model.
	 * @return The selected game model.
	 */
	public SetUpGameModel getSelectedGameModel() {
		if (gameModel == null) {
			//creates a default friendly game
			gameModel = new SetUpGameModel();
			gameModel.setFriendly(true);
			List<HumanPlayerModel> humanPlayers = playerService.getRegisteredPlayers();
			gameModel.setWhitePlayerModel(humanPlayers.get(0));
			List<EngineModel> enginePlayers = engineService.getRegisteredEngines();
			if (!enginePlayers.isEmpty()) {
				//the black player will be engine
				gameModel.setBlackPlayerModel(enginePlayers.get(0));
			} else if (humanPlayers.size() > 1) {
				//no engine found, the black player will be another human player
				gameModel.setBlackPlayerModel(humanPlayers.get(1));
			} else {
				//worse case, plays against himself
				gameModel.setBlackPlayerModel(humanPlayers.get(0));
			}
		} else {
			//refresh models, might be changed in the meanwhile
			gameModel.setWhitePlayerModel(refreshPlayerModel(gameModel.getWhitePlayerModel()));
			gameModel.setBlackPlayerModel(refreshPlayerModel(gameModel.getBlackPlayerModel()));
		}
		return gameModel;
	}

	private PlayerModel refreshPlayerModel (PlayerModel playerModel) {
		if (playerModel.isEngine()) {
			return engineService.loadEngineModel(playerModel.getId());
		} else {
			return playerService.loadPlayerModel(playerModel.getId());
		}
	}
	
	public boolean hasBookfile () {
		if (bookFile != null) {
			return bookFile.exists();
		}
		return false;
	}
	
	public Resource getBookFile() {
		return bookFile;
	}

	public void setBookFile(Resource bookFile) {
		this.bookFile = bookFile;
	}
	
	public Resource getEnginesDirectory() {
		return enginesDirectory;
	}

	public Resource getPlayersDirectory() {
		return playersDirectory;
	}

	public Resource getUserPrefs() {
		return userPrefs;
	}

	/**
	 * Set selected game model.
	 * @param currentGameModel The selected game model.
	 */
	public void setSelectedGameModel(SetUpGameModel currentGameModel) {
		this.gameModel = currentGameModel;
	}
	
	public boolean isPonderMode() {
		return ponderMode;
	}

	public void setPonderMode(boolean ponderMode) {
		this.ponderMode = ponderMode;
	}

	public boolean isAutoflag() {
		return autoflag;
	}

	public void setAutoflag(boolean autoflag) {
		this.autoflag = autoflag;
	}

	public int getDefaultEngineThinkTime() {
		return defaultEngineThinkTime;
	}

	public void setDefaultEngineThinkTime(int defaultEngineThinkTime) {
		this.defaultEngineThinkTime = defaultEngineThinkTime;
	}

	public Resource getEcoFile() {
		return ecoFile;
	}

	public void store () throws IOException {
		Properties props = new Properties();
		LocalIOUtils.loadProperties(props, configFile.getFile());
		props.setProperty("game.ponderMode", String.valueOf(this.ponderMode));
		props.setProperty("game.autoflag", String.valueOf(this.autoflag));
		props.setProperty("openingBook.filepath", this.bookFile.getFile().getCanonicalPath());
		LocalIOUtils.storeProperties(props, configFile.getFile());
	}
}
