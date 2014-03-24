package net.turrem.client.render.engine;

import java.util.ArrayList;
import java.util.HashMap;

import net.turrem.client.asset.AssetLoader;
import net.turrem.client.render.object.model.ModelIcon;
import net.turrem.client.render.texture.ITextureObject;
import net.turrem.client.render.texture.TextureObject;

public class RenderManager
{
	private ArrayList<RenderStore> holders = new ArrayList<RenderStore>();
	private HashMap<String, ITextureObject> textures = new HashMap<String, ITextureObject>();
	public int working = 0;
	public AssetLoader assets;

	public RenderManager(AssetLoader assets)
	{
		this.assets = assets;
	}

	public ITextureObject addTexture(String name)
	{
		if (!this.textures.containsKey(name))
		{
			TextureObject txt = new TextureObject(name);
			this.textures.put(name, txt);
		}

		return this.textures.get(name);
	}

	public RenderStore getHolder(String identifier)
	{
		for (int i = 0; i < this.holders.size(); i++)
		{
			RenderStore h = this.holders.get(i);
			if (h.getIdentifier().equals(identifier))
			{
				return h;
			}
		}
		return null;
	}
	
	public RenderEngine getHolder(int index)
	{
		return this.holders.get(index);
	}

	public void pushIcon(ModelIcon icon, String holder)
	{
		RenderStore h = this.getHolder(holder);

		if (h == null)
		{
			h = new RenderStore(holder);
			this.holders.add(h);
		}
		h.push(icon);
	}
}
