package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.client.IClient;

/*
 * interfaccia implementata da RMi e SOCKET player che gestisce le connessionicon il client
 */

public interface IPlayer extends Remote {

	public String yourTurn() throws RemoteException, ClassNotFoundException, IOException;

	public void showBoard(Game game) throws RemoteException, IOException;

	public void login(IClient client) throws RemoteException;
}
