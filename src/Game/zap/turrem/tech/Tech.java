package zap.turrem.tech;

public abstract class Tech
{
	public abstract int getPassCount();
	
	public abstract String getName(int pass);
	
	public final String getIdentifier(int pass)
	{
		String name = this.getClass().getName();
		if (pass > 0 || this.getPassCount() > 1)
		{
			name += "#" + pass;
		}
		return name;
	}
}
