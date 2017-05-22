package it.polimi.ingsw.LM22.model;

public class WorkBonusEffect {
	private final String typeOfWork;
	private final Integer workBonusValue;

	public WorkBonusEffect(String typeOfWork, Integer workBonusValue) {
		this.typeOfWork = typeOfWork;
		this.workBonusValue = workBonusValue;
	}

	public String getTypeOfWork() {
		return typeOfWork;
	}

	public Integer getWorkBonusValue() {
		return workBonusValue;
	}

}
