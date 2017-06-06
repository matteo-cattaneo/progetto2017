package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.excommunication.DiceCardMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.DoubleServantsEx;
import it.polimi.ingsw.LM22.model.excommunication.NoMarketEx;
import it.polimi.ingsw.LM22.model.excommunication.ResourceMalusEx;
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
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.NoCardSpaceBonusEffect;
import it.polimi.ingsw.LM22.model.NoPermanentEffect;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;

public class MoveManager {
	private static final Logger LOGGER = Logger.getLogger(MoveManager.class.getClass().getSimpleName());
	private final String UNCOLORED = "Uncolored";
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
	public void manageMove(AbstractMove move) throws IOException {
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
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		if (checkResult) {
			try {
				name = move.getClass().getSimpleName().toLowerCase() + "Handle";
				method = this.getClass().getMethod(name, new Class[] { move.getClass() });
				method.invoke(this, new Object[] { move });
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		} else
			// throw new InvalidMoveException
			return;
	}

	/*
	 * controlla se una mossa del tipo CardMove è ammessa o no
	 */
	public boolean cardmoveAllowed(CardMove cardMove) {
		if (!checkMaxNumCardLimit(cardMove))
			return false;
		if (!checkCardSpace(cardMove))
			return false;
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
		Integer servantsPower = calculateEffectiveServants(cardMove);
		if (space.getSpaceRequirement() > (cardMove.getMemberUsed().getValue() + servantsPower))
			return false;
		if (containsClass(cardMove.getPlayer().getEffects(), DiceCardMalusEx.class)) {
			for (Effect e : cardMove.getPlayer().getEffects()) {
				if (e instanceof DiceCardMalusEx) {
					if (((DiceCardMalusEx) e).getCardType().equals(cardMove.getTowerSelected())) {
						if (space.getSpaceRequirement() > (cardMove.getMemberUsed().getValue() + servantsPower
								- ((DiceCardMalusEx) e).getMalus()))
							return false;
					}
					break;
				}
			}
		}
		return true;
	}

	/*
	 * metodo che calcola l'effettivo aumento di potere grazie all'utilizzo di
	 * servitori durante un qualsiasi tipo di mossa che implica il movimento di
	 * un familiare
	 */
	private Integer calculateEffectiveServants(MemberMove move) {
		if (containsClass(move.getPlayer().getEffects(), DoubleServantsEx.class))
			return move.getServantsAdded().getServants() / 2;
		return move.getServantsAdded().getServants();
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
		Resource cardCost = calculateCardCost(cardMove, t);
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
		if (containsClass(move.getPlayer().getEffects(), NoCardSpaceBonusEffect.class))
			bonus = NOTHING;
		else {
			// da controllare se ho le scomuniche che riducono il numero di
			// risorse acquisite --> azione da fare per ogni aggiunta possibile
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
	 * metodo che calcola il costo della carta in base alla mossa e alla torre
	 * in ingresso
	 */
	private Resource calculateCardCost(CardMove cardMove, Tower t) {
		// TODO Auto-generated method stub
		return null;
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

	/*
	 * metodo che restituisce il CardSpace relativo al numero della torre
	 * selezionata e al numero del floor selezionato
	 */
	private CardSpace searchCardSpace(Integer towerSelected, Integer floorSelected) {
		return game.getBoardgame().getTowers()[towerSelected].getFloor()[floorSelected].getSpace();
	}

	/*
	 * gestisce una mossa di tipo CardMove
	 */
	public void cardmoveHandle(CardMove cardMove) {
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		space.setMember(cardMove.getMemberUsed());
		Tower t = game.getBoardgame().getTowers()[cardMove.getTowerSelected()];
		cardMove.getMemberUsed().setUsed(true);
		if (!t.isOccupied())
			t.setOccupied(true);
		if (cardMove.getMemberUsed().getColor() != UNCOLORED)
			t.getColoredMembersOnIt().add(cardMove.getMemberUsed().getColor());
		Resource playerResource = cardMove.getPlayer().getPersonalBoard().getResources();
		resourceHandler.subResource(playerResource, cardMove.getServantsAdded());
		resourceHandler.addResource(playerResource, calculateBonus(t, cardMove));
		resourceHandler.subResource(playerResource, calculateAdditionalCost(t, cardMove));
		resourceHandler.subResource(playerResource, calculateCardCost(cardMove, t));
		cardGetter(cardMove, t);
	}

	/*
	 * metodo che gestisce lo smistamento della carta da prendere nella
	 * PersonalBoard del player e gestisce le varie carte in maniera different
	 * (le carte character che hanno un effetto permanente devono anche
	 * aggiornare la lista degli effetti presente nel Player
	 */
	private void cardGetter(CardMove cardMove, Tower t) {
		switch (cardMove.getTowerSelected()) {
		case 0:
			cardMove.getPlayer().getPersonalBoard().getTerritoriesCards()
					.add((TerritoryCard) (t.getFloor()[cardMove.getLevelSelected()].getCard()));
			// metodo chiamante l'effectManager per l'effetto immediato
			break;
		case 1:
			cardMove.getPlayer().getPersonalBoard().getCharactersCards()
					.add((CharacterCard) (t.getFloor()[cardMove.getLevelSelected()].getCard()));
			if (((CharacterCard) (t.getFloor()[cardMove.getLevelSelected()].getCard())).getPermanentEffect()
					.getClass() != NoPermanentEffect.class)
				cardMove.getPlayer().getEffects().add(
						((CharacterCard) (t.getFloor()[cardMove.getLevelSelected()].getCard())).getPermanentEffect());
			// metodo chiamante l'effectManager per l'effetto immediato
			break;
		case 2:
			cardMove.getPlayer().getPersonalBoard().getBuildingsCards()
					.add((BuildingCard) (t.getFloor()[cardMove.getLevelSelected()].getCard()));
			// metodo chiamante l'effectManager per l'effetto immediato
			break;
		case 3:
			cardMove.getPlayer().getPersonalBoard().getVenturesCards()
					.add((VentureCard) (t.getFloor()[cardMove.getLevelSelected()].getCard()));
			// metodo chiamante l'effectManager per l'effetto immediato
			break;
		}
	}

	/*
	 * controlla se una mossa del tipo MarketMove è ammessa o no
	 */
	public boolean marketmoveAllowed(MarketMove marketMove) {
		// check per controllare se ho scomunica del tipo NoMarkeEx
		if (containsClass(marketMove.getPlayer().getEffects(), NoMarketEx.class))
			return false;
		int pos = marketMove.getMarketSpaceSelected();
		if (game.getBoardgame().getMarket()[pos].getMember() != null)
			if (!containsClass(marketMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class))
				return false;
		int servants = calculateEffectiveServants(marketMove);
		if (marketMove.getMemberUsed().getValue() + servants < game.getBoardgame().getMarket()[pos]
				.getSpaceRequirement())
			return false;
		return true;
	}

	/*
	 * gestisce una mossa del tipo MarketMove
	 */
	public void marketmoveHandle(MarketMove marketMove) throws IOException {
		int opt = marketMove.getMarketSpaceSelected();
		game.getBoardgame().getMarket()[opt].setMember(marketMove.getMemberUsed());
		marketMove.getMemberUsed().setUsed(true);
		/*
		 * prendo le varie risorse (i vari premi contenuti nei MarketSpace)
		 */
		Resource bonus = calculateBonus(marketMove, game.getBoardgame().getMarket()[opt]);
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(), bonus);
		resourceHandler.subResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				marketMove.getServantsAdded());
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				mainGame.selectCouncilPrivilege(game.getBoardgame().getMarket()[opt].getCouncilPrivilege(),
						marketMove.getPlayer()));
	}

	/*
	 * metodo che restituisce la risorsa bonus controllando anch eventuali
	 * scomuniche
	 */
	private Resource calculateBonus(MarketMove move, MarketSpace space) {
		Resource bonus = space.getReward();
		if (containsClass(move.getPlayer().getEffects(), ResourceMalusEx.class))
			// modifico il valore del bonus ricevuto se e solo se il bonus
			// ricevuto è dello stesso tipo
			// di quello presente nella scomunica
			return bonus;
		return bonus;
	}

	/*
	 * controlla se una mossa del tipo WorkMove è ammessa o no
	 */
	public boolean workMoveAllowed(WorkMove workMove) {
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
	public void workMoveHandle(WorkMove workMove) {
		if (workMove.getWorkType() == PRODUCTION)
			productionHandle(workMove);
	}

	/*
	 * metodo che si occupa della gestione di una azione di produzione --> devo
	 * sempre controllare le carte del player singolarmente ma solo alla fine
	 * della produzione darò tutti i bonus al player perchè alcuni effetti vanno
	 * attivati solo con le risorse già possedute dal player --> bisogna sempre
	 * chiedere al player se vuole o no ottenere tipo il cambio risorse e anche
	 * chiedere quale attivare nel caso di DoubleChangeEffect
	 */
	private void productionHandle(WorkMove move) {
		Integer valueOfAction = move.getMemberUsed().getValue() + calculateEffectiveServants(move);
		resourceHandler.subResource(move.getPlayer().getPersonalBoard().getResources(), move.getServantsAdded());
		Resource total = NOTHING;
		for (BuildingCard card : move.getPlayer().getPersonalBoard().getBuildingsCards()) {
			// if (valueOfAction >= card.getRequirement())
			// effectManager.workHandle(card.getPermanentEffect(), total);
		}
		// effectManager.workHandle(move.getPlayer().getPersonalBoard().getBonusBoard().getHarvestEffect(),
		// total);
		resourceHandler.addResource(move.getPlayer().getPersonalBoard().getResources(), total);
	}

	/*
	 * gestisce la fase di Raccolto
	 */
	private void harvestHandle(WorkMove move) throws IOException {
		// devo controllare se ho bonus da carte character oppure scomuniche
		Integer valueOfAction = move.getMemberUsed().getValue() + calculateEffectiveServants(move);
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
	public boolean councilmoveAllowed(CouncilMove councilMove) {
		Integer servants = calculateEffectiveServants(councilMove);
		if (councilMove.getMemberUsed().getValue() + servants < game.getBoardgame().getCouncilPalace()
				.getSpaceRequirement())
			return false;
		return true;
	}

	/*
	 * gestisce una mossa del tipo CouncilMove
	 */
	public void councilmoveHandle(CouncilMove councilMove) throws IOException {
		councilMove.getMemberUsed().setUsed(true);
		game.getBoardgame().getCouncilPalace().getMembers().add(councilMove.getMemberUsed());
		/*
		 * prendo le varie risorse (moneta oppure privilegi del consiglio)
		 */
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				game.getBoardgame().getCouncilPalace().getReward());
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				mainGame.selectCouncilPrivilege(game.getBoardgame().getCouncilPalace().getCouncilPrivilege(),
						councilMove.getPlayer()));
		resourceHandler.subResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				councilMove.getServantsAdded());

	}

