package zap.turrem.client.asset;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import zap.turrem.utils.models.TVFFile;

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
}
