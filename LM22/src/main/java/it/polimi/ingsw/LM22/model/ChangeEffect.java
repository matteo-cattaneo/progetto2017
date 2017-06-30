package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ChangeEffect extends ImmediateEffect implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6394703810465055798L;
	protected Resource exchangeEffect1[];

	public Resource[] getExchangeEffect1() {
		return exchangeEffect1;
	}

	@Override
	public String getInfo() {
		String info = "You can exchange " + exchangeEffect1[0].getInfo() + " to " + exchangeEffect1[1].getInfo();
		return info;
	}

}
