package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.network.client.IClient;

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
		// Gson gson = new Gson();
		/*
		 * Utilizzo Gson per creare un nuovo oggetto identico a quello di
		 * partenza per poi poter inviare le modifiche al client socket. Senza
		 * questo passaggio, il client mantiene l oggetto in locale inalterato
		 */
		// Game game2 = gson.fromJson(gson.toJson(game), game.getClass());

		out.writeUTF("board");
		out.flush();

		out.reset();

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

	@Override
	public String councilRequest(Integer number) throws IOException {
		out.writeUTF("council@" + number);
		out.flush();
		return in.readUTF();
	}

	@Override
	public String servantsRequest() throws IOException {
		out.writeUTF("servants");
		out.flush();
		return in.readUTF();
	}

	@Override
	public String towerRequest() throws IOException {
		out.writeUTF("tower");
		out.flush();
		return in.readUTF();
	}

	@Override
	public String floorRequest() throws IOException {
		out.writeUTF("floor");
		out.flush();
		return in.readUTF();
	}

	@Override
	public void showMsg(String msg) throws IOException {
		out.writeUTF("msg@" + msg);
		out.flush();
	}

}
