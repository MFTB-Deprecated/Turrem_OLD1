package net.turrem.client.asset;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import net.turrem.utils.StopTimer;
import net.turrem.utils.Toolbox;
import net.turrem.utils.models.TVFFile;
import net.turrem.utils.models.VOXFile;

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
		DataInputStream input;

		input = new DataInputStream(new GZIPInputStream(new FileInputStream(filein)));

		return TVFFile.read(input);
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

	public void convertAllVox(boolean overwrite)
	{
		System.out.println("Converting all VOX files...this may take awhile");

		StopTimer timer = new StopTimer();
		timer.start();

		String dir = this.bin + "assets/";
		File folder = new File(dir);
		int num = this.convertVoxFolder(folder, overwrite);

		timer.pause();
		String time = Toolbox.getFloat((float) timer.getTotalmili(), 2);

		System.out.println("Converted " + num + " VOX files to TVF files in " + time + "ms");
	}

	private int convertVoxFolder(File folder, boolean overwrite)
	{
		int count = 0;

		File[] files = folder.listFiles();

		for (File file : files)
		{
			if (file.isDirectory())
			{
				count += this.convertVoxFolder(file, overwrite);
			}
			if (file.isFile())
			{
				String name = file.getAbsolutePath();
				int dot = name.lastIndexOf(".");
				String type = name.substring(dot + 1);
				if ("vox".equals(type))
				{
					File tvfex = new File(name.substring(0, dot) + ".tvf");
					if (!tvfex.exists() || overwrite)
					{
						if (this.convertVox(file))
						{
							count++;
						}
					}
				}
			}
		}

		return count;
	}

	private boolean convertVox(File voxfile)
	{
		try
		{
			String vox = voxfile.getAbsolutePath();
			int ind = vox.lastIndexOf(".");
			String tvf = vox.substring(0, ind) + ".tvf";

			DataInputStream input = new DataInputStream(new FileInputStream(voxfile));
			VOXFile filevox = VOXFile.read(input);
			input.close();

			File tvffile = new File(tvf);
			tvffile.createNewFile();
			DataOutputStream output = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(tvffile)));
			TVFFile filetvf = new TVFFile(filevox);
			filetvf.write(output);
			output.close();

			return true;

		}
		catch (Exception e)
		{
			e.printStackTrace();

			return false;
		}
	}
}
