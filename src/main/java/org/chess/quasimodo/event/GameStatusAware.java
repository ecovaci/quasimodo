package org.chess.quasimodo.event;

import org.chess.quasimodo.domain.logic.Game.Status;

public interface GameStatusAware {
    void onGameStatusChanged(Status status);
}
