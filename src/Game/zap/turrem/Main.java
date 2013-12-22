package zap.turrem;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Main");
		Turrem t = new Turrem("C:/Users/Sam Sartor/Turrem/bin/jars/", new Session());
		t.doLoad();
	}
}
