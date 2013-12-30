package tech.turrem.materials.metallurgy.metals;

public class Brass extends Alloy
{
	public Brass(int pass)
	{
		super(pass);
	}

	@Override
	public String getMetalName()
	{
		return "Brass";
	}
	
	public String[] getAlloy()
	{
		return new String[] {this.getIdent(Copper.class), this.getIdent(Zinc.class)};
	}

	@Override
	public int getLevel()
	{
		return 2;
	}
}
