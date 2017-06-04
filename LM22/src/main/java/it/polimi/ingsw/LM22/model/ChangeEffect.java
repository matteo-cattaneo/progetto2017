package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ChangeEffect extends ImmediateEffect implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6394703810465055798L;
	private Resource exchangeEffect1[];
	
	public Resource[] getExchangeEffect1() {
		return exchangeEffect1;
	}

}
