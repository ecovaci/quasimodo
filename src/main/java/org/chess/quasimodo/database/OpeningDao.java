package org.chess.quasimodo.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.chess.quasimodo.pgn.domain.Opening;
import org.chess.quasimodo.pgn.domain.PGNGame;
import org.chess.quasimodo.pgn.domain.PGNGame.TagType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

public class OpeningDao extends JdbcDaoSupport {
	
    public List<Opening> getOpeningList (String moves) {
    	return getJdbcTemplate().query(
    			"select * from ECO where MOVES like ?", 
    			new Object[]{moves + "%"}, new OpeningRowMapper());
    }
    
    public Opening getOpening (String moves) {
    	List<Opening> result = getJdbcTemplate().query(
    			"select * from ECO where MOVES = ?", 
    			new Object[]{moves}, new OpeningRowMapper());
    	return result.isEmpty() ? null : result.get(0);
    }
    
    @Transactional
    public void insertOpenings (Iterator<PGNGame> iterator) {
    	PGNGame game;
    	for (;iterator.hasNext();) {
    		game = iterator.next();
    	    getJdbcTemplate().update("insert into ECO (CODE, NAME ,VARIATION, MOVES) values (?, ?, ?, ?)", 
    	    		game.tagValue(TagType.Site), game.tagValue(TagType.White),
    	    		game.tagValue(TagType.Black), game.getMoveListText().trim());
    	}
    }
    
    @Transactional
    public void createECODatabase () {
    	getJdbcTemplate().execute("create table eco (id int auto_increment primary key,"
			+ "code varchar not null,"
			+ "name varchar not null,"
			+ "variation varchar,"
			+ "moves varchar not null"
			+ ")");
    	getJdbcTemplate().execute("create index idx_code on eco (code)");
    	getJdbcTemplate().execute("create index idx_moves on eco (moves)");
    }
    
    public void shutdown () {
    	getJdbcTemplate().execute("SHUTDOWN COMPACT");
    }
    
    private static class OpeningRowMapper implements RowMapper<Opening> {
    	@Override
		public Opening mapRow(ResultSet rs, int rowNum) throws SQLException {
    		Opening valueObject = new Opening();
    		valueObject.setId(rs.getLong("id"));
    		valueObject.setCode(rs.getString("code"));
    		valueObject.setName(rs.getString("name"));
    		valueObject.setVariation(rs.getString("variation"));
    		valueObject.setMoves(rs.getString("moves"));
			return valueObject;
		}
    }
}
