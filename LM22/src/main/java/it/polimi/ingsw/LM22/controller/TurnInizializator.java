package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Floor;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.excommunication.DiceMalusEx;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import it.polimi.ingsw.LM22.model.leader.LeaderResourceEffect;
import it.polimi.ingsw.LM22.model.leader.MemberBonusEffect;
import it.polimi.ingsw.LM22.model.leader.MemberChangeEffect;
import it.polimi.ingsw.LM22.model.leader.WorkAction;

public class TurnInizializator {

	private final Integer TERRITORY = 0;
	private final Integer CHARACTER = 1;
	private final Integer BUILDING = 2;
	private final Integer VENTURE = 3;
	private final Integer FIVE_PLAYERS = 5;
	private final Integer DICE_NUMBER = 3;
	private final Integer DICE_MAX = 6;
	private final Integer DICE_MIN = 1;
	protected final String[] colors = { "Orange", "Black", "White" };
	private final Integer WORKSPACES = 2;
	private final String[] workType = { "PRODUCTION", "HARVEST" };
	private final Integer NOTHING = 0;

	private MainGameController mainGC;
	private EffectManager effectManager;
	private ResourceHandler r;

	public TurnInizializator(EffectManager effectManager, ResourceHandler resourceHandler, MainGameController mainGC) {
		this.effectManager = effectManager;
		this.r = resourceHandler;
		this.mainGC = mainGC;
	}

	/**
	 * metodo generale che gestisce tutta la parte iniziale di preparazione di
	 * un nuovo turno --> invocherà tutti i metodi minori in grado di svolgere
	 * le varie funzionalità richieste per la preparazione di un turno e la
	 * pulizia della Board : - distribuzione carte (di 4 tipi) - lancio dei dadi
	 * - calcolo nuovo ordine di turno -
	 */
	public void initializeTurn(Game game) throws IOException {
		setGameTurn(game);
		setNewPlayersOrder(game);
		cleanBoardGame(game);
		manageLeaderCards(game);
		throwDices(game);
		setFamilyMembersValue(game);
		distributeDevelopmentCards(game);
		if (game.getPlayersOrder().size() == FIVE_PLAYERS)
			distributeNewResources(game);
	}

	private void setGameTurn(Game game) {
		if (game.getRound().equals(1))
			game.setRound(2);
		else {
			game.setRound(1);
			game.setPeriod(game.getPeriod() + 1);
		}
	}

	/**
	 * metodo che ritira tutte le carte non prese e ritira tutti i familiari
	 */
	private void cleanBoardGame(Game game) {
		for (Tower tower : game.getBoardgame().getTowers()) {
			tower.setOccupied(false);
			tower.setColoredMembersOnIt(new ArrayList<String>());
			for (Floor f : tower.getFloor())
				f.getSpace().setMember(null);
		}
		retireMembers(game);
	}

	/**
	 * metodo che si occupa di riportare tutti i familiari al Player e settare
	 * il loro flag used a false pulendo tutti i vari AbstractSpace
	 */
	private void retireMembers(Game game) {
		for (Player p : game.getPlayers()) {
			for (FamilyMember m : p.getMembers()) {
				m.setUsed(false);
			}
		}
		for (MarketSpace space : game.getBoardgame().getMarket()) {
			space.setMember(null);
		}
		for (int cont = 0; cont < WORKSPACES; cont++) {
			game.getBoardgame().getWorkSpace(workType[cont]).setMembers(new ArrayList<FamilyMember>());
			game.getBoardgame().getWorkSpace(workType[cont]).setColoredMemberOnIt(new ArrayList<String>());
		}
	}

	/**
	 * metodo che consente di distribuire tutte le carte sviluppo
	 */
	protected void distributeDevelopmentCards(Game game) {
		distributeTerritoryCards(game);
		distributeCharacterCards(game);
		distributeBuildingCards(game);
		distributeVentureCards(game);
	}

