package zap.turrem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.TechList;

public class Turrem
{
	public void loadTechJar(String dir) throws IOException, ClassNotFoundException
	{
		File file = new File(dir + "tech.jar");
		URL jarfile = new URL("jar", "", "file:" + file.getAbsolutePath() + "!/");

		String[] classlist = this.getClassList(file);

		URLClassLoader cl = URLClassLoader.newInstance(new URL[] { jarfile });

		for (int i = 0; i < classlist.length; i++)
		{
			Class<?> stone = cl.loadClass(classlist[i]);
			if (TechBase.class.isAssignableFrom(stone))
			{
				if (TechList.loadTechClass(stone))
				{
					System.out.println("Loaded techs: " + stone.getName());
				}
				else
				{
					System.out.println("Could not load techs: " + stone.getName());
				}
			}
		}
	}

	protected String[] getClassList(File file) throws IOException
	{
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(file.getAbsolutePath()));
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
