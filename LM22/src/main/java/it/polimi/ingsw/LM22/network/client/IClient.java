package it.polimi.ingsw.LM22.network.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;

public interface IClient extends Remote {

	public void connect(String name, String ip) throws RemoteException;

	public void play() throws RemoteException;

	public void print(String move) throws RemoteException;

	public String getMove() throws RemoteException;

	public void showBoard(Game game);
}
