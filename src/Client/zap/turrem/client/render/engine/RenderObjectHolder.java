package zap.turrem.client.render.engine;

import java.util.ArrayList;

import zap.turrem.client.render.object.model.ModelIcon;

public abstract class RenderObjectHolder
{
	private ArrayList<ModelIcon> holder = new ArrayList<ModelIcon>();
	private boolean lockHolder = false;
	private int[] donext = new int[0];
	private boolean working = false;
	private boolean[] loaded;
	private int numloaded = 0;
	private ArrayList<Integer> important;
	private RenderEngine theEngine;
	private boolean isloaded = false;

	private void doLoad()
	{
		if (!this.isloaded)
		{
			this.isloaded = true;
			this.working = true;
			this.lockHolder = true;
			this.numloaded = 0;
			if (this.theEngine == null)
			{
				this.theEngine = new RenderEngine();
			}
			this.loaded = new boolean[this.holder.size()];
			if (this.important != null && !this.important.isEmpty())
			{
				this.donext = new int[this.important.size()];
				for (int i = 0; i < this.donext.length; i++)
				{
					this.donext[i] = this.important.get(i);
				}
				this.important.clear();
			}
			else
			{
				this.makeNext();
			}
		}
	}

	public void forceLoad()
	{
		if (!this.isloaded)
		{
			this.isloaded = true;
			this.working = true;
			this.lockHolder = true;
			this.numloaded = 0;
			while (this.numloaded < this.holder.size())
			{
				this.loadObject(this.holder.get(this.numloaded));
				this.numloaded++;
			}
			this.working = false;
			this.lockHolder = false;
		}
	}

	public void forceReload()
	{
		if (this.isloaded)
		{
			this.doUnLoad();
		}
		this.forceLoad();
	}

	public void tickLoad()
	{
		if (this.working)
		{
			for (int i = 0; i < this.donext.length; i++)
			{
				int j = this.donext[i];
				this.loaded[j] = true;
				this.loadObject(this.holder.get(j));
				this.numloaded++;
			}
			if (this.numloaded == this.holder.size())
			{
				this.working = false;
				this.lockHolder = false;
				this.loaded = null;
			}
			else
			{
				this.makeNext();
			}
		}
	}

	protected abstract void makeNext();

	protected void loadObject(ModelIcon icon)
	{

	}

	private void doUnLoad()
	{
		if (this.isloaded)
		{
			this.isloaded = false;
			this.numloaded = 0;
			this.theEngine.wipeAll();
		}
	}
}
