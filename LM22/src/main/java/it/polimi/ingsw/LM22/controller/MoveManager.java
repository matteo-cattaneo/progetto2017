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
import it.polimi.ingsw.LM22.model.excommunication.WorkMalusEx;
import it.polimi.ingsw.LM22.model.leader.CardRequest;
import it.polimi.ingsw.LM22.model.leader.CoinsDiscountEffect;
import it.polimi.ingsw.LM22.model.leader.DoubleResourceEffect;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderCardRequest;
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
import it.polimi.ingsw.LM22.model.NoPermanentEffect;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.ResourcePrivilegeEffect;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.WorkBonusEffect;

public class MoveManager {
	private static final Logger LOGGER = Logger.getLogger(MoveManager.class.getClass().getSimpleName());
	private static final String UNCOLORED = "Uncolored";
	private static final String ACTION = "Action";
	private static final Integer SINGLE_PRIVILEGE = 1;
	private static final Resource NOTHING = new Resource(0, 0, 0, 0, 0, 0, 0);
	private static final Resource THREE_COINS = new Resource(0, 0, 0, 3, 0, 0, 0);
	private static final String PRODUCTION = "PRODUCTION";
	private static final Integer WORK_MALUS = 3;
	private static final Integer[] TERRITORY_REQUEST = { 0, 0, 3, 7, 12, 18 };
	private static final Integer MAX_NUM_CARDS = 6;
	private static final Integer REQUIRED = 0;
	private Integer doubleCostChoice;
	private Game game;
	private MainGameController mainGame;
	private ResourceHandler resourceHandler = new ResourceHandler();
	private EffectManager effectManager = new EffectManager(this);

	public MoveManager(Game game, MainGameController mainGame) {
		this.game = game;
		this.mainGame = mainGame;
	}

	/**
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

	/**
	 * controlla se una mossa del tipo CardMove è ammessa o no
	 */
	public boolean cardmoveAllowed(CardMove cardMove) throws IOException {
		if (!checkMaxNumCardLimit(cardMove))
			return false;
		if (!checkCardSpace(cardMove))
			return false;
		if (!cardMove.getMemberUsed().getColor().equals(UNCOLORED)
				&& !cardMove.getMemberUsed().getColor().equals(ACTION)
				&& game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getColoredMembersOnIt()
						.contains(cardMove.getPlayer().getColor()))
			return false;
		if (!checkCardCost(cardMove))
			return false;
		return true;
	}

	/**
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

	/**
	 * esegue una serie di controlli sul posizionamento del familiare e se lo
	 * posso mettere guadagno l'ipotetico bonus relativo allo specifico spazio
	 * azione
	 */
	private boolean checkCardSpace(CardMove cardMove) {
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		if (space.getMember() != null)
			return false;
		Integer memberEffectiveValue = calculateMemberEffectiveValue(cardMove);
		if (space.getSpaceRequirement() > memberEffectiveValue)
			return false;
		return true;
	}

	/**
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

	/**
	 * metodo che calcola l'effettivo aumento di potere grazie all'utilizzo di
	 * servitori durante un qualsiasi tipo di mossa che implica il movimento di
	 * un familiare
	 */
	private Integer calculateEffectiveServants(MemberMove move) {
		if (containsClass(move.getPlayer().getEffects(), DoubleServantsEx.class))
			return move.getServantsAdded().getServants() / 2;
		return move.getServantsAdded().getServants();
	}

