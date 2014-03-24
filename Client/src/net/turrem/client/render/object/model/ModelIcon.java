package net.turrem.client.render.object.model;

import java.io.IOException;

import net.turrem.client.asset.AssetLoader;
import net.turrem.client.render.engine.RenderManager;
import net.turrem.client.render.engine.RenderStore;
import net.turrem.utils.models.TVFFile;

public class ModelIcon
{
	private int index = -1;
	public final String source;
	private boolean isLoaded = false;
	private RenderStore engine;
	public float size;
	public float xoff = 0.0F;
	public float yoff = 0.0F;
	public float zoff = 0.0F;

	public ModelIcon(String model, float size)
	{
		this.source = model;
		this.size = size;
	}

	public ModelIcon(String model, float size, float xoff, float yoff, float zoff)
	{
		this(model, size);
		this.xoff = xoff;
		this.yoff = yoff;
		this.zoff = zoff;
	}

	@Override
	public boolean equals(Object o)
	{
		return true;
	}

	public boolean isLoaded()
	{
		return this.isLoaded;
	}

	public void render()
	{
		if (this.isLoaded)
		{
			this.engine.doRenderObject(this.index);
		}
	}

	public String getName()
	{
		return this.source;
	}

	public void onpush(int index, RenderStore engine, boolean loaded)
	{
		this.engine = engine;
		this.index = index;
		this.isLoaded = loaded;
	}

	public boolean load(RenderManager man)
	{
		if (man.assets == null)
		{
			return false;
		}
		return this.load(man.assets);
	}
	public boolean load(AssetLoader assets)
	{
		if (this.isLoaded)
		{
			return true;
		}
		if (this.engine == null)
		{
			return false;
		}
		TVFFile tvf = this.makeTVF(assets);
		if (tvf == null)
		{
			return false;
		}
		this.isLoaded = true;
		float scale = (tvf.height & 0xFF) / this.size;
		this.engine.addObject(this, tvf, scale, this.xoff * (tvf.width & 0xFF), this.yoff * (tvf.height & 0xFF), this.zoff * (tvf.length & 0xFF));
		return true;
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