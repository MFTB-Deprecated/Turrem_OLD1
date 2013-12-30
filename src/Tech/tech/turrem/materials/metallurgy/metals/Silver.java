package tech.turrem.materials.metallurgy.metals;

public class Silver extends Metal
{
	public Silver(int pass)
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
		return "Silver";
	}
}
