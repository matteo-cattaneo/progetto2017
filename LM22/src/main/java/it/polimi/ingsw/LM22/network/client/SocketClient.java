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

	public String getName() {
		return name;
	}

	public SocketClient(AbstractUI UI) {
		this.UI = UI;
	}
	/*
	 * metodo che effectuala connessione con il server Socket e gestisce la
	 * varie richieste inviate dal server
	 */

	public void connect(String name, String ip) {
		this.name = name;
		try {
			// stabilisco connessione con socoket server
			socket = new Socket(ip, SOCKET_PORT);
		} catch (IOException e) {
			UI.showMsg("Socket connection error!");
		}
		UI.connectionOK();
		try {
			// inizializzo gli stream di input e output
			ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
			socketOut.writeUTF(getName());
			socketOut.flush();
			while (!socket.isClosed()) {
				// ricevo il comando dal server
				String[] socketLine = socketIn.readUTF().split("@");
				if (socketLine[0].equals("start")) {
					// se è start inizio il mio turno
					UI.printMoveMenu();
					socketOut.writeUTF(UI.getMove());
					socketOut.flush();
				} else if (socketLine[0].equals("council")) {
					socketOut.writeUTF(UI.councilRequest(Integer.parseInt(socketLine[1])));
					socketOut.flush();
				} else if (socketLine[0].equals("servants")) {
					socketOut.writeUTF(UI.printServantsAddictionMenu());
					socketOut.flush();
				} else if (socketLine[0].equals("tower")) {
					socketOut.writeUTF(UI.printTowersMenu());
					socketOut.flush();
				} else if (socketLine[0].equals("floor")) {
					socketOut.writeUTF(UI.printLevelsMenu());
					socketOut.flush();
				} else if (socketLine[0].equals("msg")) {
					UI.showMsg(socketLine[1]);
				} else if (socketLine[0].equals("board")) {
					// ricevo e visualizzo la board
					UI.showBoard((Game) socketIn.readObject());
				}
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
	public String getMove() throws RemoteException {
		return null;
	}

	@Override
	public void showBoard(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public String councilRequest(Integer number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String servantsRequest() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String towerRequest() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String floorRequest() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showMsg(String msg) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
