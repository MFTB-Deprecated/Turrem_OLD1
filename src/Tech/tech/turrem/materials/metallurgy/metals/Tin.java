package tech.turrem.materials.metallurgy.metals;

public class Tin extends Metal
{
	public Tin(int pass)
	{
		super(pass);
	}

	@Override
	public String getMetalName()
	{
		return "Tin";
	}

	@Override
	public int getLevel()
	{
		return 1;
	}
}
