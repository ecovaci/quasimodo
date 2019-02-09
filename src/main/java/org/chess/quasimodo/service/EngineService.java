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

import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.engine.EngineManager;
import org.chess.quasimodo.errors.AppException;
import org.chess.quasimodo.gui.model.SelectFileDialogModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


@Service ("engineService")
public class EngineService {
    private Logger logger = LoggerFactory.getLogger(EngineService.class);
  
    @Autowired
    private EngineManager engineManager;
    
    @Autowired
    private  Config config;
    
	public synchronized void registerNewEngine (SelectFileDialogModel engineModel) {
		try {
			engineManager.createEngine(engineModel.getPathname());
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("There is something wrong with this engine!" ,e);
		}
	}
	
	public synchronized void saveEngineOptions (EngineModel engineModel) {
		try {
			engineManager.saveEngineOptions(engineModel);
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("Cannot save options for this engine!" ,e);
		}
	}
	
	public synchronized List<EngineModel> getRegisteredEngines () {
		try {
			return engineManager.getRegisteredEngines();
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("Cannot load registered engines!", e);
		}
	}
	
	public synchronized void deleteEngine (PlayerModel model) {
		try {
			engineManager.deleteEngine(model);
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("Cannot delete engine!", e);
		}
	}
	
	public synchronized EngineModel loadEngineModel (int id) {
		try {
			return engineManager.loadEngineModel(new File (config.getEnginesDirectory().getFile() + File.separator + id));
		} catch (Exception e) {
			logger.error("", e);
			throw new AppException ("Cannot load registered engine model with id [" + id + "]!", e);
		}
	}
}
