package net.turrem.app.client.render;

import java.util.HashMap;
import java.util.Map;

import net.turrem.app.client.render.icon.IRenderIcon;
import net.turrem.app.client.render.object.IRenderObject;

public abstract class RenderStore<T extends IRenderObject> implements IRenderStore
{
	protected RenderEngine theEngine;
	private HashMap<String, T> objects = new HashMap<String, T>();
	
	public RenderStore(RenderEngine render)
	{
		this.theEngine = render;
	}
	
	@Override
	public void unloadAll()
	{
		for (T obj : this.objects.values())
		{
			obj.unload(this.theEngine);
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
	public T getObject(IRenderIcon ico)
	{
		return this.objects.get(ico.getIdentifier());
	}
	
	@Override
	public T unloadObject(IRenderIcon ico)
	{
		String name = ico.getIdentifier();
		T obj = this.objects.get(name);
		if (obj != null)
		{
			obj.unload(this.theEngine);
			this.objects.remove(name);
		}
		return obj;
	}
	
	@Override
	public T loadObject(IRenderIcon ico)
	{
		String name = ico.getIdentifier();
		T obj = this.objects.get(name);
		if (obj == null)
		{
			obj = this.createObject(ico);
			if (obj == null)
			{
				return null;
			}
			this.objects.put(name, obj);
		}
		if (!obj.isLoaded())
		{
			obj.load(this.theEngine);
		}
		return obj;
	}
	
	public abstract T createObject(IRenderIcon ico);
	
	@Override
	public T reloadObject(IRenderIcon ico)
	{
		String name = ico.getIdentifier();
		T obj = this.objects.get(name);
		if (obj == null)
		{
			obj = this.createObject(ico);
			if (obj == null)
			{
				return null;
			}
			this.objects.put(name, obj);
		}
		obj.reload(this.theEngine);
		return obj;
	}
	
	@Override
	public void reloadAll(boolean loadnew)
	{
		for (T obj : this.objects.values())
		{
			if (obj.isLoaded() || loadnew)
			{
				obj.reload(this.theEngine);
			}
		}
	}
}
