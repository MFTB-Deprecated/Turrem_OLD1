package zap.turrem.tech;

/**
 * Make sure that if you extend this, you have a constructor with a single int for parameters
 * @author Sam Sartor
 */
public abstract class TechBase extends Tech
{
	protected int pass;
	
	public TechBase(int pass)
	{
		this.pass = pass;
	}
	
	public final int getId()
	{
		return TechList.getIndex(this);
	}
	
	public final String getIdentifier()
	{
		return this.getIdentifier(this.pass);
	}
	
	public abstract String getName();
}
