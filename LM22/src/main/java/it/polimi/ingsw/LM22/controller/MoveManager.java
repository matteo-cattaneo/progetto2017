package it.polimi.ingsw.LM22.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import it.polimi.ingsw.LM22.model.excommunication.DiceCardMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.NoMarketEx;
import it.polimi.ingsw.LM22.model.leader.CardRequest;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderCardRequest;
import it.polimi.ingsw.LM22.model.leader.MemberBonusEffect;
import it.polimi.ingsw.LM22.model.leader.MemberChangeEffect;
import it.polimi.ingsw.LM22.model.leader.NoMilitaryRequestEffect;
import it.polimi.ingsw.LM22.model.leader.NoOccupiedTowerEffect;
import it.polimi.ingsw.LM22.model.leader.ResourceCardRequest;
import it.polimi.ingsw.LM22.model.leader.ResourceRequest;
import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CardSpace;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.ColorCardBonusEffect;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.NoCardSpaceBonusEffect;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;

public class MoveManager {

	private final Integer SINGLE_PRIVILEGE = 1;
	private final Resource NOTHING = new Resource(0, 0, 0, 0, 0, 0, 0);
	private final Resource THREE_COINS = new Resource(0, 0, 0, 3, 0, 0, 0);
	private final String PRODUCTION = "PRODUCTION";
	private final String HARVEST = "HARVEST";
	private final Integer WORK_MALUS = 3;
	private final Integer THIRD_TERRITORY = 3;
	private final Integer FOURTH_TERRITORY = 7;
	private final Integer FIFTH_TERRITORY = 12;
	private final Integer SIXTH_TERRITORY = 18;
	private final Integer MAX_NUM_CARDS = 6;
	// private final Integer TERRITORY = 0;
	// private final Integer CHARACTER = 1;
	// private final Integer BUILDING = 2;
	// private final Integer VENTURE = 3;
	private Game game;
	private MainGameController mainGame;
	private ResourceHandler resourceHandler = new ResourceHandler();
	public EffectManager effectManager;

	public MoveManager(Game game, MainGameController mainGame) {
		this.game = game;
		this.mainGame = mainGame;
	}

