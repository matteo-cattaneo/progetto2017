package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.controller.MainGameController;

public class StartServer {
	private static final Logger LOGGER = Logger.getLogger(StartServer.class.getClass().getSimpleName());
	private final static int SOCKET_PORT = 1337;
	private final static int RMI_PORT = 1099;
	static ServerSocket serverSocket;
	RMIPlayer serverRMI;
	final int TIMER = 5;
	final int THIRDPLAYER = 2;
	final int FOURTHPLAYER = 3;
	private IPlayer player[];
	ExecutorService executor = Executors.newCachedThreadPool();
	SocketConnection conn;

	public static void main(String[] args) {
		try {
			// avvio RMI registry
			java.rmi.registry.LocateRegistry.createRegistry(RMI_PORT);
			// avvio il server socket
			serverSocket = new ServerSocket(SOCKET_PORT);
			StartServer startServer = new StartServer();
			startServer.initilizeServers();
			startServer.start();
		} catch (IOException | InterruptedException e) {
			System.err.println("Server terminato!");
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void initilizeServers() throws RemoteException, MalformedURLException {
		// avvio in thread che gestisce le connesioni socket
		conn = new SocketConnection(serverSocket);
		executor.submit(conn);
		// condivido l'oggetto remoto del server
		serverRMI = new RMIPlayer();
		Naming.rebind("rmi://localhost/MSG", serverRMI);
	}

	// metodo che attende la connessione del client su entrambe le connessioni e
	// al raggiungimento dei requisiti crea una partita

	public void start() throws InterruptedException, IOException {
		int i = 0;
		Integer[] t = { TIMER };
		player = new IPlayer[4];
		System.out.println("Attesa client...");
		while (i < 4) {
			if (i == THIRDPLAYER || i == FOURTHPLAYER) {
				// avvio il timer di connessione
				attesaLogin(t, conn);
				if (t[0] == 0)
					break;
			} else {
				// verifico se si è connesso un client su una delle due
				// connessioni
				while (serverRMI.getClient() == null && !conn.getSocket().isConnected()) {
					System.out.print("");
				}
			}
			/*
			 * a secondo del tipo di client connesso effettuo la giusta
			 * inizializzazione del player e la reinizializzazione dell' oggetto
			 * di connessione
			 */

			if (conn.getSocket().isConnected()) {
				System.out.println("Socket: Connected client " + i);
				player[i] = new SocketPlayer(conn.getSocket());
				conn = new SocketConnection(serverSocket);
				executor.submit(conn);
			} else if (serverRMI.getClient() != null) {
				System.out.println("RMI: Connected client " + i);
				player[i] = serverRMI;
				serverRMI = new RMIPlayer();
				Naming.rebind("rmi://localhost/MSG", serverRMI);
			}
			i++;
		}
		System.out.println("Game started!!!");
		// avvio thread della partita (controller)
		executor.submit(new MainGameController(player, i));
		start();
	}

	// verifica la connessione di un client con timeout
	public void attesaLogin(Integer[] t, SocketConnection conn) throws RemoteException, InterruptedException {
		for (; t[0] > 0; t[0]--) {
			System.out.println("Mancano " + t[0] + " secondi");
			TimeUnit.SECONDS.sleep(1);
			if (serverRMI.getClient() != null || conn.getSocket().isConnected()) {
				t[0]--;
				break;
			}
		}
	}
}

// classe che gestisce le connessoni socket
class SocketConnection implements Runnable {
	Socket socket = new Socket();
	ServerSocket serverSocket;

	public SocketConnection(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			// resto in attesa di un client connesso
			socket = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Socket IOException");
		}
	}

	// restituisco il socket del client connesso
	public Socket getSocket() {
		return socket;
	}

}
