package zap.turrem.server;

public class TurremServer
{
	private static TurremServer instance;
	
	private String dir;
	
	private TurremGame theGame;

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
