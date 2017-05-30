package it.polimi.ingsw.LM22.network.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {

	public void connect(String name, String ip)throws RemoteException;

	public void play()throws RemoteException;

	public abstract void print(String move)throws RemoteException;

	public abstract String getMove()throws RemoteException;
}
