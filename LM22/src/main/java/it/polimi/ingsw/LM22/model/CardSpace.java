package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CardSpace extends AbstractSpace implements Serializable {

	private static final long serialVersionUID = 8590533407219552800L;
	private Integer level;
	private Resource reward;
	private FamilyMember member;

	public Integer getLevel() {
		return level;
	}

	public FamilyMember getMember() {
		return member;
	}

	public void setMember(FamilyMember member) {
		this.member = member;
	}

	public Resource getReward() {
		return reward;
	}

}
