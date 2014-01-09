package zap.turrem.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import zap.turrem.client.config.Config;
import zap.turrem.client.states.Game;
import zap.turrem.client.states.IState;
import zap.turrem.client.states.MainMenu;
import zap.turrem.core.loaders.java.JarFileLoader;
import zap.turrem.utils.graphics.ImgUtils;

public class Turrem
{
	private Session session;
	private String dir;

	private static Turrem instance;

	private IState state;
	private IState.EnumClientState enumstate;

	public static Turrem getTurrem()
	{
		return instance;
	}

	public Turrem(String dir, Session session)
	{
		this.dir = dir;
		this.session = session;
		instance = this;
	}

	public void run()
	{
		try
		{
			if (!Config.isFullscreen())
			{
				Display.setDisplayMode(new DisplayMode(Config.getWidth(), Config.getHeight()));
			}
			Display.setTitle("Turrem");
			Display.setVSyncEnabled(Config.isVsync());
			Display.setFullscreen(Config.isFullscreen());
			this.setIcons();

			Config.setRefreshed();

			Display.create();
		}
		catch (LWJGLException | IOException e)
		{
			e.printStackTrace();
		}

		this.enumstate = IState.EnumClientState.Menu;

		this.runloop();
	}

	public void runloop()
	{
		while (!Display.isCloseRequested())
		{
			try
			{
				this.render();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.shutdown();
				return;
			}

			Display.update();
			Display.sync(Config.getLwjglSyncRate());
		}
		this.shutdown();
	}

	public void render() throws LWJGLException, IOException
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_F11))
		{
			Config.setFullscreen(!Config.isFullscreen());
		}
		
		if (Config.isChanged())
		{
			Display.destroy();

			if (!Config.isFullscreen())
			{
				Display.setDisplayMode(new DisplayMode(Config.getWidth(), Config.getHeight()));
			}
			Display.setTitle("Turrem");
			Display.setVSyncEnabled(Config.isVsync());
			Display.setFullscreen(Config.isFullscreen());
			this.setIcons();

			Config.setRefreshed();

			Display.create();
		}

		switch (this.enumstate)
		{
			case Menu:
				this.renderMenu();
				break;
			case Game:
				this.renderGame();
				break;
		}
	}

	private void renderMenu()
	{
		if (this.state instanceof MainMenu)
		{
			this.state.tick();
		}
		else
		{
			if (this.state != null)
			{
				this.state.end();
			}
			this.state = new MainMenu(this);
			this.state.start();
		}
	}

	private void renderGame()
	{
		if (this.state instanceof Game)
		{
			this.state.tick();
		}
		else
		{
			if (this.state != null)
			{
				this.state.end();
			}
			this.state = new Game(this);
			this.state.start();
		}
	}

	public void shutdown()
	{
		Display.destroy();
		System.exit(0);
	}

	public void setIcons() throws IOException
	{
		ArrayList<ByteBuffer> icos = new ArrayList<ByteBuffer>();

		JarFileLoader assetloader = new JarFileLoader(new File(this.dir + "assets/" + "assets.jar"));

		String[] filelist = assetloader.getFileList();
		ArrayList<String> iconlist = new ArrayList<String>();

		for (int i = 0; i < filelist.length; i++)
		{
			String file = filelist[i];
			if (file.startsWith("assets/icons/ico") && file.endsWith(".png"))
			{
				iconlist.add(file);
			}
		}

		for (String icon : iconlist)
		{
			BufferedImage img = ImageIO.read(assetloader.getFileInJar(icon));
			icos.add(ImgUtils.imgToByteBuffer(img));
		}

		Display.setIcon(icos.toArray(new ByteBuffer[0]));
	}

	public Session getSession()
	{
		return this.session;
	}

	public String getDir()
	{
		return this.dir;
	}
}
