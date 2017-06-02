package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class CoinsDiscountEffect extends LeaderEffect implements Serializable{

	private Resource discount;

	public Resource getDiscount() {
		return discount;
	}
	
	
}
