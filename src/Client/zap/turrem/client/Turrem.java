package zap.turrem.client;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import zap.turrem.client.config.Config;
import zap.turrem.client.states.IState;
import zap.turrem.client.states.StateGame;
import zap.turrem.client.states.StateIntro;
import zap.turrem.client.states.StateMainMenu;
import zap.turrem.core.loaders.java.JarFileLoader;
import zap.turrem.utils.graphics.ImgUtils;
import zap.turrem.utils.models.TVFFile;
import zap.turrem.utils.models.VOXFile;

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
		this.updateDisplay();

		try
		{
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		this.enumstate = IState.EnumClientState.Intro;
		
		this.testTVF();

		this.runloop();
	}

	public void testTVF()
	{
		String fni = this.dir + "eekysam.vox";
		String fno = this.dir + "eekysam.tvf";
		
		try
		{
			File filein = new File(fni);
			DataInputStream input = new DataInputStream(new FileInputStream(filein));

			VOXFile vox = VOXFile.read(input);

			input.close();

			File fileout = new File(fno);
			fileout.createNewFile();
			DataOutputStream output = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(fileout)));

			TVFFile tvf = new TVFFile(vox);
			tvf.write(output);

			output.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
			this.updateDisplay();
		}

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
		}
	}

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
		}
	}

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
		}
	}

	public void shutdown()
	{
		Display.destroy();
		System.exit(0);
	}

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

		Config.setRefreshed();
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

	public boolean isLoading()
	{
		return false;
	}

	public void setState(IState.EnumClientState newstate)
	{
		this.enumstate = newstate;
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
