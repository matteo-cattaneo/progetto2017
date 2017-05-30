package it.polimi.ingsw.LM22.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StartServer {
	private final static int SOCKET_PORT = 1337;
	static ServerSocket serverSocket;
	static RMIPlayer serverRMI;
	final int TIMER = 10;
	final int THIRDPLAYER = 2;
	final int FOURTHPLAYER = 3;
	IPlayer player[];
	static ExecutorService executor= Executors.newCachedThreadPool();
	static SocketConnection conn;
	
	public static void main(String[] args) {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			serverSocket = new ServerSocket(SOCKET_PORT);
			StartServer startServer = new StartServer();
			conn = new SocketConnection(serverSocket);
			executor.submit(conn);
			serverRMI = new RMIPlayer();
			Naming.rebind("rmi://localhost/MSG", serverRMI);
			while (true) {
				startServer.start();
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Server terminato!");
		}
	}

	public void start() throws InterruptedException, IOException {
		int i = 0;
		Integer t = TIMER;
		int[] ordine = { 4, 4, 4, 4, 4 };
		player = new IPlayer[4];
		
		System.out.println("Attesa client...");
		while (i < 4) {
			if (i == THIRDPLAYER || i == FOURTHPLAYER) {
				t = attesaLogin(t, conn);
				if (t == 0)
					break;
			} else {
				while (serverRMI.getClient() == null && !conn.getSocket().isConnected()) {
					System.out.print("");
				}
			}

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
		executor.submit(new Room(player, ordine, i));
	}

	public Integer attesaLogin(Integer t, SocketConnection conn) throws RemoteException, InterruptedException {
		for (; t > 0; t--) {
			System.out.println("Mancano " + t + " secondi");
			TimeUnit.SECONDS.sleep(1);
			if (serverRMI.getClient() != null || conn.getSocket().isConnected()) {
				t--;
				break;
			}
		}
		return t;
	}

}

class SocketConnection implements Runnable {
	Socket socket = new Socket();
	ServerSocket serverSocket;

	public SocketConnection(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			socket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

}
