package zap.turrem.client.states;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import static org.lwjgl.opengl.GL11.*;

public class MainMenu implements IState
{
	private Turrem theTurrem;
	
	public MainMenu(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Config.getWidth(), Config.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	@Override
	public void end()
	{
		glClear(GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void tick()
	{
		glBegin(GL_QUADS);
		
		glEnd();
	}
}
