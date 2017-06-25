package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class DoubleChangeEffect extends ChangeEffect implements Serializable {

	private static final long serialVersionUID = -3264009999962347890L;
	private Resource exchangeEffect2[];

	public Resource[] getExchangeEffect2() {
		return exchangeEffect2;
	}

	public String getInfo() {
		String info;
		info = "You can exchange " + exchangeEffect1[0].getInfo() + "to " + exchangeEffect1[1].getInfo() + " OR ";
		info = info + "You can exchange " + exchangeEffect2[0].getInfo() + "to " + exchangeEffect2[1].getInfo();
		return info;
	}

}
