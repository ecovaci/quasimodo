package org.chess.quasimodo.eco;

import java.util.List;

import org.chess.quasimodo.database.OpeningDao;
import org.chess.quasimodo.pgn.domain.Opening;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("openingScout")
@Scope ("prototype")
public class OpeningScout {
	private Logger logger = LoggerFactory.getLogger(OpeningScout.class);
	
	@Autowired
	private OpeningDao openingDao;
	
    private Opening opening;
    
    private boolean active = true;
    
    public void search (String moves) {
    	logger.debug("About to search for moves " + moves);
    	Opening opening = openingDao.getOpening(moves);
    	if (opening != null) {
    		this.opening = opening;
    	}
    	List<Opening> openingList = openingDao.getOpeningList(moves);
    	if (openingList.isEmpty()) {
    		active = false;
    	}
    	logger.debug("Found: " + openingList.size());
    }

    public boolean hasOpening () {
    	return this.opening != null;
    }
    
	public Opening getOpening() {
		return this.opening;
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public String toString() {
		return "OpeningScout [opening=" + opening + ", active=" + active + "]";
	}
	
}
