package it.polimi.ingsw.LM22.model;

public class CardActionEffect implements IEffect{
	String color;
	Integer diceValue;
	Resource cardDiscount;
	Resource resource;
	Integer councilPrivilege;

	public CardActionEffect(String color, Integer diceValue, Resource cardDiscount, Resource resource,
			Integer councilPrivilege) {
		this.color = color;
		this.diceValue = diceValue;
		this.cardDiscount = cardDiscount;
		this.resource = resource;
		this.councilPrivilege = councilPrivilege;
	}

	public String getColor() {
		return color;
	}

	public Integer getDiceValue() {
		return diceValue;
	}

	public Resource getCardDiscount() {
		return cardDiscount;
	}

	public Resource getResource() {
		return resource;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
}
