package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class CoinsDiscountEffect extends LeaderEffect implements Serializable {

	private static final long serialVersionUID = 7446456147584480243L;
	private Resource discount;

	public Resource getDiscount() {
		return discount;
	}

	@Override
	public String getInfo() {
		return "Everytime you want a development card you will get a discount on its cost of " + discount.getInfo();
	}

}
