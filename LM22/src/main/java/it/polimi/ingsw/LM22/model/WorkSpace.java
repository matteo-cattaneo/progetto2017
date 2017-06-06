package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.List;

public class WorkSpace extends AbstractSpace implements Serializable {

	private static final long serialVersionUID = -4179895781015044439L;
	private String workType;
	private final Integer MALUS = 3;
	private List<FamilyMember> members;
	private List<String> coloredMemberOnIt;

	public List<FamilyMember> getMembers() {
		return members;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public void setColoredMemberOnIt(List<String> coloredMemberOnIt) {
		this.coloredMemberOnIt = coloredMemberOnIt;
	}

	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}

	public String getWorkType() {
		return workType;
	}

	public List<String> getColoredMemberOnIt() {
		return coloredMemberOnIt;
	}

}
