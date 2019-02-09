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
package org.chess.quasimodo.service;

import java.io.File;
import java.util.List;

import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.domain.logic.PlayerManager;
import org.chess.quasimodo.errors.AppException;
import org.chess.quasimodo.util.LocalIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service ("playerService")
public class PlayerService {
	private Logger logger = LoggerFactory.getLogger(PlayerService.class);
	
	@Autowired
	private PlayerManager playerManager;
	
	@Autowired
	private Config config;
	
	public void registerNewPlayer (HumanPlayerModel model) {
		try {
			playerManager.registerPlayer(model);
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("There is something wrong with this player!", e);
		}
	}
	
	public void deletePlayer (PlayerModel model) {
		try {
			playerManager.deletePlayer(model);
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("Cannot delete player!", e);
		}
	}
	
	public List<HumanPlayerModel> getRegisteredPlayers () {
		try {
			return playerManager.getRegisteredHumanPlayers();
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("Cannot load registered players!", e);
		}
	}
	
	public HumanPlayerModel loadPlayerModel (int id) {
		try {
			return playerManager.loadHumanPlayerModel(
					new File (config.getPlayersDirectory().getFile() + File.separator + id));
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("Cannot load registered human player with id [" + id + "]!", e);
		}
	}
}
