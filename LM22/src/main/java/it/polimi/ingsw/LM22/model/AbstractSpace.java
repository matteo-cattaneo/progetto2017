package it.polimi.ingsw.LM22.model;

public abstract class AbstractSpace {
	protected final Integer spaceRequirement;
	
	/*costruttore in comune a tutti gli spazi azione che estendono questa classe astratta*/
	public AbstractSpace(Integer spaceRequirement){
		this.spaceRequirement = spaceRequirement;
	}
	
	public Integer getSpaceRequirement() {
		return spaceRequirement;
	}

}
