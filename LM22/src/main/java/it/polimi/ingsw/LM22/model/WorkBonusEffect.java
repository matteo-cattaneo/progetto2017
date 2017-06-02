package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class WorkBonusEffect extends PermanentEffect implements Serializable {
	private String typeOfWork;
	private Integer workBonusValue;

	public String getTypeOfWork() {
		return typeOfWork;
	}

	public Integer getWorkBonusValue() {
		return workBonusValue;
	}

}
