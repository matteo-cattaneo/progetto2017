package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.FileParser;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.WorkSpace;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;

public class InitialConfigurator extends TurnInizializator {
	private final Logger LOGGER = Logger.getLogger(InitialConfigurator.class.getClass().getSimpleName());
	private FileParser fileParser = new FileParser();
	private ArrayList<PlayerInfo> playerRoom;
	private final Integer BASE_WOOD_STONE = 2;
	private final Integer BASE_SERVANTS = 3;
	private final Integer BASE_COINS = 5;
	private final Integer NO_RESOURCE = 0;
	private final Integer UNCOLORED_VALUE = 0;
	private final Integer INIT = 1;

	// Colori giocatori
	private final String[] PLAYER_COLOR = { "Blue", "Green", "Red", "Yellow" };
	// Colori familiari
	private final String[] MEMBER_COLOR = { "Orange", "Black", "White", "Uncolored" };

	/*
	 * costruttore che chiamerà uno dopo l'altro tutti i metodi privati che sono
	 * dichiarati successivamente all'interno di questa classe
	 */
	public InitialConfigurator(Game game, ArrayList<PlayerInfo> playerRoom, ResourceHandler r, EffectManager m) {
		super(m, r);
		this.playerRoom = playerRoom;
	}

	/*
	 * metodo in override perchè la prima volta quando viene istanziata la
	 * partita devo fare cose diverse rispetto a TurnInizializator
	 */
	@Override
	public void initializeTurn(Game game) {
		game.setPeriod(INIT);
		game.setRound(INIT);

		WorkSpace harv = new WorkSpace();
		harv.setSpaceRequirement(1);
		harv.setWorkType("HARVEST");
		game.getBoardgame().setHarvestSpace(harv);
		WorkSpace prod = new WorkSpace();
		prod.setSpaceRequirement(1);
		prod.setWorkType("PRODUCTION");
		game.getBoardgame().setProductionSpace(prod);

		throwDices(game);
		setupPlayers(game, playerRoom);
		setNewPlayersOrder(game);
		try {
			loadConfiguration(game);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Errore nel caricamento dei file JSON", e);
		}
		giveInitialResources(game);
		mixCards(game);
		distributeDevelopmentCards(game);
		leaderDistribution(game);
		personalBoardTileDistribution(game);
	}

	/*
	 * inizializzo i giocatori con i dati forniti dal network
	 */
	private void setupPlayers(Game game, ArrayList<PlayerInfo> playerRoom) {
		Player players[] = new Player[playerRoom.size()];
		game.setPlayers(players);
		int i = 0;
		for (Player p : game.getPlayers()) {
			players[i] = new Player(playerRoom.get(i).getName(), PLAYER_COLOR[i]);
			List<FamilyMember> members = new ArrayList<FamilyMember>();
			int j;
			for (j = 0; j < 3; j++) {
				FamilyMember fm = new FamilyMember(players[i], MEMBER_COLOR[j]);
				fm.setValue(game.getBoardgame().getDice(MEMBER_COLOR[j]));
				members.add(fm);
			}
			// familiare neutro
			FamilyMember fm = new FamilyMember(players[i], MEMBER_COLOR[j]);
			fm.setValue(UNCOLORED_VALUE);
			members.add(fm);
			players[i].setMembers(members);
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
		fileParser.getLeaderCards(game);
		fileParser.getFaithGrid(game);
		fileParser.getExCommunicationsTile(game);
		fileParser.getMarketSpace(game);
		fileParser.getCardSpace(game);
		fileParser.getPersonalBonusTile(game);
		fileParser.getCouncilSpace(game);
		fileParser.getMoveTimeouts(game);
	}

	/*
	 * metodo che mischia le carte utilizzando applicando il metodo shuffle
	 * sulle 4 liste di carte
	 */
	private void mixCards(Game game) {
		Collections.shuffle(game.getTerritoryCards());
		Collections.shuffle(game.getCharacterCards());
		Collections.shuffle(game.getBuildingCards());
		Collections.shuffle(game.getVentureCards());
	}

	/*
	 * metodo che implementa la fase di distribuzione random delle carte leader
	 * con relativo passaggio al giocatore successivo delle carte rimanenti
	 */
	private void leaderDistribution(Game game) {
		Random random = new Random();
		for (Player p : game.getPlayers()) {
			for (int i = 0; i < 4; i++) {
				p.getHandLeaderCards().add(game.getLeaderCards().remove(random.nextInt(game.getLeaderCards().size())));
			}
		}
	}

	// distribuzione casuale delle personal tile
	private void personalBoardTileDistribution(Game game) {
		Random random = new Random();
		int num;
		for (Player p : game.getPlayers()) {
			for (num = random.nextInt(4); game.getPersonalBonusTile()[num] == null; num = random.nextInt(4))
				;
			p.getPersonalBoard().setBonusBoard(game.getPersonalBonusTile()[num]);
			game.getPersonalBonusTile()[num] = null;
		}
	}

	/*
	 * consente di far scegliere ad ogni giocatore la carta leader e inserirla
	 * nella sua PersonalBoard //RANDOM (VEDI SOPRA)
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
