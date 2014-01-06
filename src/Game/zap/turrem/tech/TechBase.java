package zap.turrem.tech;

public abstract class TechBase extends Tech
{
	public abstract boolean isStarting(int pass);
	
	public abstract void loadBraches(int pass);
	
	public void loadAllBranches(int pass)
	{
		this.loadBraches(pass);
		if (this.isStarting(pass))
		{
			
		}
	}
}
