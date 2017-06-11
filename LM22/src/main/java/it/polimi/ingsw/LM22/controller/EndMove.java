package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.Player;

public class EndMove extends AbstractMove {
	private String error;

	public EndMove(Player p, String error) {
		super(p);
		this.error = error;
	}

	public String getError() {
		return error;
	}
}
