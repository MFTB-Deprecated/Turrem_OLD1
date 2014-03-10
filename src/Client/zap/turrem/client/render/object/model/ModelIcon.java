package zap.turrem.client.render.object.model;

import java.io.IOException;

import zap.turrem.client.asset.AssetLoader;
import zap.turrem.client.render.engine.holders.IRenderObjectHolder;
import zap.turrem.client.render.engine.holders.RenderObjectHolder;
import zap.turrem.utils.models.TVFFile;

public class ModelIcon
{
	private int heldIndex = -1;
	public final String source;
	private IRenderObjectHolder holder;
	private boolean held = false;
	private boolean isLoaded = false;
	private int engineIndex;
	public float scale;
	public float xoff = 0.0F;
	public float yoff = 0.0F;
	public float zoff = 0.0F;

	public ModelIcon(String model, float scale)
	{
		this.source = model;
		this.scale = scale;
	}
	
	public ModelIcon(String model, float scale, float xoff, float yoff, float zoff)
	{
		this(model, scale);
		this.xoff = xoff;
		this.yoff = yoff;
		this.zoff = zoff;
	}

	@Override
	public boolean equals(Object o)
	{
		return true;
	}

	public int getHeldIndex()
	{
		return this.heldIndex;
	}

	public void setHolder(IRenderObjectHolder holder, int index)
	{
		this.holder = holder;
		this.heldIndex = index;
		this.held = true;
	}

	public IRenderObjectHolder getHolder()
	{
		return this.holder;
	}

	public void setLoadImportant()
	{
		if (this.held)
		{
			if (this.holder instanceof RenderObjectHolder)
			{
				((RenderObjectHolder) this.holder).setImportant(this);
			}
		}
	}

	public void loadMe()
	{
		if (this.held)
		{
			this.holder.load();
		}
	}

	public void setLoaded()
	{
		this.isLoaded = true;
	}

	public void setUnloaded()
	{
		this.isLoaded = false;
	}

	public boolean isLoaded()
	{
		return this.isLoaded;
	}

	public void setEngineIndex(int x)
	{
		this.engineIndex = x;
	}

	public int getEngineId()
	{
		return this.engineIndex;
	}

	public void render()
	{
		if (this.isLoaded)
		{
			this.holder.renderObject(this.engineIndex);
		}
	}

	public String getName()
	{
		return this.source;
	}

	public TVFFile makeTVF(AssetLoader assets)
	{
		try
		{
			return assets.loadTVF(this.getName());
		}
		catch (IOException e)
		{
			return null;
		}
	}
}