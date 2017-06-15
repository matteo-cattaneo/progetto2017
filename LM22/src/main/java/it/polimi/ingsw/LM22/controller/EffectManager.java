package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.CardActionEffect;
import it.polimi.ingsw.LM22.model.CardToResourceEffect;
import it.polimi.ingsw.LM22.model.ChangeEffect;
import it.polimi.ingsw.LM22.model.ChangeToPrivilegeEffect;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.NoEffect;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.ResourcePrivilegeEffect;
import it.polimi.ingsw.LM22.model.ResourceToResourceEffect;
import it.polimi.ingsw.LM22.model.WorkActionEffect;
import it.polimi.ingsw.LM22.model.excommunication.ResourceMalusEx;
import it.polimi.ingsw.LM22.model.leader.ChurchSubstainEffect;
import it.polimi.ingsw.LM22.model.leader.CoinsDiscountEffect;
import it.polimi.ingsw.LM22.model.leader.DoubleResourceEffect;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderResourceEffect;
import it.polimi.ingsw.LM22.model.leader.MemberBonusEffect;
import it.polimi.ingsw.LM22.model.leader.MemberChangeEffect;
import it.polimi.ingsw.LM22.model.leader.NoMilitaryRequestEffect;
import it.polimi.ingsw.LM22.model.leader.NoOccupiedTowerEffect;
import it.polimi.ingsw.LM22.model.leader.WorkAction;

public class EffectManager {

	private static final Logger LOGGER = Logger.getLogger(EffectManager.class.getClass().getSimpleName());
	private final Integer FIRST_CHANGE = 1;
	private final Integer SECOND_CHANGE = 2;
	private final Integer NEEDED = 0;
	private final Resource NOTHING = new Resource(0, 0, 0, 0, 0, 0, 0);
	private final String UNCOLORED = "Uncolored";
	private Player player;
	private MainGameController mainGC;
	private MoveManager moveManager;
	private ResourceHandler r;

	public EffectManager(MoveManager moveManager) {
		r = new ResourceHandler();
		this.moveManager = moveManager;
	}

	public void manageEffect(Effect effect, Player player, MainGameController mainGC) {
		this.player = player;
		this.mainGC = mainGC;
		try {
			String name = effect.getClass().getSimpleName().toLowerCase() + "Manage";
			Method metodo = this.getClass().getMethod(name, new Class[] { effect.getClass() });
			if (metodo != null)
				metodo.invoke(this, new Object[] { effect });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/*
	 * DEVELOPMENT CARDS' EFFECTS
	 */

	/*
	 * metodo che gestisce l'effetto in ingresso come effetto immediato di una
	 * carta
	 */
	public void resourceprivilegeeffectManage(ResourcePrivilegeEffect effect) throws IOException {
		r.addResource(player.getPersonalBoard().getResources(),
				r.calculateResource(effect.getResource().clone(), player));
		r.addResource(player.getPersonalBoard().getResources(), r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
	}

	/*
	 * metodo che gestisce la produzione per una singola carta e che in base
	 * all'effetto che si vuole attivare invoca il metodo giusto
	 */
	public void productionHandle(Effect effect, Resource sum, Player player, MainGameController mainGC) {
		this.player = player;
		this.mainGC = mainGC;
		try {
			String name = effect.getClass().getSimpleName().toLowerCase() + "Manage";
			Method metodo = this.getClass().getMethod(name, new Class[] { effect.getClass(), sum.getClass() });
			if (metodo != null)
				metodo.invoke(this, new Object[] { effect, sum });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void changetoprivilegeeffectManage(ChangeToPrivilegeEffect effect, Resource resource) throws IOException {
		r.subResource(resource, effect.getExchangedResource());
		r.addResource(resource, r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
	}

	/*
	 * metodo che gestisce l'effetto rappresentante uno scambio di risorse (non
	 * Privilegi del Consiglio)
	 */
	public void changeeffectManage(ChangeEffect effect, Resource sum) {
		if (r.enoughResources(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED])
				&& mainGC.askChangeToPlayer(player, effect.getExchangeEffect1())) {
			r.addResource(sum, r.calculateResource(effect.getExchangeEffect1()[NEEDED + 1].clone(), player));
			r.subResource(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED]);
		}
		return;
	}

	/*
	 * metodo che gestisce l'effetto con due possibili scambi --> controlla se
	 * almeno uno dei due è fattibile (altrimenti esce) --> se solo uno è
	 * fattibile usa askChangeToPlayer() --> altrimenti usa un altro metodo
	 * (ancora da implementare)
	 */
	public void doublechangeeffectManage(DoubleChangeEffect effect, Resource sum) {
		// se entrambi i change sono disponibili chiedo quale effettuare
		// dovrei chiedere se lo vuole fare
		if (r.enoughResources(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED])
				&& r.enoughResources(player.getPersonalBoard().getResources(), effect.getExchangeEffect2()[NEEDED])) {
			// metodo che chiede quale dei due cambi si vole effettuare
			Integer choice = mainGC.askForDoubleChange(effect);
			if (choice == FIRST_CHANGE) {
				r.addResource(sum, r.calculateResource(effect.getExchangeEffect1()[NEEDED + 1].clone(), player));
				r.subResource(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED]);
			} else if (choice == SECOND_CHANGE) {
				r.addResource(sum, r.calculateResource(effect.getExchangeEffect2()[NEEDED + 1].clone(), player));
				r.subResource(player.getPersonalBoard().getResources(), effect.getExchangeEffect2()[NEEDED]);
			}
		} else if (r.enoughResources(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED])
				&& mainGC.askChangeToPlayer(player, effect.getExchangeEffect1())) {
			r.addResource(sum, r.calculateResource(effect.getExchangeEffect1()[NEEDED + 1].clone(), player));
			r.subResource(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED]);
		} else if (r.enoughResources(player.getPersonalBoard().getResources(), effect.getExchangeEffect2()[NEEDED])
				&& mainGC.askChangeToPlayer(player, effect.getExchangeEffect2())) {
			r.addResource(sum, r.calculateResource(effect.getExchangeEffect2()[NEEDED + 1].clone(), player));
			r.subResource(player.getPersonalBoard().getResources(), effect.getExchangeEffect2()[NEEDED]);
		}
	}

