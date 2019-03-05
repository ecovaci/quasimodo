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
package org.chess.quasimodo.domain.logic;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;

import org.chess.quasimodo.annotation.Design;
import org.chess.quasimodo.config.design.Designable;
import org.chess.quasimodo.config.design.Designer;
import org.chess.quasimodo.domain.logic.Game.Status;
import org.chess.quasimodo.event.NotationChangedAware;
import org.chess.quasimodo.util.HTMLFormat;
import org.chess.quasimodo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Scope ("prototype")
@Component ("notation")
public class Notation implements Designable {//TODO - variations,  retroactively apply design.
	
	private StringBuffer          content = new StringBuffer();
	private Font                  font;
	private String                foregroundColor;
	private final NotationInfo    notationInfo = new NotationInfo();
	private int                   currentMoveIndex;
	
	private List<NotationInfo>    history = new ArrayList<Notation.NotationInfo>();
	
	private class NotationInfo {
		int startHighlightingOffset;
		int endHighlightingOffset;
		
		NotationInfo() {}
		
		NotationInfo(int startHighlightingOffset,
				int endHighlightingOffset) {
			this.startHighlightingOffset = startHighlightingOffset;
			this.endHighlightingOffset = endHighlightingOffset;
		}

		public NotationInfo clone () {
			return new NotationInfo(this.startHighlightingOffset, this.endHighlightingOffset);
		}
        void reset () {
        	this.startHighlightingOffset = 0;
        	this.endHighlightingOffset = 0;
        }
		
		@Override
		public String toString() {
			return "NotationInfo [startHighlightingOffset="
					+ startHighlightingOffset + ", endHighlightingOffset="
					+ endHighlightingOffset + "]";
		}
	}
	
	@Design (key="notation.font")
	public Font getFont() {
		return font;
	}

	@Design (key="notation.font")
	public void setFont(Font font) {
		this.font = font;
	}

	@Design (key="notation.foreground.color")
	public String getForegroundColor() {
		return foregroundColor;
	}

	@Design (key="notation.foreground.color")
	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	@Autowired
	@Qualifier ("notationPanel")
	private NotationChangedAware notationChangedAware;
	
	@Autowired
	private Designer designer;
	
	@PostConstruct
	public void injectDesign () {
		designer.injectDesign(this);
	}
	
	private String getMateNotation (Player player) {
		if (player.isWhite()) {
			return Definitions.BLACK_WINS;
		} else {
			return Definitions.WHITE_WINS;
		}
	}
	
	private String composeResign (Player player) {
		return player.getColor().asString() + " resign.";
	}
	
	private String composeTimeout (Player player) {
		return player.getColor().asString() + " time expired.";
	}
	
	private String composeGameState (Player player,Status result) {
		StringBuffer state = new StringBuffer();
		if (result.isMate()) {
			state.append(getMateNotation(player));
			if (!result.isPureMate()) {
				state.append(
						" " + Utils.addRoundBrackets(
								result.isResign() ? composeResign(player):composeTimeout(player)));
			}
		} else if (result.isDraw()) {
			state.append(Definitions.DRAW);
			if (result.getDescription() != null) {
				state.append(" " + Utils.addRoundBrackets (result.getDescription()));
			}
		} else {
			state.append(Definitions.UNDECIDED);
			if (result.getDescription() != null) {
				state.append(" " + Utils.addRoundBrackets (result.getDescription()));
			}
		}
		state.insert(0, "[");
		state.append("]");
		return state.toString();
	}
	
	public void afterMove (ChessColor color, int moveCounter, String move) {
		StringBuffer buffer = new StringBuffer();
		if (color.isWhite()) {
			buffer.append(moveCounter + ".");
		} else if (history.isEmpty()) {
			buffer.append((moveCounter - 1) + "... ");
		}
		buffer.append(move + " ");
		content.append(HTMLFormat.toHTMLHyperlinkElement(history.size() + 1, 
			                                             buffer.toString(), 
			                                             font.getSize(), 
			                                             600));
		notationInfo.startHighlightingOffset = notationInfo.endHighlightingOffset + 1;
		notationInfo.endHighlightingOffset = notationInfo.endHighlightingOffset + buffer.length();
		history.add(notationInfo.clone());
		currentMoveIndex = history.size() - 1;
		notifyView();
	}
	
	public void jumpToMoveAt (int moveIndex) {
		if (moveIndex > -2 && moveIndex < history.size()) {
			if (moveIndex > -1) {
				notationInfo.startHighlightingOffset = history.get(moveIndex).startHighlightingOffset;
				notationInfo.endHighlightingOffset = history.get(moveIndex).endHighlightingOffset;
			} else {
				notationInfo.reset();
			}
			currentMoveIndex = moveIndex;
			notifyView();
		}
	}
	
	public void afterGameOver (Player player, Status result) {
		updateGameState (composeGameState(player, result));
	}
	
	private void updateGameState (String state) {
		if (content.length() > 0) {
			content.append ("<br>");
		}
		content.append (state);
		notifyView();
	}
	
	public int getMoveNumber () {
		return history.size();
	}
	
	public int getCurrentMoveIndex() {
		return currentMoveIndex;
	}

	public void reset () {
		history.clear();
		content.setLength(0);
		notifyView();
	}
	
	private void notifyView () {
		notationChangedAware.notationContentChanged(toHTML(),
				                                    new HyperlinkEvent(this, EventType.ACTIVATED, null,
				                                		   "" , new CustomTextElement()));
	}
	
	private String toHTML () {
		return HTMLFormat.toHTMLDocument(
				   HTMLFormat.toHTMLFontElement(content.toString(), font.getFontName(), foregroundColor));
	}

	@Override
	public void refreshDesign() {
		//does nothing here
	}
	
	/**
	 * A phony implementation of the {@link Element} interface.
	 * @author Eugen Covaci, created on Jun 17, 2011.
	 */
	private class CustomTextElement implements Element {

		@Override
		public Document getDocument() {
			return null;
		}

		@Override
		public Element getParentElement() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public AttributeSet getAttributes() {
			return null;
		}

		@Override
		public int getStartOffset() {
			return notationInfo.startHighlightingOffset;
		}

		@Override
		public int getEndOffset() {
			return notationInfo.endHighlightingOffset;
		}

		@Override
		public int getElementIndex(int offset) {
			return 0;
		}

		@Override
		public int getElementCount() {
			return 0;
		}

		@Override
		public Element getElement(int index) {
			return null;
		}

		@Override
		public boolean isLeaf() {
			return false;
		}
	}
}
