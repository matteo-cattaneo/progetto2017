package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;
import it.polimi.ingsw.LM22.model.excommunication.ExEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

/*
 * Classe che contiene tutti i metodi che permettono all'utente 
di interfacciarsi con la CLI
*/
public class CLIinterface extends AbstractUI {
	private final Logger LOGGER = Logger.getLogger(CLIinterface.class.getClass().getSimpleName());

	private final String DEFAULT_IP = "localhost";
	// Colori familiari
	private final String[] MEMBER_COLOR = { "Orange", "Black", "White", "Uncolored" };

	private final String CARDMOVE = "Card";
	private final String MARKETMOVE = "Market";
	private final String WORKMOVE = "Work";
	private final String COUNCILMOVE = "Council";

	private final String TERRITORY = "TERRITORY";
	private final String CHARACTER = "CHARACTER";
	private final String BUILDING = "BUILDING";
	private final String VENTURE = "VENTURE";

	private final String PRODUCTION = "PRODUCTION";
	private final String HARVEST = "HARVEST";

	private Scanner in = new Scanner(System.in);
	private String move = new String();
	private Game game;
	private String name;
	private boolean memberMove = false;
	private long timeout = 0;
	private long time;

	public void setMemberMove(boolean memberMove) {
		this.memberMove = memberMove;
	}

	/*
	 * costruisco la stringa mossa per poi poterla inviare al server
	 */
	private void setMove(String add) {
		move = move + add + "@";
	}

	/*
	 * restituisce al chiamante la mossa creata in precedenza tramite il menu
	 */
	@Override
	public String getMove() throws RemoteException {
		return move;
	}

	/*
	 * menu principale da cui parte la costruzione guidata della mossa
	 */
	@Override
	public void printMoveMenu() throws RemoteException {
		move = "";
		time = System.currentTimeMillis() / 1000;
		/*
		 * Verifico che la mossa venga eseguita nel tempo prestabilito. Se non è
		 * trascorsa l'intera durata del tempo concesso, la mossa è valida
		 */
		ExecutorService exe = Executors.newFixedThreadPool(1);
		Future<?> future = exe.submit(() -> {
			try {
				showPrincipalMenu();
			} catch (RemoteException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		});
		try {
			// eseguo l interfaccia di comunicazione in un thread con timeout
			// allo scadere del timeout termino il thread e disconnetto il
			// client
			future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// e.printStackTrace();
			move = "End@Disconnect@";
			System.err.println("Move time expired, now you have been disconnected!");
			// System.exit(0);
		}
		// calcolo il timeout da cui dovrò partire alla possima mossa
		if (!getMove().equals("End@"))
			timeout = (timeout - (System.currentTimeMillis() / 1000 - time));
	}

	private void showPrincipalMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		showMsg("Choose your Move:");
		if (!memberMove)
			showMsg("1: Move a Member");
		showMsg("2: Show card");
		if (!getPlayer(name, game).getHandLeaderCards().isEmpty())
			showMsg("3: Sell a LeaderCard");
		if (!getPlayer(name, game).getLeaderCards().isEmpty() || !getPlayer(name, game).getHandLeaderCards().isEmpty())
			showMsg("4: Activate a LeaderCard");
		showMsg("5: End turn");
		showMsg("0: Restart");
		int option = in.nextInt();
		switch (option) {
		case 1:
			if (!memberMove) {
				printMemberMoveMenu();
				break;
			} else {
				printInvalidInput();
				showPrincipalMenu();
			}
		case 2:
			showCard();
			showPrincipalMenu();
			break;
		case 3:
			if (!getPlayer(name, game).getHandLeaderCards().isEmpty())
				printSellLeaderCardMenu();
			else {
				printInvalidInput();
				showPrincipalMenu();
			}
			break;
		case 4:
			if (!getPlayer(name, game).getLeaderCards().isEmpty()
					|| !getPlayer(name, game).getHandLeaderCards().isEmpty())
				printActivateLeaderCardMenu();
			else {
				printInvalidInput();
				showPrincipalMenu();
			}
			break;
		case 5:
			setMove("End");
			memberMove = false;
			timeout = game.getMoveTimer();
			break;
		case 0:
			move = new String();
			showPrincipalMenu();
			break;
		default:
			printInvalidInput();
			showPrincipalMenu();
			break;
		}
	}

