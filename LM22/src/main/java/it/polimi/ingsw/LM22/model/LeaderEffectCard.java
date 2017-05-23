package it.polimi.ingsw.LM22.model;

public class LeaderEffectCard extends AbstractLeaderCard{

	private final TurnEffect effect;
	
	public LeaderEffectCard(String name, Resource resource/*, parametri per requirement delle carte*/, TurnEffect effect){
		super(name, resource/*altro parametro*/);
		this.effect = effect;
	}

	public TurnEffect getEffect() {
		return effect;
	}
	
	
	
	
	
}
