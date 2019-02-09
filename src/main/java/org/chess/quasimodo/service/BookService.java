package org.chess.quasimodo.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.chess.quasimodo.book.OpeningBook;
import org.chess.quasimodo.book.PolyglotBook;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.logic.Definitions;
import org.chess.quasimodo.domain.logic.Position;
import org.chess.quasimodo.errors.AppException;
import org.chess.quasimodo.util.LocalIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component("bookService")
public class BookService {
	private final Logger logger = LoggerFactory.getLogger(BookService.class);
	
	@Autowired
	private Config config;
	
    public boolean validateOpeningBook (String filepath) {
    	OpeningBook book = null;
    	try {
			book = new PolyglotBook(filepath);
			if (book.search(Definitions.E4_ZOBRIST_KEY).isEmpty()) {
				logger.warn("This opening openingBook does not contain e4 move from initial position");
				return false;
			}
			return true;
		} catch (FileNotFoundException e) {
			throw new AppException("Cannot find any file on [" + filepath + "]", e);
		} catch (Exception e) {
			throw new AppException("There is something wrong with the file on [" + filepath + "]", e);
		} finally {
			if (book != null) {
				book.close();
			}
		}
    }
    
    public void registerOpeningBook (String filepath) {
    	try {
			config.setBookFile(new FileSystemResource(filepath));
			config.store();
		} catch (Exception e) {
			throw new AppException("Cannot register opening openingBook", e);
		}
    }
    
    public static void main(String[] args) {
    	Position position = new Position();
		position.load(Definitions.INITIAL_FEN);
		System.out.println(position.makeSANMove("e4"));
		System.out.println("KEY: " + position.zobristHashKey());
	}
}
