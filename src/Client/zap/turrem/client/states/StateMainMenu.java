package zap.turrem.client.states;

import static org.lwjgl.opengl.GL11.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.client.render.TVFBuffer;
import zap.turrem.utils.models.TVFFile;

public class StateMainMenu implements IState
{
	private Turrem theTurrem;
	/*
	 * private final float h = 0.62F; private float s = 0.5F; private float b =
	 * 0.5F; private int t = 0;
	 */

	private TVFBuffer eekysam;

	public StateMainMenu(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	private float angle = 0.0F;

	@Override
	public void start()
	{
		/*
		 * glMatrixMode(GL_PROJECTION); glLoadIdentity(); glOrtho(0,
		 * Config.getWidth(), Config.getHeight(), 0, 1, -1);
		 * glMatrixMode(GL_MODELVIEW);
		 */

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(70.0F, Config.getWidth() / Config.getHeight(), 0.0F, 20.0F);
		glMatrixMode(GL_MODELVIEW);

		this.eekysam = new TVFBuffer();
		TVFFile tvf = null;

		try
		{
			String fno = this.theTurrem.getDir() + "eekysam.tvf";

			File filein = new File(fno);
			DataInputStream input;

			input = new DataInputStream(new GZIPInputStream(new FileInputStream(filein)));

			tvf = TVFFile.read(input);

			input.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.eekysam.bindTVF(tvf);
	}

	@Override
	public void end()
	{
		glClear(GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void tick()
	{
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glPushMatrix();
		GL11.glRotatef(this.angle, 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(3.0F * Math.sin(this.angle / 180.0F * Math.PI), -1.0F, -3.0F * Math.cos(this.angle / 180.0F * Math.PI));
		this.eekysam.render();
		GL11.glPopMatrix();
		this.angle += 0.1F;
		/*
		 * this.t++;
		 * 
		 * this.s = ((float) Math.sin(this.t / 40.0f + 5) + 1.0F) / 2.0F; this.b
		 * = ((float) Math.sin(this.t / 100.0f + 7) + 1.0F) / 2.0F;
		 * 
		 * Color c = Color.getHSBColor(this.h, this.s * 0.2f, this.b * 0.2f +
		 * 0.6F);
		 * 
		 * glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		 * 
		 * glColor3f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() /
		 * 255.0f); glBegin(GL_QUADS);
		 * 
		 * glVertex2f(0, 0); glVertex2f(0, Config.getHeight());
		 * glVertex2f(Config.getWidth(), Config.getHeight());
		 * glVertex2f(Config.getWidth(), 0);
		 * 
		 * glEnd();
		 * 
		 * if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
		 * this.theTurrem.setState(EnumClientState.Game); }
		 */
	}
}
