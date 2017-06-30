package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class MarketSpace extends AbstractSpace implements Serializable {

	private static final long serialVersionUID = -931354969852880256L;
	/*
	 * i bonus del marketSpace devono essere instanziati da File
	 */
	private Resource reward;
	private Integer councilPrivilege;
	private FamilyMember member;

	public FamilyMember getMember() {
		return member;
	}

	public void setMember(FamilyMember member) {
		this.member = member;
	}

	public Resource getReward() {
		return reward;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
}
