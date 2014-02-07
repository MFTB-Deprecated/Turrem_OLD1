package zap.turrem.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import zap.turrem.client.asset.AssetLoader;
import zap.turrem.client.config.Config;
import zap.turrem.client.game.world.WorldTerrain;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.states.IState;
import zap.turrem.client.states.StateGame;
import zap.turrem.client.states.StateIntro;
import zap.turrem.client.states.StateMainMenu;
import zap.turrem.utils.graphics.ImgUtils;

public class Turrem
{
	private Session session;
	private String dir;

	private static Turrem instance;

	private IState state;
	private IState.EnumClientState enumstate;

	public RenderManager theRender;
	public AssetLoader theAssets;

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

	/**
	 * Runs the client
	 */
	public void run()
	{
		this.updateDisplay();

		try
		{
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		WorldTerrain ter = new WorldTerrain(42L);
		ter.generate();
		
		try
		{
			File f = new File(this.dir + "tertest.png");
			f.createNewFile();
			ImageIO.write(ter.renderTest(0, 0, 32, 32), "png", new FileOutputStream(f));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		this.theAssets = new AssetLoader(this.dir);
		this.theRender = new RenderManager(this.theAssets);

		this.enumstate = IState.EnumClientState.Intro;
		
		this.runloop();
	}

	/**
	 * Runs the client game/gui loop
	 */
	public void runloop()
	{
		while (!Display.isCloseRequested())
		{
			try
			{
				this.render();
				this.doInputEvents();
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

	/**
	 * All client rendering starts here
	 * 
	 * @throws LWJGLException
	 * @throws IOException
	 */
	public void render() throws LWJGLException, IOException
	{
		this.loadTick();

		switch (this.enumstate)
		{
			case Intro:
				this.renderIntro();
				break;
			case Menu:
				this.renderMenu();
				break;
			case Game:
				this.renderGame();
				break;
		}
	}
	
	private void doInputEvents()
	{
		while (Mouse.next())
		{
			this.state.mouseEvent();
		}
		while (Keyboard.next())
		{
			this.state.keyEvent();;
		}
	}

	/**
	 * Intermediary function to render the intro
	 */
	private void renderIntro()
	{
		if (this.state instanceof StateIntro)
		{
			this.state.tick();
		}
		else
		{
			if (this.state != null)
			{
				this.state.end();
			}
			this.state = new StateIntro(this);
			this.state.start();
			this.state.updateGL();
		}
	}

	/**
	 * Intermediary function to render the main menu
	 */
	private void renderMenu()
	{
		if (this.state instanceof StateMainMenu)
		{
			this.state.tick();
		}
		else
		{
			if (this.state != null)
			{
				this.state.end();
			}
			this.state = new StateMainMenu(this);
			this.state.start();
			this.state.updateGL();
		}
	}

	/**
	 * Intermediary function to render the game
	 */
	private void renderGame()
	{
		if (this.state instanceof StateGame)
		{
			this.state.tick();
		}
		else
		{
			if (this.state != null)
			{
				this.state.end();
			}
			this.state = new StateGame(this);
			this.state.start();
			this.state.updateGL();
		}
	}

	public void loadTick()
	{
		this.theRender.tickHolders();
	}

	/**
	 * Called when the client is shutdown
	 */
	public void shutdown()
	{
		Display.destroy();
		System.exit(0);
	}

	/**
	 * Update any changed display settings and display mode using settings
	 * stored in the Confg class
	 */
	public void updateDisplay()
	{
		this.setDisplayMode(Config.getWidth(), Config.getHeight(), Config.isFullscreen());
		Display.setTitle("Turrem");
		Display.setVSyncEnabled(Config.isVsync());
		try
		{
			this.setIcons();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (this.state != null)
		{
			this.state.updateGL();
		}
	}

	/**
	 * Set the display mode
	 * 
	 * @param width Width of the display
	 * @param height Height of the display
	 * @param fullscreen Should the display be fullscreen
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen)
	{
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen))
		{
			return;
		}

		try
		{
			DisplayMode targetDisplayMode = null;

			if (fullscreen)
			{
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++)
				{
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height))
					{
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq))
						{
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
							{
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
						{
							targetDisplayMode = current;
							break;
						}
					}
				}
			}
			else
			{
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null)
			{
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		}
		catch (LWJGLException e)
		{
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}

	/**
	 * Loads and sets the turrem icon of the application
	 * 
	 * @throws IOException
	 */
	public void setIcons() throws IOException
	{
		ArrayList<ByteBuffer> icos = new ArrayList<ByteBuffer>();

		File folder = new File(this.dir + "assets/core/icons/");

		File[] filelist = folder.listFiles();

		for (File icon : filelist)
		{
			BufferedImage img = ImageIO.read(icon);
			icos.add(ImgUtils.imgToByteBuffer(img));
		}

		Display.setIcon(icos.toArray(new ByteBuffer[0]));
	}

	/**
	 * This will hold the game in the intro state or current loading screen if
	 * true
	 * 
	 * @return Should stay in intro or loading screen
	 */
	public boolean isLoading()
	{
		return false;
	}

	/**
	 * Sets the current game state to be updated next tick
	 * 
	 * @param newstate The new game state to switch to
	 */
	public void setState(IState.EnumClientState newstate)
	{
		this.enumstate = newstate;
	}

	/**
	 * Gets the current session
	 * 
	 * @return The current session
	 */
	public Session getSession()
	{
		return this.session;
	}

	/**
	 * Gets the bin/ directory that the game is in
	 * 
	 * @return The game's location
	 */
	public String getDir()
	{
		return this.dir;
	}

	public static AssetLoader getAssets()
	{
		return instance.theAssets;
	}
}