	/**
	 * ognuno dei seguenti metodi cerca la prima carta del periodo attuale per
	 * ogni piano della propria torre
	 */
	protected void distributeTerritoryCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[TERRITORY].getFloor()) {
			int i = 0;
			TerritoryCard c = game.getTerritoryCards().get(i);
			for (; !c.getPeriod().equals(game.getPeriod());) {
				i++;
				c = game.getTerritoryCards().get(i);
			}
			f.setCard(c);
			game.getTerritoryCards().remove(c);
		}
	}

	protected void distributeCharacterCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[CHARACTER].getFloor()) {
			int i = 0;
			CharacterCard c = game.getCharacterCards().get(i);
			for (; !c.getPeriod().equals(game.getPeriod());) {
				i++;
				c = game.getCharacterCards().get(i);
			}
			f.setCard(c);
			game.getCharacterCards().remove(c);
		}
	}

	protected void distributeBuildingCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[BUILDING].getFloor()) {
			int i = 0;
			BuildingCard c = game.getBuildingCards().get(i);
			for (; !c.getPeriod().equals(game.getPeriod());) {
				i++;
				c = game.getBuildingCards().get(i);
			}
			f.setCard(c);
			game.getBuildingCards().remove(c);
		}
	}

	protected void distributeVentureCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[VENTURE].getFloor()) {
			int i = 0;
			VentureCard c = game.getVentureCards().get(i);
			for (; !c.getPeriod().equals(game.getPeriod());) {
				i++;
				c = game.getVentureCards().get(i);
			}
			f.setCard(c);
			game.getVentureCards().remove(c);
		}
	}

	/**
	 * metodo che reinizializza i valori dei dadi
	 */
	protected void throwDices(Game game) {
		Random random = new Random();
		for (int cont = 0; cont < DICE_NUMBER; cont++) {
			game.getBoardgame().setDice(colors[cont], random.nextInt(DICE_MAX) + DICE_MIN);
		}
	}

	/**
	 * metodo che in base al valore dei dadi tirati conferisce ai familiari il
	 * loro valore --> qui avviene anche il controllo per le scomuniche che
	 * danno dei malus ai dadi + carta leader che aumenta il familiare neutro
	 */
	protected void setFamilyMembersValue(Game game) throws IOException {
		for (Player p : game.getPlayersOrder()) {
			Integer malus = 0;
			for (Effect e : p.getEffects())
				if (e instanceof DiceMalusEx)
					malus = malus - ((DiceMalusEx) e).getMalus();
			for (FamilyMember m : p.getMembers()) {
				if (!"Uncolored".equals(m.getColor()))
					m.setValue(game.getBoardgame().getDice(m.getColor()) + malus);
			}
		}
		updateFamilyMembersValue(game);
	}

	/**
	 * metodo che gestisce gli effetti di carte leader che modificano il valore
	 * dei familiari del giocatore che l'ha attivata NON GESTISCE FEDERICO DI
	 * MONTEFELTRO
	 */
	private void updateFamilyMembersValue(Game game) throws IOException {
		for (Player p : game.getPlayersOrder())
			for (Effect e : p.getEffects()) {
				if (e instanceof MemberChangeEffect) {
					effectManager.leaderEffectManage(e, p, new LeaderCard(), mainGC);
				} else if (e instanceof MemberBonusEffect) {
					effectManager.leaderEffectManage(e, p, new LeaderCard(), mainGC);
				}
			}
	}

	/**
	 * metodo che presi in ingresso l'ordine del turno appena finito e la lista
	 * dei famiiari inseriti nel CouncilSpace e genera l'ordine per il nuovo
	 * turno pulendo già il CouncilSpace
	 */
	protected void setNewPlayersOrder(Game game) {
		List<FamilyMember> members = game.getBoardgame().getCouncilPalace().getMembers();
		List<Player> newOrder = new ArrayList<Player>();
		for (FamilyMember m : members) {
			if (!newOrder.contains(m.getPlayer()))
				newOrder.add(m.getPlayer());
			m.setUsed(false);
		}
		while (!members.isEmpty())
			members.remove(0);
		for (Player p : game.getPlayersOrder()) {
			if (!newOrder.contains(p)) {
				newOrder.add(p);
			}
		}
		game.setPlayersOrder(newOrder);
	}

	/**
	 * metodo che consente di redistribuire le carte leader attivate tra
	 * activated e leaderCard in base al tipo di effetto che la carta ha NB
	 * stare attento anche alle carte leader che aggiungono un effetto alla
	 * lista Effect di player
	 */
	private void manageLeaderCards(Game game) {
		for (Player p : game.getPlayersOrder()) {
			ArrayList<LeaderCard> removeLD = new ArrayList<LeaderCard>();
			for (LeaderCard card : p.getActivatedLeaderCards()) {
				if (card.getEffect() instanceof LeaderResourceEffect
						|| (card.getEffect() instanceof MemberChangeEffect
								&& "Federico da Montefeltro".equals(card.getName()))
						|| card.getEffect() instanceof WorkAction) {
					p.getLeaderCards().add(card);
					removeLD.add(card);
				}
			}
			p.getActivatedLeaderCards().removeAll(removeLD);
		}
	}

	/**
	 * metodo ipotizzato per la modalità con il quinto giocatore --> IDEA sarebe
	 * quella di distribuire delle risorse in modo prefissato e decrescente in
	 * base all'ordine inverso di turno di quello che è stato appena calcolato
	 * per il nuovo turno che deve iniziare (il quinto giocatore prende molte
	 * risorse, il quarto meno e così via fino al primo)
	 */
	private void distributeNewResources(Game game) {
		int i = 1;
		for (Player p : game.getPlayersOrder()) {
			r.addResource(p.getPersonalBoard().getResources(), new Resource(i, i, i, i, NOTHING, NOTHING, NOTHING));
			i++;
		}
	}
}
