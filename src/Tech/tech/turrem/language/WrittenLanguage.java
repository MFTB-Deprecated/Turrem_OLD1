package tech.turrem.language;

import tech.turrem.math.Numerals;
import tech.turrem.tools.StoneTools;
import tech.turrem.writing.ClayTablet;
import zap.turrem.tech.TechBase;
import zap.turrem.tech.branch.BranchAvailable;
import zap.turrem.tech.branch.BranchSudden;

public class WrittenLanguage extends TechBase
{
	public WrittenLanguage(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		this.addBranch(new BranchAvailable(this.getIndex(ClayTablet.class, 0), this.getIndex(StoneTools.class, 2)));
		if (this.pass == 0)
		{
			this.addBranch(new BranchSudden(this.getIndex(WrittenLanguage.class, 1)));
		}
		if (this.pass == 1)
		{
			this.addBranch(new BranchAvailable(this.getIndex(Numerals.class, 0)));
			this.addBranch(new BranchAvailable(this.getIndex(SpokenLanguage.class, 2)));
			this.addBranch(new BranchAvailable(this.getIndex(WrittenLanguage.class, 2)));
		}
		if (this.pass == 2)
		{
			this.addBranch(new BranchAvailable(this.getIndex(WrittenLanguage.class, 3)));
		}
		if (this.pass == 3)
		{
			this.addBranch(new BranchAvailable(this.getIndex(WrittenLanguage.class, 4)));
		}
	}

	public static int numPass()
	{
		return 5;
	}

	@Override
	public String getName()
	{
		switch (this.pass)
		{
			case 0:
				return "Pictograms";
			case 1:
				return "Ideograms";
			case 2:
				return "Phoneme";
			case 3:
				return "Alphabet";
			case 4:
				return "Script";
			default:
				return "Written Language";
		}
	}
}