	/**
	 * metodo che gestisce il controllo del costo di una carta, gestendo anche
	 * tutti i relativi effetti che possono essere applicati ad una mossa di
	 * tipo CardMove
	 */
	private boolean checkCardCost(CardMove cardMove) throws IOException {
		/**
		 * importante tenere conto per il check sul costo i servitori che un
		 * player può già aver giocato sottraendoli nella disequazione di
		 * controllo del costo
		 */
		int tower = cardMove.getTowerSelected();
		Tower t = game.getBoardgame().getTowers()[tower];
		int floor = cardMove.getLevelSelected();
		DevelopmentCard card = game.getBoardgame().getTowers()[tower].getFloor()[floor].getCard();
		Resource bonus = resourceHandler.calculateResource(calculateBonus(cardMove).copy(), cardMove.getPlayer(),
				false);
		Resource additionalCost = calculateAdditionalCost(t, cardMove);
		Resource cardCost = calculateCardCost(cardMove, tower);
		switch (tower) {
		case 3:
			doubleCostChoice = mainGame.askForCost(cardMove);
			if (doubleCostChoice.equals(1)
					&& !resourceHandler.enoughResources(((VentureCard) card).getCardCost2()[REQUIRED].copy(), cardMove,
							additionalCost, bonus))
				return false;
			else if (!resourceHandler.enoughResources(cardCost, cardMove, additionalCost, bonus))
				return false;
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
		default:
			break;
		}
		return true;
	}

	/**
	 * metodo che calcola il bonus ottenuto dallo spazio azione del floor
	 * relativo
	 */
	private Resource calculateBonus(CardMove move) {
		Resource bonus;
		CardSpace space = searchCardSpace(move.getTowerSelected(), move.getLevelSelected());
		if (containsClass(move.getPlayer().getEffects(), NoCardSpaceBonusEffect.class))
			bonus = NOTHING.copy();
		else {
			bonus = space.getReward().copy();
		}
		return bonus;
	}

	/**
	 * calcola il possibile costo addizionale dovuto alla torre occupata -->
	 * gestisce anche l'effetto di Filippo Brunelleschi questo prezzo non può
	 * essere pagato con il bonus dello spazio azione
	 */
	private Resource calculateAdditionalCost(Tower t, CardMove move) {
		Resource additionalCost = NOTHING.copy();
		boolean occupied = t.isOccupied();
		boolean hasBrunelleschi = containsClass(move.getPlayer().getEffects(), NoOccupiedTowerEffect.class);
		if (occupied && !hasBrunelleschi)
			additionalCost = THREE_COINS.copy();
		return additionalCost;
	}

	/**
	 * metodo che calcola il costo della carta in base alla mossa e alla torre
	 * in ingresso contando anche eventuali sconti sulle carte
	 */
	private Resource calculateCardCost(CardMove cardMove, Integer tower) {
		Resource cost = NOTHING.copy();
		DevelopmentCard card = game.getBoardgame().getTowers()[tower].getFloor()[cardMove.getLevelSelected()].getCard();
		switch (tower) {
		case 0:
			break;
		case 1:
			cost = discountCard(((CharacterCard) card).getCost(), cardMove, tower);
			break;
		case 2:
			cost = discountCard(((BuildingCard) card).getCost(), cardMove, tower);
			break;
		case 3:
			cost = discountCard(((VentureCard) card).getCardCost1(), cardMove, tower);
			break;
		default:
			break;
		}
		return cost;
	}

	/**
	 * calcola il costo della carta scontandola in base agli effetti presenti
	 */
	private Resource discountCard(Resource cost, CardMove cardMove, Integer tower) {
		Resource res = cost.copy();
		for (Effect e : cardMove.getPlayer().getEffects()) {
			if (e instanceof ColorCardBonusEffect && ((ColorCardBonusEffect) e).getCardType().equals(tower))
				res = resourceHandler.cardDiscounted(res, ((ColorCardBonusEffect) e).getCardDiscount());
			else if (e instanceof CoinsDiscountEffect) {
				res = resourceHandler.cardDiscounted(res, ((CoinsDiscountEffect) e).getDiscount());
			}
		}
		return res;
	}

	/**
	 * controlla (solo per una CardMove per una TERRITORY) se il player soddisfa
	 * i requisiti relativi ai punti militari --> controlla anche se è stata
	 * attivata la carta Leader che annulla questo controllo
	 */
	private boolean militaryPointsAvailable(CardMove cardMove, Resource bonus) {
		if (!containsClass(cardMove.getPlayer().getEffects(), NoMilitaryRequestEffect.class)
				&& (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary()
						+ bonus.getMilitary()) < TERRITORY_REQUEST[cardMove.getPlayer().getPersonalBoard()
								.getTerritoriesCards().size()])
			return false;
		return true;
	}

