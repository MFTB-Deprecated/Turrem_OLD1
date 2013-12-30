package tech.turrem.materials.metallurgy.metals;

public class Zinc extends Metal
{
	public Zinc(int pass)
	{
		super(pass);
	}

	@Override
	public String getMetalName()
	{
		return "Zinc";
	}

	@Override
	public int getLevel()
	{
		return 1;
	}
}
