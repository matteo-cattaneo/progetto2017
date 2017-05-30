package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.Player;

public abstract class AbstractMove {
	private Player p;

	public AbstractMove(Player p) {
		this.p = p;
	}

	public Player getPlayer() {
		return p;
	}

}
