package zap.turrem.client.states;

public interface IState
{
	public static enum EnumClientState
	{
		Intro,
		Menu,
		Game;
	}

	public void start();

	public void end();

	public void tick();
	
	public void updateGL();
	
	public void mouseEvent();
	
	public void keyEvent();
}
