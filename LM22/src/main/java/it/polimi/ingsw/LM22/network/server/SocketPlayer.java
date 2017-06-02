package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.client.IClient;
/*
 * classe utilizzata per gestire la comunicazione socket con il client 
 */

public class SocketPlayer implements IPlayer {
	ObjectOutputStream out;
	ObjectInputStream in;
	String name;

	// inizializzo gli stream di comunicazione
	public SocketPlayer(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		// leggo il nome del client
		name = in.readUTF();
		// TODO verificare se ha gia iniziato un altra partita
	}

	// indica al client quando è il suo turno e restituisce la mossa da lui
	// effettuata
	@Override
	public String yourTurn() throws ClassNotFoundException, IOException {
		out.writeUTF("start");
		out.flush();

		return in.readUTF();
	}

	// inivia al client il model per poter visualizzare la board
	@Override
	public void showBoard(Game game) throws IOException {

		out.writeUTF("board");
		out.flush();

		out.writeObject(game);
		out.flush();
	}

	public String getName() {
		return name;
	}

	@Override
	public void login(IClient client) throws RemoteException {
		// TODO Auto-generated method stub

	}
}
