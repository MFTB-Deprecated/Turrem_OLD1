package zap.turrem.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.TechList;

public class TechLoader extends JarLoader
{
	public TechLoader(File jar)
	{
		super(jar);
	}

	public void loadJar() throws IOException, ClassNotFoundException
	{
		if (!this.jarfile.exists())
		{
			System.out.println("Error! tech.jar does not exist!");
			return;
		}
		URL jarfile = new URL("jar", "", "file:" + this.jarfile.getAbsolutePath() + "!/");

		String[] classlist = this.getClassList();

		URLClassLoader cl = URLClassLoader.newInstance(new URL[] { jarfile });

		for (int i = 0; i < classlist.length; i++)
		{
			Class<?> stone = cl.loadClass(classlist[i]);
			if (TechBase.class.isAssignableFrom(stone) && !Modifier.isAbstract(stone.getModifiers()) && stone.getPackage().getName().startsWith("tech."))
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
}
