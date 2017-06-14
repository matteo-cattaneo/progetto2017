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
import it.polimi.ingsw.LM22.model.excommunication.WorkMalusEx;
import it.polimi.ingsw.LM22.model.leader.CardRequest;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderCardRequest;
import it.polimi.ingsw.LM22.model.leader.LeaderResourceEffect;
import it.polimi.ingsw.LM22.model.leader.MemberBonusEffect;
import it.polimi.ingsw.LM22.model.leader.MemberChangeEffect;
import it.polimi.ingsw.LM22.model.leader.NoMilitaryRequestEffect;
import it.polimi.ingsw.LM22.model.leader.NoOccupiedTowerEffect;
import it.polimi.ingsw.LM22.model.leader.ResourceCardRequest;
import it.polimi.ingsw.LM22.model.leader.ResourceRequest;
import it.polimi.ingsw.LM22.model.leader.WorkAction;
import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CardSpace;
import it.polimi.ingsw.LM22.model.CardToResourceEffect;
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
import it.polimi.ingsw.LM22.model.WorkBonusEffect;

public class MoveManager {
	private static final Logger LOGGER = Logger.getLogger(MoveManager.class.getClass().getSimpleName());
	private final String UNCOLORED = "Uncolored";
	private final Integer SINGLE_PRIVILEGE = 1;
	private final Resource NOTHING = new Resource(0, 0, 0, 0, 0, 0, 0);
	private final Resource THREE_COINS = new Resource(0, 0, 0, 3, 0, 0, 0);
	private final String PRODUCTION = "PRODUCTION";
	private final Integer WORK_MALUS = 3;
	private final Integer THIRD_TERRITORY = 3;
	private final Integer FOURTH_TERRITORY = 7;
	private final Integer FIFTH_TERRITORY = 12;
	private final Integer SIXTH_TERRITORY = 18;
	private final Integer MAX_NUM_CARDS = 6;
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
	public void manageMove(AbstractMove move) throws InvalidMoveException {
		boolean checkResult = false;
		String name;
		Method method;
		if (move instanceof MemberMove && ((MemberMove) move).getMemberUsed().isUsed()) {
			throw new InvalidMoveException();
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
			throw new InvalidMoveException();
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
		if (cardMove.getMemberUsed().getColor() != UNCOLORED
				&& game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getColoredMembersOnIt()
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
		default:
			return false;
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
		if (space.getMember() != null && !containsClass(cardMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class))
			return false;
		Integer memberEffectiveValue = calculateMemberEffectiveValue(cardMove);
		if (space.getSpaceRequirement() > memberEffectiveValue)
			return false;
		return true;
	}

	/*
	 * calcola il valore effettivo del familiare contando anche il numero di
	 * servitori aggiunti, i vari effetti presenti sia come scomuniche che come
	 * effetti di carte personaggio
	 */
	private Integer calculateMemberEffectiveValue(CardMove cardMove) {
		Integer servantsPower = calculateEffectiveServants(cardMove);
		Integer total = cardMove.getMemberUsed().getValue();
		total = total + servantsPower;
		for (Effect e : cardMove.getPlayer().getEffects()) {
			if (e instanceof DiceCardMalusEx
					&& ((DiceCardMalusEx) e).getCardType().equals(cardMove.getTowerSelected())) {
				total = total - ((DiceCardMalusEx) e).getMalus();
			} else if ((e instanceof ColorCardBonusEffect)
					&& ((ColorCardBonusEffect) e).getCardType().equals(cardMove.getTowerSelected())) {
				total = total + ((ColorCardBonusEffect) e).getDiceBonus();
			}
		}
		return total;
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
		Resource bonus = calculateBonus(cardMove);
		Resource additionalCost = calculateAdditionalCost(t, cardMove);
		// cardCost già scontato rispetto agli effetti delle carte Personaggio
		Resource cardCost = calculateCardCost(cardMove, tower);
		switch (tower) {
		case 3:
			// problema sarebbe gestire il doppio costo --> se valido solo 1
			// faccio comunque la mossa
			// se invece doppio devo chiedere al'utente quale vuole utilizzare
			resourceHandler.manageVentureCost(cardCost, ((VentureCard) card).getCardCost2());
			break;
		case 2:
		case 1:
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
	private Resource calculateBonus(CardMove move) {
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
		return additionalCost;
	}

	/*
	 * metodo che calcola il costo della carta in base alla mossa e alla torre
	 * in ingresso contando anche eventuali sconti sulle carte
	 */
	private Resource calculateCardCost(CardMove cardMove, Integer tower) {
		Resource cost = NOTHING;
		DevelopmentCard card = game.getBoardgame().getTowers()[tower].getFloor()[cardMove.getLevelSelected()].getCard();
		switch (tower) {
		case 0:
			break;
		case 1: {
			cost = discountCard(((CharacterCard) card).getCost(), cardMove, tower);
			break;
		}
		case 2: {
			cost = discountCard(((BuildingCard) card).getCost(), cardMove, tower);
			break;
		}
		case 3:
			cost = discountCard(((VentureCard) card).getCardCost1(), cardMove, tower);
			break;
		}
		return cost;
	}

	/*
	 * calcola il costo della carta scontandola in base agli effetti presenti
	 */
	private Resource discountCard(Resource cost, CardMove cardMove, Integer tower) {
		Resource res = cost.clone();
		for (Effect e : cardMove.getPlayer().getEffects()) {
			if (e instanceof ColorCardBonusEffect && ((ColorCardBonusEffect) e).getCardType() == tower)
				resourceHandler.cardDiscounted(res, ((ColorCardBonusEffect) e).getCardDiscount());
		}
		return res;
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
		Tower t = game.getBoardgame().getTowers()[cardMove.getTowerSelected()];
		space.setMember(cardMove.getMemberUsed());
		cardMove.getMemberUsed().setUsed(true);
		if (!t.isOccupied())
			t.setOccupied(true);
		if (cardMove.getMemberUsed().getColor() != UNCOLORED)
			t.getColoredMembersOnIt().add(cardMove.getMemberUsed().getColor());
		Resource playerResource = cardMove.getPlayer().getPersonalBoard().getResources();
		resourceHandler.subResource(playerResource, cardMove.getServantsAdded());
		resourceHandler.addResource(playerResource, calculateBonus(cardMove));
		resourceHandler.subResource(playerResource, calculateAdditionalCost(t, cardMove));
		resourceHandler.subResource(playerResource, calculateCardCost(cardMove, cardMove.getTowerSelected()));
		cardGetter(cardMove, t);
	}

	/*
	 * metodo che gestisce lo smistamento della carta da prendere nella
	 * PersonalBoard del player e gestisce le varie carte in maniera different
	 * (le carte character che hanno un effetto permanente devono anche
	 * aggiornare la lista degli effetti presente nel Player
	 */
	private void cardGetter(CardMove cardMove, Tower t) {
		Integer level = cardMove.getLevelSelected();
		switch (cardMove.getTowerSelected()) {
		case 0:
			TerritoryCard card0 = (TerritoryCard) (t.getFloor()[level].getCard());
			cardMove.getPlayer().getPersonalBoard().getTerritoriesCards().add(card0);
			// t.getFloor()[level].setCard(null);
			// metodo chiamante l'effectManager per l'effetto immediato
			effectManager.manageEffect(card0.getImmediateEffect(), cardMove.getPlayer(), mainGame);
			break;
		case 1:
			CharacterCard card1 = (CharacterCard) (t.getFloor()[level].getCard());
			cardMove.getPlayer().getPersonalBoard().getCharactersCards().add(card1);
			// t.getFloor()[level].setCard(null);
			// metodo chiamante l'effectManager per l'effetto immediato
			effectManager.manageEffect(card1.getImmediateEffect(), cardMove.getPlayer(), mainGame);
			if (card1.getPermanentEffect().getClass() != NoPermanentEffect.class)
				cardMove.getPlayer().getEffects().add(card1.getPermanentEffect());
			break;
		case 2:
			BuildingCard card2 = (BuildingCard) (t.getFloor()[level].getCard());
			cardMove.getPlayer().getPersonalBoard().getBuildingsCards().add(card2);
			// t.getFloor()[level].setCard(null);
			// metodo chiamante l'effectManager per l'effetto immediato
			effectManager.manageEffect(card2.getImmediateEffect(), cardMove.getPlayer(), mainGame);
			break;
		case 3:
			VentureCard card3 = (VentureCard) (t.getFloor()[level].getCard());
			cardMove.getPlayer().getPersonalBoard().getVenturesCards().add(card3);
			// t.getFloor()[level].setCard(null);
			// metodo chiamante l'effectManager per l'effetto immediato != da
			// NoEffect
			effectManager.manageEffect(card3.getImmediateEffect(), cardMove.getPlayer(), mainGame);
			break;
		}
		/*
		 * parte di codice che sistemerebbe il problema di effetti presenti sia
		 * come immediati di carte character che come permanenti nelle carte
		 * building
		 */
		// if (t.getFloor()[level].getCard().getImmediateEffect() instanceof
		// CardToResourceEffect)
		// effectManager.cardtoresourceeffectManage(
		// (CardToResourceEffect)
		// t.getFloor()[level].getCard().getImmediateEffect(),
		// cardMove.getPlayer().getPersonalBoard().getResources());
	}

	/*
	 * controlla se una mossa del tipo MarketMove è ammessa o no
	 */
	public boolean marketmoveAllowed(MarketMove marketMove) {
		if (containsClass(marketMove.getPlayer().getEffects(), NoMarketEx.class))
			return false;
		int pos = marketMove.getMarketSpaceSelected();
		if (game.getBoardgame().getMarket()[pos].getMember() != null
				&& !containsClass(marketMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class))
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
		resourceHandler.subResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				marketMove.getServantsAdded());
		marketMove.getMemberUsed().setUsed(true);
		/*
		 * prendo le varie risorse (i vari premi contenuti nei MarketSpace)
		 */
		Resource bonus = calculateBonus(marketMove, game.getBoardgame().getMarket()[opt]);
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(), bonus);
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
		if (containsClass(move.getPlayer().getEffects(), ResourceMalusEx.class)) {
			// modifico il valore del bonus ricevuto se e solo se il bonus
			// ricevuto è dello stesso tipo
			// di quello presente nella scomunica
			return bonus;
		}
		return bonus;
	}

	/*
	 * controlla se una mossa del tipo WorkMove è ammessa o no
	 */
	public boolean workmoveAllowed(WorkMove workMove) {
		return checkWorkSpace(workMove);
	}

	/*
	 * controlla se il valore del familiare utilizzato + servitori soddisfa il
	 * requisito relativo allo spazio azione selezionato per la mossa + se il
	 * primo spazio è già occupato effettua una diminuzione del valore
	 * dell'azione in base al Malus specificato nelle regole
	 */
	private boolean checkWorkSpace(WorkMove workMove) {
		Integer power = findWorkEffects(workMove);
		Integer servants = calculateEffectiveServants(workMove);
		switch (game.getPlayers().length) {
		case 2:
			if (workMove.getWorkType() == "PRODUCTION") {
				if (!game.getBoardgame().getProductionSpace().getMembers().isEmpty()
						&& !containsClass(workMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class))
					return false;
				if (game.getBoardgame().getProductionSpace().getSpaceRequirement() > power
						+ workMove.getMemberUsed().getValue() + servants)
					return false;
				break;
			} else {
				if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty()
						&& !containsClass(workMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class))
					return false;
				if (game.getBoardgame().getHarvestSpace().getSpaceRequirement() > power
						+ workMove.getMemberUsed().getValue() + servants)
					return false;
				break;
			}
		case 3:
		case 4:
			if (workMove.getWorkType() == "PRODUCTION") {
				if (game.getBoardgame().getProductionSpace().getColoredMemberOnIt()
						.contains(workMove.getPlayer().getColor()))
					return false;
				if (!game.getBoardgame().getProductionSpace().getMembers().isEmpty()
						&& game.getBoardgame().getProductionSpace().getSpaceRequirement() > power
								+ workMove.getMemberUsed().getValue() + servants - WORK_MALUS)
					return false;
				break;
			} else {
				if (game.getBoardgame().getHarvestSpace().getColoredMemberOnIt()
						.contains(workMove.getPlayer().getColor()))
					return false;
				if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty()
						&& game.getBoardgame().getHarvestSpace().getSpaceRequirement() > power
								+ workMove.getMemberUsed().getValue() + servants - WORK_MALUS)
					return false;
			}
		default:
			return false;
		}
		return true;
	}

