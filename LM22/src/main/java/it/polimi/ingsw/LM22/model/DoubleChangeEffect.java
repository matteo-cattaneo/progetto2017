package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class DoubleChangeEffect extends ChangeEffect implements Serializable {

	private static final long serialVersionUID = -3264009999962347890L;
	private Resource exchangeEffect2[];
	
	public Resource[] getExchangeEffect2() {
		return exchangeEffect2;
	}

}
