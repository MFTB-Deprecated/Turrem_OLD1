package tech.turrem.materials.metallurgy.metals;

public class Bronze extends Alloy
{
	public Bronze(int pass)
	{
		super(pass);
	}
	
	public String[] getAlloy()
	{
		return new String[] {this.getIdent(Copper.class), this.getIdent(Tin.class)};
	}

	@Override
	public String getMetalName()
	{
		return "Bronze";
	}

	@Override
	public int getLevel()
	{
		return 3;
	}
}
