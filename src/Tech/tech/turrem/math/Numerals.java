package tech.turrem.math;

import tech.turrem.language.WrittenLanguage;
import branch.turrem.BranchAvailable;

public class Numerals extends MathmaticalTech
{
	@Override
	public void loadBranches(int pass)
	{
		(new BranchAvailable(this, pass)).addRequired(WrittenLanguage.class, 1).push();
	}

	@Override
	public String getName(int pass)
	{
		return "Numerals";
	}

	@Override
	public boolean isEntryLevel(int pass)
	{
		return false;
	}

	@Override
	public int getPassCount()
	{
		return 1;
	}
}
