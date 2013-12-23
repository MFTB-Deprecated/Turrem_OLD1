package tech.turrem.math;

import tech.turrem.language.WrittenLanguage;
import zap.turrem.tech.branch.BranchAvailable;

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
