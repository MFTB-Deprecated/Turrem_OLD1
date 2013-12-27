package zap.turrem.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

import zap.turrem.tech.TechBase;
import zap.turrem.tech.TechList;

public class GameLoader
{	
	private String dir;
	
	public GameLoader(String dir)
	{
		this.dir = dir;
	}
	
	public void loadGame()
	{
		try
		{
			this.loadTechJar(new JarFileLoader(new File(this.dir + "jars/" + "tech.jar")));
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadTechJar(JarFileLoader loader) throws IOException, ClassNotFoundException
	{
		if (!loader.jarfile.exists())
		{
			System.out.println("Error! tech.jar does not exist!");
			return;
		}
		URL jarfile = new URL("jar", "", "file:" + loader.jarfile.getAbsolutePath() + "!/");

		String[] classlist = loader.getClassList();

		URLClassLoader cl = URLClassLoader.newInstance(new URL[] { jarfile });

		for (int i = 0; i < classlist.length; i++)
		{
			Class<?> stone = cl.loadClass(classlist[i]);
			if (!Modifier.isAbstract(stone.getModifiers()))
			{
				if (TechBase.class.isAssignableFrom(stone) && stone.getPackage().getName().startsWith("tech."))
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
}
