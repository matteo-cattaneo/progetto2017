package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.WorkSpace;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;

public class InitialConfigurator extends TurnInizializator {
	private static final Logger logger = Logger.getLogger(InitialConfigurator.class.getClass().getSimpleName());
	private FileParser fileParser = new FileParser();
	private List<PlayerInfo> playerRoom;
	private static final Integer BASE_WOOD_STONE = 2;
	private static final Integer BASE_SERVANTS = 3;
	private static final Integer BASE_COINS = 5;
	private static final Integer NO_RESOURCE = 0;
	private static final Integer UNCOLORED_VALUE = 0;
	private static final Integer INIT = 1;

	// Colori giocatori
	private static final String[] PLAYER_COLOR = { "Blue", "Green", "Red", "Yellow" };
	// Colori familiari
	private static final String[] MEMBER_COLOR = { "Orange", "Black", "White", "Uncolored" };

	/**
	 * costruttore che chiamerà uno dopo l'altro tutti i metodi privati che sono
	 * dichiarati successivamente all'interno di questa classe
	 */
	public InitialConfigurator(List<PlayerInfo> playerRoom, EffectManager m, MainGameController mainGC) {
		super(m, mainGC);
		this.playerRoom = playerRoom;
	}

	/**
	 * metodo in override perchè la prima volta quando viene istanziata la
	 * partita devo fare cose diverse rispetto a TurnInizializator
	 */
	@Override
	public void initializeTurn(Game game) {
		game.setPeriod(INIT);
		game.setRound(INIT);

		// inizializzazione workspace
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
			logger.log(Level.SEVERE, "Errore nel caricamento dei file JSON", e);
		}
		giveInitialResources(game);
		mixCards(game);
		distributeDevelopmentCards(game);
		leaderDistribution(game);
	}

	/**
	 * inizializzo i giocatori con i dati forniti dal network
	 */
	private void setupPlayers(Game game, List<PlayerInfo> playerRoom) {
		Player[] players = new Player[playerRoom.size()];
		game.setPlayers(players);
		for (int i = 0; i < game.getPlayers().length; i++) {
			players[i] = new Player(playerRoom.get(i).getName(), PLAYER_COLOR[i]);
			List<FamilyMember> members = new ArrayList<>();
			int j;
			// assegno i 3 familiari colorari ad ogni player
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
		}
	}

	/**
	 * setta il primo ordine da seguire senza osservare il CouncilSpace -->
	 * prende l'array dei Player in game e setta la lista del turno (che è vuota
	 * inizialmente)
	 */
	@Override
	protected void setNewPlayersOrder(Game game) {
		Random random = new Random();
		List<Player> p = new ArrayList<>();
		for (Player player : game.getPlayers()) {
			p.add(random.nextInt(p.size() + 1), player);
		}
		game.setPlayersOrder(p);
	}

	/**
	 * metodo che costruisce il Model con i parametri principali per poter
	 * iniziare la partita caricandoli da file
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

	/**
	 * metodo che mischia le carte utilizzando applicando il metodo shuffle
	 * sulle 4 liste di carte
	 */
	private void mixCards(Game game) {
		Collections.shuffle(game.getTerritoryCards());
		Collections.shuffle(game.getCharacterCards());
		Collections.shuffle(game.getBuildingCards());
		Collections.shuffle(game.getVentureCards());
	}

	/**
	 * metodo che implementa la fase di distribuzione random delle carte leader,
	 * cosi da poi poterne consentire la scelta tramite la procedura iniziale
	 */
	private void leaderDistribution(Game game) {
		Collections.shuffle(game.getLeaderCards());
		for (Player p : game.getPlayers()) {
			for (int i = 0; i < 4; i++) {
				/**
				 * salvo le carte leader nella lista LeaderCards temporanemante,
				 * poi le carte selezionate del player verranno inserite man
				 * mano nella lista HandLeaderCards per l'inizio della partita
				 */
				p.getLeaderCards().add(game.getLeaderCards().remove(0));
			}
		}
	}

	/**
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
