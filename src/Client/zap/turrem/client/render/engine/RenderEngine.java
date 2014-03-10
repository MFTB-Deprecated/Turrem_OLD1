package zap.turrem.client.render.engine;

import java.util.ArrayList;

import zap.turrem.client.render.object.IRenderObject;
import zap.turrem.client.render.object.RenderObject;
import zap.turrem.client.render.object.model.TVFBuffer;
import zap.turrem.utils.models.TVFFile;

public class RenderEngine
{
	private ArrayList<IRenderObject> objects = new ArrayList<IRenderObject>();

	public RenderObject addObject()
	{
		RenderObject obj = new RenderObject(this.objects.size());
		this.objects.add(obj);
		return obj;
	}

	public RenderObject addObject(TVFFile tvf, float scale, float x, float y, float z)
	{
		RenderObject obj = this.addObject();
		TVFBuffer buff = new TVFBuffer();
		buff.bindTVF(tvf, obj, scale, x, y, z);
		return obj;
	}

	public void doRenderObject(int index)
	{
		this.objects.get(index).doRender();
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
