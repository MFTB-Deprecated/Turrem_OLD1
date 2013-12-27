package tech.turrem.math;

import branch.turrem.BranchAvailable;
import tech.turrem.language.WrittenLanguage;

public class Numerals extends MathmaticalTech
{
	public Numerals(int pass)
	{
		super(pass);
	}

	@Override
	public void loadBranches()
	{
		(new BranchAvailable(this)).addRequired(WrittenLanguage.class, 1).push();
	}

	@Override
	public String getName()
	{
		return "Numerals";
	}
}
