package net.turrem.app.client.states;

import net.turrem.app.client.Turrem;
import net.turrem.app.client.render.icon.TextureIcon;

import org.lwjgl.opengl.GL11;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateIntro implements IState
{
	protected Turrem theTurrem;
	
	public static final int zapLogoTime = 150;
	
	private int ticks = 0;
	
	private TextureIcon zaplogo = new TextureIcon("app:misc.ZapCloud", false);
	
	public StateIntro(Turrem turrem)
	{
		this.theTurrem = turrem;
	}
	
	@Override
	public void start()
	{
		this.zaplogo.load(this.theTurrem.theRender);
	}
	
	@Override
	public void end()
	{
		this.zaplogo.object.unload(this.theTurrem.theRender);
	}
	
	@Override
	public void render()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(0, this.theTurrem.getScreenHeight());
		GL11.glVertex2f(this.theTurrem.getScreenWidth(), this.theTurrem.getScreenHeight());
		GL11.glVertex2f(this.theTurrem.getScreenWidth(), 0);
		
		GL11.glEnd();
		
		this.drawLogo();
		
		if (this.ticks++ > zapLogoTime && !this.theTurrem.isLoading())
		{
			this.theTurrem.setClientState(StateMainMenu.class);
		}
	}
	
	public void drawLogo()
	{
		int sw = this.theTurrem.getScreenWidth();
		int sh = this.theTurrem.getScreenHeight();
		int w;
		int h;
		if (sw < sh * this.zaplogo.getAspect())
		{
			w = sw;
			h = (int) (w / this.zaplogo.getAspect());
		}
		else
		{
			h = sh;
			w = (int) (h * this.zaplogo.getAspect());
		}
		
		int x = sw / 2 - w / 2;
		int y = sh / 2 - h / 2;
		int X = x + w;
		int Y = y + h;
		x += 20;
		y += 20;
		X -= 20;
		Y -= 20;
		float i = 0.0F;
		float j = 0.0F;
		float I = 1.0F;
		float J = 1.0F;
		
		this.zaplogo.start();
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(i, j);
		GL11.glVertex2f(x, y);
		
		GL11.glTexCoord2f(i, J);
		GL11.glVertex2f(x, Y);
		
		GL11.glTexCoord2f(I, J);
		GL11.glVertex2f(X, Y);
		
		GL11.glTexCoord2f(I, j);
		GL11.glVertex2f(X, y);
		
		GL11.glEnd();
		this.zaplogo.end();
	}
	
	@Override
	public void updateGL()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, this.theTurrem.getScreenWidth(), this.theTurrem.getScreenHeight(), 0, 1, -1);
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