	private void pleaseInsertNumber() {
		showMsg("Please insert a number!");
	}

	private void showCard() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		showMsg("Insert the card name: (no case sensitive)");
		in.nextLine();
		String name = in.nextLine();
		DevelopmentCard card = null;
		for (int j = 0; j < 4; j++)
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName() != null)
					if (t.getFloor()[j].getCard().getName().toLowerCase().startsWith(name.toLowerCase()))
						card = t.getFloor()[j].getCard();
		showMsg("");
		if (card != null) {
			showMsg("Name: " + card.getName());
			showMsg("Period: " + card.getPeriod());
			if (card instanceof TerritoryCard) {
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf("Requirement: " + ((TerritoryCard) card).getRequirement().toString() + "%n"
						+ ((TerritoryCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Territory");
			}
			if (card instanceof CharacterCard) {
				showMsg("Card cost: ");
				System.out.printf(((CharacterCard) card).getCost().getInfo());
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf(((CharacterCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Character");
			}
			if (card instanceof BuildingCard) {
				showMsg("Card cost: ");
				System.out.printf(((BuildingCard) card).getCost().getInfo());
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf(((BuildingCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Building");
			}
			if (card instanceof VentureCard) {
				showMsg("Card cost: ");
				System.out.printf(((VentureCard) card).getCardCost1().getInfo());
				showMsg("Second card cost: ");
				showMsg("Require: ");
				System.out.printf(((VentureCard) card).getCardCost2()[0].getInfo());
				showMsg("Cost: ");
				System.out.printf(((VentureCard) card).getCardCost2()[1].getInfo());
				showMsg("Immediate effect: ");
				System.out.printf(card.getImmediateEffect().getInfo());
				showMsg("Permament effect:");
				System.out.printf(((VentureCard) card).getPermanentEffect().getInfo());
				showMsg("Card type: Venture");
			}
		} else
			showMsg("Card not found!");
		showMsg("");
	}

	@Override
	public void printMemberMoveMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		memberMove = true;
		showMsg("Choose your move:");
		showMsg("1: Card");
		showMsg("2: Market");
		showMsg("3: Work");
		showMsg("4: Council");
		showMsg("0: Restart");
		int option = in.nextInt();
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
			move = new String();
			pleaseInsertNumber();
			showPrincipalMenu();
			break;
		default:
			printInvalidInput();
			printMemberMoveMenu();
			break;
		}
	}

	@Override
	public void printCardMoveMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		setMove(CARDMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
		setMove(printTowersMenu());
		setMove(printLevelsMenu());
	}

	@Override
	public void printFamilyMemberMenu() throws RemoteException {
		showMsg("Choose your Family Member you want to move:");
		int i = 0;
		for (FamilyMember fm : getPlayer(name, game).getMembers()) {
			if (!fm.isUsed())
				showMsg((i + 1) + ": " + MEMBER_COLOR[i]);
			i++;
		}
		showMsg("0: Restart");
		int option = in.nextInt();
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
			move = new String();
			showPrincipalMenu();
			break;
		default:
			printInvalidInput();
			printFamilyMemberMenu();
		}
	}

	public String printServantsAddictionMenu() throws RemoteException {
		showMsg("Insert how many servants you want to use");
		showMsg("Please insert a positive number or zero");
		Integer servants = in.nextInt();
		if (servants >= 0 && servants <= getPlayer(name, game).getPersonalBoard().getResources().getServants()) {
			return servants.toString();
		} else {
			printInvalidInput();
			return printServantsAddictionMenu();
		}
	}

	@Override
	public String printTowersMenu() throws RemoteException {
		showMsg("Choose the Tower:");
		showMsg("1: " + TERRITORY);
		showMsg("2: " + CHARACTER);
		showMsg("3: " + BUILDING);
		showMsg("4: " + VENTURE);
		int option = in.nextInt();
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
		showMsg("Insert the level of the tower (1 - 2 - 3 - 4):");
		Integer level = in.nextInt();
		switch (level) {
		case 1:
		case 2:
		case 3:
		case 4:
			return String.valueOf(level - 1);
		default:
			printInvalidInput();
			return printLevelsMenu();
		}
	}

	@Override
	public void printMarketMoveMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		setMove(MARKETMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
		printMarketSelectionMenu();
	}

	@Override
	public void printMarketSelectionMenu() throws RemoteException {
		showMsg("Choose the Market space:");
		showMsg("1: Five coins");
		showMsg("2: Five servants");
		if (game.getPlayers().length == 4) {
			showMsg("3: Three military points + two coins");
			showMsg("4: Two different counsil privilege");
		}
		showMsg("0: Restart");
		int option = in.nextInt();
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
			move = new String();
			showPrincipalMenu();
		default:
			printInvalidInput();
			printMarketSelectionMenu();
			break;
		}
	}

	@Override
	public void printWorkMoveMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		setMove(WORKMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
		printWorkSelectionMenu();
	}

	@Override
	public void printWorkSelectionMenu() throws RemoteException {
		showMsg("Choose the Work type:");
		showMsg("1: Production");
		showMsg("2: Harvest");
		showMsg("0: Restart");
		int option = in.nextInt();
		switch (option) {
		case 1:
			setMove(PRODUCTION);
			break;
		case 2:
			setMove(HARVEST);
			break;
		case 0:
			move = new String();
			showPrincipalMenu();
			break;
		default:
			printInvalidInput();
			printWorkSelectionMenu();
			break;
		}
	}

	@Override
	public void printCouncilMoveMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		setMove(COUNCILMOVE);
		printFamilyMemberMenu();
		setMove(printServantsAddictionMenu());
	}

	@Override
	public void printSellLeaderCardMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		setMove("LeaderSell");
		showMsg("Choose the Leader card to sell:");
		int i;
		for (i = 0; i < getPlayer(name, game).getHandLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getHandLeaderCards().get(i).getName());
		}
		showMsg("0: Restart");
		int option = in.nextInt();
		if (option <= i && option > 0)
			setMove(getPlayer(name, game).getHandLeaderCards().get(option - 1).getName());
		else if (option == 0) {
			move = new String();
			showPrincipalMenu();
		} else {
			printInvalidInput();
			printSellLeaderCardMenu();
		}
	}

	@Override
	public void printActivateLeaderCardMenu() throws RemoteException {
		System.out.println("Timeout: " + (timeout - (System.currentTimeMillis() / 1000 - time)));
		ArrayList<LeaderCard> ld = new ArrayList<LeaderCard>();
		Collections.copy(ld, getPlayer(name, game).getLeaderCards());
		ld.addAll(getPlayer(name, game).getHandLeaderCards());
		setMove("LeaderAct");
		showMsg("Choose the Leader card to activate:");
		int i;
		for (i = 0; i < ld.size(); i++) {
			showMsg((i + 1) + ": " + ld.get(i).getName());
		}
		showMsg("0: Restart");
		int option = in.nextInt();
		if (option <= i && option > 0)
			setMove(ld.get(option).getName());
		else if (option == 0) {
			move = new String();
			showPrincipalMenu();
		} else {
			printInvalidInput();
			printActivateLeaderCardMenu();
		}
	}

	@Override
	public void printInvalidInput() {
		showMsg("Invalid input");
	}

	/*
	 * permette di selezionare la connessione desiderata
	 */
	@Override
	public int showConnectionSelection() {
		int connType = 0;
		while (connType != 1 && connType != 2) {
			showMsg("Connection type: ");
			showMsg("1: RMI");
			showMsg("2: Socket");
			connType = Integer.parseInt(in.nextLine());
		}
		return connType;
	}

	/*
	 * richiede il nome del giocatore e lo restituisce al chiamante
	 */
	@Override
	public String getName() {
		String name;
		showMsg("Username(Player): ");
		name = in.nextLine();
		if (name.equals(""))
			name = "Player";
		this.name = name;
		return name;
	}

	/*
	 * richiede l'IP del server a cui connettersi e lo restituisce al chiamante
	 */
	@Override
	public String getIP() {
		String ip;
		showMsg("Indirizzo ip server(" + DEFAULT_IP + "): ");
		ip = in.nextLine();
		if (ip.equals(""))
			ip = DEFAULT_IP;
		return ip;
	}

	/*
	 * metodo che semplicemente permette di visualizzare messaggi all'utente
	 */
	@Override
	public void showMsg(String s) {
		System.out.println(s);
	}

	@Override
	public void connectionOK() {
		showMsg("Connessione stabilita!");
		showMsg("Attendi il tuo turno...");
	}

	/*
	 * ricevuto il model lo analizza e visualizza il tabellone e le informazioni
	 * necessarie all'utente per poter giocare
	 * 
	 * WIP
	 */
	private void showPersonalBoard() throws RemoteException {
		System.out.printf("%-30s|%n", "| Period: " + game.getPeriod());
		System.out.printf("%-30s|%n", "| Round: " + game.getRound());
		System.out.printf("%-30s|%n", "| ");
		// visualizzo tutti i dadi sulla stessa riga
		System.out.printf("%-30s|%n| ", "| Dices:");
		for (int i = 0; i < 3; i++)
			System.out.printf("%-8s| ", MEMBER_COLOR[i]);
		System.out.printf("%n| ");
		for (int i = 0; i < 3; i++)
			System.out.printf("%-8s| ", game.getBoardgame().getDice(MEMBER_COLOR[i]));

		System.out.printf("%n|_____________________________|%n");
		System.out.printf("%-30s|%n", "| Name: " + getPlayer(name, game).getNickname());
		System.out.printf("%-30s|%n", "| Color: " + getPlayer(name, game).getColor());
		System.out.printf("%-30s|%n", "| ");
		// visualizzo i familiari su due righe
		System.out.printf("%-30s|%n| ", "| Family members:");
		for (int i = 0; i < 2; i++)
			System.out.printf("%-13s| ", MEMBER_COLOR[i] + ": " + getPlayer(name, game).getMembers().get(i).getValue());
		System.out.printf("%n| ");
		for (int i = 2; i < 4; i++)
			System.out.printf("%-13s| ", MEMBER_COLOR[i] + ": " + getPlayer(name, game).getMembers().get(i).getValue());
		System.out.printf("%n|_____________________________|__________________%n");
		System.out.printf("%-12s", "| Coins");
		System.out.printf("%-12s", "| Wood");
		System.out.printf("%-12s", "| Stone");
		System.out.printf("%-12s|%n", "| Servants");
		System.out.printf("%-12s", "| " + getPlayer(name, game).getPersonalBoard().getResources().getCoins());
		System.out.printf("%-12s", "| " + getPlayer(name, game).getPersonalBoard().getResources().getWood());
		System.out.printf("%-12s", "| " + getPlayer(name, game).getPersonalBoard().getResources().getStone());
		System.out.printf("%-12s|%n", "| " + getPlayer(name, game).getPersonalBoard().getResources().getServants());
		showMsg("|___________|___________|___________|___________|");

	}

	private void showPersonalCards() throws RemoteException {
		// Hand LeaderCard
		if (!getPlayer(name, game).getHandLeaderCards().isEmpty()) {
			System.out.printf("%-30s|%n", "| Hand leader cards:");
			for (LeaderCard ld : getPlayer(name, game).getHandLeaderCards())
				System.out.printf("%-30s|%n", "| " + ld.getName());
			showMsg("|_____________________________|");
		}
		// Table LeaderCard
		if (!getPlayer(name, game).getLeaderCards().isEmpty()
				|| !getPlayer(name, game).getActivatedLeaderCards().isEmpty()) {
			System.out.printf("%-30s|%n", "| Table leader cards:");
			for (LeaderCard ld : getPlayer(name, game).getLeaderCards())
				System.out.printf("%-30s|%n", "| " + ld.getName());
			for (LeaderCard ld : getPlayer(name, game).getActivatedLeaderCards())
				System.out.printf("%-30s|%n", "| " + ld.getName());
			showMsg("|_____________________________|");
		}
		// player dev cards
		if (!getPlayer(name, game).getPersonalBoard().getTerritoriesCards().isEmpty()) {
			System.out.printf("%-30s|" + "____________________________________________________________________________________", "| Territory cards:");
			for (TerritoryCard c : getPlayer(name, game).getPersonalBoard().getTerritoriesCards()) {
				System.out.printf("%n%-30s| ", "| " + c.getName());
				System.out.printf("%-60s| ", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
				System.out.printf("%-20s| ", "Requirement: " + c.getRequirement());
			}
			showMsg("");
			showMsg("|_____________________________|_____________________________________________________________|_____________________|");
		}
		if (!getPlayer(name, game).getPersonalBoard().getCharactersCards().isEmpty()) {
			System.out.printf("%-30s|" + "______________________________________________________________" , "| Character cards:");
			for (CharacterCard c : getPlayer(name, game).getPersonalBoard().getCharactersCards()) {
				System.out.printf("%n%-30s| ", "| " + c.getName());
				System.out.printf("%-60s| ", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
			}
			showMsg("");
			showMsg("|_____________________________|_____________________________________________________________|");
		}
		if (!getPlayer(name, game).getPersonalBoard().getBuildingsCards().isEmpty()) {
			System.out.printf("%-30s|" + "____________________________________________________________________________________", "| Building cards:");
			for (BuildingCard c : getPlayer(name, game).getPersonalBoard().getBuildingsCards()) {
				System.out.printf("%n%-30s| ", "| " + c.getName());
				System.out.printf("%-60s| ", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
				System.out.printf("%-20s| ", "Requirement: " + c.getRequirement());
			}
			showMsg("");
			showMsg("|_____________________________|_____________________________________________________________|_____________________|");
		}
		if (!getPlayer(name, game).getPersonalBoard().getVenturesCards().isEmpty()) {
			System.out.printf("%-30s|" + "______________________________________________________________", "| Ventures cards:");
			for (VentureCard c : getPlayer(name, game).getPersonalBoard().getVenturesCards()){
				System.out.printf("%n%-30s| ", "| " + c.getName());
				System.out.printf("%-60s| ", c.getPermanentEffect().getInfo().replaceAll("%n", " "));
			}
			showMsg("");
			showMsg("|_____________________________|_____________________________________________________________|");
		}
	}

	private void showBoardTracks() throws RemoteException {
		ArrayList<String> faith = new ArrayList<String>();
		ArrayList<String> military = new ArrayList<String>();
		ArrayList<String> victory = new ArrayList<String>();
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
		showMsg("________________________________________________________________________");
		System.out.printf("%-24s", "| Faith track:");
		System.out.printf("%-24s", "| Military track:");
		System.out.printf("%-24s|%n", "| Victory track:");
		for (int i = 0; i < game.getPlayersOrder().size(); i++) {
			System.out.printf("| %-22s| ", faith.get(i));
			System.out.printf("%-22s| ", military.get(i));
			System.out.printf("%-22s|%n", victory.get(i));
		}
		showMsg("|_______________________|_______________________|_______________________|");
	}

	@Override
	public void showBoard(Game game) throws RemoteException {
		this.game = game;
		if (timeout == 0)
			timeout = game.getMoveTimer();
		/*
		 * visualizzo le torri su 4 colonne. Se uno spazio azione è occupato
		 * mostro le informazioni del player che ha preso la carta
		 */
		showMsg("______________________________");
		System.out.printf("%-30s|%n", "| Towers: ");
		showMsg("|_____________________________|");
		showMsg("        _________________________________________________________________________________________________");
		for (int j = 3; j >= 0; j--) {
			System.out.print("Dice " + game.getBoardgame().getTowers()[0].getFloor()[j].getSpace().getSpaceRequirement()
					+ ": | ");
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName() != null)
					System.out.printf("%-22s| ", t.getFloor()[j].getCard().getName());
				else
					System.out.printf("%-22s| ",
							"#Name: " + t.getFloor()[j].getSpace().getMember().getPlayer().getNickname());
			showMsg("");
			System.out.print("Reward: | ");
			for (Tower t : game.getBoardgame().getTowers())
				if (t.getFloor()[j].getCard().getName() != null)
					System.out.printf("%-22s| ",
							t.getFloor()[j].getSpace().getReward().getInfo().replaceAll("%n", " "));
				else {
					if (t.getFloor()[j].getSpace().getMember().getColor().equals(MEMBER_COLOR[3]))
						System.out.printf("%-22s| ", "#Color: " + MEMBER_COLOR[3]);
					else
						System.out.printf("%-22s| ",
								"#Color: " + t.getFloor()[j].getSpace().getMember().getPlayer().getColor());
				}
			showMsg("");
			showMsg("        |_______________________|_______________________|_______________________|_______________________|");

		}
		// scomuniche
		showMsg("______________________________");
		System.out.printf("%-30s|%n", "| Excommunication tile:");
		showMsg("|_____________________________|");
		for (ExCommunication ex : game.getBoardgame().getFaithGrid().getExCommunicationTiles()) {
			System.out.print(" - " + ex.getEffect().getInfo());
			for (Player p : game.getPlayersOrder())
				for (Effect e : p.getEffects())
					if (e.getClass().equals(ex.getEffect().getClass()))
						System.out.print(" ( " + p.getNickname() + " )");
			showMsg("");
		}
		// palazzo del consiglio
		showMsg("______________________________");
		System.out.printf("%-30s|%n", "| Council Palace members: ");
		int i = 1;
		for (FamilyMember fm : game.getBoardgame().getCouncilPalace().getMembers()) {
			System.out.printf("%-30s|%n", "| " + i + ": " + fm.getPlayer().getNickname());
			i++;
		}
		showMsg("|_____________________________|");
		showMarketSpaces();
		showBoardTracks();
		showMsg("______________________________");
		showPersonalBoard();
		showPersonalCards();
		showMsg("");
		/*
		 * personal board altri giocatori?
		 */
	}

	private void showMarketSpaces() {
		/*
		 * mostro il mercato in blocchi in parallelo
		 */
		showMsg("______________________________");
		System.out.printf("%-30s|%n", "| Market spaces: ");
		System.out.print("|_____________________________|____________");
		if (game.getPlayers().length == 4)
			showMsg("__________________________________________");
		else
			showMsg("");
		System.out.print("| ");
		int nMark = 0;
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				if (ms.getMember() == null)
					System.out.printf("%-19s| ", "Reward:");
				else
					System.out.printf("%-19s| ", "Occupied by:");
				nMark++;
			}
		showMsg("");
		nMark = 0;
		System.out.print("| ");
		// visualizzo la prima risorsa
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				String res[] = ms.getReward().getInfo().split("%n");
				if (ms.getMember() == null)
					System.out.printf("%-19s| ", "-" + res[0]);
				else
					System.out.printf("%-19s| ", ms.getMember().getPlayer().getNickname());
				nMark++;
			}

		showMsg("");
		nMark = 0;
		System.out.print("| ");
		// visualizzo l'eventuale seconda risorsa
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				String res[] = ms.getReward().getInfo().split("%n");
				if (ms.getMember() == null && res.length == 2)
					System.out.printf("%-19s| ", "-" + res[1]);
				else
					System.out.printf("%-19s| ", " ");
				nMark++;
			}

		showMsg("");
		nMark = 0;
		System.out.print("| ");
		// visualizzo il privilegio del consiglio solo se non è 0
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				if (ms.getMember() == null && !ms.getCouncilPrivilege().equals(0))
					System.out.printf("%-19s| ", "-PdC: " + ms.getCouncilPrivilege());
				else
					System.out.printf("%-19s| ", " ");
				nMark++;
			}

		showMsg("");
		nMark = 0;
		System.out.print("| ");
		// visualizzo il dice requirement solo se non è occupato
		for (MarketSpace ms : game.getBoardgame().getMarket())
			if (game.getPlayers().length == 4 || nMark < 2) {
				if (ms.getMember() == null)
					System.out.printf("%-19s| ", "Requirement:" + ms.getSpaceRequirement());
				else
					System.out.printf("%-19s| ", " ");
				nMark++;
			}

		showMsg("");

		System.out.print("|____________________|____________________|");
		if (game.getPlayers().length == 4)
			showMsg("____________________|____________________|");
		else
			showMsg("");
	}

	/*
	 * metodo che permette alla utente di selezionare il bonus che vuole in
	 * cambio del privilegio del consiglio (PdC)
	 */
	@Override
	public String councilRequest(Integer number) throws RemoteException {
		ArrayList<String> list = new ArrayList<String>();
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
			int option = in.nextInt();

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
		Integer option = in.nextInt();
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
		showMsg("Choose the family member that you want improve: ");
		for (int i = 0; i < 3; i++) {
			showMsg((i + 1) + ": " + MEMBER_COLOR[i]);
		}
		Integer option = in.nextInt();
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
		showMsg("Which cost do you want pay?: ");
		showMsg("1: Resource cost");
		System.out.printf(vc.getCardCost1().getInfo());
		showMsg("2: Military points cost");
		System.out.println("Requirement: " + vc.getCardCost2()[0].getMilitary() + "military points");
		System.out.println("Cost: " + vc.getCardCost2()[1].getMilitary() + "military points");
		Integer option = in.nextInt();
		switch (option) {
		case 1:
		case 2:
			return (option - 1);
		default:
			printInvalidInput();
			return printVentureCostMenu(vc);
		}
	}

	@Override
	public boolean printChangeMenu(Resource[] exchange) throws RemoteException {
		showMsg("Do you want change");
		System.out.printf(exchange[0].getInfo());
		showMsg("with");
		System.out.printf(exchange[1].getInfo());
		showMsg("1: Yes");
		showMsg("2: No");
		Integer option = in.nextInt();
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
	public Integer printDoubleChangeMenu(DoubleChangeEffect effect) throws RemoteException {
		showMsg("Choose the effect that you want activate: ");
		showMsg("1: Change");
		System.out.printf(effect.getExchangeEffect1()[0].getInfo());
		showMsg("With");
		System.out.printf(effect.getExchangeEffect1()[1].getInfo());
		showMsg("OR");
		showMsg("2: Change");
		System.out.printf(effect.getExchangeEffect2()[0].getInfo());
		showMsg("With");
		System.out.printf(effect.getExchangeEffect2()[1].getInfo());
		Integer option = in.nextInt();
		switch (option) {
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
		showMsg("Choose the card from which you want to copy the effect:");
		int i;
		for (i = 0; i < lcards.size(); i++) {
			showMsg((i + 1) + ": " + lcards.get(i).getName());
		}
		int option = in.nextInt();
		if (option <= i)
			return lcards.get(option - 1).getName();
		else {
			printInvalidInput();
			return askToPlayerForEffectToCopy(lcards);
		}
	}

	@Override
	public Integer selectPersonalTile(Game game) throws RemoteException {
		this.game = game;
		showMsg("");
		showMsg("Choose a personal bonus tile:");
		int i;
		for (i = 0; i < game.getPersonalBonusTile().length; i++) {
			if (game.getPersonalBonusTile()[i] != null) {
				showMsg((i + 1) + ": ");
				showMsg("Harvest effect:");
				System.out.printf(
						game.getPersonalBonusTile()[i].getHarvestEffect().getInfo().replaceAll("You earn%n", ""));
				showMsg("Production effect:");
				System.out.printf(
						game.getPersonalBonusTile()[i].getProductionEffect().getInfo().replaceAll("You earn%n", ""));
			}
		}
		int option = in.nextInt();
		if (game.getPersonalBonusTile()[option - 1] != null)
			return option - 1;
		else {
			printInvalidInput();
			return selectPersonalTile(game);
		}
	}
}
