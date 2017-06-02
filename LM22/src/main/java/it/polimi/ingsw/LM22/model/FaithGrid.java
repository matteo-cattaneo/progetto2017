package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;

public class FaithGrid  implements Serializable {
	private final Integer GRIDLENGTH = 16;
	private final Integer EXNUMBER = 3;
	private final ExCommunication exCommunicationTiles[] = new ExCommunication[EXNUMBER];
	private Resource rewards[];

	public ExCommunication[] getExCommunicationTiles() {
		return exCommunicationTiles;
	}
	
	public Resource[] getRewards() {
		return rewards;
	}

	public void setRewards(Resource[] rewards) {
		this.rewards = rewards;
	}

	public Integer getGRIDLENGTH() {
		return GRIDLENGTH;
	}

}
