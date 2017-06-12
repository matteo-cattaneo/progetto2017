package it.polimi.ingsw.LM22.network.server;

public class PlayerInfo {
	private String name = "";
	private String password = "";
	private IPlayer iplayer;
	private Boolean connected = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public IPlayer getIplayer() {
		return iplayer;
	}

	public void setIplayer(IPlayer iplayer) {
		this.iplayer = iplayer;
	}

	public Boolean getConnected() {
		return connected;
	}

	public void setConnected(Boolean connected) {
		this.connected = connected;
	}
}
