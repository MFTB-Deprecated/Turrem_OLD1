package zap.turrem.client.states;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import zap.turrem.client.Turrem;
import zap.turrem.client.config.Config;
import zap.turrem.utils.graphics.ImgUtils;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateIntro implements IState
{
	protected Turrem theTurrem;

	public static final int zapLogoTime = 150;

	private int ticks = 0;

	private int width;

	private int height;

	private float aspect;

	private int textureId;

	public StateIntro(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File(this.theTurrem.getDir() + "assets/core/misc/ZapCloud.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (img != null)
		{
			this.width = img.getWidth();
			this.height = img.getHeight();
			this.aspect = (float) this.width / (float) this.height;
			ByteBuffer bytes = ImgUtils.imgToByteBuffer(img);

			int texId = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bytes);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			
			this.textureId = texId;
		}
	}

	@Override
	public void end()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDeleteTextures(this.textureId);
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

		this.drawLogo();
		
		if (this.ticks++ > zapLogoTime && !this.theTurrem.isLoading())
		{
			this.theTurrem.setState(EnumClientState.Menu);
		}
	}
	
	public void drawLogo()
	{
		int w;
		int h;
		if (Config.getWidth() < Config.getHeight() * this.aspect)
		{
			w = Config.getWidth();
			h = (int) (w / this.aspect);
		}
		else
		{
			h = Config.getHeight();
			w = (int) (h * this.aspect);
		}
		
		int x = Config.getWidth() / 2 - w / 2;
		int y = Config.getHeight() / 2 - h / 2;
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
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId);
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
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