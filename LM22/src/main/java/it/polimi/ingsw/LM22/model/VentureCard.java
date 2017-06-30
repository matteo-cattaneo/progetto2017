package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class VentureCard extends DevelopmentCard implements Serializable {

	private static final long serialVersionUID = 5037136863567493067L;
	private Resource cardCost1;
	private Resource[] cardCost2;
	private Resource permanentEffect;

	public Resource getCardCost1() {
		return cardCost1;
	}

	public Resource[] getCardCost2() {
		return cardCost2;
	}

	public Resource getPermanentEffect() {
		return permanentEffect;
	}

	@Override
	public String getInfo() {
		String info;
		info = "Name: " + getName() + "%n";
		info = info + "Period: " + getPeriod() + "%n";
		if (!"No resource%n".equals(getCardCost1().getInfo()))
			info = info + "Card cost:%n" + getCardCost1().getInfo();
		if (!"No resource%n".equals(getCardCost1().getInfo()) && !"No resource%n".equals(getCardCost2()[0].getInfo()))
			info = info + "Second ";
		if (!"No resource%n".equals(getCardCost2()[0].getInfo()))
			info = info + "Card cost:%nRequire:%n" + getCardCost2()[0].getInfo() + "Cost:%n"
					+ getCardCost2()[1].getInfo();
		info = info + "Immediate effect:%n" + getImmediateEffect().getInfo();
		info = info + "Permament effect:%n" + getPermanentEffect().getInfo();
		info = info + "Card type: Venture%n";
		return info;
	}

}