	/*
	 * calcola il valore totale di eventuali bonus o malus provenienti da
	 * scomuniche o effetti permanenti di carte leader
	 */
	private Integer findWorkEffects(WorkMove move) {
		Integer total = 0;
		String type = move.getWorkType();
		for (Effect e : move.getPlayer().getEffects()) {
			if (e instanceof WorkMalusEx && type == ((WorkMalusEx) e).getTypeOfWork())
				total = total - ((WorkMalusEx) e).getValueOfMalus();
			if (e instanceof WorkBonusEffect && type == ((WorkBonusEffect) e).getTypeOfWork())
				total = total + ((WorkBonusEffect) e).getWorkBonusValue();
		}
		return total;
	}

	/*
	 * gestisce una mossa del tipo WorkMove
	 */
	public void workmoveHandle(WorkMove workMove) throws IOException {
		if (workMove.getWorkType() == PRODUCTION)
			productionHandle(workMove);
		else
			harvestHandle(workMove);
	}

	/*
	 * metodo che si occupa della gestione di una azione di produzione --> devo
	 * sempre controllare le carte del player singolarmente ma solo alla fine
	 * della produzione darò tutti i bonus al player perchè alcuni effetti vanno
	 * attivati solo con le risorse già possedute dal player --> bisogna sempre
	 * chiedere al player se vuole o no ottenere tipo il cambio risorse e anche
	 * chiedere quale attivare nel caso di DoubleChangeEffect
	 */
	private void productionHandle(WorkMove move) throws IOException {
		Integer power = findWorkEffects(move);
		Integer servants = calculateEffectiveServants(move);
		Integer valueOfAction = move.getMemberUsed().getValue() + servants + power;
		if (!game.getBoardgame().getProductionSpace().getMembers().isEmpty())
			valueOfAction = valueOfAction - WORK_MALUS;
		game.getBoardgame().getProductionSpace().getMembers().add(move.getMemberUsed());
		move.getMemberUsed().setUsed(true);
		if (move.getMemberUsed().getColor() != UNCOLORED)
			game.getBoardgame().getProductionSpace().getColoredMemberOnIt().add(move.getMemberUsed().getColor());
		resourceHandler.subResource(move.getPlayer().getPersonalBoard().getResources(), move.getServantsAdded());
		Resource total = NOTHING;
		for (BuildingCard card : move.getPlayer().getPersonalBoard().getBuildingsCards()) {
			if (valueOfAction >= card.getRequirement()) {
				// effectManager.productionHandle(card.getPermanentEffect(),
				// total);
			}
		}
		effectManager.productionHandle(move.getPlayer().getPersonalBoard().getBonusBoard().getProductionEffect(),
				total);
		resourceHandler.addResource(move.getPlayer().getPersonalBoard().getResources(), total);
	}

