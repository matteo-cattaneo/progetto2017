package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CouncilSpace extends AbstractSpace implements Serializable {

	private static final long serialVersionUID = 923010337475400979L;
	private Integer councilPrivilege;
	private Resource reward;
	private List<FamilyMember> members = new ArrayList<FamilyMember>();

	public List<FamilyMember> getMembers() {
		return members;
	}

	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}

	public Resource getReward() {
		return reward;
	}

}
