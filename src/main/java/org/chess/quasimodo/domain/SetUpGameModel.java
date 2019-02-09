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
package org.chess.quasimodo.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SetUpGameModel {
	
    private boolean          oneTC;
    private boolean          friendly;
    
    private PlayerModel      whitePlayer;
    private PlayerModel      blackPlayer;
    
    private TimeControlModel firstTC;
    private TimeControlModel secondTC;
    private TimeControlModel thirdTC;
    
    private List<PlayerModel> playerModels = new ArrayList<PlayerModel>();
    
	public boolean isOneTimeControl() {
		return oneTC;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public boolean hasTimeControl () {
		return !friendly;
	}
	
	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}

	public boolean isThreeTimeControls() {
		return !oneTC && !friendly;
	}
	
	public void setOneTimeControl(boolean oneTC) {
		this.oneTC = oneTC;
	}

	public PlayerModel getWhitePlayerModel() {
		return whitePlayer;
	}


	public void setWhitePlayerModel(PlayerModel whitePlayer) {
		this.whitePlayer = whitePlayer;
	}


	public PlayerModel getBlackPlayerModel() {
		return blackPlayer;
	}


	public void setBlackPlayerModel(PlayerModel blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public void addPlayerModel (PlayerModel player) {
		this.playerModels.add(player);
	}
	
	public void addAllPlayerModels (Collection<? extends PlayerModel> players) {
		this.playerModels.addAll(players);
	}
	
	public List<PlayerModel> getPlayerModels() {
		return playerModels;
	}
	
	public void setPlayerModels(List<PlayerModel> players) {
		this.playerModels = players;
	}

	public TimeControlModel getFirstTC() {
		return firstTC;
	}


	public void setFirstTC(TimeControlModel firstTC) {
		this.firstTC = firstTC;
	}


	public TimeControlModel getSecondTC() {
		return secondTC;
	}


	public void setSecondTC(TimeControlModel secondTC) {
		this.secondTC = secondTC;
	}


	public TimeControlModel getThirdTC() {
		return thirdTC;
	}


	public void setThirdTC(TimeControlModel thirdTC) {
		this.thirdTC = thirdTC;
	}
	
	public String getTimeDescription () {
		if (isOneTimeControl())  {
			return thirdTC.asText();
		} else if (isThreeTimeControls()) {
			return firstTC.asText() + "/40+" + secondTC.asText() + "/20+" + thirdTC.asText();
		}
		return "Friendly game";
	}
	
	@Override
	public String toString() {
		return whitePlayer + " vs " + blackPlayer 
	       + " " +  getGameType();
	}

	public String getGameType () {
		if (oneTC) {
			return "1 x TC";
		} else if (friendly) {
			return "Friendly";
		} else {
			return "3 x TC";
		}
	}
}
