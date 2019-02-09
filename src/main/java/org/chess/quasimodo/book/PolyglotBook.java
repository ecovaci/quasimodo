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
package org.chess.quasimodo.book;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * It provides access to the opening openingBook.<br><br>
 * <b>OBS.</b> The opening openingBook must be in polyglot format.
 * @author Eugen Covaci, created on Jan 28, 2011.
 */
public class PolyglotBook implements OpeningBook {
	private Log logger = LogFactory.getLog(PolyglotBook.class);
	
	public static final int MOVE_MASK = 7;
	
	private RandomAccessFile bookFile;
	
    /**
     * Constructor
     * @param filepath The pathname of the openingBook.
     * @throws FileNotFoundException
     */
	public PolyglotBook(String filepath) throws FileNotFoundException {
		bookFile = new RandomAccessFile(filepath, "r");
	}
    
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.book.OpeningBook#search(long)
	 */
    @Override
	public Set<BookEntry> search (long key) throws IOException {
    	Set<BookEntry> bookEntries = new HashSet<BookEntry>();
    	long keyPosition = seekPosition(key);
    	if (keyPosition > -1) {//key found, search around key position
    		long pos = keyPosition;
    		
    		//************** search downwards *************//
    		while (readKey(pos) == key) {
    			bookEntries.add(readBookEntry(pos));
    			pos--;
    		}
    		//*********************************************//
    		
    		pos = keyPosition + 1;
    		//************** search upwards *************//
    		while (readKey(pos) == key) {
    			bookEntries.add(readBookEntry(pos));
    			pos++;
    		}
    		//*********************************************//
    	}
    	return bookEntries;
    }
    
    /* (non-Javadoc)
	 * @see org.chess.quasimodo.book.OpeningBook#randomBookEntry(long)
	 */
    @Override
	public BookEntry randomBookEntry (long key) throws IOException {
    	Set<BookEntry> entries = search(key);
    	if (!entries.isEmpty()) {
    		return pickUpRandomEntry (entries);
    	}
    	return null;
    }
    
    private BookEntry pickUpRandomEntry (Set<BookEntry> entries) {
    	//calculate the sum
    	int sum = 0;
    	for (Iterator<BookEntry> itr = entries.iterator();itr.hasNext();) {
    		sum += itr.next().getWeight();
    	}
    	//generate a random number between [0, sum)
    	Random random = new Random ();
    	int randomInt = random.nextInt(sum);
    	//the entry that makes the sum bigger then the generated random
    	//is the one we want
    	sum = 0;
    	BookEntry entry;
    	for (Iterator<BookEntry> itr = entries.iterator();itr.hasNext();) {
    		entry = itr.next();
    		sum += entry.getWeight();
    		if (sum > randomInt) {
    			return entry;
    		}
    	}
    	throw new IllegalArgumentException("Something is very wrong with these entries " + entries);
    }
    
    /**
     * Search openingBook for position.
     * @param key The zobrist key to search for.
     * @return The offset of the found position or negative value if position not found.
     * @throws IOException
     */
    private long seekPosition (long key) throws IOException {
    	long pos = -1;
    	
    	//********** define the window search ************//
    	long low = 0;
    	long high = bookFile.length() / 16;
    	//************************************************//
    	
    	Long currentKey = null;
    	while (1 < high - low) {
    		pos = (high + low) / 2;
    		currentKey = readKey (pos);
    		if (currentKey == key) {
    			break;
    		} else if (isLessThanUnsigned (currentKey, key)) {
    			low = pos;
    		} else {
    			high = pos;
    		}
    	}
    	//if key not found, 
    	//search the minimal window, below
    	if (currentKey != key) {
    		pos = low;
    		currentKey = readKey (pos);
    	}
    	//if key not found, 
    	//search the minimal window, up
    	if (currentKey != key) {
    		pos = high;
    		currentKey = readKey (pos);
    	}
    	
    	if (currentKey == key) {
    		return pos;
    	} 
    	return -1;
    }
    
    public boolean isLessThanUnsigned(long n1, long n2) {
    	return (n1 < n2) ^ ((n1 < 0) != (n2 < 0));
    }
    
    private long readKey (long offset) throws IOException {
    	bookFile.seek(16 * offset);
    	return bookFile.readLong();
    }
    
    private BookEntry readBookEntry (long offset) throws IOException {
    	bookFile.seek(16 * offset + 8);
    	BookEntry bookEntry = new BookEntry();
    	int move = bookFile.readShort() & 0xffff;
    	bookEntry.setTo_file(move & MOVE_MASK);
    	bookEntry.setTo_row((move >> 3) & MOVE_MASK);
    	bookEntry.setFrom_file((move >> 6) & MOVE_MASK);
    	bookEntry.setFrom_row((move >> 9) & MOVE_MASK);
    	bookEntry.setPromotedPiece((move >> 12) & MOVE_MASK);
    	bookEntry.setWeight(bookFile.readChar());
    	return bookEntry;
    }
    
    /* (non-Javadoc)
	 * @see org.chess.quasimodo.book.OpeningBook#close()
	 */
    @Override
	public void close () {
    	try {
			bookFile.close();
		} catch (IOException e) {
			logger.warn("Cannot close openingBook file's stream", e);
		}
    }
}
