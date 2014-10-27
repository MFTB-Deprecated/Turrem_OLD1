package net.turrem.app.client.render.object;

import net.turrem.app.client.render.RenderEngine;

public interface IRenderObject
{
	public String getSource();
	
	public String getIdentifier();
	
	public boolean isLoaded();
	
	public boolean load(RenderEngine render);
	
	public boolean reload(RenderEngine render);
	
	public boolean unload(RenderEngine render);
}
