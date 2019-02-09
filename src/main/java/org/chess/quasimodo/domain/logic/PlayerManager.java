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
package org.chess.quasimodo.domain.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.SerializationUtils;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.HumanPlayerModel;
import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.errors.PlayerException;
import org.chess.quasimodo.util.LocalIOUtils;
import org.chess.quasimodo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component ("playerManager")
public class PlayerManager {
	private Logger logger = LoggerFactory.getLogger(PlayerManager.class);
	
	@Autowired
	private Config config;
	
	@PostConstruct
    public void preparePlayerDirectory () throws PlayerException {
    	try {
			File playerDir = config.getPlayersDirectory().getFile();
			logger.info("Check player directory existence");
			if (playerDir.exists()) {
				logger.info("Player directory already created");
			} else {
				playerDir.mkdir();
				logger.info("Player directory has been created");
			}
		} catch (Exception e) {
			throw new PlayerException("Failed to create players' directory", e);
		}
    }
	
    public void registerPlayer (HumanPlayerModel model) throws PlayerException, IOException {
    	if (model.getId() == null) {
    	    model.setId(LocalIOUtils.getNextPlayerId(config.getPlayersDirectory().getFile()));
    	}
    	try {
			SerializationUtils.serialize(model, 
					new FileOutputStream(config.getPlayersDirectory().getFile() + File.separator + model.getId()));
		} catch (Exception e) {
			throw new PlayerException (e);
		}
    }
    
    public void deletePlayer (PlayerModel model) throws PlayerException {
    	try {
			File engineFile = new File(config.getPlayersDirectory().getFile() + File.separator + model.getId());
			if (engineFile.exists()) {
				engineFile.delete();
			}
		} catch (Exception e) {
			throw new PlayerException("Cannot delete player", e);
		}
    }
    
    public List<HumanPlayerModel> getRegisteredHumanPlayers () throws PlayerException {
    	List<HumanPlayerModel> players = new ArrayList<HumanPlayerModel>();
    	try {
			File playersDir = config.getPlayersDirectory().getFile();
			for (File file:playersDir.listFiles()) {
				if (Utils.isPositiveInteger(file.getName())) {
					players.add(loadHumanPlayerModel (file));
				}
			}
		} catch (Exception e) {
			throw new PlayerException("Failed to get registered players", e);
		} 
    	return players;
    }
    
    public HumanPlayerModel loadHumanPlayerModel (File location) throws FileNotFoundException {
    	return (HumanPlayerModel)SerializationUtils.deserialize(new FileInputStream (location));
    }
}
