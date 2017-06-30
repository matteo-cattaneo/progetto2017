package it.polimi.ingsw.LM22.network.client;

import java.rmi.RemoteException;
import java.util.List;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
/**
 * Classe che contiene tutti i metodi che permettono all'utente di interfacciarsi con la GUI 
*/
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

//WIP
public class GUIinterface extends AbstractUI {

	@Override
	public void printMoveMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printMemberMoveMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printCardMoveMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printFamilyMemberMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public String printServantsAddictionMenu() throws RemoteException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String printTowersMenu() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String printLevelsMenu() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void printMarketMoveMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printWorkMoveMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printWorkSelectionMenu() {
		// Auto-generated method stub

	}

	@Override
	public void printSellLeaderCardMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printActivateLeaderCardMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public int showConnectionSelection() {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String getIP() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void connectionOK() {
		// Auto-generated method stub

	}

	@Override
	public String getMove() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void showMsg(String s) {
		// Auto-generated method stub

	}

	@Override
	public void showBoard(Game game) throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printMarketSelectionMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public void printInvalidInput() {
		// Auto-generated method stub

	}

	@Override
	public String councilRequest(Integer number) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void printCouncilMoveMenu() throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public boolean printSupportMenu() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public String printColorMenu() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Integer printVentureCostMenu(VentureCard vc) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean printChangeMenu(Resource[] exchange) {
		// Auto-generated method stub
		return false;
	}

	@Override
	public Integer printDoubleChangeMenu(DoubleChangeEffect effect) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String askToPlayerForEffectToCopy(List<LeaderCard> lcards) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectPersonalTile(Game game) throws RemoteException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setMemberMove(boolean memberMove) {
		// Auto-generated method stub

	}

	@Override
	public void selectLeaderCard(Game game) throws RemoteException {
		// Auto-generated method stub

	}

	@Override
	public String getLeaderCard() throws RemoteException {
		// Auto-generated method stub
		return null;
	}

}
