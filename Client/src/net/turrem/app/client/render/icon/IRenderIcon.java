package net.turrem.app.client.render.icon;

import net.turrem.app.client.render.RenderEngine;

public interface IRenderIcon
{
	public String getSource();
	
	public String getIdentifier();
	
	public void load(RenderEngine engine);
}
