package it.polimi.ingsw.LM22.model;

public class ColorCardBonusEffect {
	private final String cardType;
	private final Integer diceBonus;
	private final Resource cardDiscount;

	public ColorCardBonusEffect(String cardType, Integer diceBonus, Resource cardDiscount) {
		this.cardType = cardType;
		this.diceBonus = diceBonus;
		this.cardDiscount = cardDiscount;
	}

	public String getCardType() {
		return cardType;
	}

	public Integer getDiceBonus() {
		return diceBonus;
	}

	public Resource getCardDiscount() {
		return cardDiscount;
	}

}
