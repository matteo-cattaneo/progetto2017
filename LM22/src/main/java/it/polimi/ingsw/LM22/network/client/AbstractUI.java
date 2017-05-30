package it.polimi.ingsw.LM22.network.client;

public abstract class AbstractUI {

	public abstract void printMoveMenu();

	public abstract void printMemberMoveMenu();

	public abstract void printCardMoveMenu();

	public abstract void printFamilyMemberMenu();

	public abstract void printServantsAddictionMenu();

	public abstract void printTowersMenu();

	public abstract void printLevelsMenu();

	public abstract void printMarketMoveMenu();

	public abstract void printWorkMoveMenu();

	public abstract void printWorkSelectionMenu();

	public abstract void printSellLeaderCardMenu();

	public abstract void printActivateLeaderCardMenu();

	public abstract void showLoginMenu();

	public abstract int showConnectionSelection();

	public abstract String getName();

	public abstract String getIP();

	public abstract String getMove();

	public abstract void showMsg(String s);
}