	public boolean leadercardsellingAllowed(LeaderCardSelling move) {
		return true;
	}

	/*
	 * gestisce la vendita di una carta leader e consentirà la scelta del
	 * privilegio del consiglio + se la carta era attiva devo anche togliere il
	 * suo effetto dalla lista degli effetti attualmente attivi
	 */
	public void leadercardsellingHandle(LeaderCardSelling move) throws IOException {
		if (move.getPlayer().getLeaderCards().contains(move.getLeaderCard()))
			move.getPlayer().getLeaderCards().remove(move.getLeaderCard());
		else {
			if (move.getLeaderCard().getEffect() instanceof MemberBonusEffect
					|| move.getLeaderCard().getEffect() instanceof MemberChangeEffect)
				// gestisco la rimozione di un effetto che modifica il valore
				// dei familiari
				move.getPlayer().getEffects().remove(move.getLeaderCard().getEffect());
			move.getPlayer().getActivatedLeaderCards().remove(move.getLeaderCard());
		}
		mainGame.selectCouncilPrivilege(SINGLE_PRIVILEGE, move.getPlayer());
	}

	/*
	 * controlla se i requisiti della carta leader che il player vuole attivare
	 * sono effettivmanete soddisfatti e nel caso indica quella carta come
	 * attivata --> sarà poi gestita dai vari controlli comunque interni a
	 * questa classe
	 */
	public boolean leadercardactivationAllowed(LeaderCardActivation move) {
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
	public void leadercardactivationHandle(LeaderCardActivation move) {
		// move.getPlayer().getEffects().add(move.getLeaderCard().getEffect());
	}

	/*
	 * metodo che dice se la lista di effetti di un player contiene un elemento
	 * di quella classe
	 */
	public boolean containsClass(List<Effect> list, Object o) {
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

	public boolean endmoveAllowed(EndMove move) {
		return true;
	}

	public void endmoveHandle(EndMove move) {

	}
}
