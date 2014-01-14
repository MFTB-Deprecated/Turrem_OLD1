package zap.turrem.client.states;

import static org.lwjgl.opengl.GL11.*;
import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;

public class StateIntro implements IState
{
	private Turrem theTurrem;

	public static final int zapLogoTime = 5;

	private int ticks = 0;

	public StateIntro(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		/*
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Config.getWidth(), Config.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		*/
	}

	@Override
	public void end()
	{
		/*
		glClear(GL_COLOR_BUFFER_BIT);
		*/
	}

	@Override
	public void tick()
	{
		/*
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glColor3f(1.0f, 1.0f, 1.0f);
		glBegin(GL_QUADS);

		glVertex2f(0, 0);
		glVertex2f(0, Config.getHeight());
		glVertex2f(Config.getWidth(), Config.getHeight());
		glVertex2f(Config.getWidth(), 0);

		glEnd();
		*/

		if (this.ticks++ > zapLogoTime && !this.theTurrem.isLoading())
		{
			this.theTurrem.setState(EnumClientState.Menu);
		}
	}
}