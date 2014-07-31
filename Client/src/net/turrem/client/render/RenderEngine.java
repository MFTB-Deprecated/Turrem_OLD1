package net.turrem.client.render;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.turrem.client.asset.AssetLoader;
import net.turrem.client.render.object.RenderObjectTexture;

public class RenderEngine
{
	public AssetLoader assets;
	
	public RenderEngine(AssetLoader assets)
	{
		this.assets = assets;
	}
	
	public BufferedImage loadTexture(RenderObjectTexture object)
	{
		if(object.isLoaded())
		{
			return null;
		}
		try
		{
			return this.assets.loadTexture(object.getPath());
		}
		catch (IOException e)
		{
			return null;
		}
	}
}
