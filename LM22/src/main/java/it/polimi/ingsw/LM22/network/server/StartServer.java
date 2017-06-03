package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.LM22.controller.MainGameController;
/*
 * classe che gestisce le connessioni in ingresso
 * allo scadere del time out o all raggiungimento di 4 giocatori
 * genera un thread per l inizio della partita 
 */

public class StartServer {
	private final static int SOCKET_PORT = 1337;
	static ServerSocket serverSocket;
	static RMIPlayer serverRMI;
	final int TIMER = 10;
	final int THIRDPLAYER = 2;
	final int FOURTHPLAYER = 3;
	IPlayer player[];
	static ExecutorService executor = Executors.newCachedThreadPool();
	static SocketConnection conn;

	public static void main(String[] args) {
		try {
			// avvio RMI registry
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			// avvio il server socket
			serverSocket = new ServerSocket(SOCKET_PORT);
			StartServer startServer = new StartServer();
			// avvio in thread che gestisce le connesioni socket
			conn = new SocketConnection(serverSocket);
			executor.submit(conn);
			// condivido l'oggetto remoto del server
			serverRMI = new RMIPlayer();
			Naming.rebind("rmi://localhost/MSG", serverRMI);
			while (true) {
				// apro una sessione di creazione partita
				startServer.start();
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Server terminato!");
			e.printStackTrace();
		}
	}

	// metodo che attende la connessione del client su entrambe le connessioni e
	// al raggiungimento dei requisiti crea una partita

	public void start() throws InterruptedException, IOException {
		int i = 0;
		Integer[] t = { TIMER };
		int[] ordine = { 4, 4, 4, 4, 4 };
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
				System.out.println("Socket: Connesso client " + i);
				player[i] = new SocketPlayer(conn.getSocket());
				conn = new SocketConnection(serverSocket);
				executor.submit(conn);
			} else if (serverRMI.getClient() != null) {
				System.out.println("RMI: Connesso client " + i);
				player[i] = serverRMI;
				serverRMI = new RMIPlayer();
				Naming.rebind("rmi://localhost/MSG", serverRMI);
			}

			ordine[i] = i;
			i++;
		}
		System.out.println("Inizio partita");
		// avvio thread della partita (controller)
		executor.submit(new MainGameController(player, ordine, i));
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
			e.printStackTrace();
		}
	}

	// restituisco il socket del client connesso
	public Socket getSocket() {
		return socket;
	}

}