	/*
	 * gestisce la fase di Raccolto
	 */
	private void harvestHandle(WorkMove move) throws IOException {
		Integer power = findWorkEffects(move);
		Integer servants = calculateEffectiveServants(move);
		Integer valueOfAction = move.getMemberUsed().getValue() + power + servants;
		if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty())
			valueOfAction = valueOfAction - WORK_MALUS;
		game.getBoardgame().getHarvestSpace().getMembers().add(move.getMemberUsed());
		move.getMemberUsed().setUsed(true);
		if (move.getMemberUsed().getColor() != UNCOLORED)
			game.getBoardgame().getHarvestSpace().getColoredMemberOnIt().add(move.getMemberUsed().getColor());
		resourceHandler.subResource(move.getPlayer().getPersonalBoard().getResources(), move.getServantsAdded());
		Resource total = NOTHING;
		for (TerritoryCard card : move.getPlayer().getPersonalBoard().getTerritoriesCards()) {
			if (valueOfAction >= card.getRequirement())
				effectManager.harvestHandle(card.getPermanentEffect(), total);
		}
		effectManager.harvestHandle(move.getPlayer().getPersonalBoard().getBonusBoard().getHarvestEffect(), total);
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
		resourceHandler.subResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				councilMove.getServantsAdded());
		/*
		 * prendo le varie risorse (moneta oppure privilegi del consiglio)
		 */
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				game.getBoardgame().getCouncilPalace().getReward());
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				mainGame.selectCouncilPrivilege(game.getBoardgame().getCouncilPalace().getCouncilPrivilege(),
						councilMove.getPlayer()));
	}

	/*
	 * metodo che controlla se la carta Leader che il player vuole vendere è già
	 * stata attivata (per adesso non può venderla se è già stata attivata nel
	 * turno mentre se l'aveva attivata in passato ma non risulta
	 * "attiva in questo momento" il controllo da un esit positivo lo stesso
	 * 
	 * IMPORTANTE --> necessaria altra lista per tenere in memoria tutte le
	 * carte effettivamente attivate durante tutto il corso della partita?
	 */
	public boolean leadercardsellingAllowed(LeaderCardSelling move) {
		if ((move.getPlayer().getActivatedLeaderCards().contains(move.getLeaderCard())
				|| move.getPlayer().getLeaderCards().contains(move.getLeaderCard())))
			return false;
		return true;
	}

	/*
	 * gestisce la vendita di una carta leader e consentirà la scelta del
	 * privilegio del consiglio --> posso solo vendere carte non attive
	 */
	public void leadercardsellingHandle(LeaderCardSelling move) throws IOException {
		move.getPlayer().getHandLeaderCards().remove(move.getLeaderCard());
		resourceHandler.addResource(move.getPlayer().getPersonalBoard().getResources(),
				mainGame.selectCouncilPrivilege(SINGLE_PRIVILEGE, move.getPlayer()));
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
			if (!checkCardRequest(r, move))
				return false;
		} else if (req instanceof ResourceRequest) {
			ResourceRequest r = ((ResourceRequest) move.getLeaderCard().getRequest());
			if (!checkResourceRequest(r, move))
				return false;
		} else if (req instanceof ResourceCardRequest) {
			ResourceCardRequest r = ((ResourceCardRequest) move.getLeaderCard().getRequest());
			if (!checkResourceCardRequest(r, move))
				return false;
		}
		return true;
	}

	private boolean checkCardRequest(CardRequest r, LeaderCardActivation move) {
		if (r.getTerritoryCards() > move.getPlayer().getPersonalBoard().getTerritoriesCards().size()
				|| r.getCharacterCards() > move.getPlayer().getPersonalBoard().getCharactersCards().size()
				|| r.getBuildingCards() > move.getPlayer().getPersonalBoard().getBuildingsCards().size()
				|| r.getVentureCards() > move.getPlayer().getPersonalBoard().getVenturesCards().size())
			return false;
		return true;
	}

	private boolean checkResourceRequest(ResourceRequest r, LeaderCardActivation move) {
		if (!resourceHandler.enoughResources(move.getPlayer().getPersonalBoard().getResources(), r.getResource()))
			return false;
		return true;
	}

	private boolean checkResourceCardRequest(ResourceCardRequest r, LeaderCardActivation move) {
		if ((r.getTerritoryCards() > move.getPlayer().getPersonalBoard().getTerritoriesCards().size()
				|| r.getCharacterCards() > move.getPlayer().getPersonalBoard().getCharactersCards().size()
				|| r.getBuildingCards() > move.getPlayer().getPersonalBoard().getBuildingsCards().size()
				|| r.getVentureCards() > move.getPlayer().getPersonalBoard().getVenturesCards().size())
				|| !resourceHandler.enoughResources(move.getPlayer().getPersonalBoard().getResources(),
						r.getResource()))
			return false;
		return true;
	}

	/*
	 * metodo che gestisce la procedura di attivazione di una carta -->
	 * comportamento differente se l'effetto è una volta per turno oppure se è
	 * un effetto permanente
	 */
	public void leadercardactivationHandle(LeaderCardActivation move) {
		if (!(move.getLeaderCard().getEffect() instanceof LeaderResourceEffect)
				&& !(move.getLeaderCard().getEffect() instanceof MemberChangeEffect
						&& ((MemberChangeEffect) move.getLeaderCard().getEffect()).getTypeOfMember() == "COLORED")
				&& !(move.getLeaderCard().getEffect() instanceof WorkAction)) {
			move.getPlayer().getEffects().add(move.getLeaderCard().getEffect());
		}
		effectManager.manageEffect(move.getLeaderCard().getEffect(), move.getPlayer(), mainGame);
		move.getPlayer().getActivatedLeaderCards().add(move.getLeaderCard());
		move.getPlayer().getLeaderCards().remove(move.getLeaderCard());
		move.getPlayer().getHandLeaderCards().remove(move.getLeaderCard());
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

	/*
	 * metodo invocato se: - player dice intenzionalmente di aver finito il suo
	 * turno - timer per effettuare il proprio turno scade
	 */
	public void endmoveHandle(EndMove move) {
		if (!move.getError().equals("noError"))
			mainGame.disconnectPlayer(move.getPlayer());
	}
}
