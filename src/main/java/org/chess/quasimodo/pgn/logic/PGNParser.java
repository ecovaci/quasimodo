package org.chess.quasimodo.pgn.logic;

import java.util.Map;

import org.chess.quasimodo.errors.PGNParseException;
import org.chess.quasimodo.pgn.domain.PGNGame.TagType;


public interface PGNParser {

	void parse() throws PGNParseException;

	void close();

	void processGameData(Map<TagType, String> tagMap,
			StringBuffer moveBuffer, int gameCounter) throws Exception;

}