package zap.turrem.client.render.engine.holders;

import java.util.ArrayList;

import zap.turrem.client.render.engine.RenderEngine;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.utils.models.TVFFile;

public abstract class RenderObjectHolder implements IRenderObjectHolder
{
	public final RenderManager myManager;
	public final int managedAt;
	public final String managedName;
	private ArrayList<ModelIcon> holder = new ArrayList<ModelIcon>();
	private boolean lockHolder = false;
	protected int[] donext = new int[0];
	private boolean working = false;
	protected boolean[] loaded;
	private int numloaded = 0;
	private ArrayList<Integer> important;
	private RenderEngine theEngine;
	private boolean isloaded = false;

	public RenderObjectHolder(RenderManager manager, int index, String name)
	{
		this.myManager = manager;
		this.managedAt = index;
		this.managedName = name;
	}

	private void doLoad()
	{
		if (!this.working)
		{
			if (!this.isloaded)
			{
				this.isloaded = true;
				this.working = true;
				this.lockHolder = true;
				this.myManager.working++;
				this.numloaded = 0;
				if (this.theEngine == null)
				{
					this.theEngine = new RenderEngine();
				}
				this.loaded = new boolean[this.holder.size()];
				this.onStartLoad();
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
	}

	@Override
	public void load()
	{
		this.doLoad();
	}

	@Override
	public int numHeld()
	{
		return this.holder.size();
	}

	@Override
	public void forceLoad()
	{
		if (!this.isloaded)
		{
			this.isloaded = true;
			this.working = true;
			this.lockHolder = true;
			this.numloaded = 0;
			this.onStartLoad();
			while (this.numloaded < this.holder.size())
			{
				this.loadObject(this.holder.get(this.numloaded));
				this.numloaded++;
			}
			this.working = false;
			this.lockHolder = false;
			this.onEndLoad();
		}
	}

	@Override
	public void forceReload()
	{
		if (this.isloaded)
		{
			this.doUnload();
		}
		this.forceLoad();
	}

	@Override
	public boolean handModel(ModelIcon model)
	{
		if (this.lockHolder)
		{
			return false;
		}
		int i = this.holder.size();
		if (this.holder.add(model))
		{
			model.setHolder(this, i);
		}
		return true;
	}

	public void setImportant(ModelIcon model)
	{
		this.important.add(model.getHeldIndex());
	}

	@Override
	public void tickLoad()
	{
		if (this.working)
		{
			for (int i = 0; i < this.donext.length; i++)
			{
				int j = this.donext[i];
				if (!this.loaded[j])
				{
					this.loaded[j] = true;
					this.loadObject(this.holder.get(j));
					this.numloaded++;
				}
			}
			if (this.numloaded == this.holder.size())
			{
				this.working = false;
				this.lockHolder = false;
				this.loaded = null;
				this.myManager.working--;
				this.onEndLoad();
			}
			else
			{
				this.makeNext();
			}
		}
	}

	protected abstract void makeNext();

	protected abstract void onStartLoad();

	protected abstract void onEndLoad();

	protected void loadObject(ModelIcon icon)
	{
		TVFFile file = icon.makeTVF(this.myManager.assets);
		if (file != null)
		{
			int rei = this.theEngine.addObject(file, icon.scale, icon.xoff, icon.yoff, icon.zoff).getEngineIndex();
			icon.setLoaded();
			icon.setEngineIndex(rei);
		}
	}

	@Override
	public void renderObject(int engineid)
	{
		if (this.isloaded)
		{
			this.theEngine.doRenderObject(engineid);
		}
	}

	@Override
	public void renderObject(ModelIcon icon)
	{
		if (icon.isLoaded())
		{
			this.renderObject(icon.getEngineId());
		}
	}

	private void doUnload()
	{
		if (!this.working)
		{
			if (this.isloaded)
			{
				this.isloaded = false;
				this.numloaded = 0;
				this.theEngine.wipeAll();
				for (int i = 0; i < this.holder.size(); i++)
				{
					this.holder.get(i).setUnloaded();
				}
			}
		}
	}

	@Override
	public void unload()
	{
		this.doUnload();
	}

	@Override
	public String getIdentifier()
	{
		return this.managedName;
	}

	@Override
	public int getIndex()
	{
		return this.managedAt;
	}
}
