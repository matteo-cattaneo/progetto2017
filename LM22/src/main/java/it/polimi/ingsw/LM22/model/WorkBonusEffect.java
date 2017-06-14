package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class WorkBonusEffect extends PermanentEffect implements Serializable {
	
	private static final long serialVersionUID = 8242131733492712063L;
	private String typeOfWork;
	private Integer workBonusValue;

	public String getTypeOfWork() {
		return typeOfWork;
	}

	public Integer getWorkBonusValue() {
		return workBonusValue;
	}
	
	public String getInfo(){
		String info ="";
		info = info + "You get a bonus of " + workBonusValue + " for " + typeOfWork + " actions%n";
		return info;
	}

}
