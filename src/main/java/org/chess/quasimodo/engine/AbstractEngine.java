package org.chess.quasimodo.engine;

import java.io.FileNotFoundException;

import org.chess.quasimodo.book.OpeningBook;
import org.chess.quasimodo.book.PolyglotBook;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.engine.model.Option;
import org.chess.quasimodo.engine.uci.EngineHandler;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.event.EngineOutputAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public abstract class AbstractEngine {
	@Autowired
	private EngineRepository engineRepository;
	
	private EngineModel model;
	
	private EngineHandler engineHandler;

	private OpeningBook openingBook;

	protected EngineHandler getEngineHandler () {
		return this.engineHandler;
	}
	
	protected OpeningBook getOpeningBook() {
		return this.openingBook;
	}

	public boolean hasOpeningBook () {
		return this.openingBook != null;
	}
	
	/**
	 * Close engine and remove it from repository.
	 * @throws EngineException
	 */
	public void remove() throws EngineException {
		engineHandler.quit();
		engineRepository.removeEngine(this);
	}
	
	public void bootstrap(EngineModel model) throws EngineException {
		engineHandler = new EngineHandler(model.getPathname());
		engineHandler.start ();
		engineHandler.uci();
		engineHandler.isReady();
		// set the modified options, compared to default values
		for (Option option : model.getIdOptions().getOptions()) {
			if (option.isModified()) {
				engineHandler.setOption(option);
			}
		}
		// open openingBook file (if any)
		if (StringUtils.hasLength(model.getBookFilePath())) {
		    try {
				openingBook = new PolyglotBook(model.getBookFilePath());
			} catch (FileNotFoundException e) {
				throw new EngineException ("Book file not found", e);
			}
		}
		this.model = model;
	}

	public void endCommand() {
		engineHandler.stopCommandAndListen();
	}

	public String getEngineFilepath() {
		return engineHandler != null ? engineHandler.getEngineFilepath() : null;
	}

	public Integer getId() {
		return model != null ? model.getId() : null;
	}
	
	public abstract void registerEngineOutputListener(EngineOutputAware engineOutputAware);
}