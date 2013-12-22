package tech.turrem;

import zap.turrem.tech.TechBase;

public class Flint extends TechBase
{
	public Flint(int pass)
	{
		super(pass);
	}

	public static int numPass()
	{
		return 2;
	}
	
	@Override
	public String getName()
	{
		return this.pass == 0 ? "Flint and a Rock" : "Flint and Steel";
	}
}
