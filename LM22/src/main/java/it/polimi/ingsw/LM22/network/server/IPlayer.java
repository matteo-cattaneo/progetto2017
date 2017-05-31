package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.client.IClient;

public interface IPlayer extends Remote {

	public String yourTurn() throws RemoteException, IOException;

	public void showBoard(Game game) throws RemoteException, IOException;

	public void login(IClient client) throws RemoteException;
}
