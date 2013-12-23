package zap.turrem.client;

import zap.turrem.Turrem;

//TODO Create GUI
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
		// TODO Game loop?
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
			// TODO Actual main menu
			newMenu = false;
			this.theTurrem.gotoGame();
		}
	}
}
