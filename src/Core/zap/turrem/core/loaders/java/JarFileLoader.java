package zap.turrem.core.loaders.java;

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

	/**
	 * Creates a new class for working with jar file contents
	 * 
	 * @param jar The jar file to use
	 */
	public JarFileLoader(File jar)
	{
		this.jarfile = jar;
	}

	/**
	 * Gets an input stream for a file in the jar
	 * 
	 * @param path Path to the file inside the jar
	 * @return InputStream
	 */
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

	/**
	 * Gets the list of all files in the jar
	 * 
	 * @return List of file paths
	 * @throws IOException
	 */
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

	/**
	 * Gets the list of all .class files in the jar
	 * 
	 * @return The names of each class (Example: net.foo.Bar)
	 * @throws IOException
	 */
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