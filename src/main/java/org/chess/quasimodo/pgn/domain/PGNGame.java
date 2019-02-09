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
package org.chess.quasimodo.pgn.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.chess.quasimodo.domain.logic.Definitions;

public class PGNGame extends PGNEntitySequence {
	
	private final Map<TagType,String> tagMap = new HashMap<TagType, String>();
	
	private int lastBookMoveIndex;
	
	public PGNGame() {
		tagMap.put(TagType.FEN, Definitions.INITIAL_FEN);
	}

	public static void processTag (String  tag, Map<TagType,String> tagMap) {
		String tagContent = tag.substring(1, tag.length() - 1);
		try {
			tagMap.put(TagType.valueOf(TagType.class, tagContent.substring(0, tagContent.indexOf('"')).trim()), 
					tagContent.substring(tagContent.indexOf('"') + 1, tagContent.lastIndexOf('"')).trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processTag (String  strTag) {
		processTag(strTag, this.tagMap);
	}
	
	public String tagValue (TagType type) {
		return tagMap.get(type);
	}

	public boolean containsTag (TagType type) {
		return tagMap.containsKey(type);
	}
	
	public void putTag (TagType type, String value) {
		tagMap.put(type, value);
	}
	
	public int getLastBookMoveIndex() {
		return lastBookMoveIndex;
	}

	public void setLastBookMoveIndex(int lastBookMoveIndex) {
		this.lastBookMoveIndex = lastBookMoveIndex;
	}

	public void incrementLastBookMoveIndex () {
		this.lastBookMoveIndex++;
	}
	
	public enum TagType {
		Event("Event"), 
		Site("Site"), 
		Date("Date"),
		Round("Round"), 
		White("White"), 
		Black("Black"), 
		Result("Result"), 
		WhiteTitle("WhiteTitle"),
		WhiteElo("WhiteElo"), 
		WhiteUSCF("WhiteUSCF"), 
		WhiteNA("WhiteNA"),
		WhiteType("WhiteType"),
		BlackTitle("BlackTitle"),
		BlackElo("BlackElo"),
		BlackUSCF("BlackUSCF"),
		BlackNA("BlackNA"),
		BlackType("BlackType"),
		EventDate("EventDate"), 
		EventSponsor ("EventSponsor"), 
		Section("Section"),
		Stage("Stage"),
		Board("Board"),
		Opening("Opening"), 
		Variation("Variation"), 
		SubVariation("SubVariation"),
		ECO("ECO"),
		NIC("NIC"),
		Time ("Time"), 
		UTCTime("UTCTime"), 
		UTCDate("UTCDate"),
		TimeControl("TimeControl"),
		SetUp("SetUp"), 
		FEN("FEN"), 
		Termination("Termination"), 
		Annotator("Annotator"), 
		Mode("Mode"), 
		PlyCount("PlyCount");
		
		TagType(String type) {
			this.name = type;
		}

		private String name;

		public String getName() {
			return name;
		}
	}
    
	@Override
	public String toPGN () {
		return printTags().append("\n").append(super.toPGN()).toString();
	}

	public String toSimplePGN () {
		StringBuffer buffer = printTags().append("\n");
		int ply = getStartPlyIndex();
		for (PGNMove move : getMoveList()) {
			ply++;
			if (ply % 2 == 1) {
				buffer.append(ply % 2).append(".");
			} else if (getStartPlyIndex() == ply) {
				buffer.append(ply % 2).append("...");
			}
			buffer.append(" ").append(move.toPGN());
		}
		return buffer.toString();
	}
	
	public StringBuffer printTags () {
		StringBuffer buffer = new StringBuffer();
		for (Entry<TagType, String> entry : tagMap.entrySet()) {
			buffer.append("[").append(entry.getKey()).append(" ").append("\"")
					.append(entry.getValue()).append("\"").append("]\n");
		}
		return buffer;
	}

	@Override
	public String toString() {
		return "PGNGame [tagMap=" + tagMap + ", lastBookMoveIndex="
				+ lastBookMoveIndex + ", toString()=" + super.toString() + "]";
	}
	
}
