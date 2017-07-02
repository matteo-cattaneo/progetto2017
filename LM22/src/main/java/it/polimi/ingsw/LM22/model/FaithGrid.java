package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;

public class FaithGrid implements Serializable {

	private static final long serialVersionUID = -1137579846689196478L;
	private final static Integer GRIDLENGTH = 16;
	private ArrayList<ExCommunication> exCommunicationTiles = new ArrayList<ExCommunication>();
	private Resource[] rewards;

	public List<ExCommunication> getExCommunicationTiles() {
		return exCommunicationTiles;
	}

	public ExCommunication getExCommunication(Integer period) {
		return exCommunicationTiles.get(period - 1);
	}

	public Resource[] getRewards() {
		return rewards;
	}

	public Resource getReward(Integer faith) {
		return rewards[faith];
	}

	public void setRewards(Resource[] rewards) {
		this.rewards = rewards;
	}

	public Integer getGRIDLENGTH() {
		return GRIDLENGTH;
	}

}
