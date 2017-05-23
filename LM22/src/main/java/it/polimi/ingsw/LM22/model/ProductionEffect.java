package it.polimi.ingsw.LM22.model;

public class ProductionEffect {
	private HarvestEffect harvestEffect;
	private CardToResourceEffect cardToResourceEffect;
	private ChangeEffect changeEffect;

	public HarvestEffect getHarvestEffect() {
		return harvestEffect;
	}

	public CardToResourceEffect getCardToResourceEffect() {
		return cardToResourceEffect;
	}

	public ChangeEffect getChangeEffect() {
		return changeEffect;
	}

}
