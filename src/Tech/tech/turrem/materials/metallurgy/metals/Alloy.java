package tech.turrem.materials.metallurgy.metals;

import tech.turrem.materials.metallurgy.Metallurgy;
import zap.turrem.loadable.JarLoader;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.Branch;
import branch.turrem.BranchAvailable;

public abstract class Alloy extends Metal
{
	public Alloy(int pass)
	{
		super(pass);
	}

	public abstract String[] getAlloy();

	@Override
	public void loadBranches()
	{
		super.loadBranches();
		if (this.pass == 0)
		{
			Branch b = new BranchAvailable(this);
			String[] alloy = this.getAlloy();
			for (int i = 0; i < alloy.length; i++)
			{
				b.addRequired(alloy[i]);
			}
			b.addRequired(Metallurgy.class, 2);
			b.push();
		}
	}

	public boolean isAlloy()
	{
		return true;
	}
	
	public String getIdent(Class<? extends Metal> metal)
	{
		return JarLoader.getStaticIdentifier(metal, 1);
	}
	
	public String getIdent(Class<? extends TechBase> tech, int pass)
	{
		return JarLoader.getStaticIdentifier(tech, pass);
	}
}
