package it.polimi.ingsw.LM22.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;

public class SocketClient implements IClient {
	private final int SOCKET_PORT = 1337;
	private Socket socket;
	private AbstractUI UI;
	private String name;

	public SocketClient(AbstractUI UI) {
		this.UI = UI;
	}

	public void connect(String name, String ip) {
		this.name = name;
		try {
			socket = new Socket(ip, SOCKET_PORT);
		} catch (IOException e) {
			UI.showMsg("Socket connection error!");
		}
		UI.connectionOK();
		try {
			ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
			while (true) {
				String socketLine = socketIn.readObject().toString();
				UI.showMsg(socketLine);
				if (socketLine.equals("start")) {
					UI.printMoveMenu();
					socketOut.writeChars(UI.getMove());
				} else
					UI.showBoard((Game) socketIn.readObject());
					
			}
		} catch (ClassNotFoundException | IOException e) {
			UI.showMsg("Connessione chiusa");
		}

	}

	@Override
	public void play() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(String move) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getMove() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showBoard(Game game) {
		// TODO Auto-generated method stub

	}

}
