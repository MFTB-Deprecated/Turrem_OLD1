package zap.turrem.client.render.engine.holders;

import zap.turrem.client.render.engine.RenderManager;

public class RenderObjectHolderSimple extends RenderObjectHolder
{
	private int current;
	
	public RenderObjectHolderSimple(RenderManager manager, int index, String name)
	{
		super(manager, index, name);
	}

	@Override
	protected void makeNext()
	{
		this.donext = new int[1];
		do
		{
			if (this.loaded[this.current])
			{
				this.current++;
			}
			else
			{
				this.donext[0] = this.current;
				this.current++;
				break;
			}
		}
		while (this.current < this.numHeld());
	}

	@Override
	protected void onStartLoad()
	{
		this.current = 0;
	}

	@Override
	protected void onEndLoad()
	{
	}
}
