package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tower implements Serializable {

	private static final long serialVersionUID = -7964586559793879706L;
	private final Integer LEVELS = 4;
	private final Floor floor[] = new Floor[LEVELS];
	private boolean occupied;
	private List<String> coloredMembersOnIt = new ArrayList<String>();

	public Floor[] getFloor() {
		return floor;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public List<String> getColoredMembersOnIt() {
		return coloredMembersOnIt;
	}

	public void setColoredMembersOnIt(List<String> coloredMembersOnIt) {
		this.coloredMembersOnIt = coloredMembersOnIt;
	}

}
