package zap.turrem.loaders.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarFileLoader
{
	protected File jarfile;

	public JarFileLoader(File jar)
	{
		this.jarfile = jar;
	}

	public InputStream getFileInJar(String path)
	{
		InputStream in = null;
		String inputFile = "jar:file:/" + this.jarfile.getAbsolutePath() + "!/" + path;
		if (inputFile.startsWith("jar:"))
		{
			try
			{
				URL inputURL = new URL(inputFile);
				JarURLConnection conn = (JarURLConnection) inputURL.openConnection();
				return conn.getInputStream();
			}
			catch (MalformedURLException e1)
			{
				System.err.println("Malformed input URL: " + inputFile);
			}
			catch (IOException e1)
			{
				System.err.println("IO error open connection");
			}
		}
		return in;
	}

	public String[] getFileList() throws IOException
	{
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(this.jarfile.getAbsolutePath()));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if (!entry.isDirectory())
			{
				StringBuilder className = new StringBuilder();
				for (String part : entry.getName().split("/"))
				{
					if (className.length() != 0)
					{
						className.append("/");
					}
					className.append(part);
				}
				classNames.add(className.toString());
			}
		}
		zip.close();
		return classNames.toArray(new String[0]);
	}
	
	public String[] getClassList() throws IOException
	{
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(this.jarfile.getAbsolutePath()));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if (entry.getName().endsWith(".class") && !entry.isDirectory())
			{
				StringBuilder className = new StringBuilder();
				for (String part : entry.getName().split("/"))
				{
					if (className.length() != 0)
					{
						className.append(".");
					}
					className.append(part);
					if (part.endsWith(".class"))
					{
						className.setLength(className.length() - ".class".length());
					}
				}
				classNames.add(className.toString());
			}
		}
		zip.close();
		return classNames.toArray(new String[0]);
	}
}