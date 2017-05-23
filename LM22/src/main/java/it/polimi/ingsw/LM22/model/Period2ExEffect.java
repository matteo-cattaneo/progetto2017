package it.polimi.ingsw.LM22.model;

public class Period2ExEffect implements ExInterface{
	
	private final ColorMalusEx colorMalusEx;
	private final boolean noMarketEx;
	private final boolean noFirstTurn;
	private final boolean halfServants;
	
	public Period2ExEffect(ColorMalusEx colorMalusEx, boolean noMarketEx, boolean noFirstTurn, boolean halfServants){
		this.colorMalusEx = colorMalusEx;
		this.noMarketEx = noMarketEx;
		this.noFirstTurn = noFirstTurn;
		this.halfServants = halfServants;
	}

	public ColorMalusEx getColorMalusEx() {
		return colorMalusEx;
	}

	public boolean isNoMarketEx() {
		return noMarketEx;
	}

	public boolean isNoFirstTurn() {
		return noFirstTurn;
	}

	public boolean isHalfServants() {
		return halfServants;
	}
	
	
	
	
	
	
}
