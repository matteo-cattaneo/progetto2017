package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.LM22.controller.FileParser;
import it.polimi.ingsw.LM22.controller.MainGameController;

/**
 * classe principale del server, attende la connessione dei giocatori e crea le
 * room di gioco
 */
public class StartServer {
	private static final Logger LOGGER = Logger.getLogger(StartServer.class.getClass().getSimpleName());
	private static final int SOCKET_PORT = 1337;
	private static final int RMI_PORT = 1099;
	private Integer TIMER;
	private static final int THIRDPLAYER = 2;
	private static final int FOURTHPLAYER = 3;
	private static ServerSocket serverSocket;
	private RMIPlayer serverRMI;
	private ArrayList<ArrayList<PlayerInfo>> serverInfo = new ArrayList<>();
	private ExecutorService executor = Executors.newCachedThreadPool();
	private SocketConnection conn;

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
			LOGGER.log(Level.SEVERE, "Server terminato!", e);
		}
	}

	/**
	 * eseguito solo all'avvio: esegue i server RMI e socket
	 */
	public void initilizeServers() throws IOException {
		// carico timeout da file
		TIMER = FileParser.getLoginTimeouts();
		// avvio in thread che gestisce le connesioni socket
		conn = new SocketConnection(serverSocket);
		executor.submit(conn);
		// condivido l'oggetto remoto del server
		serverRMI = new RMIPlayer();
		Naming.rebind("rmi://localhost/MSG", serverRMI);
	}

	/**
	 * metodo che attende la connessione del client su entrambe le connessioni e
	 * al raggiungimento dei requisiti crea una partita
	 */
	public void start() throws InterruptedException, IOException {
		int i = 0;
		Integer t = TIMER;
		// creo lista giocatori della nuova room
		ArrayList<PlayerInfo> playerRoom = new ArrayList<>();
		// salvo nel server la nuova lista
		serverInfo.add(playerRoom);
		System.out.println("Waiting for clients...");
		while (i < 4) {
			if (i == THIRDPLAYER || i == FOURTHPLAYER) {
				// avvio il timer di connessione
				t = attesaLogin(t, conn, playerRoom);
				if (t == 0)
					break;
			} else {
				/**
				 * verifico se si è connesso un client su una delle
				 * dueconnessioni
				 */
				while (serverRMI.getClient() == null && !conn.getSocket().isConnected()) {
					Thread.sleep(1);
				}
			}
			/**
			 * a secondo del tipo di client connesso effettuo la giusta
			 * inizializzazione del player e la reinizializzazione dell' oggetto
			 * di connessione
			 */
			PlayerInfo player = new PlayerInfo();
			if (conn.getSocket().isConnected()) {
				player.setIplayer(new SocketPlayer(conn.getSocket()));
				System.out.print("Socket-> ");
				conn = new SocketConnection(serverSocket);
				executor.submit(conn);
			} else if (serverRMI.getClient() != null) {
				player.setIplayer(serverRMI);
				System.out.print("RMI-> ");
				serverRMI = new RMIPlayer();
				Naming.rebind("rmi://localhost/MSG", serverRMI);
			}
			// ottengo il nome del player
			player.setName(player.getIplayer().getName());
			System.out.println("Connected client: " + player.getName());
			// agiungo il player alla lista della room adatta
			if (!playerExist(player)) {
				playerRoom.add(player);
				i++;
			}
		}
		/**
		 * avvio thread della partita (controller) passandogli la lista dei
		 * giocatori
		 */
		executor.submit(new MainGameController(playerRoom));
		cleanServer();
		start();
	}

	/**
	 * verifico se un player è già stato inserito in una partita in corso
	 */
	private boolean playerExist(PlayerInfo player) throws IOException {
		for (ArrayList<PlayerInfo> room : serverInfo)
			for (PlayerInfo pi : room)
				if (pi.getName().equals(player.getName())) {
					if (pi.getConnected()) {
						// se player rieffettua l'accesso con una sessione
						// valida attiva viene disconnesso
						player.getIplayer().showMsg("You are already connected to another session");
						player.getIplayer().showMsg("Please login again...");
						player.getIplayer().close();
					} else {
						// altrimenti viene reinserito nella room
						pi.setIplayer(player.getIplayer());
						pi.setConnected(true);
					}
					return true;
				}
		return false;
	}

	/**
	 * pulizia dalla lista delle room delle room vuote (partite giàterminate)
	 */
	private void cleanServer() {
		for (int i = 0; i < serverInfo.size();) {
			if (serverInfo.get(i).isEmpty()) {
				serverInfo.remove(i);
			} else
				i++;
		}
	}

	/**
	 * verifica la connessione di un client con timeout
	 */
	public Integer attesaLogin(Integer t, SocketConnection conn, ArrayList<PlayerInfo> room)
			throws InterruptedException, IOException {
		for (; t > 0; t--) {
			printAll("The game starts in " + t + " seconds", room);
			TimeUnit.SECONDS.sleep(1);
			if (serverRMI.getClient() != null || conn.getSocket().isConnected()) {
				t--;
				break;
			}
		}
		return t;
	}

	/**
	 * invia un messaggio a tutti i player di una room
	 */
	public void printAll(String s, ArrayList<PlayerInfo> room) throws IOException {
		for (PlayerInfo p : room)
			p.getIplayer().showMsg(s);
	}
}

/**
 * classe che gestisce le connessoni socket
 */
class SocketConnection implements Runnable {
	private Socket socket = new Socket();
	private ServerSocket serverSocket;
	private final Logger LOGGER = Logger.getLogger(SocketConnection.class.getClass().getSimpleName());

	public SocketConnection(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			// resto in attesa di un client connesso
			socket = serverSocket.accept();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Socket IOException", e);
		}
	}

	// restituisco il socket del client connesso
	public Socket getSocket() {
		return socket;
	}

}
