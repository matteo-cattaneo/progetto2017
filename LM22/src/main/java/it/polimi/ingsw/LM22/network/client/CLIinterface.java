package it.polimi.ingsw.LM22.network.client;

import java.util.Scanner;

import it.polimi.ingsw.LM22.model.Game;

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

	private final String LEVEL_1 = "1";
	private final String LEVEL_2 = "2";
	private final String LEVEL_3 = "3";
	private final String LEVEL_4 = "4";

	private Scanner in = new Scanner(System.in);
	String move = new String();

	public void setMove(String add) {
		move = move + add + "$";
	}

	@Override
	public String getMove() {
		return move;
	}

	@Override
	public void printMoveMenu() {
		showMsg("Choose your Move:");
		showMsg("1: Move a Member");
		showMsg("2: Sell a LeaderCard");
		showMsg("3: Activate a LeaderCard");
		int option = in.nextInt();
		switch (option) {
		case 1:
			printMemberMoveMenu();
			break;
		case 2:
			printSellLeaderCardMenu();
			break;
		case 3:
			printActivateLeaderCardMenu();
			break;
		default:
			printInvalidInput();
			printMoveMenu();
			break;
		}
	}

	@Override
	public void printMemberMoveMenu() {
		showMsg("Choose your move:");
		showMsg("1: Card");
		showMsg("2: Market");
		showMsg("3: Work");
		showMsg("4: Council");
		int option = in.nextInt();
		switch (option) {
		case 1:
			setMove(CARDMOVE);
			printCardMoveMenu();
			break;
		case 2:
			setMove(MARKETMOVE);
			printMarketMoveMenu();
			break;
		case 3:
			setMove(WORKMOVE);
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
	public void printCardMoveMenu() {
		printFamilyMemberMenu();
		printServantsAddictionMenu();
		printTowersMenu();
		printLevelsMenu();
	}

	@Override
	public void printFamilyMemberMenu() {
		showMsg("Choose your Family Member you want to move:");
		// println("0: Restart your move");
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
	public void printServantsAddictionMenu() {
		// String useless = in.nextLine();
		showMsg("Insert how many servants you want to use");
		showMsg("Please insert a positive number or zero");
		Integer servants = in.nextInt();
		if (servants >= 0) {
			setMove(servants.toString());
		} else {
			showMsg("Negative Number :(");
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
	public void printMarketMoveMenu() {
		showMsg("Choose the Tower:");
		// showMsg("1: "+ FIRSTSPACE);
		// showMsg("2: "+ SECONDSPACE);
		// showMsg("3: "+ );
		// showMsg("4: "+ );
		int option = in.nextInt();
		switch (option) {
		case 1:
			setMove(TERRITORY);
			printCardMoveMenu();
			break;
		case 2:
			setMove(CHARACTER);
			printMarketMoveMenu();
			break;
		case 3:
			setMove(BUILDING);
			printWorkMoveMenu();
			break;
		case 4:
			setMove(VENTURE);
			break;
		default:
			printInvalidInput();
			printMarketMoveMenu();
			break;
		}
	}

	@Override
	public void printWorkMoveMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printWorkSelectionMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printSellLeaderCardMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printActivateLeaderCardMenu() {
		// TODO Auto-generated method stub

	}

	public void printInvalidInput() {
		showMsg("Invalid input");
	}

	@Override
	public void showLoginMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public int showConnectionSelection() {
		int connType;
		showMsg("Connection type: ");
		showMsg("1: RMI");
		showMsg("2: Socket");
		connType = Integer.parseInt(in.nextLine());
		return connType;
	}

	@Override
	public String getName() {
		String name;
		showMsg("Username(Matteo): ");
		name = in.nextLine();
		if (name.equals(""))
			name = "Matteo";
		return name;
	}

	@Override
	public String getIP() {
		String ip;
		showMsg("Indirizzo ip server(127.0.0.1): ");
		ip = in.nextLine();
		if (ip.equals(""))
			ip = "127.0.0.1";
		return ip;
	}

	@Override
	public void showMsg(String s) {
		System.out.println(s);
	}

	@Override
	public void connectionOK() {
		showMsg("Connessione stabilita!");
		showMsg("Attendi il tuo turno...");
	}

	@Override
	public void showBoard(Game game) {
		// game object deserialization
		showMsg("_________________________");
		showMsg("| " + game + " \t \t |");
		showMsg("| \t \t \t |");
		showMsg("|________________________|");
	}
}
