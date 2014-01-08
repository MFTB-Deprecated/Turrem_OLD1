package zap.turrem.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import zap.turrem.loaders.java.JarFileLoader;
import zap.turrem.utils.graphics.ImgUtils;

public class Turrem
{
	private Session session;
	private String dir;

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
			Display.setDisplayMode(new DisplayMode(720, 640));
			Display.setInitialBackground(1.0F, 1.0F, 1.0F);
			Display.setTitle("Turrem");
			this.setIcons();
			Display.create();
		}
		catch (LWJGLException | IOException e)
		{
			e.printStackTrace();
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

	public Session getSession()
	{
		return this.session;
	}

	public String getDir()
	{
		return this.dir;
	}
}
