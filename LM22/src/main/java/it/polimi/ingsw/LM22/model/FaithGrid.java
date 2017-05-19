package it.polimi.ingsw.LM22.model;

public class FaithGrid {
	private final Integer GRIDLENGTH = 15;
	private final Integer EXNUMBER = 3;
	private ExInterface exCommunicationTiles[];
	private Resource rewards[];
	
	public ExInterface[] getExCommunicationTiles() {
		return exCommunicationTiles;
	}
	public void setExCommunicationTiles(ExInterface[] exCommunicationTiles) {
		this.exCommunicationTiles = exCommunicationTiles;
	}
	public Resource[] getRewards() {
		return rewards;
	}
	public void setRewards(Resource[] rewards) {
		this.rewards = rewards;
	}
	
}
