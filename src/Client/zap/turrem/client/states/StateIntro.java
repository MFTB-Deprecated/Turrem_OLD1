package zap.turrem.client.states;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;
import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;

public class StateIntro implements IState
{
	private Turrem theTurrem;

	public static final int zapLogoTime = 450;

	private int ticks = 0;

	public StateIntro(Turrem turrem)
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
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glColor3f(1.0f, 1.0f, 1.0f);
		glBegin(GL_QUADS);

		glVertex2f(0, 0);
		glVertex2f(0, Config.getHeight());
		glVertex2f(Config.getWidth(), Config.getHeight());
		glVertex2f(Config.getWidth(), 0);

		glEnd();

		if (this.ticks++ > zapLogoTime && !this.theTurrem.isLoading())
		{
			this.theTurrem.setState(EnumClientState.Menu);
		}
	}
}