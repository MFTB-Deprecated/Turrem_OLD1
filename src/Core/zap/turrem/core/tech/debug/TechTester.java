package zap.turrem.core.tech.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import zap.turrem.core.tech.branch.Branch;
import zap.turrem.core.tech.branch.BranchList;
import zap.turrem.core.tech.list.TechList;
import zap.turrem.utils.data.IntHistogram;

/**
 * Util to help debug the tech tree
 */
public class TechTester
{
	public ArrayList<Integer> techs;
	public HashMap<Integer, IntHistogram> histo = new HashMap<Integer, IntHistogram>();

	public int ticks;

	public Random rand = new Random();

	public void newRun()
	{
		this.techs = new ArrayList<Integer>();
		this.ticks = 0;
		this.rand = new Random();
	}

	private boolean tick(boolean output)
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

				for (int k = 0; k < this.techs.size(); k++)
				{
					tflag |= ((this.techs.get(k)) == t);
				}

				brflag &= tflag;
			}

			if (brflag)
			{
				boolean tflag = false;
				int t = b.getTechs()[0];

				for (int k = 0; k < this.techs.size(); k++)
				{
					tflag |= ((this.techs.get(k)) == t);
				}

				if (!tflag)
				{
					branches.add(b.getId());
				}
			}
		}

		if (branches.isEmpty())
		{
			return false;
		}
		Branch b = BranchList.get(branches.get(this.rand.nextInt(branches.size())));
		int[] req = b.getRequiredTechs();
		int newTechs = b.getTechs()[0];

		if (output)
		{
			String print = this.ticks + ":";

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
		}

		this.log(newTechs, this.ticks);

		this.ticks++;

		this.techs.add(newTechs);

		return true;
	}

	private void log(int tech, int tick)
	{
		if (!this.histo.containsKey(tech))
		{
			this.histo.put(tech, new IntHistogram());
		}
		this.histo.get(tech).add(tick);
	}

	public void run(boolean output)
	{
		this.newRun();
		while (this.tick(output))
		{
			;
		}
	}

	public void runMulti(int times, boolean disp)
	{
		for (int i = 0; i < times; i++)
		{
			this.run(i == 0 && disp);
		}
	}

	public IntHistogram getHist(int tech)
	{
		if (this.histo.containsKey(tech))
		{
			return this.histo.get(tech);
		}
		return null;
	}

	public int getRandom()
	{
		return this.techs.get(this.rand.nextInt(this.techs.size()));
	}
}
