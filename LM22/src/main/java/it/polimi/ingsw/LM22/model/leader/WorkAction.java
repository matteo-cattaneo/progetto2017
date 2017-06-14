package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class WorkAction extends LeaderEffect implements Serializable{

	private static final long serialVersionUID = 648377154947587601L;
	private String typeOfWork;
	private Integer valueOfWork;
	
	public String getTypeOfWork() {
		return typeOfWork;
	}
	public Integer getValueOfWork() {
		return valueOfWork;
	}
	@Override
	public String getInfo() {
		return "You are able to do a " + typeOfWork + " action with a value of " + valueOfWork;
	}
	
	
}
