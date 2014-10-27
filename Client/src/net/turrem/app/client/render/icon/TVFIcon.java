package net.turrem.app.client.render.icon;

import net.turrem.app.client.render.RenderEngine;
import net.turrem.app.client.render.object.RenderObjectTVF;

import org.lwjgl.opengl.GL11;

public class TVFIcon extends RenderIcon3D
{
	private final String name;
	public final float scale;
	private final float xoffset;
	private final float yoffset;
	private final float zoffset;
	public RenderObjectTVF object;
	
	public TVFIcon(String name, float scale, float xoffset, float yoffset, float zoffset)
	{
		this.name = name;
		this.scale = scale;
		this.xoffset = xoffset / scale;
		this.yoffset = yoffset / scale;
		this.zoffset = zoffset / scale;
	}
	
	public boolean loaded()
	{
		return this.object != null && this.object.isLoaded();
	}
	
	@Override
	public void load(RenderEngine engine)
	{
		this.object = engine.renderStoreTVF.loadObject(this);
	}
	
	@Override
	public String getSource()
	{
		return this.name;
	}
	
	@Override
	public String getIdentifier()
	{
		return this.name + String.format("[%.2f", this.scale);
	}
	
	@Override
	public void render()
	{
		if (this.loaded())
		{
			GL11.glTranslatef(-this.xoffset, -this.yoffset, -this.zoffset);
			this.object.render();
			GL11.glTranslatef(this.xoffset, this.yoffset, this.zoffset);
		}
	}
}
