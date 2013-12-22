package zap.turrem;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Main");
		Turrem t = new Turrem();
		try
		{
			t.loadTechJar("C:/Users/Sam Sartor/Turrem/bin/jars/");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
