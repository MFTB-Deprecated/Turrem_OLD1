package tech.turrem;

import zap.turrem.tech.TechBase;

public class StoneTools extends TechBase
{
	public StoneTools(int pass)
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
		return (this.pass == 0 ? "Stone Tools" : "Sharp Stone Tools");
	}
}
