package it.polimi.ingsw.LM22.model;

import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;

public class FaithGrid {
	private final Integer GRIDLENGTH = 15;
	private final Integer EXNUMBER = 3;
	private final ExCommunication exCommunicationTiles[];
	private Resource rewards[];

	/*
	 * I reward devono essere caricati da file --> ATTENZIONE ai costruttori
	 */

	public ExCommunication[] getExCommunicationTiles() {
		return exCommunicationTiles;
	}

	public FaithGrid(ExCommunication[] exCommunicationTiles, Resource[] rewards) {
		this.exCommunicationTiles = exCommunicationTiles;
		this.rewards = rewards;
	}

	public Resource[] getRewards() {
		return rewards;
	}

	public void setRewards(Resource[] rewards) {
		this.rewards = rewards;
	}

}
