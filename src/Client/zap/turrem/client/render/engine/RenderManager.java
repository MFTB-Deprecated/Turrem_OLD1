package zap.turrem.client.render.engine;

import java.util.ArrayList;

public class RenderManager
{
	private ArrayList<RenderObjectHolder> holders = new ArrayList<RenderObjectHolder>();

	private void doTickHolders()
	{
		for (int i = 0; i < this.holders.size(); i++)
		{
			RenderObjectHolder holder = this.holders.get(i);

			if (holder != null)
			{
				holder.tickLoad();
			}
		}
	}
}
