package it.polimi.ingsw.LM22.model.excommunication;

import it.polimi.ingsw.LM22.model.Resource;
import java.io.Serializable;

public class ResourceMalusEx extends ExEffect implements Serializable {

	private static final long serialVersionUID = -3432906272232042819L;
	private Resource malus;

	public Resource getMalus() {
		return malus;
	}

}
