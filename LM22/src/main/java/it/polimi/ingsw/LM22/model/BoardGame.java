package it.polimi.ingsw.LM22.model;

import java.util.HashMap;


public class BoardGame {
	private final String TERRITORY = "TERRITORY";
	private final String CHARACTER = "CHARACTER";
	private final String BUILDING = "BUILDING";
	private final String VENTURE = "VENTURE";
	private Tower towers[];
	private MarketSpace market[];
	private WorkSpace productionSpace;
	private WorkSpace harvestSpace;
	private FaithGrid faithGrid;
	private CouncilSpace councilPalace;
	private HashMap<String, Integer> dices;
	
	
	public Tower[] getTowers() {
		return towers;
	}
	public void setTowers(Tower[] towers) {
		this.towers = towers;
	}
	public MarketSpace[] getMarket() {
		return market;
	}
	public void setMarket(MarketSpace[] market) {
		this.market = market;
	}
	public WorkSpace getProductionSpace() {
		return productionSpace;
	}
	public void setProductionSpace(WorkSpace productionSpace) {
		this.productionSpace = productionSpace;
	}
	public WorkSpace getHarvestSpace() {
		return harvestSpace;
	}
	public void setHarvestSpace(WorkSpace harvestSpace) {
		this.harvestSpace = harvestSpace;
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
	public HashMap<String, Integer> getDices() {
		return dices;
	}
	public void setDices(HashMap<String, Integer> dices) {
		this.dices = dices;
	}
	
}