	/**
	 * metodo che restituisce il CardSpace relativo al numero della torre
	 * selezionata e al numero del floor selezionato
	 */
	private CardSpace searchCardSpace(Integer towerSelected, Integer floorSelected) {
		return game.getBoardgame().getTowers()[towerSelected].getFloor()[floorSelected].getSpace();
	}

	/**
	 * gestisce una mossa di tipo CardMove
	 */
	public void cardmoveHandle(CardMove cardMove) throws InvalidMoveException {
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		Tower t = game.getBoardgame().getTowers()[cardMove.getTowerSelected()];
		space.setMember(cardMove.getMemberUsed());
		cardMove.getMemberUsed().setUsed(true);
		if (!cardMove.getMemberUsed().getColor().equals(UNCOLORED)
				&& !cardMove.getMemberUsed().getColor().equals(ACTION))
			t.getColoredMembersOnIt().add(cardMove.getPlayer().getColor());
		Resource playerResource = cardMove.getPlayer().getPersonalBoard().getResources();
		/** sottraggo i servitori usati */
		resourceHandler.subResource(playerResource, cardMove.getServantsAdded());
		/** sommo il bonus */
		resourceHandler.addResource(playerResource,
				resourceHandler.calculateResource(calculateBonus(cardMove).copy(), cardMove.getPlayer(), false));
		/** sottraggo il costo addizionale */
		resourceHandler.subResource(playerResource, calculateAdditionalCost(t, cardMove));
		Resource cardCost;
		if (cardMove.getTowerSelected().equals(3) && doubleCostChoice.equals(1))
			cardCost = discountCard(
					((VentureCard) (t.getFloor()[cardMove.getLevelSelected()].getCard())).getCardCost2()[REQUIRED + 1],
					cardMove, cardMove.getLevelSelected());
		else
			cardCost = calculateCardCost(cardMove, cardMove.getTowerSelected());
		/** sottraggo il costo effettivo della carta */
		resourceHandler.subResource(playerResource, cardCost);
		if (!t.isOccupied() && !cardMove.getMemberUsed().getColor().equals(ACTION))
			t.setOccupied(true);
		cardGetter(cardMove, t);
	}

	/**
	 * metodo che gestisce lo smistamento della carta da prendere nella
	 * PersonalBoard del player e gestisce le varie carte in maniera different
	 * (le carte character che hanno un effetto permanente devono anche
	 * aggiornare la lista degli effetti presente nel Player
	 */
	private void cardGetter(CardMove cardMove, Tower t) throws InvalidMoveException {
		Integer level = cardMove.getLevelSelected();
		DevelopmentCard card;
		switch (cardMove.getTowerSelected()) {
		case 0:
			card = t.getFloor()[level].getCard();
			cardMove.getPlayer().getPersonalBoard().getTerritoriesCards().add((TerritoryCard) card);
			t.getFloor()[level].setCard(new TerritoryCard());
			break;
		case 1:
			card = t.getFloor()[level].getCard();
			cardMove.getPlayer().getPersonalBoard().getCharactersCards().add((CharacterCard) card);
			t.getFloor()[level].setCard(new CharacterCard());
			if (((CharacterCard) card).getPermanentEffect().getClass() != NoPermanentEffect.class)
				cardMove.getPlayer().getEffects().add(((CharacterCard) card).getPermanentEffect());
			break;
		case 2:
			card = t.getFloor()[level].getCard();
			cardMove.getPlayer().getPersonalBoard().getBuildingsCards().add((BuildingCard) card);
			t.getFloor()[level].setCard(new BuildingCard());
			break;
		case 3:
			card = t.getFloor()[level].getCard();
			cardMove.getPlayer().getPersonalBoard().getVenturesCards().add((VentureCard) card);
			t.getFloor()[level].setCard(new VentureCard());
			break;
		default:
			card = new TerritoryCard();
			break;
		}
		// metodo chiamante l'effectManager per l'effetto immediato
		effectManager.manageEffect(card.getImmediateEffect(), cardMove.getPlayer(),
				cardMove.getPlayer().getPersonalBoard().getResources(), mainGame);
		if (card.getImmediateEffect() instanceof ResourcePrivilegeEffect
				&& containsClass(cardMove.getPlayer().getEffects(), DoubleResourceEffect.class)) {
			Resource effect = ((ResourcePrivilegeEffect) card.getImmediateEffect()).getResource();
			effect.setFaith(0);
			effect.setMilitary(0);
			effect.setVictory(0);
			resourceHandler.addResource(cardMove.getPlayer().getPersonalBoard().getResources(), effect);
		}
	}

