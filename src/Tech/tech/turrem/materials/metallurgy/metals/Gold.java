package tech.turrem.materials.metallurgy.metals;

public class Gold extends Metal
{
	public Gold(int pass)
	{
		super(pass);
	}

	@Override
	public String getMetalName()
	{
		return "Gold";
	}

	@Override
	public int getLevel()
	{
		return 0;
	}
}
