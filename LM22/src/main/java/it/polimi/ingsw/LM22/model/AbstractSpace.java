package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public abstract class AbstractSpace  implements Serializable {
	private static final long serialVersionUID = 1L;
	protected final Integer spaceRequirement;
	
	/*
	 * costruttore in comune a tutti gli spazi azione 
	 * che estendono questa classe astratta
	 * SEBBENE SIANO DA CARICARE DA FILE QUINDI
	 * POTREBBE ESSERE INUTILE
	 * */
	public AbstractSpace(Integer spaceRequirement){
		this.spaceRequirement = spaceRequirement;
	}
	
	public Integer getSpaceRequirement() {
		return spaceRequirement;
	}

}
