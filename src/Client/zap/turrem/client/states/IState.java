package zap.turrem.client.states;

public interface IState
{
	public static enum EnumClientState
	{
		Menu,
		Game;
	}
	
	public void start();
	
	public void end();
	
	public void tick();
}
