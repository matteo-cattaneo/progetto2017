package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class WorkAction extends LeaderEffect implements Serializable{

	private String typeOfWork;
	private Integer valueOfWork;
	
	public String getTypeOfWork() {
		return typeOfWork;
	}
	public Integer getValueOfWork() {
		return valueOfWork;
	}
	
	
}
