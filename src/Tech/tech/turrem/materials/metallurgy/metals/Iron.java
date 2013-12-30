package tech.turrem.materials.metallurgy.metals;

public class Iron extends Metal
{
	public Iron(int pass)
	{
		super(pass);
	}

	@Override
	public String getMetalName()
	{
		return "Iron";
	}

	@Override
	public int getLevel()
	{
		return 2;
	}
}
