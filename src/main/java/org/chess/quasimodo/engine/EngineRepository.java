package org.chess.quasimodo.engine;

import java.util.ArrayList;
import java.util.List;

import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component ("engineRepository")
public class EngineRepository {
	private final Logger logger = LoggerFactory.getLogger(EngineRepository.class);
	
	@Autowired
	private ApplicationContextAdapter contextAdapter;
	
    private List<Engine> engineList = new ArrayList<Engine>();
    
    public Engine createEngine () {
    	Engine engine = contextAdapter.createEngine();
    	engineList.add(engine);
    	return engine;
    }
    
    public AbstractEngine getEngineById (Integer id) {
    	for (AbstractEngine engine:engineList) {
    		if (engine.getId().equals(id)) {
    			return engine;
    		}
    	}
    	return null;
    }
    
    public void removeEngine (AbstractEngine engine) {
    	engineList.remove(engine);
    }
    
    public void closeEngines () {
    	for (AbstractEngine engine:engineList) {
    		try {
				engine.getEngineHandler().quit();
			} catch (Exception e) {
				logger.error("Failed to close engine [" + engine.getEngineFilepath() + "]", e);
			}
    	}
    }
}
