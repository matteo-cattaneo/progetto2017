package it.polimi.ingsw.LM22.model;

public class VentureCard extends DevelopmentCard{
	
	public final Resource cardCost1;
	public final Resource cardCost2;
	public final Resource permanentEffect;
	
	public VentureCard(String name, Integer period, String immediateType/*, IEffect*/, Resource cardCost1, Resource cardCost2, Resource permanentEffect){
		super(name, period, immediateType);
		this.cardCost1 = cardCost1;
		this.cardCost2 = cardCost2;
		this.permanentEffect = permanentEffect;
	}

	public Resource getCardCost1() {
		return cardCost1;
	}

	public Resource getCardCost2() {
		return cardCost2;
	}

	public Resource getPermanentEffect() {
		return permanentEffect;
	}
	
	public void selectCardCost(){
		
	}
	

}
