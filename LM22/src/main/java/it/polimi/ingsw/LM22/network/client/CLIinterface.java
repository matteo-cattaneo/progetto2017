package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;
import java.util.Scanner;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

/*
 * Classe che contiene tutti i metodi che permettono all'utente 
di interfacciarsi con la CLI
*/
public class CLIinterface extends AbstractUI {

	private final String ORANGE = "Orange";
	private final String BLACK = "Black";
	private final String WHITE = "White";
	private final String UNCOLORED = "Uncolored";

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

	private final String LEVEL_1 = "1";
	private final String LEVEL_2 = "2";
	private final String LEVEL_3 = "3";
	private final String LEVEL_4 = "4";

	private Scanner in = new Scanner(System.in);
	private String move = new String();
	private Game game;
	private String name;
	private Boolean memberMove;

	/*
	 * costruisco la stringa mossa per poi poterla inviare al server
	 */
	public void setMove(String add) {
		move = move + add + "$";
	}

	/*
	 * restituisce al chiamante la mossa creata in precedenza tramite il menu
	 */
	@Override
	public String getMove() {
		return move;
	}

	/*
	 * menu principale da cui parte la costruzione guidata della mossa
	 */
	@Override
	public void printMoveMenu() throws RemoteException {
		showMsg("Choose your Move:");
		showMsg("1: Move a Member");
		showMsg("2: Sell a LeaderCard");
		showMsg("3: Activate a LeaderCard");
		showMsg("4: End turn");
		int option = in.nextInt();
		switch (option) {
		case 1:
			printMemberMoveMenu();
			memberMove = true;
			break;
		case 2:
			printSellLeaderCardMenu();
			break;
		case 3:
			printActivateLeaderCardMenu();
			break;
		case 4:
			setMove("End");
			memberMove = false;
			break;
		default:
			printInvalidInput();
			printMoveMenu();
			break;
		}
	}