	/*
	 * metodo che utilizzando la reflection chiama il metodo di check giusto e
	 * successivamente (se il check dà esito positivo) anche il metodo di manage
	 * della mossa giusto
	 */
	public void manageMove(AbstractMove move) {
		boolean checkResult = false;
		String name;
		Method method;
		if (move instanceof MemberMove) {
			if (((MemberMove) move).getMemberUsed().isUsed())
				// dobbiamo segnalare al giocatore che la mossa non è valida
				// --> InvalidMoveException ?
				return;
		}
		try {
			name = move.getClass().getSimpleName().toLowerCase() + "Allowed";
			method = this.getClass().getMethod(name, new Class[] { move.getClass() });
			checkResult = (boolean) method.invoke(this, new Object[] { move });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {

		}
		if (checkResult) {
			try {
				name = move.getClass().getSimpleName().toLowerCase() + "Handle";
				method = this.getClass().getMethod(name, new Class[] { move.getClass() });
				method.invoke(this, new Object[] { move });
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

			}
		} else
			// throw new InvalidMoveException
			return;
	}

	/*
	 * controlla se una mossa del tipo CardMove è ammessa o no
	 */
	private boolean cardmoveAllowed(CardMove cardMove) {
		if (!checkMaxNumCardLimit(cardMove))
			return false;
		if (!checkCardSpace(cardMove))
			return false;
		// check se la torre è già occupata da un familiare dello stesso player
		if (game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getColoredMembersOnIt()
				.contains(cardMove.getPlayer().getColor()))
			return false;
		if (!checkCardCost(cardMove))
			return false;
		return true;
	}

	/*
	 * controlla se ho già sei carte di quel tipo nella PersonalBoard
	 */
	private boolean checkMaxNumCardLimit(CardMove cardMove) {
		switch (cardMove.getTowerSelected()) {
		case 0:
			if (cardMove.getPlayer().getPersonalBoard().getTerritoriesCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		case 1:
			if (cardMove.getPlayer().getPersonalBoard().getCharactersCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		case 2:
			if (cardMove.getPlayer().getPersonalBoard().getBuildingsCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		case 3:
			if (cardMove.getPlayer().getPersonalBoard().getVenturesCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		}
		return true;
	}

	/*
	 * esegue una serie di controlli sul posizionamento del familiare e se lo
	 * posso mettere guadagno l'ipotetico bonus relativo allo specifico spazio
	 * azione
	 */
	private boolean checkCardSpace(CardMove cardMove) {
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		if (space.getMember() != null)
			if (!containsClass(cardMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class))
				return false;
		if (space.getSpaceRequirement() > (cardMove.getMemberUsed().getValue()
				+ cardMove.getServantsAdded().getServants()))
			return false;
		if (containsClass(cardMove.getPlayer().getEffects(), DiceCardMalusEx.class)) {
			for (Effect e : cardMove.getPlayer().getEffects()) {
				if (e instanceof DiceCardMalusEx) {
					if (((DiceCardMalusEx) e).getCardType().equals(cardMove.getTowerSelected())) {
						if (space.getSpaceRequirement() > (cardMove.getMemberUsed().getValue()
								+ cardMove.getServantsAdded().getServants() - ((DiceCardMalusEx) e).getMalus()))
							return false;
					}
					break;
				}
			}
		}
		return true;
	}

	/*
	 * metodo che gestisce il controllo del costo di una carta, gestendo anche
	 * tutti i relativi effetti che possono essere applicati ad una mossa di
	 * tipo CardMove
	 */
	private boolean checkCardCost(CardMove cardMove) {
		/*
		 * importante tenere conto per il check sul costo i servitori che un
		 * player può già aver giocato sottraendoli nella disequazione di
		 * controllo del costo
		 */
		int tower = cardMove.getTowerSelected();
		Tower t = game.getBoardgame().getTowers()[tower];
		int floor = cardMove.getLevelSelected();
		DevelopmentCard card = game.getBoardgame().getTowers()[tower].getFloor()[floor].getCard();
		Resource bonus = calculateBonus(t, cardMove);
		Resource additionalCost = calculateAdditionalCost(t, cardMove);
		Resource cardCost = NOTHING;
		switch (tower) {
		case 3:
			// problema sarebbe gestire il doppio costo
			// --> se valido solo 1 faccio comunque la mossa
			// se invece doppio devo chiedere al'utente quale vuole utilizzare
			resourceHandler.manageVentureCost(cardMove);
			break;
		case 2:
			// aggiungere controlli su effetti permanenti per riduzione costo
			// carte
			ColorCardBonusEffect e = new ColorCardBonusEffect();
			// CONTROLLO SE HO EFFETTI DI QUALSIASI TIPO SU QUELLA TORRE
			// if (cardMove.getPlayer().getEffects()
			// .contains(e.getCardType() == "BUILDING" && e.getCardDiscount() !=
			// null))
			cardCost = ((BuildingCard) card).getCost();
			if (!resourceHandler.enoughResources(cardCost, cardMove, additionalCost, bonus))
				return false;
			break;
		case 1:
			// aggiungere controlli su effetti permanenti per riduzione costo
			// carte
			cardCost = ((CharacterCard) card).getCost();
			if (!resourceHandler.enoughResources(cardCost, cardMove, additionalCost, bonus))
				return false;
			break;
		case 0:
			if (!militaryPointsAvailable(cardMove, bonus)
					|| !resourceHandler.enoughResources(cardCost, cardMove, additionalCost, bonus))
				return false;
			break;
		}
		// ISTRUZIONE DA ESEGUIRE NELL'HANDLE
		// resourceHandler.addResource(cardMove.getPlayer().getPersonalBoard().getResources(),
		// bonus);
		return true;
	}

	/*
	 * metodo che calcola il bonus ottenuto dallo spazio azione del floor
	 * relativo
	 */
	private Resource calculateBonus(Tower t, CardMove move) {
		Resource bonus;
		CardSpace space = searchCardSpace(move.getTowerSelected(), move.getLevelSelected());
		if (move.getPlayer().getEffects().contains(NoCardSpaceBonusEffect.class))
			bonus = NOTHING;
		else {
			// da controllare se ho le scomuniche che riducono il numero di
			// risorse acquisite
			bonus = space.getReward();
		}
		return bonus;
	}

	/*
	 * calcola il possibile costo addizionale dovuto alla torre occupata -->
	 * gestisce anche l'effetto di Filippo Brunelleschi questo prezzo non può
	 * essere pagato con il bonus dello spazio azione
	 */
	private Resource calculateAdditionalCost(Tower t, CardMove move) {
		Resource additionalCost = NOTHING;
		boolean occupied = t.isOccupied();
		boolean hasBrunelleschi = containsClass(move.getPlayer().getEffects(), NoOccupiedTowerEffect.class);
		if (occupied && !hasBrunelleschi)
			additionalCost = THREE_COINS;
		else if (!occupied || hasBrunelleschi)
			additionalCost = NOTHING;
		return additionalCost;
	}

	/*
	 * controlla (solo per una CardMove per una TERRITORY) se il player soddisfa
	 * i requisiti relativi ai punti militari --> controlla anche se è stata
	 * attivata la carta Leader che annulla questo controllo
	 */
	private boolean militaryPointsAvailable(CardMove cardMove, Resource bonus) {
		if (!containsClass(cardMove.getPlayer().getEffects(), NoMilitaryRequestEffect.class)) {
			switch (cardMove.getPlayer().getPersonalBoard().getTerritoriesCards().size()) {
			case 0:
			case 1:
				return true;
			case 2:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary()
						+ bonus.getMilitary() < THIRD_TERRITORY)
					return false;
				return true;
			case 3:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary()
						+ bonus.getMilitary() < FOURTH_TERRITORY)
					return false;
				return true;
			case 4:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary()
						+ bonus.getMilitary() < FIFTH_TERRITORY)
					return false;
				return true;
			case 5:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary()
						+ bonus.getMilitary() < SIXTH_TERRITORY)
					return false;
				return true;
			default:
				return false;
			}
		}
		return true;
	}

	private CardSpace searchCardSpace(Integer towerSelected, Integer levelSelected) {
		return game.getBoardgame().getTowers()[towerSelected].getFloor()[levelSelected].getSpace();
	}

	/*
	 * gestisce una mossa di tipo CardMove
	 */
	private void cardmoveHandle(CardMove cardMove) {

	}

	/*
	 * controlla se una mossa del tipo MarketMove è ammessa o no
	 */
	private boolean marketmoveAllowed(MarketMove marketMove) {
		// check per controllare se ho scomunica del tipo NoMarkeEx
		if (containsClass(marketMove.getPlayer().getEffects(), NoMarketEx.class))
			return false;
		int pos = marketMove.getMarketSpaceSelected();
		if (game.getBoardgame().getMarket()[pos].getMember() != null)
			if (!containsClass(marketMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class))
				return false;
		if (marketMove.getMemberUsed().getValue()
				+ marketMove.getServantsAdded().getServants() < game.getBoardgame().getMarket()[pos]
						.getSpaceRequirement())
			return false;
		return true;
	}

	/*
	 * gestisce una mossa del tipo MarketMove
	 */
	private void marketmoveHandle(MarketMove marketMove) {
		int opt = marketMove.getMarketSpaceSelected();
		game.getBoardgame().getMarket()[opt].setMember(marketMove.getMemberUsed());
		marketMove.getMemberUsed().setUsed(true);
		/*
		 * prendo le varie risorse (i vari premi contenuti nei MarketSpace)
		 */
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				game.getBoardgame().getMarket()[opt].getReward());
		resourceHandler.subResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				marketMove.getServantsAdded());
		mainGame.selectCouncilPrivilege(game.getBoardgame().getMarket()[opt].getCouncilPrivilege());
	}

	/*
	 * controlla se una mossa del tipo WorkMove è ammessa o no
	 */
	private boolean workMoveAllowed(WorkMove workMove) {
		return checkWorkSpace(workMove);
	}

	/*
	 * controlla se il valore del familiare utilizzato + servitori soddisfa il
	 * requisito relativo allo spazio azione selezionato per la mossa + se il
	 * primo spazio è già occupato effettua una diminuzione del valore
	 * dell'azione in base al Malus specificato nelle regole
	 */
	private boolean checkWorkSpace(WorkMove workMove) {
		if (!containsClass(workMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class)) {
			switch (game.getPlayers().length) {
			case 2:
				if (workMove.getWorkType() == "PRODUCTION") {
					if (!game.getBoardgame().getProductionSpace().getMembers().isEmpty())
						return false;
					break;
				} else {
					if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty())
						return false;
					break;
				}
			case 3:
			case 4:
				if (workMove.getWorkType() == "PRODUCTION") {
					if (game.getBoardgame().getProductionSpace().getColoredMemberOnIt()
							.contains(workMove.getPlayer().getColor()))
						return false;
					if (!game.getBoardgame().getProductionSpace().getMembers().isEmpty())
						workMove.getMemberUsed().setValue(workMove.getMemberUsed().getValue() - WORK_MALUS);
					break;
				} else {
					if (game.getBoardgame().getHarvestSpace().getColoredMemberOnIt()
							.contains(workMove.getPlayer().getColor()))
						return false;
					if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty())
						workMove.getMemberUsed().setValue(workMove.getMemberUsed().getValue() - WORK_MALUS);
					break;
				}
			default:
				return false;
			}
		}
		return true;
	}

	/*
	 * gestisce una mossa del tipo WorkMove
	 */
	private void workMoveHandle(WorkMove workMove) {
		if (workMove.getWorkType() == PRODUCTION)
			productionHandle();
	}

	/*
	 * metodo che si occupa della gestione di una azione di produzione --> devo
	 * sempre controllare le carte del player singolarmente ma solo alla fine
	 * della produzione darò tutti i bonus al player perchè alcuni effetti vanno
	 * attivati solo con le risorse già possedute dal player --> bisogna sempre
	 * chiedere al player se vuole o no ottenere tipo il cambio risorse e anche
	 * chiedere quale attivare nel caso di DoubleChangeEffect
	 */
	private void productionHandle() {

	}

	/*
	 * gestisce la fase di Raccolto
	 */
	private void harvestHandle(WorkMove move) {
		Integer valueOfAction = move.getMemberUsed().getValue() + move.getServantsAdded().getServants();
		resourceHandler.subResource(move.getPlayer().getPersonalBoard().getResources(), move.getServantsAdded());
		Resource total = NOTHING;
		for (TerritoryCard card : move.getPlayer().getPersonalBoard().getTerritoriesCards()) {
			if (valueOfAction >= card.getRequirement())
				effectManager.workHandle(card.getPermanentEffect(), total);
		}
		effectManager.workHandle(move.getPlayer().getPersonalBoard().getBonusBoard().getHarvestEffect(), total);
		resourceHandler.addResource(move.getPlayer().getPersonalBoard().getResources(), total);
	}

	/*
	 * controlla se una mossa del tipo CouncilMove è ammessa o no
	 */
	private boolean councilmoveAllowed(CouncilMove councilMove) {
		if (councilMove.getMemberUsed().getValue() + councilMove.getServantsAdded().getServants() < game.getBoardgame()
				.getCouncilPalace().getSpaceRequirement())
			return false;
		return true;
	}

	/*
	 * gestisce una mossa del tipo CouncilMove
	 */
	private void councilmoveHandle(CouncilMove councilMove) {
		councilMove.getMemberUsed().setUsed(true);
		game.getBoardgame().getCouncilPalace().getMembers().add(councilMove.getMemberUsed());
		/*
		 * prendo le varie risorse (moneta oppure privilegi del consiglio)
		 */
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				game.getBoardgame().getCouncilPalace().getReward());
		mainGame.selectCouncilPrivilege(game.getBoardgame().getCouncilPalace().getCouncilPrivilege());
	}

	private boolean leadercardsellingAllowed(LeaderCardSelling move) {
		return true;
	}

	/*
	 * gestisce la vendita di una carta leader e consentirà la scelta del
	 * privilegio del consiglio + se la carta era attiva devo anche togliere il
	 * suo effetto dalla lista degli effetti attualmente attivi
	 */
	private void leadercardsellingHandle(LeaderCardSelling move) {
		if (move.getPlayer().getLeaderCards().contains(move.getLeaderCard()))
			move.getPlayer().getLeaderCards().remove(move.getLeaderCard());
		else {
			if (move.getLeaderCard().getEffect() instanceof MemberBonusEffect || move.getLeaderCard().getEffect() instanceof MemberChangeEffect)
				//gestisco la rimozione di un effetto che modifica il valore dei familiari
			move.getPlayer().getEffects().remove(move.getLeaderCard().getEffect());
			move.getPlayer().getActivatedLeaderCards().remove(move.getLeaderCard());
		}
		mainGame.selectCouncilPrivilege(SINGLE_PRIVILEGE);
	}

	/*
	 * controlla se i requisiti della carta leader che il player vuole attivare
	 * sono effettivmanete soddisfatti e nel caso indica quella carta come
	 * attivata --> sarà poi gestita dai vari controlli comunque interni a
	 * questa classe
	 */
	private boolean leadercardactivationAllowed(LeaderCardActivation move) {
		LeaderCardRequest req = move.getLeaderCard().getRequest();
		if (move.getLeaderCard().getName() == "Lucrezia Borgia") {
			CardRequest r = ((CardRequest) move.getLeaderCard().getRequest());
			if (r.getTerritoryCards() <= move.getPlayer().getPersonalBoard().getTerritoriesCards().size()
					|| r.getCharacterCards() <= move.getPlayer().getPersonalBoard().getCharactersCards().size()
					|| r.getBuildingCards() <= move.getPlayer().getPersonalBoard().getBuildingsCards().size()
					|| r.getVentureCards() <= move.getPlayer().getPersonalBoard().getVenturesCards().size()) {
				return true;
			} else
				return false;
		}
		if (req instanceof CardRequest) {
			CardRequest r = ((CardRequest) move.getLeaderCard().getRequest());
			if (r.getTerritoryCards() > move.getPlayer().getPersonalBoard().getTerritoriesCards().size()
					|| r.getCharacterCards() > move.getPlayer().getPersonalBoard().getCharactersCards().size()
					|| r.getBuildingCards() > move.getPlayer().getPersonalBoard().getBuildingsCards().size()
					|| r.getVentureCards() > move.getPlayer().getPersonalBoard().getVenturesCards().size())
				return false;
		} else if (req instanceof ResourceRequest) {
			ResourceRequest r = ((ResourceRequest) move.getLeaderCard().getRequest());
			if (!resourceHandler.enoughResources(move.getPlayer().getPersonalBoard().getResources(), r.getResource()))
				return false;
		} else if (req instanceof ResourceCardRequest) {
			ResourceCardRequest r = ((ResourceCardRequest) move.getLeaderCard().getRequest());
			if ((r.getTerritoryCards() > move.getPlayer().getPersonalBoard().getTerritoriesCards().size()
					|| r.getCharacterCards() > move.getPlayer().getPersonalBoard().getCharactersCards().size()
					|| r.getBuildingCards() > move.getPlayer().getPersonalBoard().getBuildingsCards().size()
					|| r.getVentureCards() > move.getPlayer().getPersonalBoard().getVenturesCards().size())
					|| !resourceHandler.enoughResources(move.getPlayer().getPersonalBoard().getResources(),
							r.getResource()))
				return false;
		}
		return true;
	}

	/*
	 * metodo che gestisce la procedura di attivazione di una carta
	 */
	private void leadercardactivationHandle(LeaderCardActivation move) {
		// move.getPlayer().getEffects().add(move.getLeaderCard().getEffect());
	}

	/*
	 * metodo che dice se la lista di effetti di un player contiene un elemento
	 * di quella classe
	 */
	private boolean containsClass(List<Effect> list, Object o) {
		for (Effect e : list) {
			if (e.getClass().equals(o.getClass())) {
				return true;
			}
		}
		return false;
	}

	// private Effect giveIfContainedClass(List<Effect> list, Object o){
	//
	// }
}
