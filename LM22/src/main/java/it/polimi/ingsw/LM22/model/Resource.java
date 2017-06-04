package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class Resource implements Serializable {
	private Integer wood;
	private Integer stone;
	private Integer servants;
	private Integer coins;
	private Integer faith;
	private Integer military;
	private Integer victory;

	public Resource(Integer wood, Integer stone, Integer servants, Integer coins, Integer faith, Integer military,
			Integer victory) {
		this.wood = wood;
		this.stone = stone;
		this.servants = servants;
		this.coins = coins;
		this.faith = faith;
		this.military = military;
		this.victory = victory;
	}

	public Integer getWood() {
		return wood;
	}

	public Integer getStone() {
		return stone;
	}

	public Integer getServants() {
		return servants;
	}

	public Integer getCoins() {
		return coins;
	}

	public Integer getFaith() {
		return faith;
	}

	public Integer getMilitary() {
		return military;
	}

	public Integer getVictory() {
		return victory;
	}

}
