package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.FileParser;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.WorkSpace;
import it.polimi.ingsw.LM22.network.server.IPlayer;

public class InitialConfigurator extends TurnInizializator {
	FileParser fileParser = new FileParser();
	private final Integer BASE_WOOD_STONE = 2;
	private final Integer BASE_SERVANTS = 3;
	private final Integer BASE_COINS = 5;
	private final Integer NO_RESOURCE = 0;

	// Colori giocatori
	private final String[] PLAYER_COLOR = { "Blue", "Green", "Red", "Yellow" };
	// Colori familiari
	private final String[] MEMBER_COLOR = { "Orange", "Black", "White", "Uncolored" };

	/*
	 * costruttore che chiamerà uno dopo l'altro tutti i metodi privati che sono
	 * dichiarati successivamente all'interno di questa classe
	 */
	public InitialConfigurator(Game game, IPlayer iplayer[], int nPlayer) throws IOException {
		initializeTurn(game);
		throwDices(game);
		setupPlayers(game, iplayer, nPlayer);
		loadConfiguration(game);
		setNewPlayersOrder(game);
		giveInitialResources(game);
		distribuiteCards();
	}

	/*
	 * metodo in override perchè la prima volta quando viene istanziata la
	 * partita devo fare cose diverse rispetto a TurnInizializator
	 */
	@Override
	public void initializeTurn(Game game) {
		game.setPeriod(1);
		game.setRound(1);

		WorkSpace harv = new WorkSpace();
		harv.setSpaceRequirement(1);
		harv.setWorkType("PRODUCTION");
		game.getBoardgame().setHarvestSpace(harv);
		WorkSpace prod = new WorkSpace();
		prod.setSpaceRequirement(1);
		prod.setWorkType("HARVEST");
		game.getBoardgame().setProductionSpace(prod);

		// boardgame
		// altri parametri?
	}

	/*
	 * inizializzo i giocatori con i dati forniti dal network
	 */
	private void setupPlayers(Game game, IPlayer iplayer[], int nPlayer) throws RemoteException {
		Player players[] = new Player[nPlayer];
		game.setPlayers(players);
		int i = 0;
		for (Player p : game.getPlayers()) {
			List<FamilyMember> members = new ArrayList<FamilyMember>();
			int j;
			for (j = 0; j < 3; j++) {
				FamilyMember fm = new FamilyMember(p, MEMBER_COLOR[j]);
				fm.setValue(game.getBoardgame().getDice(MEMBER_COLOR[j]));
				members.add(fm);
			}
			FamilyMember fm = new FamilyMember(p, MEMBER_COLOR[j]);
			fm.setValue(0);
			members.add(fm);
			// personalBoard: personal tile & dev cards
			players[i] = new Player(iplayer[i].getName(), PLAYER_COLOR[i], members);
			i++;
		}
	}

	/*
	 * setta il primo ordine da seguire senza osservare il CouncilSpace -->
	 * prende l'array dei Player in game e setta la lista del turno (che è vuota
	 * inizialmente)
	 */
	@Override
	protected void setNewPlayersOrder(Game game) {
		Random random = new Random();
		List<Player> p = new ArrayList<Player>();
		for (Player player : game.getPlayers()) {
			// i put the new item in the list randomly
			p.add(random.nextInt(p.size() + 1), player);
		}
		game.setPlayersOrder(p);
	}

	/*
	 * metodo che consente di istanziare tutto il model con i relativi
	 * caricamenti da file, in modo da ottenere tutti gli oggetti utili alla
	 * partita --> tendenzialmente una delle prime cose da fare alla creazione
	 * di una nuova partita
	 */
	private void gameCreation() {

	}

	/*
	 * metodo che costruisce il Model con i parametri principali per poter
	 * iniziare la partita
	 */
	private void loadConfiguration(Game game) throws IOException {
		fileParser.getDevCards(game);
		// carte scomunica
		fileParser.getLeaderCards(game);// carte leader (random)
		fileParser.getFaithGrid(game);
		fileParser.getMarketSpace(game);
		// torri
		// plancia : selezionare personalTile (random)
		fileParser.getCouncilSpace(game);
	}

	private void distribuiteCards() {

	}

	/*
	 * metodo che implementa la fase di scelta delle carte leader con relativo
	 * passaggio al giocatore successivo delle carte rimanenti
	 */
	private void leaderDistribution() {

	}

	/*
	 * consente di far scegliere ad ogni giocatore la carta leader e inserirla
	 * nella sua PersonalBoard
	 */
	private void leaderSelection() {

	}

	/*
	 * metodo invocato direttamente nel costruttore di questa classe che
	 * permette di distribuire le risorse con cui i player iniziano la partita
	 * in base all'ordine random generato nella creazione della partita stessa
	 */
	private void giveInitialResources(Game game) {
		int cont = 0;
		for (Player p : game.getPlayersOrder()) {
			p.getPersonalBoard().setResources(new Resource(BASE_WOOD_STONE, BASE_WOOD_STONE, BASE_SERVANTS,
					BASE_COINS + cont, NO_RESOURCE, NO_RESOURCE, NO_RESOURCE));
			cont++;
		}
	}
}
