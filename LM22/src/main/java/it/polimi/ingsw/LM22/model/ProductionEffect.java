package it.polimi.ingsw.LM22.model;

public class ProductionEffect {
	HarvestEffect harvestEffect;
	CardToResourceEffect cardToResourceEffect;
	ChangeEffect changeEffect;

	public ProductionEffect(HarvestEffect harvestEffect, CardToResourceEffect cardToResourceEffect,
			ChangeEffect changeEffect) {
		this.harvestEffect = harvestEffect;
		this.cardToResourceEffect = cardToResourceEffect;
		this.changeEffect = changeEffect;
	}

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
