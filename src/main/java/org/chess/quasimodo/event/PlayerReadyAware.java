package org.chess.quasimodo.event;

/**
 * To be implemented by classes listening for player-ready events.
 * @author Eugen Covaci
 *
 */
public interface PlayerReadyAware {
    /**
     * Called when the system has done 
     * all the preparations for the player to move.
     */
	public abstract void onPlayerReady();

}