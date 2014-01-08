package zap.turrem.unit.tech;

import zap.turrem.core.tech.TechBase;
import zap.turrem.core.tech.branch.BranchUnitTech;
import zap.turrem.core.tech.item.TechItem;
import zap.turrem.unit.Unit;

public class TechUnit extends TechBase
{
	private Unit unit;

	public TechUnit(Unit theUnit)
	{
		this.unit = theUnit;
	}

	@Override
	public boolean isEntryLevel(int pass)
	{
		return this.unit.isStarting();
	}

	@Override
	public void loadBranches(int pass)
	{
		TechItem[] items = this.unit.getRequiredTechs();
		if (items != null)
		{
			BranchUnitTech branch = new BranchUnitTech(0);
			for (int i = 0; i < items.length; i++)
			{
				branch.addRequired(items[i].getId());
			}
			branch.push();
		}
	}

	@Override
	public int getPassCount()
	{
		return 1;
	}

	@Override
	public String getName(int pass)
	{
		return this.unit.getName();
	}

	public int push()
	{
		TechItem item = this.buildItem(0);
		item.push();
		return item.getId();
	}
}
