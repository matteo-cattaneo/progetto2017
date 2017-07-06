package it.polimi.ingsw.LM22.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class SocketClient implements IConnection {
	private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getClass().getSimpleName());
	private static final int SOCKET_PORT = 1337;
	private Socket socket;
	private AbstractUI UI;
	String[] socketLine;
	ObjectOutputStream socketOut;
	ObjectInputStream socketIn;

	public SocketClient(AbstractUI UI) {
		this.UI = UI;
	}

	/**
	 * metodo che effectuala connessione con il server Socket
	 */
	@Override
	public void connect(String name, String password, String ip) {
		try {
			// stabilisco connessione con socoket server
			socket = new Socket(ip, SOCKET_PORT);
			UI.connectionOK();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Socket connection error!", e);
			StartClient.setup();
		}
		// inizializzo gli stream di input e output
		try {
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketIn = new ObjectInputStream(socket.getInputStream());
			// comunico il mio nome e la password al server
			socketOut.writeUTF(name);
			socketOut.flush();
			socketOut.writeUTF(password);
			socketOut.flush();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Socket connection error!!", e);
			StartClient.setup();
		}
		play();
	}

	/**
	 * metodo che gestisce la varie richieste inviate dal server
	 */
	@Override
	public void play() {
		try {
			// finche la connessione è attiva
			while (!socket.isClosed()) {
				// ricevo il comando dal server
				// effettuo uno switch sulla prima parte del comando ricevuto
				// ed eseguo la relativa procedura
				socketLine = socketIn.readUTF().split("@");

				switch (socketLine[0]) {
				case "msg":
					// visualizzo sulla UI un messaggio
					if (socketLine[1].contains("member"))
						UI.setMemberMove(false);
					UI.alert(socketLine[1]);
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
				case "askCopy":
					copyEffect();
					break;
				case "personalTile":
					socketOut.writeInt(UI.selectPersonalTile((Game) socketIn.readObject()));
					socketOut.flush();
					break;
				case "leader":
					UI.selectLeaderCard((Game) socketIn.readObject());
					break;
				case "getLeader":
					socketOut.writeUTF(UI.getLeaderCard());
					socketOut.flush();
					break;
				default:
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Socket connection error!!", e);
		}
		StartClient.setup();
	}

	private void copyEffect() throws IOException, ClassNotFoundException {
		List<LeaderCard> lcards = new ArrayList<>();
		// ottengo lunghezza lista
		int N = socketIn.readInt();

		// ottengo i singoli elementi della lista
		for (int i = 0; i < N; i++)
			lcards.add((LeaderCard) socketIn.readObject());

		socketOut.writeUTF(UI.askToPlayerForEffectToCopy(lcards));
		socketOut.flush();
	}
}