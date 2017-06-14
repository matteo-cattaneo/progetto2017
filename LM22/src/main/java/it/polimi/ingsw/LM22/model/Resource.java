package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class Resource implements Serializable {

	private static final long serialVersionUID = -2464073930476505293L;
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

	public void setWood(Integer wood) {
		this.wood = wood;
	}

	public void setStone(Integer stone) {
		this.stone = stone;
	}

	public void setServants(Integer servants) {
		this.servants = servants;
	}

	public void setCoins(Integer coins) {
		this.coins = coins;
	}

	public void setFaith(Integer faith) {
		this.faith = faith;
	}

	public void setMilitary(Integer military) {
		this.military = military;
	}

	public void setVictory(Integer victory) {
		this.victory = victory;
	}

	public Resource clone() {
		return new Resource(new Integer(this.wood), new Integer(this.stone), new Integer(this.servants),
				new Integer(this.coins), new Integer(this.faith), new Integer(this.military),
				new Integer(this.victory));
	}

	public String getInfo() {
		String info = "";
		if (wood != 0)
			info = info + "wood: " + wood + "%n";
		if (stone != 0)
			info = info + "stone: " + stone + "%n";
		if (servants != 0)
			info = info + "servants: " + servants + "%n";
		if (coins != 0)
			info = info + "coins: " + coins + "%n";
		if (faith != 0)
			info = info + "faith: " + faith + "%n";
		if (military != 0)
			info = info + "military: " + military + "%n";
		if (victory != 0)
			info = info + "victory: " + victory + "%n";
		if (wood.equals(0) && stone.equals(0) && servants.equals(0) && coins.equals(0) && faith.equals(0)
				&& military.equals(0) && victory.equals(0))
			info = "No resource%n";
		return info;
	}

}
