package net.turrem.app.client.render.icon;

import net.turrem.app.client.render.RenderEngine;

public class ShaderIcon implements IRenderIcon
{
	private final String name;
	
	public ShaderIcon(String shader)
	{
		this.name = shader;
	}
	
	@Override
	public String getSource()
	{
		return this.name;
	}
	
	@Override
	public String getIdentifier()
	{
		return this.name;
	}
	
	@Override
	public void load(RenderEngine engine)
	{
	}
}
