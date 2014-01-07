package tech.turrem.materials.metallurgy.metals;

import tech.turrem.materials.metallurgy.Metallurgy;
import zap.turrem.tech.Tech;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.Branch;
import branch.turrem.BranchAvailable;

public abstract class Alloy extends Metal
{
	public abstract String[] getAlloy();

	@Override
	public void loadBranches(int pass)
	{
		if (pass == 0)
		{
			Branch b = new BranchAvailable(this, pass);
			String[] alloy = this.getAlloy();
			for (int i = 0; i < alloy.length; i++)
			{
				b.addRequired(alloy[i]);
			}
			b.addRequired(Metallurgy.class, 2);
			b.push();
		}
		else
		{
			super.loadBranches(pass);
		}
	}

	public boolean isAlloy()
	{
		return true;
	}
	
	public String getIdent(Class<? extends Metal> metal)
	{
		return Tech.getClassIdentifier(metal, 1);
	}
	
	public String getIdent(Class<? extends TechBase> tech, int pass)
	{
		return  Tech.getClassIdentifier(tech, pass);
	}
}
