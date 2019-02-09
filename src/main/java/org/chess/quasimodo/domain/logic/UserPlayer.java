package org.chess.quasimodo.domain.logic;

public class UserPlayer extends Player {

	@Override
	public boolean isUser() {
		return true;
	}

}
