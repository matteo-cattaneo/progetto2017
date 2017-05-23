package it.polimi.ingsw.LM22.model;

public class Period1ExEffect implements ExInterface{
	
	private final ResourceMalusEx resourceMalusEx;
	private final WorkMalusEx workMalusEx;
	private final DiceMalusEx diceMalusEx;
	
	public Period1ExEffect(ResourceMalusEx resourceMalusEx, WorkMalusEx workMalusEx, DiceMalusEx diceMalusEx){
		this.resourceMalusEx = resourceMalusEx;
		this.workMalusEx = workMalusEx;
		this.diceMalusEx = diceMalusEx;
	}
	
	public ResourceMalusEx getResourceMalusEx() {
		return resourceMalusEx;
	}
	
	public WorkMalusEx getWorkMalusEx() {
		return workMalusEx;
	}
	
	public DiceMalusEx getDiceMalusEx() {
		return diceMalusEx;
	}
	
	
	
	
}
