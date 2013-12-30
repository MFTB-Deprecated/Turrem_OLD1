package tech.turrem.materials.metallurgy.metals;

public class Lead extends Metal
{
	public Lead(int pass)
	{
		super(pass);
	}

	@Override
	public int getLevel()
	{
		return 0;
	}

	@Override
	public String getMetalName()
	{
		return "Lead";
	}
}
