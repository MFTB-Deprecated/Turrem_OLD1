package zap.turrem.client.render.texture;

import zap.turrem.client.asset.AssetLoader;

public interface ITextureObject
{
	public String getName();
	
	public int bind(AssetLoader assets);
	
	public void unbind();

	void start();

	void end();
}
