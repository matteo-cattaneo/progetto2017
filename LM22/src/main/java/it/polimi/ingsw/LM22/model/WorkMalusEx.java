package it.polimi.ingsw.LM22.model;

public class WorkMalusEx {
	
	private final Integer period;
	private final String workType;
	private final Integer workValue;
	
	public WorkMalusEx(Integer period, String workType, Integer workValue){
		this.period = period;
		this.workType = workType;
		this.workValue = workValue;
	}

	public Integer getPeriod() {
		return period;
	}

	public String getWorkType() {
		return workType;
	}

	public Integer getWorkValue() {
		return workValue;
	}
	
	
		
}
