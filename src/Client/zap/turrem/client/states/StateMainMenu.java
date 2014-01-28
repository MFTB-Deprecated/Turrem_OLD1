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

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.lwjgl.input.Mouse;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.render.font.Font;
import zap.turrem.client.render.font.FontRender;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateMainMenu implements IState
{
	protected Turrem theTurrem;

	private final float h = 0.62F;
	private float s = 0.5F;
	private float b = 0.5F;
	private int t = 0;

	public FontRender testFont;

	public StateMainMenu(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		Font font = new Font("basicintro");
		try
		{
			font.loadTextureFile(new File(Turrem.getTurrem().getDir() + "assets/core/fonts/basic.png"));
			font.push();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.testFont = new FontRender(font);
	}

	@Override
	public void end()
	{
		this.testFont.theFont.unload();
	}

	@Override
	public void tick()
	{
		this.t++;

		this.s = ((float) Math.sin(this.t / 40.0f + 5) + 1.0F) / 2.0F;
		this.b = ((float) Math.sin(this.t / 100.0f + 7) + 1.0F) / 2.0F;

		Color c = Color.getHSBColor(this.h, this.s * 0.2f, this.b * 0.2f + 0.6F);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glColor3f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
		glBegin(GL_QUADS);

		glVertex2f(0, 0);
		glVertex2f(0, Config.getHeight());
		glVertex2f(Config.getWidth(), Config.getHeight());
		glVertex2f(Config.getWidth(), 0);

		glEnd();

		this.testFont.renderTextCentered("- Press any Key -", Config.getWidth() / 2, Config.getHeight() / 2, 50.0F);
	}

	@Override
	public void updateGL()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Config.getWidth(), Config.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	@Override
	public void mouseEvent()
	{
		if (Mouse.getEventButton() != -1)
		{
			this.theTurrem.setState(EnumClientState.Game);
		}
	}

	@Override
	public void keyEvent()
	{
		this.theTurrem.setState(EnumClientState.Game);
	}
}
