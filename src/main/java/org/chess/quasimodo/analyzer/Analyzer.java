package org.chess.quasimodo.analyzer;

import java.util.Date;

import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.pgn.domain.PGNEntitySequence;
import org.chess.quasimodo.pgn.domain.PGNGame;

public interface Analyzer {
	/**
	 * Analyzes chess game. This method should be executed asynchroniously.
	 * @param game The game to be analyzed.
	 * @param engineModel The model of the engine to be used for.
	 * @param multipv How many variations are needed.
	 * @param threshold TODO
	 */
	void analyzeGame(PGNGame game, EngineModel engineModel, int multipv, int threshold);

	/**
	 * Gets the annotated analyzed game.
	 * @return The analysis result.
	 * @throws IllegalStateException if {@link #analyzeGame(PGNGame, EngineModel, int, int)} has not been called 
	 * or the analysis is still in progress.
	 */
	PGNEntitySequence getResult ();
	
	/**
	 * Checks for the errors during the analysis.
	 * @return <code>true</code> only if errors were encountred during the analysis.
	 */
	boolean hasErrors();
	
	/**
	 * Monitors the execution progress, mainly for logging purposes.
	 * @return The analysis progress (procents between 0..100).
	 */
	int progress();

	/**
	 * Getter for the analyzing start time.
	 * @return The analyzing start time.
	 */
	Date getStartTime();
	
	/**
	 * Getter for the analyzing end time.
	 * @return The analyzing end time.
	 */
	Date getEndTime();
	
	/**
	 * Check if the analyzer is currently working.
	 * @return <code>true</code> if analyzer is working.
	 */
	boolean isWorking ();
	
	/**
	 * Check if the analyzer is has finished working.
	 * @return <code>true</code> if analyzer has finished working.
	 * @throws IllegalStateException if {@link #analyzeGame(PGNGame, EngineModel, int, int)} has not been called.
	 */
	boolean isDone ();
	
}
