package zap.turrem.client.asset;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import zap.turrem.utils.models.TVFFile;
import zap.turrem.utils.models.VOXFile;

public class AssetLoader
{
	String bin;
	
	public AssetLoader(String bindir)
	{
		this.bin = bindir;
	}
	
	public TVFFile loadTVF(String name) throws IOException
	{
		String dir = this.bin + "/assets/" + name.replaceAll("\\.", "/") + ".tvf";
		
		File filein = new File(dir);
		DataInputStream input;

		input = new DataInputStream(new GZIPInputStream(new FileInputStream(filein)));

		return TVFFile.read(input);
	}
	
	public void convertAllVox()
	{
		String dir = this.bin + "/assets/";
		File folder = new File(dir);
		this.convertVoxFolder(folder);
	}
	
	private void convertVoxFolder(File folder)
	{
		File[] files = folder.listFiles();
		
		for (File file : files)
		{
			if (file.isDirectory())
			{
				this.convertVoxFolder(file);
			}
			if (file.isFile())
			{
				String name = file.getAbsolutePath();
				int dot = name.lastIndexOf(".");
				String type = name.substring(dot + 1);
				if ("vox".equals(type))
				{
					this.convertVox(file);
				}
			}
		}
	}
	
	private void convertVox(File voxfile)
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

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
