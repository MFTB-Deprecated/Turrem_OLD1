package zap.turrem;

import zap.turrem.client.ITurremGame;
import zap.turrem.client.TurremGame;
import zap.turrem.client.TurremMenu;

public class Turrem
{
	private Session session;
	private String dir;

	private ITurremGame game;

	private static Turrem instance;

	public static Turrem getTurrem()
	{
		return instance;
	}

	public Turrem(String dir, Session session)
	{
		this.dir = dir;
		this.session = session;
		instance = this;
	}

	public void run()
	{
		this.gotoMenu();
	}

	public void gotoGame()
	{
		this.game = new TurremGame(this);
		this.game.run();
	}

	public void gotoMenu()
	{
		this.game = new TurremMenu(this);
		this.game.run();
	}

	public Session getSession()
	{
		return this.session;
	}

	public String getDir()
	{
		return this.dir;
	}
}
