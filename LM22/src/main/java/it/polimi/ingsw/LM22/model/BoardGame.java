package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.HashMap;

public class BoardGame implements Serializable {

	private static final long serialVersionUID = 3178699533089217722L;
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
	private MarketSpace market[] = new MarketSpace[4];
	private WorkSpace productionSpace = new WorkSpace(1, PRODUCTION);
	private WorkSpace harvestSpace = new WorkSpace(1, HARVEST);
	private FaithGrid faithGrid = new FaithGrid();
	private CouncilSpace councilPalace = new CouncilSpace(1, new Resource(0, 0, 0, 1, 0, 0, 0), 1);
	private HashMap<String, Integer> dices = new HashMap<String, Integer>();

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
