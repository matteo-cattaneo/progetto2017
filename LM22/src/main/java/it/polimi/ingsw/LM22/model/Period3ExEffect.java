package it.polimi.ingsw.LM22.model;

public class Period3ExEffect implements ExInterface{

	
	/*fatto in modo diverso dall'UML perchè non ha senso secondo
	 * me creare una nuova classe NoFinalCardPointsEx e metterci dentro
	 * come attributo solo una stringa;
	 * analogamente non ha senso per me avere EndVictoryPoints a sè stante
	 * con solo un attributo risorsa al suo interno, tanto vale mettere i due
	 * rispettivi attributi in questa classe (anche perchè tale scomuniche 
	 * non possono mai avvenire contemporaneamente*/
	private final String noFinalCardPointsEx;
	private final Resource resourcePenalty;
	
	public Period3ExEffect(String noFinalCardPointsEx, Resource resourcePenalty){
		this.noFinalCardPointsEx = noFinalCardPointsEx;
		this.resourcePenalty = resourcePenalty;
	}

	public String getNoFinalCardPointsEx() {
		return noFinalCardPointsEx;
	}

	public Resource getResourcePenalty() {
		return resourcePenalty;
	}
	
	
	
	
}
