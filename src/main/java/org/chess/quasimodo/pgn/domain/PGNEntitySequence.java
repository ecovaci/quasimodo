package org.chess.quasimodo.pgn.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.chess.quasimodo.errors.InvalidPGNException;
import org.chess.quasimodo.errors.PGNParseException;
import org.chess.quasimodo.util.Utils;

public class PGNEntitySequence implements PGNEntity<List<PGNEntity<?>>>, PGNParsable {
	
	private final StringBuffer moveBuffer = new StringBuffer();
	
	private final List<PGNEntity<?>> entities = new ArrayList<PGNEntity<?>>();
	
	private int initialPly;
	
	public StringBuffer getMoveBuffer() {
		return moveBuffer;
	}

	public void loadTextContent (String textContent) {
		if (moveBuffer.length() > 0) {
			throw new IllegalStateException("Cannot load text move list twice");
		}
		moveBuffer.append(textContent);
	}
	
	public List<PGNMove> getMoveList() {
		List<PGNMove> moveList = new ArrayList<PGNMove>();
		for (PGNEntity<?> entity:entities) {
			if (entity instanceof PGNMove) {
				moveList.add((PGNMove)entity);
			}
		}
		return moveList;
	}

	public String getMoveListText() {
		return moveBuffer.toString();
	}

	@Override
	public void parse () throws PGNParseException {
		String movesText = PGNNumericGlyph.stripAll(
				               PGNComment.stripAll(
				            		PGNVariation.stripAll(moveBuffer.toString())));
		String[] tokens = movesText.split("\\s+");
		if (tokens.length != 0) {
			int dotIndex = tokens[0].indexOf('.');
			if (dotIndex > -1) {
				initialPly = Integer.parseInt(tokens[0].substring(0, dotIndex));
				Pattern pattern = Pattern.compile("^\\d+") ;
				for (String token : tokens) {
					if (!pattern.matcher(token).find()) {
						entities.add(new PGNMove(token));
					}
				}
			}
		}
		
	}
	
	public void fullParse () {
		String movesTxt = moveBuffer.toString();
		Matcher matcher;
		int length;
		do {
			length = movesTxt.length();
			//check comment
			matcher = PGNComment.START_WITH.matcher(movesTxt);
			if (matcher.find()) {
				entities.add(new PGNComment(movesTxt.substring(matcher.start() + 1, 
						matcher.end() - 1)));
			} else {
				//check variation
				matcher = PGNVariation.START_WITH.matcher(movesTxt);
				if (matcher.find()) {
					entities.add(new PGNVariation(movesTxt.substring(matcher.start() + 1, 
							matcher.end() - 1)));
				} else {
					//check nag
					matcher = PGNNumericGlyph.START_WITH.matcher(movesTxt);
					if (matcher.find()) {
						entities.add(new PGNNumericGlyph(matcher.group()));
					} else {
						//check initial ply
						if (initialPly < 1) {
							matcher = PGNMove.MOVE_NUMBER.matcher(movesTxt);
							if (matcher.find()) {
								initialPly = Integer.parseInt(matcher.group());
							} 
						}
						//check move
						matcher = PGNMove.PATTERN.matcher(movesTxt);
						if (matcher.find()) {
							entities.add(new PGNMove(matcher.group()));
						} else {
							break;
						}
					}
				}
			}
			if (matcher.end() + 1 > movesTxt.length() - 1) {
				break;
			}
			movesTxt = movesTxt.substring(matcher.end() + 1).trim();
		} 
		while (length != movesTxt.length());
	}

	
	public int getStartPlyIndex() {
		return initialPly;
	}
	
	public void setStartPlyIndex(int startPlyIndex) {
		this.initialPly = startPlyIndex;
	}
	
	@Override
	public String toPGN() {
		StringBuffer buffer = new StringBuffer();
		int ply = initialPly;
		boolean hasVariation = false;
		for (PGNEntity<?> entity:entities) {
			if (entity instanceof PGNMove) {
				ply++;
				if (ply % 2 == 1) {
					buffer.append(ply % 2).append(".");
				} else if (hasVariation || initialPly == ply) {
					buffer.append(ply % 2).append("...");
				}
			}
			buffer.append(" ").append(entity.toPGN());
			hasVariation = entity instanceof PGNVariation;
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		return "PGNEntitySequence [moveBuffer=" + moveBuffer + ", entities="
				+ entities + ", initialPly=" + initialPly + "]";
	}

	@Override
	public List<PGNEntity<?>> getContent() {
		return entities;
	}
}
