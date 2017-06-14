package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class NoCardSpaceBonusEffect extends PermanentEffect implements Serializable {

	private static final long serialVersionUID = 4287583019839138198L;

	@Override
	public String getInfo() {
		String info ="";
		info = info + "You can no more earn Bonuses in Card Action Spaces%n";
		return info;
	}

}
