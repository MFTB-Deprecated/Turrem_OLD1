package zap.turrem.client.render.engine;

import java.util.ArrayList;
import java.util.HashMap;

import zap.turrem.client.asset.AssetLoader;
import zap.turrem.client.render.engine.holders.IRenderObjectHolder;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.client.render.texture.ITextureObject;
import zap.turrem.client.render.texture.TextureObject;

public class RenderManager
{
	private ArrayList<IRenderObjectHolder> holders = new ArrayList<IRenderObjectHolder>();
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

	
	private void doTickHolders()
	{
		for (int i = 0; i < this.holders.size(); i++)
		{
			IRenderObjectHolder holder = this.holders.get(i);

			if (holder != null)
			{
				holder.tickLoad();
			}
		}
	}
	
	public void tickHolders()
	{
		if (this.working != 0)
		{
			this.doTickHolders();
		}
	}

	public IRenderObjectHolder getHolder(String identifier)
	{
		for (int i = 0; i < this.holders.size(); i++)
		{
			IRenderObjectHolder h = this.holders.get(i);
			if (h.getIdentifier().equals(identifier))
			{
				return h;
			}
		}
		return null;
	}

	public IRenderObjectHolder getHolder(int index)
	{
		return this.holders.get(index);
	}

	public boolean pushIcon(ModelIcon icon, String holder, Class<? extends IRenderObjectHolder> newHolderType)
	{
		IRenderObjectHolder h = this.getHolder(holder);

		if (h == null)
		{
			try
			{
				h = newHolderType.getConstructor(RenderManager.class, int.class, String.class).newInstance(this, this.holders.size(), holder);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			this.holders.add(h);
		}
		if (h != null)
		{
			return h.handModel(icon);
		}
		return false;
	}
}
