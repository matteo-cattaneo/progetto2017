package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.HashMap;

public class BoardGame implements Serializable {
	/*
	 * costanti da mettere forse nel controller?
	 */
	private final String TERRITORY = "TERRITORY";
	private final String CHARACTER = "CHARACTER";
	private final String BUILDING = "BUILDING";
	private final String VENTURE = "VENTURE";
	private final String PRODUCTION = "PRODUCTION";
	private final String HARVEST = "HARVEST";
	private final Integer NUM_TOWERS = 4;
	private Tower towers[] = new Tower[NUM_TOWERS];
	private MarketSpace market[];
	private WorkSpace productionSpace = new WorkSpace(1, PRODUCTION);
	private WorkSpace harvestSpace = new WorkSpace(1, HARVEST);
	private FaithGrid faithGrid = new FaithGrid();
	private CouncilSpace councilPalace;
	private HashMap<String, Integer> dices;

	/*
	 * i vari bonus arbitrari degli spazi saranno da caricare da file -->
	 * ATTENZIONE con i costruttori
	 */

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

	public WorkSpace getProductionSpace() {
		return productionSpace;
	}

	public WorkSpace getHarvestSpace() {
		return harvestSpace;
	}

	public FaithGrid getFaithGrid() {
		return faithGrid;
	}

	public void setFaithGrid(FaithGrid faithGrid) {
		this.faithGrid = faithGrid;
	}

	public CouncilSpace getCouncilPalace() {
		return councilPalace;
	}

	public void setCouncilPalace(CouncilSpace councilPalace) {
		this.councilPalace = councilPalace;
	}

	// public HashMap<String, Integer> getDices() {
	// return dices;
	// }

	public void setDices(HashMap<String, Integer> dices) {
		this.dices = dices;
	}

	public void setDice(String color, Integer value) {
		dices.replace(color, value);
	}

	/*
	 * metodo che ritorna il valore del dado del colore passato come parametro
	 */
	public Integer getDice(String color) {
		return dices.get(color);
	}

}
