package it.polimi.ingsw.LM22.model;

public class ResourceMalusEx {
	
	private final Integer period;
	private final Resource resourceMalus;
	
	public ResourceMalusEx(Integer period, Resource resourceMalus){
		this.period = period;
		this.resourceMalus = resourceMalus;
	}

	public Integer getPeriod() {
		return period;
	}

	public Resource getResourceMalus() {
		return resourceMalus;
	}
	
	
	
	
}
