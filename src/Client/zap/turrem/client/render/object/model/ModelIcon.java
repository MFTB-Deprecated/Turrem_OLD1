package zap.turrem.client.render.object.model;

import zap.turrem.client.render.engine.RenderObjectHolder;

public class ModelIcon
{
	private int heldIndex = -1;
	private RenderObjectHolder holder;
	private boolean held = false;

	@Override
	public boolean equals(Object o)
	{
		return true;
	}

	public int getHeldIndex()
	{
		return this.heldIndex;
	}

	public void setHolder(RenderObjectHolder holder, int index)
	{
		this.holder = holder;
		this.heldIndex = index;
		this.held = true;
	}

	public RenderObjectHolder getHolder()
	{
		return this.holder;
	}

	public void loadMeASAP()
	{
		if (this.held)
		{
			this.holder.setImportant(this);
			this.loadMe();
		}
	}

	public void loadMe()
	{
		if (this.held)
		{
			this.holder.load();
		}
	}
}