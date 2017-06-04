package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class ExCommunication implements Serializable{
	
	private static final long serialVersionUID = -4587740058082723812L;
	private Integer period;
	private ExEffect effect;
	
	public Integer getPeriod() {
		return period;
	}
	
	public ExEffect getEffect() {
		return effect;
	}
	
		
}
