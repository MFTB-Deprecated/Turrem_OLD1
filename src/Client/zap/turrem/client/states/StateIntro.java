package zap.turrem.client.states;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateIntro implements IState
{
	protected Turrem theTurrem;

	public static final int zapLogoTime = 0;

	private int ticks = 0;

	public StateIntro(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{

	}

	@Override
	public void end()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void tick()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(0, Config.getHeight());
		GL11.glVertex2f(Config.getWidth(), Config.getHeight());
		GL11.glVertex2f(Config.getWidth(), 0);

		GL11.glEnd();

		if (this.ticks++ > zapLogoTime && !this.theTurrem.isLoading())
		{
			this.theTurrem.setState(EnumClientState.Menu);
		}
	}

	@Override
	public void updateGL()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Config.getWidth(), Config.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	@Override
	public void mouseEvent()
	{
	}

	@Override
	public void keyEvent()
	{
	}
}