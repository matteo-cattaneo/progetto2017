package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.network.client.IClient;

/*
 * interfaccia implementata da RMi e SOCKET player che gestisce le connessionicon il client
 */

public interface IPlayer extends Remote {

	public String yourTurn() throws RemoteException, ClassNotFoundException, IOException;

	public void showBoard(Game game) throws RemoteException, IOException;

	public void login(IClient client) throws RemoteException;

	public String getName() throws RemoteException;

	public String councilRequest(Integer number) throws IOException;

	public String servantsRequest() throws IOException;

	public String towerRequest() throws IOException;

	public String floorRequest() throws IOException;

	public boolean supportRequest() throws IOException;

	public String colorRequest() throws IOException;

	public Integer ventureCostRequest(VentureCard vc) throws IOException;

	public boolean changeRequest(Resource[] exchange) throws IOException;

	public Integer doubleChangeRequest(DoubleChangeEffect effect) throws IOException;

	public void showMsg(String msg) throws IOException;
}
