package org.chess.quasimodo.book;

import java.io.IOException;
import java.util.Set;

public interface OpeningBook {

	/**
	 * Retrieves all opening book's entries for a position.
	 * @param key The zobrist key for the position to search for.
	 * @return All the opening book's entries found, or an empty set if none found.
	 * @throws IOException
	 */
	public abstract Set<BookEntry> search(long key) throws IOException;
    
	/**
	 * Gets a random opening book entry for a given position.
	 * @param key The zobrist key for the position to search for.
	 * @return A random chosen opening book entry, if found.
	 * @throws IOException
	 */
	public abstract BookEntry randomBookEntry(long key) throws IOException;

	/**
	 * Closes the opening book's file.
	 */
	public abstract void close();

}