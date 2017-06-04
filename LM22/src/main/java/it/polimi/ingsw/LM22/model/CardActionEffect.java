package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CardActionEffect extends ImmediateEffect implements Serializable {

	private static final long serialVersionUID = 8311517314130702912L;
	private String cardType;
	private Integer diceValue;
	private Resource cardDiscount;
	private Resource resource;
	private Integer councilPrivilege;

	public String getCardType() {
		return cardType;
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
