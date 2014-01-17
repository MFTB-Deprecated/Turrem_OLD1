package zap.turrem.client.render.engine;

import java.util.ArrayList;

import zap.turrem.client.render.object.IRenderObject;
import zap.turrem.client.render.object.RenderObject;
import zap.turrem.client.render.object.model.TVFBuffer;
import zap.turrem.utils.models.TVFFile;

public class RenderEngine
{
	private ArrayList<IRenderObject> objects = new ArrayList<IRenderObject>();
	
	public RenderObject addObject(String ident)
	{
		RenderObject obj = new RenderObject(ident, this.objects.size());
		objects.add(obj);
		return obj;
	}
	
	public RenderObject addObject(TVFFile tvf, String name)
	{
		RenderObject obj = this.addObject(name);
		TVFBuffer buff = new TVFBuffer();
		buff.bindTVF(tvf, obj);
		return obj;
	}
	
	public void doRenderObject(int index)
	{
		this.objects.get(index).doRender();
	}
	
	/**
	 * Try to minimize use
	 * @param name Object name
	 * @return Object index
	 */
	public int getObjectIndex(String name)
	{
		for (int i = 0; i < this.objects.size(); i++)
		{
			if (this.objects.get(i).getIdentifier().equals(name))
			{
				return i;
			}
		}
		return -1;
	}
	
	public RenderObject getRenderObject(int index)
	{
		return (RenderObject) this.objects.get(index);
	}
	
	public void wipeAll()
	{
		for (int i = 0; i < this.objects.size(); i++)
		{
			this.objects.get(i).doDelete();
		}
		this.objects.clear();
	}
}
