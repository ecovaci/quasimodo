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
package org.chess.quasimodo.service;

import java.io.File;
import java.io.FileNotFoundException;

import org.chess.quasimodo.database.OpeningDao;
import org.chess.quasimodo.database.PGNDatabaseDao;
import org.chess.quasimodo.database.SqliteDataSourceFactory;
import org.chess.quasimodo.pgn.logic.PGNGameWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;


@Service ("databaseService")
public class DatabaseService {
	@Autowired
    private	OpeningDao openingDao;
    
    public void generateECO () {
    	PGNGameWalker parser = null;
    	try {
    		openingDao.createECODatabase();
			parser = new PGNGameWalker(Thread.currentThread().getContextClassLoader()
				      .getResource("eco/eco.pgn").getFile());
			openingDao.insertOpenings(parser.iterator());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
    }
    
    public static void main(String[] args) {
    	ApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(new String[] {"./quasimodo/chess/application/resources/spring-app-config.xml"});
		} catch (Throwable e) {
			e.printStackTrace();	
			System.exit(1);
		}
    	
    	try {
			//PGNGameWalker parser = new PGNGameWalker(new File ("D:/movies/IB20113B.pgn"));
    		DatabaseService dao = context.getBean("databaseService", DatabaseService.class);
			
			dao.generateECO();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	System.out.println("Main - finished");
    	System.exit(0);
	}
}
