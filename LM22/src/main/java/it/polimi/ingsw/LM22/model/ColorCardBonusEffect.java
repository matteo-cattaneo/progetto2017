package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ColorCardBonusEffect extends PermanentEffect implements Serializable {

	private static final long serialVersionUID = -1867843047550443588L;
	private Integer cardType;
	private Integer diceBonus;
	private Resource cardDiscount;
	
	public Integer getCardType() {
		return cardType;
	}

	public Integer getDiceBonus() {
		return diceBonus;
	}

	public Resource getCardDiscount() {
		return cardDiscount;
	}

}
