package zap.turrem.tech;

import java.util.ArrayList;
import java.util.Random;

import zap.turrem.tech.branch.Branch;
import zap.turrem.tech.branch.BranchList;
import zap.turrem.tech.list.TechList;

public class TechTester
{
	public static ArrayList<Integer> techs = new ArrayList<Integer>();

	public static int ticks = 0;

	public static Random rand = new Random();

	public static void tick()
	{
		ArrayList<Integer> branches = new ArrayList<Integer>();

		for (int i = 0; i < BranchList.branchCount(); i++)
		{
			Branch b = BranchList.get(i);

			boolean brflag = true;

			int[] req = b.getRequiredTechs();

			for (int j = 0; j < req.length; j++)
			{
				int t = req[j];

				boolean tflag = false;

				for (int k = 0; k < techs.size(); k++)
				{
					tflag |= (((int) techs.get(k)) == t);
				}

				brflag &= tflag;
			}

			if (brflag)
			{
				boolean tflag = false;
				int t = b.getTechs()[0];

				for (int k = 0; k < techs.size(); k++)
				{
					tflag |= (((int) techs.get(k)) == t);
				}

				if (!tflag)
				{
					branches.add(b.getId());
				}
			}
		}

		if (branches.isEmpty())
		{
			return;
		}
		Branch b = BranchList.get(branches.get(rand.nextInt(branches.size())));
		int[] req = b.getRequiredTechs();
		int newTechs = b.getTechs()[0];

		String print = ticks + ":";

		if (req.length > 0)
		{
			for (int j = 0; j < req.length; j++)
			{
				if (j == 0)
				{
					print += " ";
				}
				else
				{
					print += " + ";
				}
				print += "[" + TechList.get(req[j]).getName() + "]";
			}

			print += " ->";
		}

		print += " ";
		print += "[" + TechList.get(newTechs).getName() + "]";

		System.out.println(print);

		ticks++;

		techs.add(newTechs);
	}
}
