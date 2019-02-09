package org.chess.quasimodo.analyzer;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.chess.quasimodo.book.OpeningBook;
import org.chess.quasimodo.book.PolyglotBook;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.domain.logic.Definitions;
import org.chess.quasimodo.domain.logic.Position;
import org.chess.quasimodo.eco.OpeningScout;
import org.chess.quasimodo.engine.AnalyzerEngine;
import org.chess.quasimodo.engine.AnalyzerEngine.AnalyzerOutput;
import org.chess.quasimodo.errors.InvalidPGNException;
import org.chess.quasimodo.pgn.domain.PGNEntitySequence;
import org.chess.quasimodo.pgn.domain.PGNGame;
import org.chess.quasimodo.pgn.domain.PGNGame.TagType;
import org.chess.quasimodo.pgn.domain.PGNMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Chess game analyser, as follows:
 * <ul>
 * <li>
 * <b>STEP 1</b><br>
 * Find last openingBook's move by searching the first positions in openingBook until no
 * results are found.</li>
 * <li>
 * <b>STEP 2</b><br>
 * Analyze last position. First, ask engine to search for mate. If something is
 * found, the last move is marked accordingly (mate in x moves); else, the
 * position is analyzed according to STEP 3 bellow.</li>
 * <li>
 * <b>STEP 3</b><br>
 * Analyze each move, backwardly, until the last openingBook's move is reached, following
 * the rules: <br>
 * 1. Ask engine to search the best move for the position that occurs before the
 * current move has been made. If there is difference between the first move
 * and the current move (compared against a threshold), the mainline and the
 * engine’s variations will become variations for the current move. If a
 * variation is inferior to the mainline retrieved from engine (modulo a
 * threshold), it will be ignored. Then, the current position gets evaluated by
 * the engine. <br>
 * 2. A move is marked (ex +, -, +=, ??, ?) when evaluation compared to the last
 * move changes (modulo a threshold). <br>
 * 3. A move can be commented (free text) with words like “Better is” “Black is
 * lost anyway” “Equally better is”.</li>
 * </ul>
 * 
 * @author Eugen Covaci
 * 
 */
@Component("simpleAnalyzer")
@Scope("prototype")
public class SimpleAnalyzer implements Analyzer {
	
	private final Logger logger = LoggerFactory.getLogger(SimpleAnalyzer.class);
	
	/**
	 * Start time.
	 */
	private Date startTime;

	/**
	 * End time.
	 */
	private Date endTime;
    
	/**
	 * The annotated game.
	 */
	private PGNGame analysisResult = new PGNGame();
	
	/**
	 * Signals if errors were encountred.
	 */
	private boolean hasErrors;
	
	/**
	 * Monitors execution progress.
	 */
	private int progress;
	
	@Autowired
	private Config config;
	
	@Autowired
	private OpeningScout openingScout;
	
	@Autowired
	private AnalyzerEngine analyzerEngine;
	
	@Override
	public void analyzeGame(PGNGame game, EngineModel engineModel, int multipv, 
			int threshold) {
		logger.info("About to start analyzing thread ...");
	    //new Thread(new AnalyzeRunnable(game, engineModel, multipv, threshold)).start();
		new AnalyzeRunnable(game, engineModel, multipv, threshold).run();
	    logger.info("Analysis thread started");
	}
    
