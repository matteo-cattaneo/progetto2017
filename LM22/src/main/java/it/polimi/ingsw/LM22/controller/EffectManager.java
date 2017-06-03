package it.polimi.ingsw.LM22.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.polimi.ingsw.LM22.model.CardActionEffect;
import it.polimi.ingsw.LM22.model.CardToResourceEffect;
import it.polimi.ingsw.LM22.model.ChangeEffect;
import it.polimi.ingsw.LM22.model.ChangeToPrivilegeEffect;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.NoEffect;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.ResourcePrivilegeEffect;
import it.polimi.ingsw.LM22.model.ResourceToResourceEffect;
import it.polimi.ingsw.LM22.model.WorkActionEffect;

public class EffectManager {
	Player player;
	MainGameController mainGC;

	public void manageEffect(Effect effect, Player player, MainGameController mainGC) {
		this.player = player;
		this.mainGC = mainGC;
		try {
			String name = effect.getClass().getSimpleName().toLowerCase() + "Manage";
			Method metodo = this.getClass().getMethod(name, new Class[] { effect.getClass() });
			if (metodo != null)
				metodo.invoke(this, new Object[] { effect });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void resourceprivilegeeffectManage(ResourcePrivilegeEffect effect) {
		// rivedere ResourceHandler
		ResourceHandler r = new ResourceHandler();
		r.addResource(player.getPersonalBoard().getResources(), effect.getResource());
		mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege());
	}

	private void resourcetoresourceeffectManage(ResourceToResourceEffect effect) {

	}

	private void cardactioneffectManage(CardActionEffect effect) {

	}

	private void workactioneffectManage(WorkActionEffect effect) {

	}

	private void cardtoresourceeffectManage(CardToResourceEffect effect) {

	}

	private void changeeffectManage(ChangeEffect effect) {

	}

	private void doublechangeeffectManage(DoubleChangeEffect effect) {

	}

	private void changetoprivilegeeffectManage(ChangeToPrivilegeEffect effect) {

	}

	public void noeffectManage(NoEffect effect) {

	}
}
