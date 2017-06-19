package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class NoMarketEx extends ExEffect implements Serializable{

	private static final long serialVersionUID = 4106194796035300031L;

	@Override
	public String getInfo() {
		return "You won't be able anymore to do Market Move";
	}

}
