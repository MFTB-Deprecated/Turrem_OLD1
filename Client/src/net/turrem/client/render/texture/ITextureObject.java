package net.turrem.client.render.texture;

import net.turrem.client.asset.AssetLoader;

public interface ITextureObject
{
	public String getName();

	public int bind(AssetLoader assets);
	
	int rebind(AssetLoader assets);

	public void unbind();

	void start();

	void end();

	float getAspect();

	int getHeight();

	int getWidth();
}
