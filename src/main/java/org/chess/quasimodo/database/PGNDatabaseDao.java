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
package org.chess.quasimodo.database;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.chess.quasimodo.pgn.domain.PGNParsable;
import org.chess.quasimodo.pgn.domain.PGNGame;
import org.chess.quasimodo.pgn.domain.PGNGame.TagType;
import org.chess.quasimodo.pgn.logic.AbstractPGNParser;
import org.chess.quasimodo.util.LocalIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;


@Repository("pgnDatabaseDao")
public class PGNDatabaseDao extends JdbcDaoSupport {
	Logger logger = LoggerFactory.getLogger(PGNDatabaseDao.class);
	int BATCH_SIZE = 100;
	
    public PGNParsable getPGNGame (long gameId) {
    	List<PGNGame> result = getJdbcTemplate().query("select MOVELIST from PGN_GAME where id = ?", 
    			new Object[]{Long.valueOf(gameId)}, new RowMapper<PGNGame>() {
			@Override
			public PGNGame mapRow(ResultSet rs, int rowNum) throws SQLException {
				PGNGame game = new PGNGame();
				game.loadTextContent(rs.getString("MOVELIST"));
				return game;
			}
    	});
    	return result.isEmpty() ? null : result.get(0);
    }
    
    
    public void findGames (long position) {//TODO - prepared statement
    	//select content from game limit 100 offset 100
    	getJdbcTemplate().query("select content from game", new GameContentResultSetExtractor(position));
    }
    
    public void insertPGNGames (String pathfile) throws SQLException {
    	getJdbcTemplate().execute(new MyConnectionCallback(pathfile));
    }
    
    private class MyConnectionCallback implements ConnectionCallback<Object> {
    	private String filepath;
    	
	    MyConnectionCallback(String filepath) {
			this.filepath = filepath;
		}

		@Override
		public Object doInConnection(Connection con) throws SQLException,
				DataAccessException {
			try {
				PGNDatabaseLoader parser = new PGNDatabaseLoader(this.filepath, con);
				parser.parse();
				parser.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

    }
    
    private class PGNDatabaseLoader extends AbstractPGNParser {
    	Connection con;
    	PreparedStatement statement;
    	
		private PGNDatabaseLoader(String filepath, Connection con)
				throws FileNotFoundException, SQLException {
			super(filepath);
			statement = con.prepareStatement (
			     "insert into Game (content, event, site, game_date , round , white , black , result, white_title, white_elo, black_title, black_elo ,eco) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			con.setAutoCommit(false);
			this.con = con;
			
		}

		private PGNDatabaseLoader(String filepath) throws FileNotFoundException {
			super(filepath);
		}

		@Override
		public void processGameData(Map<TagType, String> tagMap,
				StringBuffer moveBuffer, int gameCounter) throws Exception {
			statement.setBytes(1, LocalIOUtils.compress(moveBuffer.toString().getBytes()));
		    if (tagMap.containsKey(TagType.Event)) {
		    	statement.setString(2, tagMap.get(TagType.Event));
		    } else {
		    	statement.setString(2, null);
		    }
		    if (tagMap.containsKey(TagType.Site)) {
		    	statement.setString(3, tagMap.get(TagType.Site));
		    } else {
		    	statement.setString(3, null);
		    }
		    if (tagMap.containsKey(TagType.Date)) {
		    	statement.setString(4, tagMap.get(TagType.Date));
		    }  else {
		    	statement.setString(4, null);
		    }
		    if (tagMap.containsKey(TagType.Round)) {
		    	statement.setString(5, tagMap.get(TagType.Round));
		    }  else {
		    	statement.setString(5, null);
		    }
		    if (tagMap.containsKey(TagType.White)) {
		    	statement.setString(6, tagMap.get(TagType.White));
		    }  else {
		    	statement.setString(6, null);
		    }
		    if (tagMap.containsKey(TagType.Black)) {
		    	statement.setString(7, tagMap.get(TagType.Black));
		    }  else {
		    	statement.setString(7, null);
		    }
		    if (tagMap.containsKey(TagType.Result)) {
		    	statement.setString(8, tagMap.get(TagType.Result));
		    }  else {
		    	statement.setString(8, null);
		    }
		    if (tagMap.containsKey(TagType.WhiteTitle)) {
		    	statement.setString(9, tagMap.get(TagType.WhiteTitle));
		    }  else {
		    	statement.setString(9, null);
		    }
		    if (tagMap.containsKey(TagType.WhiteElo)) {
		    	statement.setString(10, tagMap.get(TagType.WhiteElo));
		    }  else {
		    	statement.setString(10, null);
		    }
		    if (tagMap.containsKey(TagType.BlackTitle)) {
		    	statement.setString(11, tagMap.get(TagType.BlackTitle));
		    }  else {
		    	statement.setString(11, null);
		    }
		    if (tagMap.containsKey(TagType.BlackElo)) {
		    	statement.setString(12, tagMap.get(TagType.BlackElo));
		    }  else {
		    	statement.setString(12, null);
		    }
		    if (tagMap.containsKey(TagType.ECO)) {
		    	statement.setString(13, tagMap.get(TagType.ECO));
		    }  else {
		    	statement.setString(13, null);
		    }
		    statement.addBatch();
			if (gameCounter % 500 == 0) {
				statement.executeBatch();
			}
			if (gameCounter % 1500 == 0) {
				con.commit();
			}
		}
    	
		public void close () {
			try {
				statement.executeBatch();
				con.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JdbcUtils.closeStatement(statement);
		}
    }
    
    private class GameContentResultSetExtractor implements ResultSetExtractor<Object> {
    	long position;
    	
		GameContentResultSetExtractor(long position) {
			this.position = position;
		}

		@Override
		public Object extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			int counter = 0;
			while (rs.next()) {
				rs.getBytes("content");
				System.out.println(++counter);
			}
			return null;
		}
    	
    }
}
