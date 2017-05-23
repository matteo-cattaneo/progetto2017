package it.polimi.ingsw.LM22.model;

public class ColorMalusEx {

	private final Integer period;
	private final String cardType;
	private final Integer malus;
	
	public ColorMalusEx(Integer period, String cardType, Integer malus){
		this.period = period;
		this.cardType = cardType;
		this.malus = malus;
	}

	public Integer getPeriod() {
		return period;
	}

	public String getCardType() {
		return cardType;
	}

	public Integer getMalus() {
		return malus;
	}
	
	
	
	
	
	
}
