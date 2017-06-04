package it.polimi.ingsw.LM22.model.excommunication;

import it.polimi.ingsw.LM22.model.Resource;
import java.io.Serializable;

public class FinalResourceMalusEx extends ExEffect implements Serializable{

	private static final long serialVersionUID = -6074575920306989995L;
	private Resource resource;

	public Resource getResource() {
		return resource;
	}
	
	
}
