package zap.turrem;


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

	public void doLoad()
	{
		try
		{
			JarLoader.loadTechJar(this.dir);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Session getSession()
	{
		return session;
	}
	
	public String getDir()
	{
		return dir;
	}
}
