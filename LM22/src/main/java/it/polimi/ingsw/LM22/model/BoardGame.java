package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoardGame implements Serializable {

	private static final long serialVersionUID = 3178699533089217722L;
	private static final String PRODUCTION = "PRODUCTION";
	private static final Integer NUM_TOWERS = 4;
	private static final Integer NUM_MARKET = 4;
	private Tower[] towers = new Tower[NUM_TOWERS];
	private MarketSpace[] market = new MarketSpace[NUM_MARKET];
	private WorkSpace productionSpace;
	private WorkSpace harvestSpace;
	private FaithGrid faithGrid;
	private CouncilSpace councilPalace;
	private Map<String, Integer> dices = new HashMap<>();

	/*
	 * i vari bonus arbitrari degli spazi saranno da caricare da file -->
	 * ATTENZIONE con i costruttori
	 */

	public void setTowers(Tower[] towers) {
		this.towers = towers;
	}

	public void setMarket(MarketSpace[] market) {
		this.market = market;
	}

	public void setFaithGrid(FaithGrid faithGrid) {
		this.faithGrid = faithGrid;
	}

	public Tower[] getTowers() {
		return towers;
	}

	public MarketSpace[] getMarket() {
		return market;
	}

	public WorkSpace getWorkSpace(String workType) {
		if (workType == PRODUCTION) {
			return productionSpace;
		} else
			return harvestSpace;
	}

	public void setProductionSpace(WorkSpace productionSpace) {
		this.productionSpace = productionSpace;
	}

	public void setHarvestSpace(WorkSpace harvestSpace) {
		this.harvestSpace = harvestSpace;
	}

	public WorkSpace getProductionSpace() {
		return productionSpace;
	}

	public WorkSpace getHarvestSpace() {
		return harvestSpace;
	}

	public FaithGrid getFaithGrid() {
		return faithGrid;
	}

	public CouncilSpace getCouncilPalace() {
		return councilPalace;
	}

	public void setCouncilPalace(CouncilSpace councilPalace) {
		this.councilPalace = councilPalace;
	}

	public void setDices(Map<String, Integer> dices) {
		this.dices = dices;
	}

	public void setDice(String color, Integer value) {
		dices.put(color, value);
		dices.replace(color, value);
	}

	/*
	 * metodo che ritorna il valore del dado del colore passato come parametro
	 */
	public Integer getDice(String color) {
		return dices.get(color);
	}

}
