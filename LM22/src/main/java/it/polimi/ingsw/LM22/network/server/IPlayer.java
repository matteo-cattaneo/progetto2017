package it.polimi.ingsw.LM22.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.network.client.IClient;


public interface IPlayer extends Remote{

	public String yourTurn() throws RemoteException;

	public void showBoard(String msg) throws RemoteException;
	
	public void login(IClient client)throws RemoteException ;
}
