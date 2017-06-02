package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class MemberBonusEffect extends MemberValueEffect implements Serializable{

	private String typeOfMember;
	private Integer valueOfBonus;
	public String getTypeOfMember() {
		return typeOfMember;
	}
	public Integer getValueOfBonus() {
		return valueOfBonus;
	}
	
	
}
