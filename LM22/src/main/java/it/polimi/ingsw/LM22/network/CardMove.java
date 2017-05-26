package it.polimi.ingsw.LM22.network;

public class CardMove extends MemberMove {
	private String towerSelected;
	private Integer levelSelected;
	private EffectManager effectManager;

	public String getTowerSelected() {
		return towerSelected;
	}

	public Integer getLevelSelected() {
		return levelSelected;
	}

	public EffectManager getEffectManager() {
		return effectManager;
	}
}
