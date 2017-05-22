package it.polimi.ingsw.LM22.model;

public class CharacterCard extends DevelopmentCard{
	public final Resource coinsCost;
	public final CharacterEffect permanentEffect;
	
	public CharacterCard(String name, Integer period, String immediateType/*, IEffect*/, Resource coinsCost, CharacterEffect permanentEffect){
		super(name, period, immediateType);
		this.coinsCost = coinsCost;
		this.permanentEffect = permanentEffect;
	}
	
	public Resource getCost(){
		return coinsCost;
	}
	
	public CharacterEffect getPermanentEffect(){
		return permanentEffect;
	}
}
