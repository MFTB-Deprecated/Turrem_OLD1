package zap.turrem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import zap.turrem.client.ITurremGame;
import zap.turrem.client.TurremGame;
import zap.turrem.client.TurremMenu;
import zap.turrem.loader.JarFileLoader;
import zap.turrem.utils.ImgUtils;

public class Turrem
{
	private Session session;
	private String dir;

	private ITurremGame game;

	private static Turrem instance;

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
			Display.setDisplayMode(new DisplayMode(200, 200));
			Display.setTitle("Turrem");
			this.setIcons();
			Display.create();
		}
		catch (LWJGLException | IOException e)
		{
			e.printStackTrace();
		}

		this.gotoMenu();
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

	public void gotoGame()
	{
		this.game = new TurremGame(this);
		this.game.run();
	}

	public void gotoMenu()
	{
		this.game = new TurremMenu(this);
		this.game.run();
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
