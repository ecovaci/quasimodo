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
package org.chess.quasimodo.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.SerializationUtils;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.domain.PlayerModel;
import org.chess.quasimodo.engine.uci.EngineHandler;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.event.EngineOutputOptionsListener;
import org.chess.quasimodo.util.LocalIOUtils;
import org.chess.quasimodo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component ("engineManager")
public class EngineManager {
    private Logger logger = LoggerFactory.getLogger(EngineManager.class);
    
    @Autowired
    private  Config config;
	
    public PlayerModel createEngine (String pathname) throws EngineException, IOException {//FIXME - check identical
    	EngineModel model = fetchEngineModel(pathname);
    	logger.info("Now serialize model: " + model);
    	serializeEngineModel (model);
    	return model;
    }
    
    public void saveEngineOptions (EngineModel model) throws EngineException {
    	logger.info("Serialize model: " + model);
    	serializeEngineModel (model);
    }
    
    public List<EngineModel> getRegisteredEngines () throws EngineException, IOException {
		List<EngineModel> engines = new ArrayList<EngineModel>();
		File engineDir = config.getEnginesDirectory().getFile();
		for (File file:engineDir.listFiles()) {
			if (Utils.isPositiveInteger(file.getName())) {
				engines.add(loadEngineModel (file));
			}
		}
		return engines;
	}
    
    public void deleteEngine (PlayerModel model) throws EngineException {
    	try {
			File engineFile = new File(config.getEnginesDirectory().getFile() + File.separator + model.getId());
			if (engineFile.exists()) {
				engineFile.delete();
			}
		} catch (Exception e) {
			throw new EngineException("Cannot delete engine", e);
		}
    }
    
    @PostConstruct
    public void prepareEngineDirectory () throws EngineException {
    	try {
			File engineDir = config.getEnginesDirectory().getFile();
			logger.info("Check engine directory existence");
			if (engineDir.exists()) {
				logger.info("Engine directory already created");
			} else {
				engineDir.mkdir();
				logger.info("Engine directory has been created");
			}
		} catch (Exception e) {
			throw new EngineException("Failed to create engines directory", e);
		}
    }
    
    private EngineModel fetchEngineModel (String pathname) throws EngineException, IOException {
    	EngineOutputOptionsListener optionsListener = new EngineOutputOptionsListener();
    	EngineHandler engineHandler = null;
		try {
			engineHandler = new EngineHandler(pathname);
			engineHandler.start();
			engineHandler.uci(optionsListener);
		} finally {
			if (engineHandler != null) {
				try {
					engineHandler.quit();
				} catch (Exception e) {
					logger.warn("Failed to close engine [" + engineHandler.getEngineFilepath() + "]", e);
				}
			}
		}
    	EngineModel model = new EngineModel();
    	model.setId(LocalIOUtils.getNextEngineId(config.getEnginesDirectory().getFile()));
    	model.setIdOptions(optionsListener.getIdOptions());
    	model.setPathname(pathname);
    	return model;
    }
    
    private void serializeEngineModel (EngineModel model) throws EngineException {
    	try {
			SerializationUtils.serialize(model, 
					new FileOutputStream(
							config.getEnginesDirectory().getFile() + File.separator 
							+ model.getId()));
		} catch (Exception e) {
			throw new EngineException("Failed to serialize the engine's model", e);
		}
    }
    
    public EngineModel loadEngineModel (File engineFile) throws EngineException {
    	try {
			return (EngineModel)SerializationUtils.deserialize(new FileInputStream (engineFile));
		} catch (Exception e) {
			throw new EngineException("Failed to load engine's model for " + engineFile, e);
		}
    }
}
