package it.polimi.ingsw.LM22.network.client;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.PersonalBonusTile;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

/**
 * Classe che contiene tutti i metodi che permettono all'utente di
 * interfacciarsi con la CLI
 */
public class CLIinterface extends AbstractUI {
	private static final String DEFAULT_IP = "localhost";
	private static final String UNCOLORED = "Uncolored";
	private static final String RESTART = "Restart@";
	private static final String RESTART_CHOICE = "0: Restart";
	private static final String TIMEOUT = "Timeout: ";
	private static final String SECONDS = " seconds";
	// Colori familiari
	private static final String[] MEMBER_COLOR = { "Orange", "Black", "White", "Uncolored" };

	private static final String CARDMOVE = "Card";
	private static final String MARKETMOVE = "Market";
	private static final String WORKMOVE = "Work";
	private static final String COUNCILMOVE = "Council";

	private static final String TERRITORY = "TERRITORY";
	private static final String CHARACTER = "CHARACTER";
	private static final String BUILDING = "BUILDING";
	private static final String VENTURE = "VENTURE";

	private static final String PRODUCTION = "PRODUCTION";
	private static final String HARVEST = "HARVEST";

	private Scanner in = new Scanner(System.in);
	private String move = new String();
	private Game game;
	private String name;
	private boolean memberMove = false;
	private long timeout = 0;
	private long time;
	private String leaderSelected = "";

	/**
	 * permette di indicare se è possibile muovere un familiare
	 */
	@Override
	public void setMemberMove(boolean memberMove) {
		this.memberMove = memberMove;
	}

	/**
	 * costruisco la stringa mossa per poi poterla inviare al server
	 */
	private void setMove(String add) {
		move = move + add + "@";
	}

	/**
	 * restituisce al chiamante la mossa creata in precedenza tramite il menu
	 */
	@Override
	public String getMove() throws RemoteException {
		return move;
	}

	/**
	 * menu principale da cui parte la costruzione guidata della mossa
	 */
	@Override
	public void printMoveMenu() throws RemoteException {
		move = RESTART;
		// mostro il menu se l'utente ha richiesto di rieffettuare la mossa
		while (RESTART.equals(move)) {
			move = "";
			showMsg(TIMEOUT + timeout + SECONDS);
			showMsg("Choose your Move:");
			if (!memberMove)
				showMsg("1: Move a Member");
			showMsg("2: Show card");
			if (!getPlayer(name, game).getHandLeaderCards().isEmpty())
				showMsg("3: Sell a LeaderCard");
			if (!getPlayer(name, game).getLeaderCards().isEmpty()
					|| !getPlayer(name, game).getHandLeaderCards().isEmpty())
				showMsg("4: Activate a LeaderCard");
			showMsg("5: End turn");

			int option = input();
			switch (option) {
			case 1:
				if (!memberMove)
					printMemberMoveMenu();
				else
					printInvalidInput();
				break;
			case 2:
				showCard();
				break;
			case 3:
				if (!getPlayer(name, game).getHandLeaderCards().isEmpty())
					printSellLeaderCardMenu();
				else
					printInvalidInput();
				break;
			case 4:
				if (!getPlayer(name, game).getLeaderCards().isEmpty()
						|| !getPlayer(name, game).getHandLeaderCards().isEmpty())
					printActivateLeaderCardMenu();
				else
					printInvalidInput();
				break;
			case 5:
				setMove("End");
				memberMove = false;
				timeout = game.getMoveTimer();
				break;
			default:
				printInvalidInput();
				move = RESTART;
				break;
			}
		}
	}

	private void pleaseInsertNumber() {
		showMsg("Please insert a number!");
	}

	/**
	 * metodo che permette all utente di visualizzare i dettagli di una carta,
	 * inserendo anche solo una parte del nome della carta
	 */
	private void showCard() throws RemoteException {
		String cardName;
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Insert the card name: (no case sensitive)");
		// gestione timeout
		time = System.currentTimeMillis() / 1000;
		ExecutorService exe = Executors.newFixedThreadPool(1);
		Future<String> future = exe.submit(() -> in.nextLine());
		try {
			cardName = future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			cardName = "";
			StartClient.logger.log(Level.SEVERE, e.getMessage(), e);
			move = "End@Disconnect@";
			alert("Move time expired, now you have been disconnected!");
			alert("Please login again...");
			System.exit(0);
		}
		timeout = timeout - (System.currentTimeMillis() / 1000 - time);
		DevelopmentCard card = null;
		boolean leader = false;
		showMsg("");
		if (!"".equals(cardName)) {
			// verifico se è una development card
			for (int j = 0; j < 4; j++)
				for (Tower t : game.getBoardgame().getTowers())
					if (t.getFloor()[j].getCard().getName() != null
							&& t.getFloor()[j].getCard().getName().toLowerCase().startsWith(cardName.toLowerCase()))
						card = t.getFloor()[j].getCard();
			// verifico se una carta leader
			leader = showLeader(cardName);
		}
		if (card != null)
			msgFormat(card.getInfo());
		else if (!leader)
			showMsg("Card not found!");
		showMsg("");
		move = RESTART;
	}

