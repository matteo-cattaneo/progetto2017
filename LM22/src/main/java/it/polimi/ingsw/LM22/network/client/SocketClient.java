package it.polimi.ingsw.LM22.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;

public class SocketClient implements IClient {
	private final Logger LOGGER = Logger.getLogger(SocketClient.class.getClass().getSimpleName());
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
			LOGGER.log(Level.SEVERE, "Socket connection error!", e);
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
				switch (socketLine[0]) {
				case "msg":
					UI.showMsg(socketLine[1]);
					break;
				case "board":
					// ricevo e visualizzo la board
					UI.showBoard((Game) socketIn.readObject());
					break;
				case "start":
					// se è start inizio il mio turno
					UI.printMoveMenu();
					socketOut.writeUTF(UI.getMove());
					socketOut.flush();
					break;
				case "council":
					socketOut.writeUTF(UI.councilRequest(Integer.parseInt(socketLine[1])));
					socketOut.flush();
					break;
				case "servants":
					socketOut.writeUTF(UI.printServantsAddictionMenu());
					socketOut.flush();
					break;
				case "tower":
					socketOut.writeUTF(UI.printTowersMenu());
					socketOut.flush();
					break;
				case "floor":
					socketOut.writeUTF(UI.printLevelsMenu());
					socketOut.flush();
					break;
				case "support":
					socketOut.writeBoolean(UI.printSupportMenu());
					socketOut.flush();
					break;
				case "color":
					socketOut.writeUTF(UI.printColorMenu());
					socketOut.flush();
					break;
				case "ventureCost":
					socketOut.writeInt(UI.printVentureCostMenu((VentureCard) socketIn.readObject()));
					socketOut.flush();
					break;
				case "change":
					socketOut.writeBoolean(UI.printChangeMenu((Resource[]) socketIn.readObject()));
					socketOut.flush();
					break;
				case "doubleChange":
					socketOut.writeInt(UI.printDoubleChangeMenu((DoubleChangeEffect) socketIn.readObject()));
					socketOut.flush();
					break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			LOGGER.log(Level.SEVERE, "Connessione chiusa", e);
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

	@Override
	public boolean supportRequest() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String colorRequest() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer ventureCostRequest(VentureCard vc) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changeRequest(Resource[] exchange) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer doubleChangeRequest(DoubleChangeEffect effect) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
