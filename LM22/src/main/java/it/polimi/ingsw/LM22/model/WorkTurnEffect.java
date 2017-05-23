package it.polimi.ingsw.LM22.model;

public class WorkTurnEffect implements TurnEffect{
	
	private final String workType;
	private final Integer valueOfAction;
	
	public WorkTurnEffect(String workType, Integer valueOfAction){
		this.workType = workType;
		this.valueOfAction = valueOfAction;
	}

	public String getWorkType() {
		return workType;
	}

	public Integer getValueOfAction() {
		return valueOfAction;
	}
	
	
	
	
	
	
}
