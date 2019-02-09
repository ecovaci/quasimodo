package org.chess.quasimodo.pgn.domain;

/**
 * A logical entity involved in PGN format.
 * @author Eugen Covaci
 * @param <T> The type of the content.
 */
public interface PGNEntity<T> {
	
	/**
	 * It generates entity in PGN format.
	 * @return The entity in PGN format.
	 */
    String toPGN ();
    
    /**
     * The actual content of this entity.
     * @return The content.
     */
    T getContent ();
}
