package it.polimi.ingsw.LM22.model;

public abstract class AbstractLeaderCard {

	private final String name;
	private final Resource resourceRequirement;
	/*decisione da prendere tra HashMap<String, Integer> per le richieste 
	 * relative alle carte oppur adottare una soluzione alternativa*/
	
	public AbstractLeaderCard(String name, Resource resourceRequirement/**/){
		this.name = name;
		this.resourceRequirement = resourceRequirement;
	}

	public String getName() {
		return name;
	}

	public Resource getResourceRequirement() {
		return resourceRequirement;
	}
	
	
	/*forse sarà presente un metodo in comune a tutti le classi che estenderanno questa
	 * che adotteranno overriding di questo metodo*/
	
	
	
}
