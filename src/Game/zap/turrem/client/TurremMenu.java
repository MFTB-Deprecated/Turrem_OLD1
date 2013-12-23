package zap.turrem.client;

import zap.turrem.Turrem;

public class TurremMenu implements ITurremGame
{
	public static boolean newMenu = true;

	public Turrem theTurrem;

	public TurremMenu(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void runGameLoop()
	{

	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void run()
	{
		if (newMenu)
		{
			newMenu = false;
			this.theTurrem.gotoGame();
		}
	}
}
