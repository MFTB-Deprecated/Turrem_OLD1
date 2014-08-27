package net.turrem.client.asset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.turrem.tvf.TVFFile;

public class AssetLoader
{
	String bin;

	public AssetLoader(String bindir)
	{
		this.bin = bindir;
	}

	public TVFFile loadTVF(String name) throws IOException
	{
		String dir = this.bin + "assets/" + name.replaceAll("\\.", "/") + ".tvf";
		File filein = new File(dir);
		if (filein.exists())
		{
			return TVFFile.read(filein);
		}
		return null;
	}

	public BufferedImage loadTexture(String name) throws IOException
	{
		String dir = this.bin + "assets/" + name.replaceAll("\\.", "/") + ".png";
		return ImageIO.read(new File(dir));
	}

	public boolean doesTextureFileExist(String name)
	{
		String dir = this.bin + "assets/" + name.replaceAll("\\.", "/") + ".png";
		return (new File(dir)).exists();
	}
}
