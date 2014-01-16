package zap.turrem.client.render;

import java.util.ArrayList;

import zap.turrem.utils.models.TVFFile;

public class RenderEngine
{
	private ArrayList<RenderObject> objects = new ArrayList<RenderObject>();
	
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
			if (this.objects.get(i).identifier.equals(name))
			{
				return i;
			}
		}
		return -1;
	}
	
	public RenderObject getRenderObject(int index)
	{
		return this.objects.get(index);
	}
}
