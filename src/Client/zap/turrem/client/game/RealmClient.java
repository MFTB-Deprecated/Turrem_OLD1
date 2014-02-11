package zap.turrem.client.game;

import java.util.ArrayList;
import java.util.Random;

import zap.turrem.core.player.Player;
import zap.turrem.core.realm.Realm;
import zap.turrem.core.tech.branch.Branch;
import zap.turrem.core.tech.branch.BranchList;
import zap.turrem.core.tech.item.TechItem;
import zap.turrem.core.tech.list.TechList;

public class RealmClient extends Realm
{
	public ArrayList<Integer> techs = new ArrayList<Integer>();
	public ArrayList<Integer> branches = new ArrayList<Integer>();
	
	public Random rand = new Random();

	public RealmClient(Player thePlayer)
	{
		super(thePlayer);
	}
	
	public void onStart()
	{
		this.findTechBranches();
	}

	public void gainTech(TechItem tech, boolean override)
	{
		if (this.canGain(tech) || override)
		{
			this.techs.add(tech.getId());
			System.out.println("Gained " + tech.getName() + " [" + tech.getIdentifier() + "]");
		}
		this.findTechBranches();
	}
	
	public void tickTechs()
	{
		ArrayList<TechItem> gained = new ArrayList<TechItem>();
		for (int bid : this.branches)
		{
			Branch b = BranchList.get(bid);
			if (b.sudden())
			{
				gained.add(TechList.get(b.getTechs()[0]));
			}
			//else if (b.random() && this.rand.nextInt(1000) == 0)
			//{
			//	gained.add(TechList.get(b.getTechs()[0]));
			//}
		}		
		for (TechItem tech : gained)
		{
			this.gainTech(tech, false);
		}
	}

	public boolean canGain(TechItem tech)
	{
		int id = tech.getId();
		boolean flag = false;
		for (int bid : this.branches)
		{
			Branch b = BranchList.get(bid);
			if (b.getTechs()[0] == id)
			{
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

				flag |= brflag;
			}
		}
		return flag;
	}

	public void findTechBranches()
	{
		branches.clear();
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
	}
}
