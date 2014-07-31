package net.turrem.client.render;

import java.util.HashMap;
import java.util.Map;

import net.turrem.client.render.object.IRenderObject;
import net.turrem.client.render.object.RenderObjectTexture;

public class RenderStoreTexture implements IRenderStore
{
	protected RenderEngine theEngine;
	private HashMap<String, RenderObjectTexture> objects = new HashMap<String, RenderObjectTexture>();

	public RenderStoreTexture(RenderEngine render)
	{
		this.theEngine = render;
	}

	@Override
	public void unloadAll()
	{
		for (RenderObjectTexture tex : this.objects.values())
		{
			tex.unload(this.theEngine);
		}
		this.objects.clear();
	}

	@Override
	public int size()
	{
		return this.objects.size();
	}

	@Override
	public Map<String, IRenderObject> getMap()
	{
		return this.getMap();
	}

	@Override
	public RenderObjectTexture getObject(String name)
	{
		return this.objects.get(name);
	}

	@Override
	public RenderObjectTexture unloadObject(String name)
	{
		RenderObjectTexture obj = this.objects.get(name);
		if (obj != null)
		{
			obj.unload(this.theEngine);
			this.objects.remove(name);
		}
		return obj;
	}

	@Override
	public RenderObjectTexture loadObject(String name)
	{
		RenderObjectTexture obj = this.objects.get(name);
		if (obj == null)
		{
			obj = new RenderObjectTexture(name);
			this.objects.put(name, obj);
		}
		if (!obj.isLoaded())
		{
			obj.load(this.theEngine);
		}
		return obj;
	}

	@Override
	public RenderObjectTexture reloadObject(String name)
	{
		RenderObjectTexture obj = this.objects.get(name);
		if (obj == null)
		{
			obj = new RenderObjectTexture(name);
			this.objects.put(name, obj);
		}
		obj.reload(this.theEngine);
		return obj;
	}

	@Override
	public void reloadAll(boolean loadnew)
	{
		for (RenderObjectTexture tex : this.objects.values())
		{
			if (tex.isLoaded() || loadnew)
			{
				tex.reload(this.theEngine);
			}
		}
	}
}