	/*
	 * metodo che gestisce il raccolto per ogni singola carta e che in base
	 * all'effetto che si vuole attivare invoca il metodo giusto
	 */
	public void harvestHandle(Effect effect, Resource resource, Player player, MainGameController mainGC)
			throws IOException {
		this.player = player;
		this.mainGC = mainGC;
		try {
			String name = effect.getClass().getSimpleName().toLowerCase() + "Manage";
			Method metodo = this.getClass().getMethod(name, new Class[] { effect.getClass(), resource.getClass() });
			if (metodo != null)
				metodo.invoke(this, new Object[] { effect, resource });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/*
	 * gestisce l'effetto del Generale (carta Character)
	 */
	public void resourcetoresourceeffectManage(ResourceToResourceEffect effect) {
		Integer points = player.getPersonalBoard().getResources().getMilitary() / effect.getRequirement().getMilitary();
		Resource bonus = r.resourceMultiplication(effect.getReward().clone(), points);
		bonus = r.calculateResource(bonus, player);
		r.addResource(player.getPersonalBoard().getResources(), bonus);
	}

	/*
	 * gestisce un effetto di tipo CardAction
	 */
	public void cardactioneffectManage(CardActionEffect effect) throws IOException, InvalidMoveException {
		r.addResource(player.getPersonalBoard().getResources(),
				r.calculateResource(effect.getResource().clone(), player));
		r.addResource(player.getPersonalBoard().getResources(), r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
		Resource servants = mainGC.askForServants(player);
		Integer[] info = mainGC.askForCardSpace(player, effect);
		Integer tower = info[0];
		Integer floor = info[1];
		FamilyMember other = new FamilyMember(player, UNCOLORED);
		other.setValue(effect.getDiceValue());
		other.setUsed(false);
		CardMove move = new CardMove(player, other, servants, tower, floor);
		//TODO
		moveManager.manageMove(move);
	}

	public void workactioneffectManage(WorkActionEffect effect) throws IOException, InvalidMoveException {
		r.addResource(player.getPersonalBoard().getResources(),
				r.calculateResource(effect.getResource().clone(), player));
		r.addResource(player.getPersonalBoard().getResources(), r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
		Resource servants = mainGC.askForServants(player);
		FamilyMember other = new FamilyMember(player, UNCOLORED);
		other.setUsed(false);
		other.setValue(effect.getWorkActionValue());
		WorkMove move = new WorkMove(player, other, servants, effect.getTypeOfWork());
		//TODO
		moveManager.manageMove(move);
	}

	/*
	 * metodo che gestisce tale effetto PROBLEMA - se è un effetto immediato
	 * allora posso subito aggiungere il reward alle risorse del player - se
	 * invece è un effetto permanente devo poterlo sommare alla risorsa
	 * sommatrice della produzione
	 */
	public void cardtoresourceeffectManage(CardToResourceEffect effect, Resource resource) {
		Resource bonus = NOTHING;
		switch (effect.getCardRequired()) {
		case "TERRITORY":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getTerritoriesCards().size());
		case "CHARACTER":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getCharactersCards().size());
		case "BUILDING":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getBuildingsCards().size());
		case "VENTURE":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getVenturesCards().size());
		}
		r.addResource(resource, r.calculateResource(bonus.clone(), player));
	}

