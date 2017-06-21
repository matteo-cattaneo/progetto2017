package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class LeaderResourceEffect extends LeaderEffect implements Serializable {

	private static final long serialVersionUID = 1724300998820029020L;
	private Resource resource;
	private Integer councilPrivilege;

	public Resource getResource() {
		return resource;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}

	@Override
	public String getInfo() {
		String info = "You will get " + resource.getInfo();
		if (councilPrivilege > 0)
			info = info + " and " + councilPrivilege + " councilPrivilege(s)%n";
		return info;
	}

}