	@Override
	public void printMemberMoveMenu() throws RemoteException {
		showMsg("Choose your move:");
		showMsg("1: Card");
		showMsg("2: Market");
		showMsg("3: Work");
		showMsg("4: Council");
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
			setMove(COUNCILMOVE);
		default:
			printInvalidInput();
			printMoveMenu();
			break;
		}
	}

	@Override
	public void printCardMoveMenu() throws RemoteException {
		setMove(CARDMOVE);
		printFamilyMemberMenu();
		printServantsAddictionMenu();
		printTowersMenu();
		printLevelsMenu();
	}

	@Override
	public void printFamilyMemberMenu() {
		showMsg("Choose your Family Member you want to move:");
		showMsg("1: " + ORANGE);
		showMsg("2: " + BLACK);
		showMsg("3: " + WHITE);
		showMsg("4: " + UNCOLORED);
		int option = in.nextInt();
		switch (option) {
		case 1:
			setMove(ORANGE);
			break;
		case 2:
			setMove(BLACK);
			break;
		case 3:
			setMove(WHITE);
			break;
		case 4:
			setMove(UNCOLORED);
			break;
		default:
			printInvalidInput();
			printFamilyMemberMenu();
			break;
		}
	}

	@Override
	public void printServantsAddictionMenu() throws RemoteException {
		showMsg("Insert how many servants you want to use");
		showMsg("Please insert a positive number or zero");
		Integer servants = in.nextInt();
		if (servants >= 0 && servants <= getPlayer(name, game).getPersonalBoard().getResources().getServants()) {
			setMove(servants.toString());
		} else {
			printInvalidInput();
			printServantsAddictionMenu();
		}
	}

	@Override
	public void printTowersMenu() {
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
			setMove(String.valueOf(option - 1));
			break;
		default:
			printInvalidInput();
			printTowersMenu();
			break;
		}
	}

	@Override
	public void printLevelsMenu() {
		showMsg("Insert the level of the tower (1 - 2 - 3 - 4):");
		Integer servants = in.nextInt();
		switch (servants) {
		case 1:
			setMove(LEVEL_1);
			break;
		case 2:
			setMove(LEVEL_2);
			break;
		case 3:
			setMove(LEVEL_3);
			break;
		case 4:
			setMove(LEVEL_4);
			break;
		default:
			printInvalidInput();
			printLevelsMenu();
		}
	}

	@Override
	public void printMarketMoveMenu() throws RemoteException {
		setMove(MARKETMOVE);
		printFamilyMemberMenu();
		printServantsAddictionMenu();
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
				break;
			}
		default:
			printInvalidInput();
			printMarketMoveMenu();
			break;
		}
	}

	@Override
	public void printWorkMoveMenu() throws RemoteException {
		setMove(WORKMOVE);
		printFamilyMemberMenu();
		printServantsAddictionMenu();
		printWorkSelectionMenu();
	}

	@Override
	public void printWorkSelectionMenu() {
		showMsg("Choose the Work type:");
		showMsg("1: Production");
		showMsg("2: Harvest");
		int option = in.nextInt();
		switch (option) {
		case 1:
			setMove(PRODUCTION);
			break;
		case 2:
			setMove(HARVEST);
			break;
		default:
			printInvalidInput();
			printWorkSelectionMenu();
			break;
		}
	}

	// TODO test con carte leader caricate
	@Override
	public void printSellLeaderCardMenu() throws RemoteException {
		setMove("LeaderSell");
		showMsg("Choose the Leader card to sell:");
		int i, j;
		for (i = 0; i < getPlayer(name, game).getLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getLeaderCards().get(i).getName());
		}
		for (j = 0; j < getPlayer(name, game).getActivatedLeaderCards().size(); j++) {
			showMsg((j + 1 + i) + ": " + getPlayer(name, game).getActivatedLeaderCards().get(j).getName());
		}
		int option = in.nextInt();
		if (option <= i)
			setMove(getPlayer(name, game).getLeaderCards().get(option - 1).getName());
		else if (option > i && option < i + j)
			setMove(getPlayer(name, game).getActivatedLeaderCards().get(option - 1).getName());
		else {
			printInvalidInput();
			printWorkSelectionMenu();
		}
	}

	// TODO test con carte leader caricate
	@Override
	public void printActivateLeaderCardMenu() throws RemoteException {
		setMove("LeaderAct");
		showMsg("Choose the Leader card to activate:");
		for (int i = 0; i < getPlayer(name, game).getLeaderCards().size(); i++) {
			showMsg((i + 1) + ": " + getPlayer(name, game).getLeaderCards().get(i).getName());
		}
		int option = in.nextInt();
		if (option <= getPlayer(name, game).getLeaderCards().size())
			setMove(getPlayer(name, game).getLeaderCards().get(option).getName());
		else {
			printInvalidInput();
			printWorkSelectionMenu();
		}
		// TODO controlli su carte speciali per richiedere servitori(Integer) e
		// colore(String)
	}

	public void printInvalidInput() {
		showMsg("Invalid input");
	}

	@Override
	public void showLoginMenu() {
		// TODO Password & name
	}

	/*
	 * permette di selezionare la connessione desiderata
	 */
	@Override
	public int showConnectionSelection() {
		int connType;
		showMsg("Connection type: ");
		showMsg("1: RMI");
		showMsg("2: Socket");
		connType = Integer.parseInt(in.nextLine());
		return connType;
	}

	/*
	 * richiede il nome del giocatore e lo restituisce al chiamante
	 */
	@Override
	public String getName() {
		String name;
		showMsg("Username(Matteo): ");
		name = in.nextLine();
		if (name.equals(""))
			name = "Matteo";
		this.name = name;
		return name;
	}

	/*
	 * richiede l'IP del server a cui connettersi e lo restituisce al chiamante
	 */
	@Override
	public String getIP() {
		String ip;
		showMsg("Indirizzo ip server(127.0.0.1): ");
		ip = in.nextLine();
		if (ip.equals(""))
			ip = "127.0.0.1";
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
	@Override
	public void showBoard(Game game) throws RemoteException {
		this.game = game;
		// game object deserialization
		showMsg("_________________________");
		showMsg("| Periodo:" + game.getPeriod() + " \t \t |");
		showMsg("| Round:" + game.getRound() + " \t \t |");
		showMsg("| \t \t \t |");
		showMsg("| Name:" + getPlayer(name, game).getNickname() + " \t \t |");
		showMsg("| Color:" + getPlayer(name, game).getColor() + " \t \t |");
		showMsg("| Coins:" + getPlayer(name, game).getPersonalBoard().getResources().getCoins() + " \t \t |");
		showMsg("| Servants:" + getPlayer(name, game).getPersonalBoard().getResources().getServants() + " \t \t |");
		showMsg("|________________________|");
	}
}
