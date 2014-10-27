package net.turrem.app.client.render.object;

import net.turrem.app.client.Config;
import net.turrem.app.client.render.RenderEngine;

public class RenderObjectTVF implements IRenderObject
{
	private String source;
	private String identifier;
	private RenderTVF tvf = null;
	
	private float scale;
	
	public RenderObjectTVF(String source, String identifier, float scale)
	{
		this.source = source;
		this.scale = scale;
		this.identifier = identifier;
	}
	
	public void render()
	{
		if (this.tvf != null)
		{
			this.tvf.doRender();
		}
	}
	
	@Override
	public boolean isLoaded()
	{
		return this.tvf != null;
	}
	
	@Override
	public boolean load(RenderEngine render)
	{
		if (this.tvf != null)
		{
			return false;
		}
		this.tvf = render.loadTVFRender(this.source, this.scale, Config.doAO);
		return true;
	}
	
	@Override
	public boolean reload(RenderEngine render)
	{
		this.unload(render);
		this.tvf = render.loadTVFRender(this.source, this.scale, Config.doAO);
		return true;
	}
	
	@Override
	public boolean unload(RenderEngine render)
	{
		if (this.tvf != null)
		{
			this.tvf.delete();
			this.tvf = null;
			return true;
		}
		return false;
	}
	
	@Override
	public String getSource()
	{
		return this.source;
	}
	
	@Override
	public String getIdentifier()
	{
		return this.identifier;
	}
}
