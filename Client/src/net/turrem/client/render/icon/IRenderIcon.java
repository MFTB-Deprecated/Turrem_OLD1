package net.turrem.client.render.icon;

import net.turrem.client.render.RenderEngine;

public interface IRenderIcon
{
	public String getSource();
	
	public String getIdentifier();
	
	public void load(RenderEngine engine);
}
