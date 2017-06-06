package it.polimi.ingsw.LM22.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Floor;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.excommunication.DiceMalusEx;
import it.polimi.ingsw.LM22.model.leader.MemberBonusEffect;
import it.polimi.ingsw.LM22.model.leader.MemberChangeEffect;
import it.polimi.ingsw.LM22.model.leader.MemberValueEffect;

public class TurnInizializator {

	private final Integer TERRITORY = 0;
	private final Integer CHARACTER = 1;
	private final Integer BUILDING = 2;
	private final Integer VENTURE = 3;
	private final Integer FIVE_PLAYERS = 5;
	private final Integer UNCOLORED_MEMBER = 0;
	private final String UNCOLORED = "Uncolored";
	private final Integer DICE_NUMBER = 3;
	private final Integer DICE_MAX = 6;
	private final Integer DICE_MIN = 1;
	protected final String[] colors = { "Orange", "Black", "White" };
	private final Integer WORKSPACES = 2;
	private final String[] workType = { "PRODUCTION", "HARVEST" };
	private final Integer TOWERS_NUM = 4;

	/*
	 * metodo generale che gestisce tutta la parte iniziale di preparazione di
	 * un nuovo turno --> invocherà tutti i metodi minori in grado di svolgere
	 * le varie funzionalità richieste per la preparazione di un turno e la
	 * pulizia della Board : - distribuzione carte (di 4 tipi) - lancio dei dadi
	 * - calcolo nuovo ordine di turno -
	 */
	public void initializeTurn(Game game) {
		setNewPlayersOrder(game);
		cleanBoardGame(game);
		throwDices(game);
		setFamilyMembersValue(game);
		distributeDevelopmentCards(game);
		if (game.getPlayersOrder().size() == FIVE_PLAYERS)
			distributeNewResources(game);
	}

	/*
	 * metodo che ritira tutte le carte non prese e ritira tutti i familiari
	 */
	private void cleanBoardGame(Game game) {
		for (Tower tower : game.getBoardgame().getTowers()) {
			tower.setOccupied(false);
			for (Floor f : tower.getFloor())
				f.setCard(null);
			// settaggio a null dello spazio delle carte
		}
		retireMembers(game);
	}

	/*
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
			game.getBoardgame().getWorkSpace(workType[cont]).setMembers(null);
		}
	}

	/*
	 * metodo che consente di distribuire tutte e carte sviluppo
	 */
	protected void distributeDevelopmentCards(Game game) {
		distributeTerritoryCards(game);
		distributeCharacterCards(game);
		distributeBuildingCards(game);
		distributeVentureCards(game);
	}

	protected void distributeTerritoryCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[TERRITORY].getFloor()) {
			for (TerritoryCard c : game.getTerritoryCards())
				if (c.getPeriod() == game.getPeriod()) {
					f.setCard(c);
					game.getTerritoryCards().remove(c);
				}
		}
	}

	protected void distributeCharacterCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[CHARACTER].getFloor()) {
			for (CharacterCard c : game.getCharacterCards())
				if (c.getPeriod() == game.getPeriod()) {
					f.setCard(c);
					game.getCharacterCards().remove(c);
				}
		}
	}

	protected void distributeBuildingCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[BUILDING].getFloor()) {
			for (BuildingCard c : game.getBuildingCards())
				if (c.getPeriod() == game.getPeriod()) {
					f.setCard(c);
					game.getBuildingCards().remove(c);
				}
		}
	}

	protected void distributeVentureCards(Game game) {
		for (Floor f : game.getBoardgame().getTowers()[VENTURE].getFloor()) {
			for (VentureCard c : game.getVentureCards())
				if (c.getPeriod() == game.getPeriod()) {
					f.setCard(c);
					game.getVentureCards().remove(c);
				}
		}
	}

	/*
	 * metodo che reinizializza i valori dei dadi
	 */
	protected void throwDices(Game game) {
		Random random = new Random();
		for (int cont = 0; cont < DICE_NUMBER; cont++) {
			game.getBoardgame().setDice(colors[cont], (random.nextInt(DICE_MAX) + DICE_MIN));
		}
	}

	/*
	 * metodo che in base al valore dei dadi tirati conferisce ai familiari il
	 * loro valore --> qui avviene anche il controllo per le scomuniche che
	 * danno dei malus ai dadi
	 */
	protected void setFamilyMembersValue(Game game) {
		for (Player p : game.getPlayersOrder()) {
			Integer malus = 0;
			for (Effect e : p.getEffects())
				if (e instanceof DiceMalusEx)
					malus = malus - ((DiceMalusEx) e).getMalus();
			for (FamilyMember m : p.getMembers()) {
				if (m.getColor() != "Uncolored")
					m.setValue(game.getBoardgame().getDice(m.getColor() + malus));
				else
					m.setValue(UNCOLORED_MEMBER);
			}
		}
	}

	/*
	 * metodo che gestisce gli effetti di carte leader che modificano il valore
	 * dei familiari del giocatore che l'ha attivata NON GESTISCE FEDERICO DI
	 * MONTEFELTRO
	 */
	public void updateFamilyMembersValue(Player p, MemberValueEffect e) {
		String color = new String();
		if (e instanceof MemberChangeEffect) {
			color = ((MemberChangeEffect) e).getTypeOfMember();
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
						m.setValue(((MemberChangeEffect) e).getNewValueOfMember());
						break;
					}
			}
		} else if (e instanceof MemberBonusEffect) {
			color = ((MemberBonusEffect) e).getTypeOfMember();
			switch (color) {
			case "ALL": {
				for (FamilyMember f : p.getMembers()) {
					f.setValue(f.getValue() + ((MemberBonusEffect) e).getValueOfBonus());
				}
			}
			}
		}
	}

	/*
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
		game.getBoardgame().getCouncilPalace().setMembers(null);
		for (Player p : game.getPlayersOrder()) {
			if (!newOrder.contains(p)) {
				newOrder.add(p);
			}
		}
		game.setPlayersOrder(newOrder);
	}

	/*
	 * metodo ipotizzato per la modalità con il quinto giocatore --> IDEA sarebe
	 * quella di distribuire delle risorse in modo prefissato e decrescente in
	 * base all'ordine inverso di turno di quello che è stato appena calcolato
	 * per il nuovo turno che deve iniziare (il quinto giocatore prende molte
	 * risorse, il quarto meno e così via fino al primo)
	 */
	private void distributeNewResources(Game game) {

	}
}