	public void noeffectManage(NoEffect effect) {
		return;
	}

	/*
	 * LEADER CARDS' EFFECTS
	 */

	public void leaderresourceeffectManage(LeaderResourceEffect effect) throws IOException {
		r.addResource(player.getPersonalBoard().getResources(),
				r.calculateResource(effect.getResource().clone(), player));
		r.addResource(player.getPersonalBoard().getResources(), r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
	}

	/*
	 * da decidere se mettere il throws oppure usare il try catch TODO
	 */
	public void workactionManage(WorkAction effect) throws IOException, InvalidMoveException{
		Resource servants = mainGC.askForServants(player);
		FamilyMember other = new FamilyMember(player, UNCOLORED);
		other.setUsed(false);
		other.setValue(effect.getValueOfWork());
		WorkMove move = new WorkMove(player, other, servants, effect.getTypeOfWork());
		//TODO
		moveManager.manageMove(move);
	}
	
	
	/*
	 * metodo che gestisce il metodo di modifica dei valori dei familiari in
	 * base all'effetto
	 */
	public void memberchangeeffectManage(MemberChangeEffect e, Player p) {
		String color = e.getTypeOfMember();
		switch (color) {
		case "ALL":
			for (FamilyMember m : p.getMembers()) {
				if (m.getColor() != UNCOLORED)
					m.setValue(((MemberChangeEffect) e).getNewValueOfMember());
			}
			break;
		case "UNCOLORED":
			for (FamilyMember m : p.getMembers())
				if (m.getColor() == UNCOLORED) {
					m.setValue(e.getNewValueOfMember());
					break;
				}
		case "COLORED": {
			String choice = mainGC.askForColor(p);
			for (FamilyMember m : p.getMembers())
				if (m.getColor() == choice) {
					m.setValue(e.getNewValueOfMember());
					break;
				}
		}
		}
	}

	/*
	 * metodo che gestisce tale effetto, ossia l'aumento del valore del proprio
	 * familiare
	 */
	public void memberbonuseffectManage(MemberBonusEffect e, Player p) {
		String color = e.getTypeOfMember();
		switch (color) {
		case "ALL": {
			for (FamilyMember f : p.getMembers()) {
				f.setValue(f.getValue() + e.getValueOfBonus());
			}
		}
		}
	}
	
	public void nooccupiedtowereffectManage(NoOccupiedTowerEffect effect){
		player.getEffects().add(effect);
	}

	public void inoccupiedspaceeffectManage(InOccupiedSpaceEffect effect){
		player.getEffects().add(effect);
	}
	
	public void nomilitaryrequesteffectManage(NoMilitaryRequestEffect effect){
		player.getEffects().add(effect);
	}
	
	public void churchsubstaineffectManage(ChurchSubstainEffect effect){
		player.getEffects().add(effect);
	}
	
	public void doubleresourceeffectManage(DoubleResourceEffect effect){
		player.getEffects().add(effect);
	}
	
	public void coinsdiscounteffectManage(CoinsDiscountEffect effect){
		player.getEffects().add(effect);
	}
	
}
