package it.polimi.ingsw.LM22.model;

public class DiceValueEffect implements TurnEffect{
	
	private final String goal;
	private final Integer valueOfBonus;
	
	public DiceValueEffect(String goal, Integer valueOfBonus){
		this.goal = goal;
		this.valueOfBonus = valueOfBonus;
	}

	public String getGoal() {
		return goal;
	}

	public Integer getValueOfBonus() {
		return valueOfBonus;
	}
	
	
	
	
	
}
