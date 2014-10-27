package net.turrem.app.client.states;

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

import java.awt.Color;

import net.turrem.app.client.Turrem;
import net.turrem.app.client.render.font.Font;
import net.turrem.app.client.render.font.FontRender;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateMainMenu implements IState
{
	protected Turrem theTurrem;
	
	private float h = 0.62F;
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
		Font font = new Font();
		font.loadTexture("app:font.screen", this.theTurrem.theRender, true);
		this.testFont = new FontRender(font);
	}
	
	@Override
	public void end()
	{
		
	}
	
	@Override
	public void render()
	{
		this.t++;
		
		this.h = 0.5F + (float) Math.sin(this.t / 457.0f) / 6.0F;
		this.s = ((float) Math.sin(this.t / 47.0f + 5) + 1.0F) / 2.0F;
		this.b = ((float) Math.sin(this.t / 109.0f + 7) + 1.0F) / 2.0F;
		
		Color c = Color.getHSBColor(this.h, this.s * 0.2f, this.b * 0.2f + 0.6F);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glColor3f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
		glBegin(GL_QUADS);
		
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(0, this.theTurrem.getScreenHeight());
		GL11.glVertex2f(this.theTurrem.getScreenWidth(), this.theTurrem.getScreenHeight());
		GL11.glVertex2f(this.theTurrem.getScreenWidth(), 0);
		
		glEnd();
		
		glColor3f(1.0F, 1.0F, 1.0F);
		
		glColor3f(0.0F, 0.0F, 0.0F);
		this.testFont.renderTextCentered("- Press any Key -", this.theTurrem.getScreenWidth() / 2, this.theTurrem.getScreenHeight() / 2, 50.0F);
		this.testFont.renderTextCentered("Right Click - Move Citizen", this.theTurrem.getScreenWidth() / 2, (this.theTurrem.getScreenHeight() / 14) * 1, 50.0F);
		this.testFont.renderTextCentered("Left Click - Move Camera", this.theTurrem.getScreenWidth() / 2, (this.theTurrem.getScreenHeight() / 14) * 2, 50.0F);
		this.testFont.renderTextCentered("Middle Click - Rotate Camera", this.theTurrem.getScreenWidth() / 2, (this.theTurrem.getScreenHeight() / 14) * 3, 50.0F);
		this.testFont.renderTextCentered("Scroll - Zoom", this.theTurrem.getScreenWidth() / 2, (this.theTurrem.getScreenHeight() / 14) * 4, 50.0F);
		this.testFont.renderTextCentered("Don't Forget to Start the Server!", this.theTurrem.getScreenWidth() / 2, (this.theTurrem.getScreenHeight() / 14) * 10, 50.0F);
		glColor3f(1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void updateGL()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, this.theTurrem.getScreenWidth(), this.theTurrem.getScreenHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	@Override
	public void mouseEvent()
	{
		if (Mouse.getEventButton() != -1)
		{
			this.theTurrem.setClientState(StateGame.class);
		}
	}
	
	@Override
	public void keyEvent()
	{
		this.theTurrem.setClientState(StateGame.class);
	}
}
