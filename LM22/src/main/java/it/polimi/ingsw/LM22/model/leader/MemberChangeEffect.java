package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class MemberChangeEffect extends MemberValueEffect implements Serializable{

	private String typeOfMember;
	private Integer newValueOfMember;
	public String getTypeOfMember() {
		return typeOfMember;
	}
	public Integer getNewValueOfMember() {
		return newValueOfMember;
	}
	
}
