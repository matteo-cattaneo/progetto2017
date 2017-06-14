package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CardActionEffect extends ImmediateEffect implements Serializable {

	private static final long serialVersionUID = 8311517314130702912L;
	private final Integer ALL = -1;
	private Integer cardType;
	private Integer diceValue;
	private Resource cardDiscount;
	private Resource resource;
	private Integer councilPrivilege;

	public Integer getCardType() {
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
	
	public String getInfo(){
		String info = "";
		if (cardType!=ALL)
			info = info + "You can get a card on the " + (cardType+1) + "tower%n";
		else 
			info = info + "You can get a card on the tower you choose%n" ;
		info = info + "The action has a value of " + diceValue + "%n";
		info = info + "You get a card discount: " + cardDiscount.getInfo();
		info = info + "You get a reward: " + resource.getInfo();
		if (councilPrivilege != 0)
			info = info + "You get " + councilPrivilege + "council privilege(s)%n";
		return info;
	}
}