	/**
	 * Does the actual job.
	 * @throws Exception
	 */
	private void doAnalyze (PGNGame game, EngineModel model, int multipv, int threshold) throws Exception {
		Assert.notNull(game, "game cannot be null");
		Assert.notNull(model, "model cannot be null");
		List<PGNMove> moveList = game.getMoveList();
		if (moveList.isEmpty()) {
			logger.info("Nothing to analyze: no move found!");
			return;
		}
		Position position = new Position ();
		position.load(game.tagValue(TagType.FEN));
		
		/*if (position.isInitial()) {
			checkOpening(game);
		}*/
		
		logger.info("Check for book moves");
		if (config.hasBookfile()) {
			searchForBookMoves(game, position.clone());
		}
		
		logger.info("Now start the engine");
		analyzerEngine.bootstrap(model);
		
		//switch to analyze mode
		analyzerEngine.analyzeMode();
		
		for (int i = 0;i < game.getLastBookMoveIndex();i++) {
			position.makeSANMove(moveList.get(i).getContent());
		}
		AnalyzerOutput output;
		AnalyzerOutput lastOutput = analyzerEngine.analyze(position.exportFEN(), 5000);
		for (int i = game.getLastBookMoveIndex();i < moveList.size() - 1;i++) {
			logger.debug("analyze move: " + moveList.get(i));
			logger.debug("lastOutput: " + lastOutput);
			position.makeSANMove(moveList.get(i).getContent());
			output =  analyzerEngine.analyze(position.exportFEN(), 5000);
			logger.debug("output: " + output);
			if (moveList.get(i).equals(position.toSAN(lastOutput.getUCIBestMove()))) {
				logger.info("!!!! Best move: " + moveList.get(i));
			} else {
				if (lastOutput.isMateFound()) {
					int lastMate = lastOutput.getPVMate();
					if (output.isMateFound()) {
						int mate = output.getPVMate();
						if (lastMate * mate > 0) {
							logger.info("?? Missed mate in " + lastMate + ", now he is matted");
						} else if (mate > -lastMate - 1) {
							logger.info("There is a shorter path, mate in: " + lastMate);
						} else {
							
						}
					} else {
						logger.info("?? Missed mate in " + lastMate);
					}
				} else if (output.isMateFound()) {
					
				} else if (Math.abs(lastOutput.getPVCp() + output.getPVCp()) > threshold) {
					logger.info("There is a better move here " + lastOutput.getBestMove().move);
				} else {
					
				}
			}
			lastOutput = output;
		}
		
	}
	
	private void checkOpening (PGNGame game) {
		int index = 0;
		StringBuffer textMoves = new StringBuffer();
		for (PGNMove move : game.getMoveList()) {
			logger.debug("textMoves for opening: " + textMoves);
			if (index > 0) {
				textMoves.append(" ");
			}
			if (++index % 2 == 1) {
				textMoves.append(index + ".");
			}
			textMoves.append(move.getContent());
			openingScout.search(textMoves.toString());
			if (!openingScout.isActive()) {
				if (openingScout.hasOpening()) {
					analysisResult.putTag(TagType.FEN, openingScout.getOpening().getCode());
				} else {
					analysisResult.putTag(TagType.FEN, Definitions.IRREGULAR_OPENING_CODE);
				}
				break;
			}
		}
	}
	
	/**
	 * Search for opening openingBook moves and check move's validity.
	 * @param game
	 * @param position Start position.
	 * @throws IOException
	 * @throws InvalidPGNException 
	 */
	private void searchForBookMoves (PGNGame game,Position position) throws IOException {
		OpeningBook book = new PolyglotBook(config.getBookFile().getFile().getAbsolutePath());
		for (PGNMove move : game.getMoveList()) {
			if (position.makeSANMove(move.getContent())) {
				if(book.search(position.zobristHashKey()).size() > 0) {
					analysisResult.incrementLastBookMoveIndex();
				} else {
					break;
				}
			} else {
				throw new InvalidPGNException("Invalid move [" + move + "]");
			}
		}
	}
	
	@Override
	public int progress() {
		return progress;
	}

	@Override
	public Date getStartTime() {
		return startTime;
	}

	@Override
	public Date getEndTime() {
		return endTime;
	}

	@Override
	public boolean isWorking() {
		return startTime != null && endTime == null;
	}

	@Override
	public boolean isDone() {
		if (startTime == null) {
			throw new IllegalStateException("Analysis not started");
		}
		return endTime != null;
	}

	@Override
	public PGNEntitySequence getResult() {
		if (isDone()) {
			return analysisResult;
		} else {
			throw new IllegalStateException("Analysis still in progress");
		}
	}

	@Override
	public boolean hasErrors() {
		return hasErrors;
	}
	
	private class AnalyzeRunnable implements Runnable {
		PGNGame      game;
		EngineModel  model;
		int          multipv;
		int          threshold;
		
		AnalyzeRunnable(PGNGame game, EngineModel model, int multipv,
				int threshold) {
			this.game = game;
			this.model = model;
			this.multipv = multipv;
			this.threshold = threshold;
		}

		@Override
		public void run() {
			try {
				startTime = new Date();
				doAnalyze(this.game, this.model, this.multipv, this.threshold);
				logger.info("Analysis ended.");
			} catch (Exception e) {
				hasErrors = true;
				System.err.println(e);
				logger.error ("Error on analyzing game", e);
			} finally {
				endTime = new Date();
			}
		}
	}
}
