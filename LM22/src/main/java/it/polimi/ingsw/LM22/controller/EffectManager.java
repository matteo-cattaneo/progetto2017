package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
import it.polimi.ingsw.LM22.model.leader.ChurchSubstainEffect;
import it.polimi.ingsw.LM22.model.leader.CoinsDiscountEffect;
import it.polimi.ingsw.LM22.model.leader.CopyEffect;
import it.polimi.ingsw.LM22.model.leader.DoubleResourceEffect;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
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
	private final Integer TOWER = 0;
	private final Integer FLOOR = 0;
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

	public void manageEffect(Effect effect, Player player, Resource sum, MainGameController mainGC) {
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

	/*
	 * DEVELOPMENT CARDS' EFFECTS
	 */

	/*
	 * metodo che gestisce l'effetto in ingresso come effetto immediato di una
	 * carta
	 */
	public void resourceprivilegeeffectManage(ResourcePrivilegeEffect effect, Resource sum) throws IOException {
		r.addResource(sum, r.calculateResource(effect.getResource().clone(), player));
		r.addResource(sum, r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
	}

	public void changetoprivilegeeffectManage(ChangeToPrivilegeEffect effect, Resource sum) throws IOException {
		r.subResource(player.getPersonalBoard().getResources(), effect.getExchangedResource());
		r.addResource(sum, r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
	}

	/*
	 * metodo che gestisce l'effetto rappresentante uno scambio di risorse (non
	 * Privilegi del Consiglio)
	 */
	public void changeeffectManage(ChangeEffect effect, Resource sum) throws IOException {
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
	public void doublechangeeffectManage(DoubleChangeEffect effect, Resource sum) throws IOException {
		// se entrambi i change sono disponibili chiedo quale effettuare
		// dovrei chiedere se lo vuole fare
		if (r.enoughResources(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED])
				&& r.enoughResources(player.getPersonalBoard().getResources(), effect.getExchangeEffect2()[NEEDED])) {
			// metodo che chiede quale dei due cambi si vole effettuare
			Integer choice = mainGC.askForDoubleChange(player, effect);
			if (choice.equals(FIRST_CHANGE)) {
				r.addResource(sum, r.calculateResource(effect.getExchangeEffect1()[NEEDED + 1].clone(), player));
				r.subResource(player.getPersonalBoard().getResources(), effect.getExchangeEffect1()[NEEDED]);
			} else if (choice.equals(SECOND_CHANGE)) {
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
	public void resourcetoresourceeffectManage(ResourceToResourceEffect effect, Resource sum) {
		Integer points = sum.getMilitary() / effect.getRequirement().getMilitary();
		Resource bonus = r.resourceMultiplication(effect.getReward().clone(), points);
		bonus = r.calculateResource(bonus, player);
		r.addResource(sum, bonus);
	}

	/*
	 * gestisce un effetto di tipo CardAction
	 */
	public void cardactioneffectManage(CardActionEffect effect, Resource sum) throws IOException {
		r.addResource(player.getPersonalBoard().getResources(),
				r.calculateResource(effect.getResource().clone(), player));
		r.addResource(player.getPersonalBoard().getResources(), r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
		Resource servants = mainGC.askForServants(player);
		Integer[] info = mainGC.askForCardSpace(player, effect);
		Integer tower = info[TOWER];
		Integer floor = info[FLOOR];
		FamilyMember other = new FamilyMember(player, UNCOLORED);
		other.setValue(effect.getDiceValue());
		other.setUsed(false);
		CardMove move = new CardMove(player, other, servants, tower, floor);

		try {
			moveManager.manageMove(move);
		} catch (InvalidMoveException e) {
			mainGC.getIPlayer(player).showMsg("Effect lost!");
		}
	}

	public void workactioneffectManage(WorkActionEffect effect, Resource sum) throws IOException {
		r.addResource(sum, r.calculateResource(effect.getResource().clone(), player));
		r.addResource(sum, r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
		Resource servants = mainGC.askForServants(player);
		FamilyMember other = new FamilyMember(player, UNCOLORED);
		other.setUsed(false);
		other.setValue(effect.getWorkActionValue());
		WorkMove move = new WorkMove(player, other, servants, effect.getTypeOfWork());

		try {
			moveManager.manageMove(move);
		} catch (InvalidMoveException e) {
			mainGC.getIPlayer(player).showMsg("Work lost!");
		}

	}

	/*
	 * metodo che gestisce tale effetto PROBLEMA - se è un effetto immediato
	 * allora posso subito aggiungere il reward alle risorse del player - se
	 * invece è un effetto permanente devo poterlo sommare alla risorsa
	 * sommatrice della produzione
	 */
	public void cardtoresourceeffectManage(CardToResourceEffect effect, Resource sum) {
		Resource bonus = NOTHING;
		switch (effect.getCardRequired()) {
		case "TERRITORY":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getTerritoriesCards().size());
			break;
		case "CHARACTER":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getCharactersCards().size());
			break;
		case "BUILDING":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getBuildingsCards().size());
			break;
		case "VENTURE":
			bonus = r.resourceMultiplication(effect.getReward().clone(),
					player.getPersonalBoard().getVenturesCards().size());
			break;
		}
		r.addResource(sum, r.calculateResource(bonus.clone(), player));
	}

	public void noeffectManage(NoEffect effect, Resource sum) {
		return;
	}

	/*
	 * LEADER CARDS' EFFECTS
	 */

	public void leaderEffectManage(Effect effect, Player player, MainGameController mainGC) {
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

	public void leaderresourceeffectManage(LeaderResourceEffect effect) throws IOException {
		r.addResource(player.getPersonalBoard().getResources(),
				r.calculateResource(effect.getResource().clone(), player));
		r.addResource(player.getPersonalBoard().getResources(), r.calculateResource(
				mainGC.selectCouncilPrivilege(effect.getCouncilPrivilege(), player).clone(), player));
	}

	/*
	 * metodo che permette di gestire una nuova mossa work proveniente
	 * dall'attivazione di una carta leader
	 */
	public void workactionManage(WorkAction effect) throws IOException {
		Resource servants = mainGC.askForServants(player);
		FamilyMember other = new FamilyMember(player, UNCOLORED);
		other.setUsed(false);
		other.setValue(effect.getValueOfWork());
		WorkMove move = new WorkMove(player, other, servants, effect.getTypeOfWork());

		try {
			moveManager.manageMove(move);
		} catch (InvalidMoveException e) {
			mainGC.getIPlayer(player).showMsg("Work lost!");
		}

	}

	/*
	 * metodo che gestisce il metodo di modifica dei valori dei familiari in
	 * base all'effetto
	 */
	public void memberchangeeffectManage(MemberChangeEffect e) throws IOException {
		String color = e.getTypeOfMember();
		switch (color) {
		case "ALL":
			for (FamilyMember m : player.getMembers()) {
				if (m.getColor() != UNCOLORED)
					m.setValue(((MemberChangeEffect) e).getNewValueOfMember());
			}
			player.getEffects().add(e);
			break;
		case "UNCOLORED":
			for (FamilyMember m : player.getMembers())
				if (m.getColor().equals(UNCOLORED)) {
					m.setValue(e.getNewValueOfMember());
					player.getEffects().add(e);
					break;
				}
			break;
		case "COLORED": {
			String choice = mainGC.askForColor(player);
			for (FamilyMember m : player.getMembers())
				if (m.getColor().equals(choice)) {
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
	public void memberbonuseffectManage(MemberBonusEffect e) {
		String color = e.getTypeOfMember();
		switch (color) {
		case "ALL": {
			for (FamilyMember f : player.getMembers()) {
				f.setValue(f.getValue() + e.getValueOfBonus());
			}
		}
		}
		player.getEffects().add(e);
	}

	public void nooccupiedtowereffectManage(NoOccupiedTowerEffect effect) {
		player.getEffects().add(effect);
	}

	public void inoccupiedspaceeffectManage(InOccupiedSpaceEffect effect) {
		player.getEffects().add(effect);
	}

	public void nomilitaryrequesteffectManage(NoMilitaryRequestEffect effect) {
		player.getEffects().add(effect);
	}

	public void churchsubstaineffectManage(ChurchSubstainEffect effect) {
		player.getEffects().add(effect);
	}

	public void doubleresourceeffectManage(DoubleResourceEffect effect) {
		player.getEffects().add(effect);
	}

	public void coinsdiscounteffectManage(CoinsDiscountEffect effect) {
		player.getEffects().add(effect);
	}

	/*
	 * metodo che raggruppa tutte le carte leader giocate o attive attualmente
	 * da tutti i giocatori diversi dal richiedente di questo metodo -->
	 * raccolta la lista si chiede al player richiedente quale carta di vuole
	 * copiare
	 */
	public void copyeffectManage(CopyEffect effect) throws IOException {
		List<LeaderCard> lcards = new ArrayList<LeaderCard>();
		for (Player p : mainGC.getGame().getPlayersOrder()) {
			if (p != player) {
				for (LeaderCard card : p.getActivatedLeaderCards()) {
					lcards.add(card);
				}
				for (LeaderCard card1 : p.getLeaderCards()) {
					lcards.add(card1);
				}
			}
		}
		String choice = mainGC.askToPlayerForEffectToCopy(player, lcards);
		/*
		 * qui va modificato il comportamento in base al tipo di effetto che è
		 * stato scelto POSSIBILI SOLUZIONI - modifico l'effetto della carta da
		 * CopyEffect a quello nuovo richiesto (necessita di avere la carta
		 * leader attivata e va bene per effetti permanenti) - aggiungo alla
		 * lista degli effetti attivi quello rchiesto (a patto che non sia un
		 * effetto valido una volta per turno)
		 */
		for (LeaderCard chosen : lcards) {
			if (chosen.getName().equals(choice)) {
				player.getEffects().add(chosen.getEffect());
			}
		}

	}

}