	/**
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

	/**
	 * gestisce una mossa del tipo MarketMove
	 */
	public void marketmoveHandle(MarketMove marketMove) throws IOException {
		int opt = marketMove.getMarketSpaceSelected();
		game.getBoardgame().getMarket()[opt].setMember(marketMove.getMemberUsed());
		resourceHandler.subResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				marketMove.getServantsAdded());
		marketMove.getMemberUsed().setUsed(true);
		/**
		 * prendo le varie risorse (i vari premi contenuti nei MarketSpace)
		 */
		Resource bonus = resourceHandler.calculateResource(game.getBoardgame().getMarket()[opt].getReward().copy(),
				marketMove.getPlayer(), false);
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(), bonus);
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				resourceHandler.calculateResource(
						mainGame.selectCouncilPrivilege(game.getBoardgame().getMarket()[opt].getCouncilPrivilege(),
								marketMove.getPlayer()).copy(),
						marketMove.getPlayer(), false));
	}

	/**
	 * controlla se una mossa del tipo WorkMove è ammessa o no
	 */
	public boolean workmoveAllowed(WorkMove workMove) {
		return checkWorkSpace(workMove);
	}

	/**
	 * controlla se il valore del familiare utilizzato + servitori soddisfa il
	 * requisito relativo allo spazio azione selezionato per la mossa + se il
	 * primo spazio è già occupato effettua una diminuzione del valore
	 * dell'azione in base al Malus specificato nelle regole
	 */
	private boolean checkWorkSpace(WorkMove workMove) {
		Integer power = findWorkEffects(workMove);
		Integer servants = calculateEffectiveServants(workMove);
		switch (game.getPlayersOrder().size()) {
		case 2:
			if ("PRODUCTION".equals(workMove.getWorkType())) {
				if (!(game.getBoardgame().getProductionSpace().getMembers().isEmpty()
						|| containsClass(workMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class)
						|| workMove.getMemberUsed().getColor().equals(ACTION)))
					return false;
				if (game.getBoardgame().getProductionSpace().getSpaceRequirement() > power
						+ workMove.getMemberUsed().getValue() + servants)
					return false;
				break;
			} else {
				if (!(game.getBoardgame().getHarvestSpace().getMembers().isEmpty()
						|| containsClass(workMove.getPlayer().getEffects(), InOccupiedSpaceEffect.class)
						|| workMove.getMemberUsed().getColor().equals(ACTION)))
					return false;
				if (game.getBoardgame().getHarvestSpace().getSpaceRequirement() > power
						+ workMove.getMemberUsed().getValue() + servants)
					return false;
				break;
			}
		case 3:
		case 4:
			if ("PRODUCTION".equals(workMove.getWorkType())) {
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
				break;
			}
		default:
			return false;
		}
		return true;
	}

	/**
	 * calcola il valore totale di eventuali bonus o malus provenienti da
	 * scomuniche o effetti permanenti di carte leader
	 */
	private Integer findWorkEffects(WorkMove move) {
		Integer total = 0;
		String type = move.getWorkType();
		for (Effect e : move.getPlayer().getEffects()) {
			if (e instanceof WorkMalusEx && type.equals(((WorkMalusEx) e).getTypeOfWork()))
				total = total - ((WorkMalusEx) e).getValueOfMalus();
			if (e instanceof WorkBonusEffect && type.equals(((WorkBonusEffect) e).getTypeOfWork()))
				total = total + ((WorkBonusEffect) e).getWorkBonusValue();
		}
		return total;
	}

	/**
	 * gestisce una mossa del tipo WorkMove
	 */
	public void workmoveHandle(WorkMove workMove) throws IOException {
		if (workMove.getWorkType().equals(PRODUCTION))
			productionHandle(workMove);
		else
			harvestHandle(workMove);
	}

	/**
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
		if (!move.getMemberUsed().getColor().equals(ACTION))
			game.getBoardgame().getProductionSpace().getMembers().add(move.getMemberUsed());
		move.getMemberUsed().setUsed(true);
		if (!move.getMemberUsed().getColor().equals(UNCOLORED) && !move.getMemberUsed().getColor().equals(ACTION))
			game.getBoardgame().getProductionSpace().getColoredMemberOnIt().add(move.getPlayer().getColor());
		resourceHandler.subResource(move.getPlayer().getPersonalBoard().getResources(), move.getServantsAdded());
		Resource total = NOTHING.copy();
		for (BuildingCard card : move.getPlayer().getPersonalBoard().getBuildingsCards()) {
			if (valueOfAction >= card.getRequirement()) {
				effectManager.manageEffect(card.getPermanentEffect(), move.getPlayer(), total, mainGame);
			}
		}
		effectManager.manageEffect(move.getPlayer().getPersonalBoard().getBonusBoard().getProductionEffect(),
				move.getPlayer(), total, mainGame);
		resourceHandler.addResource(move.getPlayer().getPersonalBoard().getResources(), total);
	}

	/**
	 * gestisce la fase di Raccolto
	 */
	private void harvestHandle(WorkMove move) throws IOException {
		Integer power = findWorkEffects(move);
		Integer servants = calculateEffectiveServants(move);
		Integer valueOfAction = move.getMemberUsed().getValue() + power + servants;
		if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty())
			valueOfAction = valueOfAction - WORK_MALUS;
		if (!move.getMemberUsed().getColor().equals(ACTION))
			game.getBoardgame().getHarvestSpace().getMembers().add(move.getMemberUsed());
		move.getMemberUsed().setUsed(true);
		if (!move.getMemberUsed().getColor().equals(UNCOLORED) && !move.getMemberUsed().getColor().equals(ACTION))
			game.getBoardgame().getHarvestSpace().getColoredMemberOnIt().add(move.getMemberUsed().getColor());
		resourceHandler.subResource(move.getPlayer().getPersonalBoard().getResources(), move.getServantsAdded());
		Resource total = NOTHING.copy();
		for (TerritoryCard card : move.getPlayer().getPersonalBoard().getTerritoriesCards()) {
			if (valueOfAction >= card.getRequirement())
				effectManager.harvestHandle(card.getPermanentEffect(), total, move.getPlayer(), mainGame);
		}
		effectManager.harvestHandle(move.getPlayer().getPersonalBoard().getBonusBoard().getHarvestEffect(), total,
				move.getPlayer(), mainGame);
		resourceHandler.addResource(move.getPlayer().getPersonalBoard().getResources(), total);
	}

	/**
	 * controlla se una mossa del tipo CouncilMove è ammessa o no
	 */
	public boolean councilmoveAllowed(CouncilMove councilMove) {
		Integer servants = calculateEffectiveServants(councilMove);
		if (councilMove.getMemberUsed().getValue() + servants < game.getBoardgame().getCouncilPalace()
				.getSpaceRequirement())
			return false;
		return true;
	}

	/**
	 * gestisce una mossa del tipo CouncilMove prendendo i relativi bonus
	 */
	public void councilmoveHandle(CouncilMove councilMove) throws IOException {
		councilMove.getMemberUsed().setUsed(true);
		game.getBoardgame().getCouncilPalace().getMembers().add(councilMove.getMemberUsed());
		resourceHandler.subResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				councilMove.getServantsAdded());
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				resourceHandler.calculateResource(game.getBoardgame().getCouncilPalace().getReward().copy(),
						councilMove.getPlayer(), false));
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				resourceHandler.calculateResource(
						mainGame.selectCouncilPrivilege(game.getBoardgame().getCouncilPalace().getCouncilPrivilege(),
								councilMove.getPlayer()).copy(),
						councilMove.getPlayer(), false));
	}

	/**
	 * metodo che controlla se la carta Leader che il player vuole vendere è già
	 * stata attivata (per adesso non può venderla se è già stata attivata nel
	 * turno mentre se l'aveva attivata in passato ma non risulta "attiva in
	 * questo momento" il controllo da un esit positivo lo stesso
	 * 
	 * IMPORTANTE --> necessaria altra lista per tenere in memoria tutte le
	 * carte effettivamente attivate durante tutto il corso della partita?
	 */
	public boolean leadercardsellingAllowed(LeaderCardSelling move) {
		if (!move.getPlayer().getHandLeaderCards().contains(move.getLeaderCard()))
			return false;
		return true;
	}

	/**
	 * gestisce la vendita di una carta leader e consentirà la scelta del
	 * privilegio del consiglio --> posso solo vendere carte non attive
	 */
	public void leadercardsellingHandle(LeaderCardSelling move) throws IOException {
		move.getPlayer().getHandLeaderCards().remove(move.getLeaderCard());
		resourceHandler.addResource(move.getPlayer().getPersonalBoard().getResources(),
				resourceHandler.calculateResource(
						mainGame.selectCouncilPrivilege(SINGLE_PRIVILEGE, move.getPlayer()).copy(), move.getPlayer(),
						false));
	}

	/**
	 * controlla se i requisiti della carta leader che il player vuole attivare
	 * sono effettivmanete soddisfatti e nel caso indica quella carta come
	 * attivata --> sarà poi gestita dai vari controlli comunque interni a
	 * questa classe
	 */
	public boolean leadercardactivationAllowed(LeaderCardActivation move) {
		LeaderCardRequest req = move.getLeaderCard().getRequest();
		if ("Lucrezia Borgia".equals(move.getLeaderCard().getName())) {
			CardRequest r = (CardRequest) move.getLeaderCard().getRequest();
			return r.getTerritoryCards() <= move.getPlayer().getPersonalBoard().getTerritoriesCards().size()
					|| r.getCharacterCards() <= move.getPlayer().getPersonalBoard().getCharactersCards().size()
					|| r.getBuildingCards() <= move.getPlayer().getPersonalBoard().getBuildingsCards().size()
					|| r.getVentureCards() <= move.getPlayer().getPersonalBoard().getVenturesCards().size();

		}
		if (req instanceof CardRequest) {
			CardRequest r = (CardRequest) move.getLeaderCard().getRequest();
			if (!checkCardRequest(r, move))
				return false;
		} else if (req instanceof ResourceRequest) {
			ResourceRequest r = (ResourceRequest) move.getLeaderCard().getRequest();
			if (!checkResourceRequest(r, move))
				return false;
		} else if (req instanceof ResourceCardRequest) {
			ResourceCardRequest r = (ResourceCardRequest) move.getLeaderCard().getRequest();
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

	/**
	 * metodo che gestisce la procedura di attivazione di una carta -->
	 * comportamento differente se l'effetto è una volta per turno oppure se è
	 * un effetto permanente
	 */
	public void leadercardactivationHandle(LeaderCardActivation move) {
		effectManager.leaderEffectManage(move.getLeaderCard().getEffect(), move.getPlayer(), move.getLeaderCard(),
				mainGame);
		move.getPlayer().getActivatedLeaderCards().add(move.getLeaderCard());
		move.getPlayer().getLeaderCards().remove(move.getLeaderCard());
		move.getPlayer().getHandLeaderCards().remove(move.getLeaderCard());
	}

	/**
	 * metodo che dice se la lista di effetti di un player contiene un elemento
	 * di quella classe
	 */
	public boolean containsClass(List<Effect> list, Class<?> o) {
		for (Effect e : list) {
			if (e.getClass().equals(o)) {
				return true;
			}
		}
		return false;
	}

	public boolean endmoveAllowed(EndMove move) {
		return true;
	}

	/**
	 * metodo invocato se: - player dice intenzionalmente di aver finito il suo
	 * turno - timer per effettuare il proprio turno scade
	 */
	public void endmoveHandle(EndMove move) {
		if (!"noError".equals(move.getError()))
			mainGame.disconnectPlayer(move.getPlayer());
	}
}
