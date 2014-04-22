package net.turrem.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.turrem.client.asset.AssetLoader;
import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.states.IState;
import net.turrem.client.states.StateIntro;
import net.turrem.utils.graphics.ImgUtils;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Turrem
{
	private static Turrem instance;

	public final Session theSession;
	public final String theGameDir;

	public RenderManager theRender;
	public AssetLoader theAssets;

	private IState thisState;
	private long statechange = 0;

	private long renderCount = 0;
	
	public static String networkLoc;

	public Turrem(Session session, String dir)
	{
		this.theSession = session;
		this.theGameDir = dir;

		instance = this;
	}

	protected void run()
	{
		this.updateDisplay(1280, 800, false, true);

		this.theAssets = new AssetLoader(this.theGameDir);
		this.theRender = new RenderManager(this.theAssets);

		try
		{
			this.setIcons();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		this.onRun();
		this.runloop();
	}

	public void onRun()
	{
		System.out.println("bin - " + this.theGameDir);
		this.setClientState(StateIntro.class);
	}

	public void afterStart()
	{
		System.out.println("Username: " + this.theSession.username);
		this.theAssets.convertAllVox(true);
	}

	public void render()
	{
		if (this.renderCount == 2)
		{
			this.afterStart();
		}
		if (this.thisState != null)
		{
			if (this.statechange == 0)
			{
				this.thisState.updateGL();
			}
			this.thisState.render();
		}
		this.statechange++;
		this.renderCount++;
	}

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
			Display.sync(60);
		}
		this.shutdown();
	}

	private void doInputEvents()
	{
		while (Mouse.next())
		{
			if (this.thisState != null)
			{
				this.thisState.mouseEvent();
			}
		}
		while (Keyboard.next())
		{
			if (this.thisState != null)
			{
				this.thisState.keyEvent();
			}
		}
	}

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
				System.err.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		}
		catch (LWJGLException e)
		{
			System.err.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}

	public void shutdown()
	{
		Display.destroy();
		System.exit(0);
	}

	public void updateDisplay(int width, int height, boolean full, boolean vsync)
	{
		this.setDisplayMode(width, height, full);
		Display.setTitle("Turrem");
		Display.setVSyncEnabled(vsync);
	}

	public void setIcons() throws IOException
	{
		ArrayList<ByteBuffer> icos = new ArrayList<ByteBuffer>();

		File folder = new File(this.theGameDir + "assets/core/icons/");

		File[] filelist = folder.listFiles();

		for (File icon : filelist)
		{
			BufferedImage img = ImageIO.read(icon);
			icos.add(ImgUtils.imgToByteBuffer(img));
		}

		Display.setIcon(icos.toArray(new ByteBuffer[0]));
	}

	public static AssetLoader getAssets()
	{
		return instance.theAssets;
	}

	public static RenderManager getRender()
	{
		return instance.theRender;
	}

	public int getScreenWidth()
	{
		return Display.getWidth();
	}

	public int getScreenHeight()
	{
		return Display.getHeight();
	}

	public boolean setClientState(Class<? extends IState> state, Object... pars)
	{
		Class<?>[] parc = new Class[pars.length + 1];
		parc[0] = this.getClass();
		for (int i = 0; i < pars.length; i++)
		{
			parc[i + 1] = pars[i].getClass();
		}

		Object[] obs = new Object[pars.length + 1];
		obs[0] = this;
		for (int i = 0; i < pars.length; i++)
		{
			obs[i + 1] = pars[i];
		}

		IState newstate = null;
		try
		{
			newstate = state.getConstructor(parc).newInstance(obs);
		}
		catch (Exception e)
		{
			System.err.println("Could not make new state: " + state.getSimpleName() + " (Current: " + this.thisState + " )");
			e.printStackTrace();
			return false;
		}

		this.statechange = 0;

		if (this.thisState != null)
		{
			this.thisState.end();
		}
		this.thisState = newstate;
		this.thisState.start();

		return true;
	}

	public boolean isLoading()
	{
		return false;
	}
}