	private boolean showLeader(String cardName) throws RemoteException {
		LeaderCard lcard = null;
		// verifico se è una leader card
		for (LeaderCard ld : getPlayer(name, game).getHandLeaderCards())
			if (ld.getName().toLowerCase().startsWith(cardName.toLowerCase()))
				lcard = ld;
		for (LeaderCard ld : getPlayer(name, game).getLeaderCards())
			if (ld.getName().toLowerCase().startsWith(cardName.toLowerCase()))
				lcard = ld;
		for (LeaderCard ld : getPlayer(name, game).getActivatedLeaderCards())
			if (ld.getName().toLowerCase().startsWith(cardName.toLowerCase()))
				lcard = ld;
		if (lcard != null) {
			msgFormat(lcard.getInfo());
			return true;
		} else
			return false;
	}

	@Override
	public void printMemberMoveMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		memberMove = true;
		showMsg("Choose your move:");
		showMsg("1: Card");
		showMsg("2: Market");
		showMsg("3: Work");
		showMsg("4: Council");
		showMsg(RESTART_CHOICE);

		int option = input();
		switch (option) {
		case 1:
			printCardMoveMenu();
			break;
		case 2:
			printMarketMoveMenu();
			break;
		case 3:
			printWorkMoveMenu();
			break;
		case 4:
			printCouncilMoveMenu();
			break;
		case 0:
			move = RESTART;
			memberMove = false;
			break;
		default:
			printInvalidInput();
			printMemberMoveMenu();
			break;
		}
	}

	@Override
	public void printCardMoveMenu() throws RemoteException {
		setMove(CARDMOVE);
		printFamilyMemberMenu();
		if (!RESTART.equals(move))
			setMove(printServantsAddictionMenu());
		if (!RESTART.equals(move))
			setMove(printTowersMenu());
		if (!RESTART.equals(move))
			setMove(printLevelsMenu());
	}

	@Override
	public void printFamilyMemberMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Choose your Family Member you want to move:");
		int i = 0;
		for (FamilyMember fm : getPlayer(name, game).getMembers()) {
			if (!fm.isUsed())
				showMsg((i + 1) + ": " + MEMBER_COLOR[i]);
			i++;
		}
		showMsg(RESTART_CHOICE);

		int option = input();
		switch (option) {
		case 1:
		case 2:
		case 3:
		case 4:
			if (!getPlayer(name, game).getMembers().get(option - 1).isUsed()) {
				setMove(MEMBER_COLOR[option - 1]);
			} else {
				printInvalidInput();
				printFamilyMemberMenu();
			}
			break;
		case 0:
			move = RESTART;
			memberMove = false;
			break;
		default:
			printInvalidInput();
			printFamilyMemberMenu();
		}
	}

	@Override
	public String printServantsAddictionMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Insert how many servants you want to use");
		Integer servants = input();
		if (servants >= 0 && servants <= getPlayer(name, game).getPersonalBoard().getResources().getServants()) {
			return servants.toString();
		} else {
			printInvalidInput();
			return printServantsAddictionMenu();
		}
	}

	@Override
	public String printTowersMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Choose the Tower:");
		showMsg("1: " + TERRITORY);
		showMsg("2: " + CHARACTER);
		showMsg("3: " + BUILDING);
		showMsg("4: " + VENTURE);

		int option = input();
		switch (option) {
		case 1:
		case 2:
		case 3:
		case 4:
			return String.valueOf(option - 1);
		default:
			printInvalidInput();
			return printTowersMenu();
		}
	}

	@Override
	public String printLevelsMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Insert the level of the tower (1 - 2 - 3 - 4):");

		int option = input();
		switch (option) {
		case 1:
		case 2:
		case 3:
		case 4:
			return String.valueOf(option - 1);
		default:
			printInvalidInput();
			return printLevelsMenu();
		}
	}

	@Override
	public void printMarketMoveMenu() throws RemoteException {
		setMove(MARKETMOVE);
		printFamilyMemberMenu();
		if (!RESTART.equals(move))
			setMove(printServantsAddictionMenu());
		if (!RESTART.equals(move))
			printMarketSelectionMenu();
	}

	@Override
	public void printMarketSelectionMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Choose the Market space:");
		showMsg("1: " + game.getBoardgame().getMarket()[0].getReward().getInfo().replaceAll("%n", " "));
		showMsg("2: " + game.getBoardgame().getMarket()[1].getReward().getInfo().replaceAll("%n", " "));
		if (game.getPlayers().length == 4) {
			showMsg("3: " + game.getBoardgame().getMarket()[2].getReward().getInfo().replaceAll("%n", " "));
			showMsg("4: " + game.getBoardgame().getMarket()[3].getCouncilPrivilege()
					+ " different council Privilege(s)");
		}
		showMsg(RESTART_CHOICE);

		int option = input();
		switch (option) {
		case 1:
		case 2:
			setMove(String.valueOf(option - 1));
			break;
		case 3:
		case 4:
			if (game.getPlayers().length == 4) {
				setMove(String.valueOf(option - 1));
			} else {
				printInvalidInput();
				printMarketSelectionMenu();
			}
			break;
		case 0:
			move = RESTART;
			memberMove = false;
			break;
		default:
			printInvalidInput();
			printMarketSelectionMenu();
			break;
		}
	}

	@Override
	public void printWorkMoveMenu() throws RemoteException {
		setMove(WORKMOVE);
		printFamilyMemberMenu();
		if (!RESTART.equals(move))
			setMove(printServantsAddictionMenu());
		if (!RESTART.equals(move))
			printWorkSelectionMenu();
	}

	@Override
	public void printWorkSelectionMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Choose the Work type:");
		showMsg("1: Production");
		showMsg("2: Harvest");
		showMsg(RESTART_CHOICE);

		int option = input();
		switch (option) {
		case 1:
			setMove(PRODUCTION);
			break;
		case 2:
			setMove(HARVEST);
			break;
		case 0:
			move = RESTART;
			memberMove = false;
			break;
		default:
			printInvalidInput();
			printWorkSelectionMenu();
			break;
		}
	}

	@Override
	public void printCouncilMoveMenu() throws RemoteException {
		setMove(COUNCILMOVE);
		printFamilyMemberMenu();
		if (!RESTART.equals(move))
			setMove(printServantsAddictionMenu());
	}

	@Override
	public void printSellLeaderCardMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		setMove("LeaderSell");
		showMsg("Choose the Leader card to sell:");
		int i;
		for (i = 0; i < getPlayer(name, game).getHandLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getHandLeaderCards().get(i).getName());
		}
		showMsg(RESTART_CHOICE);

		int option = input();
		if (option <= i && option > 0)
			setMove(getPlayer(name, game).getHandLeaderCards().get(option - 1).getName());
		else if (option == 0) {
			move = RESTART;
		} else {
			printInvalidInput();
			printSellLeaderCardMenu();
		}
	}

	@Override
	public void printActivateLeaderCardMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		ArrayList<LeaderCard> ld = new ArrayList<>();
		if (!getPlayer(name, game).getLeaderCards().isEmpty())
			ld.addAll(getPlayer(name, game).getLeaderCards());
		if (!getPlayer(name, game).getHandLeaderCards().isEmpty())
			ld.addAll(getPlayer(name, game).getHandLeaderCards());
		setMove("LeaderAct");
		showMsg("Choose the Leader card to activate:");
		int i;
		for (i = 0; i < ld.size(); i++) {
			showMsg((i + 1) + ": " + ld.get(i).getName());
		}
		showMsg(RESTART_CHOICE);

		int option = input();
		if (option <= i && option > 0)
			setMove(ld.get(option - 1).getName());
		else if (option == 0) {
			move = RESTART;
		} else {
			printInvalidInput();
			printActivateLeaderCardMenu();
		}
	}

	/**
	 * stampo messaggio d errore di default
	 */
	@Override
	public void printInvalidInput() {
		showMsg("Invalid input");
	}

	/**
	 * permette di selezionare la connessione desiderata
	 */
	@Override
	public int showConnectionSelection() {
		showMsg("Connection type: ");
		showMsg("1: RMI");
		showMsg("2: Socket");
		Integer option = -1;
		while (option == -1)
			try {
				option = Integer.parseInt(in.nextLine());
			} catch (NumberFormatException e) {
				pleaseInsertNumber();
				option = -1;
			}
		return option;
	}

	/**
	 * richiede il nome del giocatore e lo restituisce al chiamante
	 */
	@Override
	public String getName() {
		showMsg("Username(Player): ");
		name = in.nextLine();
		if ("".equals(name))
			name = "Player";
		return name;
	}

	/**
	 * richiede la password del giocatore e lo restituisce al chiamante
	 */
	@Override
	public String getPassword() {
		String password;
		showMsg("Password: ");
		password = in.nextLine();
		// ottengo l'hash della password
		String hashtext = new String();
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(password.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			hashtext = bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			StartClient.logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return hashtext;
	}

	/**
	 * richiede l'IP del server a cui connettersi e lo restituisce al chiamante
	 */
	@Override
	public String getIP() {
		String ip;
		showMsg("Server IP Address(" + DEFAULT_IP + "): ");
		ip = in.nextLine();
		if ("".equals(ip))
			ip = DEFAULT_IP;
		return ip;
	}

	/**
	 * metodo che semplicemente permette di visualizzare messaggi all'utente
	 */
	@Override
	public void showMsg(String s) {
		System.out.println(s);
	}

	private void msgFormat(String form, String msg) {
		System.out.printf(form, msg);
	}

	private void msgFormat(String msg) {
		System.out.printf(msg);
	}

	private void print(String msg) {
		System.out.print(msg);
	}

	@Override
	public void alert(String msg) {
		System.err.println(msg);
	}

	@Override
	public void connectionOK() {
		showMsg("Connection established!");
		showMsg("Wait for the login of other players...");
	}

	/**
	 * ricevuto il model lo analizza e visualizza il tabellone e le informazioni
	 * necessarie all'utente per poter giocare
	 * 
	 */
	private void showPersonalBoard() throws RemoteException {
		showMsg("_____________________________________________________________");
		msgFormat("%-30s|", "| Period: " + game.getPeriod());
		msgFormat("%-29s|%n", " Name: " + getPlayer(name, game).getNickname());
		msgFormat("%-30s|", "| Round: " + game.getRound());
		msgFormat("%-29s|%n", " Color: " + getPlayer(name, game).getColor());
		msgFormat("|_____________________________|_____________________________|%n");

		// visualizzo i familiari su due righe se non ancora utilizzati
		msgFormat("%-60s|%n| ", "| Family members:");
		for (int i = 0; i < 4; i++)
			if (!getPlayer(name, game).getMembers().get(i).isUsed())
				msgFormat("%-13s| ", MEMBER_COLOR[i] + ": " + getPlayer(name, game).getMembers().get(i).getValue());
			else
				msgFormat("%-13s| ", "");
		msgFormat("%n|___________________________________________________________|%n");
		msgFormat("%-12s", "| Coins");
		msgFormat("%-12s", "| Wood");
		msgFormat("%-12s", "| Stone");
		msgFormat("%-12s|%n", "| Servants");
		msgFormat("%-12s", "| " + getPlayer(name, game).getPersonalBoard().getResources().getCoins());
		msgFormat("%-12s", "| " + getPlayer(name, game).getPersonalBoard().getResources().getWood());
		msgFormat("%-12s", "| " + getPlayer(name, game).getPersonalBoard().getResources().getStone());
		msgFormat("%-12s|%n", "| " + getPlayer(name, game).getPersonalBoard().getResources().getServants());
		print("|___________|___________|___________|___________|");

	}

	/**
	 * stampo le carte leader e le carte sviluppo del player
	 */
	private void showPersonalCards() throws RemoteException {
		// Leader cards
		if (!getPlayer(name, game).getHandLeaderCards().isEmpty()
				|| !getPlayer(name, game).getActivatedLeaderCards().isEmpty()
				|| !getPlayer(name, game).getLeaderCards().isEmpty()) {
			showMsg("_____________________________________________");
			msgFormat("|%-30s|", " Hand leader cards:");
			msgFormat("%-30s|", " Table leader cards:");
			msgFormat("%-30s|%n|", " Activated leader cards:");
			// calcolo il massimo numero di righe della tabella
			int max = getPlayer(name, game).getHandLeaderCards().size();
			if (getPlayer(name, game).getLeaderCards().size() > max)
				max = getPlayer(name, game).getLeaderCards().size();
			if (getPlayer(name, game).getActivatedLeaderCards().size() > max)
				max = getPlayer(name, game).getActivatedLeaderCards().size();
			for (int i = 0; i < max; i++) {
				if (i < getPlayer(name, game).getHandLeaderCards().size())
					msgFormat("%-30s|", " " + getPlayer(name, game).getHandLeaderCards().get(i).getName());
				else
					msgFormat("%-30s|", "");
				if (i < getPlayer(name, game).getLeaderCards().size())
					msgFormat("%-30s|", " " + getPlayer(name, game).getLeaderCards().get(i).getName());
				else
					msgFormat("%-30s|", "");
				if (i < getPlayer(name, game).getActivatedLeaderCards().size())
					msgFormat("%-30s|", " " + getPlayer(name, game).getActivatedLeaderCards().get(i).getName());
				else
					msgFormat("%-30s|", "");
				msgFormat("%n|%s", "");
			}
			showMsg("______________________________|______________________________|______________________________|");
		} else
			showMsg("");
		// player dev cards
		if (!getPlayer(name, game).getPersonalBoard().getTerritoriesCards().isEmpty()) {
			msgFormat("%-30s|______________________", "| Territory cards:");
			for (TerritoryCard c : getPlayer(name, game).getPersonalBoard().getTerritoriesCards()) {
				msgFormat("%n%-30s| ", "| " + c.getName());
				msgFormat("%-20s| ", "Requirement: " + c.getRequirement());
				msgFormat("%-60s", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
			}
			showMsg("");
			showMsg("|_____________________________|_____________________|");
		}
		if (!getPlayer(name, game).getPersonalBoard().getCharactersCards().isEmpty()) {
			msgFormat("%-30s|", "| Character cards:");
			for (CharacterCard c : getPlayer(name, game).getPersonalBoard().getCharactersCards()) {
				msgFormat("%n%-30s| ", "| " + c.getName());
				msgFormat("%-82s", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
			}
			showMsg("");
			showMsg("|_____________________________|");
		}
		if (!getPlayer(name, game).getPersonalBoard().getBuildingsCards().isEmpty()) {
			msgFormat("%-30s|______________________", "| Building cards:");
			for (BuildingCard c : getPlayer(name, game).getPersonalBoard().getBuildingsCards()) {
				msgFormat("%n%-30s| ", "| " + c.getName());
				msgFormat("%-20s| ", "Requirement: " + c.getRequirement());
				msgFormat("%-60s", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
			}
			showMsg("");
			showMsg("|_____________________________|_____________________|");
		}
		if (!getPlayer(name, game).getPersonalBoard().getVenturesCards().isEmpty()) {
			msgFormat("%-30s|", "| Ventures cards:");
			for (VentureCard c : getPlayer(name, game).getPersonalBoard().getVenturesCards()) {
				msgFormat("%n%-30s| ", "| " + c.getName());
				msgFormat("%-82s", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
			}
			showMsg("");
			showMsg("|_____________________________|");
		}
	}

	/**
	 * visualizzo le classifiche in colonna ordinate
	 */
	private void showBoardTracks() throws RemoteException {
		ArrayList<String> faith = new ArrayList<>();
		ArrayList<String> military = new ArrayList<>();
		ArrayList<String> victory = new ArrayList<>();
		// trovo il massimo faith
		int maxF = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getFaith() > maxF)
				maxF = p.getPersonalBoard().getResources().getFaith();
		// genero classifica faith
		for (int j = maxF; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getFaith() == j)
					faith.add(j + " " + p.getNickname());
		// trovo il massimo military
		int maxM = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getMilitary() > maxM)
				maxM = p.getPersonalBoard().getResources().getMilitary();
		// genero classifica military
		for (int j = maxM; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getMilitary() == j)
					military.add(j + " " + p.getNickname());
		// trovo il massimo victory
		int maxV = 0;
		for (Player p : game.getPlayers())
			if (p.getPersonalBoard().getResources().getVictory() > maxV)
				maxV = p.getPersonalBoard().getResources().getVictory();
		// genero classifica victory
		for (int j = maxV; j >= 0; j--)
			for (Player p : game.getPlayers())
				if (p.getPersonalBoard().getResources().getVictory() == j)
					victory.add(j + " " + p.getNickname());
		showMsg("________________________________________________________________________________________________");
		msgFormat("%-24s", "| Faith track:");
		msgFormat("%-24s", "| Military track:");
		msgFormat("%-24s", "| Victory track:");
		msgFormat("%-24s|%n", "| Players order:");
		for (int i = 0; i < game.getPlayersOrder().size(); i++) {
			msgFormat("| %-22s| ", faith.get(i));
			msgFormat("%-22s| ", military.get(i));
			msgFormat("%-22s| ", victory.get(i));
			msgFormat("%-22s|%n", (i + 1) + " " + game.getPlayersOrder().get(i).getNickname());
		}
		showMsg("|_______________________|_______________________|_______________________|_______________________|");
	}

	/**
	 * visualizzo le torri su 4 colonne. Se uno spazio azione è occupato mostro le
	 * informazioni del player che ha preso la carta
	 */
	private void showTowers() {
		showMsg("______________________________");
		msgFormat("%-30s|%n", "| Towers: ");
		showMsg("|_____________________________|");
		showMsg("        _________________________________________________________________________________________________");
		for (int j = 3; j >= 0; j--) {
			print("Dice " + game.getBoardgame().getTowers()[0].getFloor()[j].getSpace().getSpaceRequirement() + ": | ");
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName() != null)
					msgFormat("%-22s| ", t.getFloor()[j].getCard().getName());
				else
					msgFormat("%-22s| ", "#Name: " + t.getFloor()[j].getSpace().getMember().getPlayer().getNickname());
			showMsg("");
			print("Reward: | ");
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName() != null)
					msgFormat("%-22s| ", t.getFloor()[j].getSpace().getReward().getInfo().replaceAll("%n", " "));
				else {
					if (t.getFloor()[j].getSpace().getMember().getColor().equals(MEMBER_COLOR[3]))
						msgFormat("%-22s| ", "#Color: " + MEMBER_COLOR[3]);
					else
						msgFormat("%-22s| ",
								"#Color: " + t.getFloor()[j].getSpace().getMember().getPlayer().getColor());
				}
			showMsg("");
			showMsg("        |_______________________|_______________________|_______________________|_______________________|");

		}
	}

	/**
	 * visualizzo le 3 scomuniche con i player che le hanno ricevute
	 */
	private void showExcommunication() {
		showMsg("______________________________");
		msgFormat("%-30s|%n", "| Excommunication tile:");
		showMsg("|_____________________________|");
		for (ExCommunication ex : game.getBoardgame().getFaithGrid().getExCommunicationTiles()) {
			print(" - " + ex.getEffect().getInfo());
			for (Player p : game.getPlayersOrder())
				for (Effect e : p.getEffects())
					if (e.getClass().equals(ex.getEffect().getClass()))
						print(" ( " + p.getNickname() + " )");
			showMsg("");
		}
	}

	/**
	 * visualizzo i membri del palazzo del consiglio
	 */
	private void showCounsilPalace() {
		if (!game.getBoardgame().getCouncilPalace().getMembers().isEmpty()) {
			showMsg("______________________________");
			msgFormat("%-30s|%n", "| Council Palace members: ");
			int i = 1;
			for (FamilyMember fm : game.getBoardgame().getCouncilPalace().getMembers()) {
				msgFormat("%-30s|%n", "| " + i + ": " + fm.getPlayer().getNickname());
				i++;
			}
			showMsg("|_____________________________|");
		}
	}

	/**
	 * metodo di riferimento per mostrare la board
	 */
	@Override
	public void showBoard(Game game) throws RemoteException {
		this.game = game;
		if (timeout == 0)
			timeout = game.getMoveTimer();
		showTowers();
		showExcommunication();
		showCounsilPalace();
		showMarketSpaces();
		showBoardTracks();
		showWorkSpaces();
		showPersonalBoard();
		showPersonalCards();
		showMsg("");
	}

	/**
	 * metodo che visualizza gli spazi harvest e production in colonna
	 */
	private void showWorkSpaces() throws RemoteException {
		showMsg("");
		showMsg("Production bonus: " + getPlayer(name, game).getPersonalBoard().getBonusBoard().getProductionEffect()
				.getInfo().replaceAll("%n", " "));
		showMsg("Harvest bonus: " + getPlayer(name, game).getPersonalBoard().getBonusBoard().getHarvestEffect()
				.getInfo().replaceAll("%n", " "));
		if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty()
				|| !game.getBoardgame().getProductionSpace().getMembers().isEmpty()) {
			showMsg("_________________________________________________________________________________");
			msgFormat("%-40s|", "| Production players:");
			msgFormat("%-40s|%n", " Harvest players:");
			for (int i = 0; i < game.getBoardgame().getProductionSpace().getMembers().size()
					|| i < game.getBoardgame().getHarvestSpace().getMembers().size(); i++) {
				List<FamilyMember> production = game.getBoardgame().getProductionSpace().getMembers();
				List<FamilyMember> harvest = game.getBoardgame().getHarvestSpace().getMembers();
				if (game.getBoardgame().getProductionSpace().getMembers().size() > i) {
					String prodColor;
					if (!UNCOLORED.equals(production.get(i).getColor()))
						prodColor = production.get(i).getPlayer().getColor();
					else
						prodColor = UNCOLORED;
					msgFormat("%-40s|", "| " + production.get(i).getPlayer().getNickname() + " - " + prodColor);
				} else
					msgFormat("%-40s|", "|");
				if (game.getBoardgame().getHarvestSpace().getMembers().size() > i) {
					String harvColor;
					if (!UNCOLORED.equals(harvest.get(i).getColor()))
						harvColor = harvest.get(i).getPlayer().getColor();
					else
						harvColor = UNCOLORED;
					msgFormat("%-40s|%n", " " + harvest.get(i).getPlayer().getNickname() + " - " + harvColor);
				} else
					msgFormat("%-40s|%n", " ");
			}
			showMsg("|_______________________________________|________________________________________|");
		}

	}

	/**
	 * metodo che mostra il mercato in blocchi in parallelo
	 */
	private void showMarketSpaces() {
		showMsg("______________________________");
		msgFormat("%-30s|%n", "| Market spaces: ");
		print("|_____________________________|____________");
		if (game.getPlayers().length == 4)
			showMsg("__________________________________________");
		else
			showMsg("");
		print("| ");
		int nMark = 0;
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				if (ms.getMember() == null)
					msgFormat("%-19s| ", "Reward:");
				else
					msgFormat("%-19s| ", "Occupied by:");
				nMark++;
			}
		showMsg("");
		nMark = 0;
		print("| ");
		// visualizzo la prima risorsa
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				String[] res = ms.getReward().getInfo().split("%n");
				if (ms.getMember() == null)
					msgFormat("%-19s| ", "-" + res[0]);
				else
					msgFormat("%-19s| ", ms.getMember().getPlayer().getNickname());
				nMark++;
			}

		showMsg("");
		nMark = 0;
		print("| ");
		// visualizzo l'eventuale seconda risorsa
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				String[] res = ms.getReward().getInfo().split("%n");
				if (ms.getMember() == null && res.length == 2)
					msgFormat("%-19s| ", "-" + res[1]);
				else
					msgFormat("%-19s| ", " ");
				nMark++;
			}

		showMsg("");
		nMark = 0;
		print("| ");
		// visualizzo il privilegio del consiglio solo se non è 0
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				if (ms.getMember() == null && !ms.getCouncilPrivilege().equals(0))
					msgFormat("%-19s| ", "-Privilege: " + ms.getCouncilPrivilege());
				else
					msgFormat("%-19s| ", " ");
				nMark++;
			}

		showMsg("");
		nMark = 0;
		print("| ");
		// visualizzo il dice requirement solo se non è occupato
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				if (ms.getMember() == null)
					msgFormat("%-19s| ", "Requirement: " + ms.getSpaceRequirement());
				else
					msgFormat("%-19s| ", " ");
				nMark++;
			}

		showMsg("");

		print("|____________________|____________________|");
		if (game.getPlayers().length == 4)
			showMsg("____________________|____________________|");
		else
			showMsg("");
	}

	/**
	 * metodo che permette alla utente di selezionare il bonus che vuole in cambio
	 * del privilegio del consiglio (PdC)
	 */
	@Override
	public String councilRequest(Integer number) throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		ArrayList<String> list = new ArrayList<>();
		String result = new String();
		String[] council = { "wood&stone", "servants", "coins", "military", "faith" };
		for (int k = 0; k < number;) {
			showMsg("Choose the Council Privilege reward:");
			// permetto di selezionare solo PdC con bonus differenti
			if (!list.contains("wood&stone"))
				showMsg("1: one stone & one wood");
			if (!list.contains("servants"))
				showMsg("2: two servants");
			if (!list.contains("coins"))
				showMsg("3: two coins");
			if (!list.contains("military"))
				showMsg("4: two military points");
			if (!list.contains("faith"))
				showMsg("5: one faith point");

			int option = input();
			if (!list.contains(council[option - 1])) {
				list.add(council[option - 1]);
				k++;
			} else
				printInvalidInput();
		}
		for (String s : list)
			result = result + "@" + s;

		return result;
	}

	@Override
	public boolean printSupportMenu() throws RemoteException {
		showMsg("Do you want support the Vatican?: ");
		showMsg("1: Yes");
		showMsg("2: No");

		int option = input();
		switch (option) {
		case 1:
			return true;
		case 2:
			return false;
		default:
			printInvalidInput();
			return printSupportMenu();
		}
	}

	@Override
	public String printColorMenu() throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Choose the family member that you want improve: ");
		for (int i = 0; i < 3; i++) {
			showMsg((i + 1) + ": " + MEMBER_COLOR[i]);
		}

		int option = input();
		switch (option) {
		case 1:
		case 2:
		case 3:
			return MEMBER_COLOR[option - 1];
		default:
			printInvalidInput();
			return printColorMenu();
		}
	}

	@Override
	public Integer printVentureCostMenu(VentureCard vc) throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Which cost do you want pay?: ");
		showMsg("1: Resource cost");
		msgFormat(vc.getCardCost1().getInfo());
		showMsg("2: Military points cost");
		showMsg("Requirement: " + vc.getCardCost2()[0].getMilitary() + " military points");
		showMsg("Cost: " + vc.getCardCost2()[1].getMilitary() + " military points");

		int option = input();
		switch (option) {
		case 1:
		case 2:
			return option - 1;
		default:
			printInvalidInput();
			return printVentureCostMenu(vc);
		}
	}

	@Override
	public boolean printChangeMenu(Resource[] exchange) throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Do you want change");
		msgFormat(exchange[0].getInfo());
		showMsg("with");
		msgFormat(exchange[1].getInfo());
		showMsg("1: Yes");
		showMsg("2: No");

		int option = input();
		switch (option) {
		case 1:
			return true;
		case 2:
			return false;
		default:
			printInvalidInput();
			return printChangeMenu(exchange);
		}
	}
	
	@Override
	public boolean printChangeMenu(Resource exchange, Integer privileges) throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Do you want change");
		msgFormat(exchange.getInfo());
		showMsg("with");
		msgFormat(privileges + " council Privilege(s)");
		showMsg("1: Yes");
		showMsg("2: No");

		int option = input();
		switch (option) {
		case 1:
			return true;
		case 2:
			return false;
		default:
			printInvalidInput();
			return printChangeMenu(exchange, privileges);
		}
	}

	@Override
	public Integer printDoubleChangeMenu(DoubleChangeEffect effect) throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Choose the effect that you want activate: ");
		showMsg("1: Change");
		msgFormat(effect.getExchangeEffect1()[0].getInfo());
		showMsg("With");
		msgFormat(effect.getExchangeEffect1()[1].getInfo());
		showMsg("OR");
		showMsg("2: Change");
		msgFormat(effect.getExchangeEffect2()[0].getInfo());
		showMsg("With");
		msgFormat(effect.getExchangeEffect2()[1].getInfo());
		showMsg("0: Don't activate effect");
		int option = input();
		switch (option) {
		case 0:
		case 1:
		case 2:
			return option;
		default:
			printInvalidInput();
			return printDoubleChangeMenu(effect);
		}
	}

	@Override
	public String askToPlayerForEffectToCopy(List<LeaderCard> lcards) throws RemoteException {
		showMsg(TIMEOUT + timeout + SECONDS);
		showMsg("Choose the card from which you want to copy the effect:");
		int i;
		for (i = 0; i < lcards.size(); i++) {
			showMsg((i + 1) + ": " + lcards.get(i).getName());
		}

		int option = input();
		if (option <= i && option > 0)
			return lcards.get(option - 1).getName();
		else {
			printInvalidInput();
			return askToPlayerForEffectToCopy(lcards);
		}
	}

	/**
	 * visualizzo all utente la scelta delle personal tile in colonna
	 */
	@Override
	public Integer selectPersonalTile(Game game) throws RemoteException {
		this.game = game;
		timeout = game.getMoveTimer();
		int i;
		PersonalBonusTile[] pbt = game.getPersonalBonusTile();
		showMsg(TIMEOUT + timeout + SECONDS);
		for (i = 0; i < pbt.length; i++)
			if (pbt[i] != null)
				print("______________________");
		msgFormat("%n| ");
		// intestazione generale
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				msgFormat("%-20s| ", "Number " + (i + 1) + ":");
		}
		msgFormat("%n|");
		for (i = 0; i < pbt.length; i++)
			if (pbt[i] != null)
				print("_____________________|");
		msgFormat("%n| ");
		// intestazione Harvest
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				msgFormat("%-20s| ", "Harvest effect:");
		}
		msgFormat("%n| ");
		// Harvest effect wood
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getHarvestEffect().getResource().getWood().equals(0))
					msgFormat("%-20s| ", "Woods: " + pbt[i].getHarvestEffect().getResource().getWood());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Harvest effect stone
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getHarvestEffect().getResource().getStone().equals(0))
					msgFormat("%-20s| ", "Stones: " + pbt[i].getHarvestEffect().getResource().getStone());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Harvest effect coins
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getHarvestEffect().getResource().getCoins().equals(0))
					msgFormat("%-20s| ", "Coins: " + pbt[i].getHarvestEffect().getResource().getCoins());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Harvest effect servants
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getHarvestEffect().getResource().getServants().equals(0))
					msgFormat("%-20s| ", "Servants: " + pbt[i].getHarvestEffect().getResource().getServants());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Harvest effect military
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getHarvestEffect().getResource().getMilitary().equals(0))
					msgFormat("%-20s| ", "Military: " + pbt[i].getHarvestEffect().getResource().getMilitary());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n|");
		for (i = 0; i < pbt.length; i++)
			if (pbt[i] != null)
				print("_____________________|");
		msgFormat("%n| ");
		// intestazione Production
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				msgFormat("%-20s| ", "Production effect:");
		}
		msgFormat("%n| ");
		// Production effect wood
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getProductionEffect().getResource().getWood().equals(0))
					msgFormat("%-20s| ", "Woods: " + pbt[i].getProductionEffect().getResource().getWood());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Production effect stone
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getProductionEffect().getResource().getStone().equals(0))
					msgFormat("%-20s| ", "Stones: " + pbt[i].getProductionEffect().getResource().getStone());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Production effect coins
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getProductionEffect().getResource().getCoins().equals(0))
					msgFormat("%-20s| ", "Coins: " + pbt[i].getProductionEffect().getResource().getCoins());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Production effect servants
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getProductionEffect().getResource().getServants().equals(0))
					msgFormat("%-20s| ", "Servants: " + pbt[i].getProductionEffect().getResource().getServants());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n| ");
		// Production effect military
		for (i = 0; i < pbt.length; i++) {
			if (pbt[i] != null)
				if (!pbt[i].getProductionEffect().getResource().getMilitary().equals(0))
					msgFormat("%-20s| ", "Military: " + pbt[i].getProductionEffect().getResource().getMilitary());
				else
					msgFormat("%-20s| ", "");
		}
		msgFormat("%n|");
		for (i = 0; i < pbt.length; i++)
			if (pbt[i] != null)
				print("_____________________|");
		msgFormat("%nChoose a personal bonus tile:%n");
		int option = input();
		if (option <= i && option > 0 && game.getPersonalBonusTile()[option - 1] != null) {
			showMsg("Wait other player...");
			showMsg("");
			return option - 1;
		} else {
			printInvalidInput();
			return selectPersonalTile(game);
		}
	}

	/**
	 * permette all utente di inserire in input solo numeri gestendo le eccezioni di
	 * convesione da stringa e il timeout della mossa
	 */
	private Integer input() {
		Integer result = -1;
		time = System.currentTimeMillis() / 1000;
		/**
		 * Verifico che la mossa venga eseguita nel tempo prestabilito. Se non è
		 * trascorsa l'intera durata del tempo concesso, la mossa è valida
		 */
		ExecutorService exe = Executors.newFixedThreadPool(1);
		Future<Integer> future = exe.submit(() -> {
			Integer option = -1;
			while (option == -1)
				try {
					option = Integer.parseInt(in.nextLine());
				} catch (NumberFormatException e) {
					pleaseInsertNumber();
					option = -1;
				}
			return option;
		});
		try {
			// eseguo l'interfaccia di comunicazione in un thread con timeout
			// allo scadere del timeout termino il thread e disconnetto il
			// client
			result = future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			StartClient.logger.log(Level.SEVERE, e.getMessage(), e);
			move = "End@Disconnect@";
			alert("Move time expired, now you have been disconnected!");
			alert("Please login again...");
			System.exit(0);
		}
		timeout = timeout - (System.currentTimeMillis() / 1000 - time);
		return result;
	}

	/**
	 * permetto all utente di selezionare le carte leader desiderate tramite
	 * l'appropriata procedura di scambio carta con gli altri giocatori
	 */
	@Override
	public void selectLeaderCard(Game game) throws RemoteException {
		leaderSelected = "";
		this.game = game;
		timeout = game.getMoveTimer();
		showMsg(TIMEOUT + timeout + SECONDS);
		if (!getPlayer(name, game).getHandLeaderCards().isEmpty()) {
			showMsg("_______________________________");
			msgFormat("%-31s|%n", "| Selected leader cards:");
			for (LeaderCard ld : getPlayer(name, game).getHandLeaderCards())
				msgFormat("%-31s|%n", "| " + ld.getName());
			showMsg("|______________________________|");
		}
		int i = 0;
		showMsg("_______________________________");
		msgFormat("%-31s|%n", "| LeaderCard to be selected");
		for (LeaderCard ld : getPlayer(name, game).getLeaderCards()) {
			msgFormat("%-31s|%n", "| " + (i + 1) + ": " + ld.getName());
			i++;
		}
		msgFormat("%-31s|%n", "| 0: Show card info");
		showMsg("|______________________________|");
		showMsg("Choose a Leader card:");
		int option = input();
		if (option <= i && option > 0) {
			showMsg("Wait other player...");
			showMsg("");
			leaderSelected = getPlayer(name, game).getLeaderCards().get(option - 1).getName();
		} else if (option == 0) {
			showMsg("Insert the card name: (no case sensitive)");
			String cardName = in.nextLine();
			showLeader(cardName);
			selectLeaderCard(game);
		} else {
			printInvalidInput();
			selectLeaderCard(game);
		}
		timeout = game.getMoveTimer();
	}

	@Override
	public String getLeaderCard() throws RemoteException {
		String cardName;
		cardName = new String(leaderSelected);
		leaderSelected = "";
		return cardName;
	}
}
