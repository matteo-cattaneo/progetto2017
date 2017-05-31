package it.polimi.ingsw.LM22.network.client;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.server.IPlayer;

public class RMIClient extends UnicastRemoteObject implements IClient {

	private static final long serialVersionUID = 1L;
	private String move;
	private String name;
	private AbstractUI UI;

	public RMIClient(AbstractUI UI) throws RemoteException {
		this.UI = UI;
	}

	public String getMove() throws RemoteException {
		return move;
	}

	public void connect(String name, String ip) throws RemoteException {
		try {
			this.name = name;
			IPlayer server = (IPlayer) Naming.lookup("rmi://" + ip + "/MSG");
			server.login(this);
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			UI.showMsg("RMI connection error!");
		}
		UI.connectionOK();
	}

	public void play() throws RemoteException {
		UI.printMoveMenu();
		move = UI.getMove();
	}

	public void print(String move) throws RemoteException {
		UI.showMsg(move);
	}

	@Override
	public void showBoard(Game game) throws RemoteException {
		UI.showBoard(game);
	}
}