package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class MemberChangeEffect extends MemberValueEffect implements Serializable{

	private static final long serialVersionUID = 6461929235057767416L;
	private String typeOfMember;
	private Integer newValueOfMember;
	public String getTypeOfMember() {
		return typeOfMember;
	}
	public Integer getNewValueOfMember() {
		return newValueOfMember;
	}
	
	@Override
	public String getInfo() {
		String info = "You will get a " + newValueOfMember + "for " + typeOfMember + "member";
		return info;
	}
	
}
