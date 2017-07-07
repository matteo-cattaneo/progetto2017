package it.polimi.ingsw.LM22.network.client;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import it.polimi.ingsw.LM22.network.server.IPlayer;

public class RMIClient extends UnicastRemoteObject implements IClient, IConnection {
	private static final long serialVersionUID = 5918010069011921777L;
	private String move;
	private String name;
	private String password;
	private transient AbstractUI ui;

	public RMIClient(AbstractUI ui) throws RemoteException {
		this.ui = ui;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getMove() throws RemoteException {
		return move;
	}

	/**
	 * metodo che permettee la connessione con il server RMI
	 */
	@Override
	public void connect(String name, String password, String ip) throws RemoteException {
		try {
			this.name = name;
			this.password = password;
			// ottendo l'oggetto remoto del server
			IPlayer server = (IPlayer) Naming.lookup("rmi://" + ip + "/MSG");
			// mando il mio oggetto al server
			server.login(this);
			ui.connectionOK();
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			StartClient.logger.log(Level.SEVERE, "RMI connection close!", e);
			ui.showMsg("RMI connection close!");
			StartClient.setup();
		}
	}

	/**
	 * metodo invocato da remoto quando è il proprio turno
	 */
	@Override
	public void play() throws RemoteException {
		ui.printMoveMenu();
		move = ui.getMove();
	}

	/**
	 * metodo invocato da remoto per visualizzre la board
	 */

	@Override
	public void showBoard(Game game) throws RemoteException {
		ui.showBoard(game);
	}

	@Override
	public String councilRequest(Integer number) throws RemoteException {
		return ui.councilRequest(number);
	}

	@Override
	public String servantsRequest() throws RemoteException {
		return ui.printServantsAddictionMenu();
	}

	@Override
	public String towerRequest() throws RemoteException {
		return ui.printTowersMenu();
	}

	@Override
	public String floorRequest() throws RemoteException {
		return ui.printLevelsMenu();
	}

	@Override
	public void showMsg(String msg) throws RemoteException {
		if (msg.contains("member"))
			ui.setMemberMove(false);
		ui.alert(msg);
	}

	@Override
	public boolean supportRequest() throws RemoteException {
		return ui.printSupportMenu();
	}

	@Override
	public String colorRequest() throws RemoteException {
		return ui.printColorMenu();
	}

	@Override
	public Integer ventureCostRequest(VentureCard vc) throws RemoteException {
		return ui.printVentureCostMenu(vc);
	}

	@Override
	public boolean changeRequest(Resource[] exchange) throws RemoteException {
		return ui.printChangeMenu(exchange);
	}

	@Override
	public Integer doubleChangeRequest(DoubleChangeEffect effect) throws RemoteException {
		return ui.printDoubleChangeMenu(effect);
	}

	@Override
	public String askToPlayerForEffectToCopy(List<LeaderCard> lcards) throws RemoteException {
		return ui.askToPlayerForEffectToCopy(lcards);
	}

	@Override
	public Integer selectPersonalTile(Game game) throws RemoteException {
		return ui.selectPersonalTile(game);
	}

	@Override
	public void selectLeaderCard(Game game) throws RemoteException {
		ui.selectLeaderCard(game);
	}

	@Override
	public String getLeaderCard() throws RemoteException {
		return ui.getLeaderCard();
	}

	@Override
	public void close() throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				StartClient.setup();
			}
		}.start();
	}
}