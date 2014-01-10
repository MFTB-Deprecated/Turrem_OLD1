package zap.turrem.server;

import java.util.ArrayList;
import java.util.List;

import zap.turrem.server.player.ClientSession;

public class TurremServer
{
	private static TurremServer instance;

	private String dir;

	private TurremGame theGame;

	private List<ClientSession> connected = new ArrayList<ClientSession>();

	public static TurremServer getTurrem()
	{
		return instance;
	}

	public TurremServer(String dir)
	{
		this.dir = dir;
		instance = this;
	}

	public void run()
	{
		this.runGame();
	}

	public void addClient(ClientSession client)
	{
		this.connected.add(client);
	}

	public void runGame()
	{
		this.theGame = new TurremGame(this);
		this.theGame.run();
	}

	public String getDir()
	{
		return this.dir;
	}
}
