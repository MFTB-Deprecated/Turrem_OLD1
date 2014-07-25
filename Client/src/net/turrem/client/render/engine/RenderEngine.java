package net.turrem.client.render.engine;

import net.turrem.client.Config;
import net.turrem.client.render.object.RenderObject;
import net.turrem.client.render.object.model.TVFBuffer;
import net.turrem.utils.models.TVFFile;

public class RenderEngine
{
	private int index = 0;
	protected boolean mat;

	public RenderEngine()
	{
	}

	public RenderObject makeObject(TVFFile tvf, float scale, float x, float y, float z)
	{
		RenderObject obj = new RenderObject(this.index++);
		TVFBuffer buff = new TVFBuffer();
		buff.renderSettingsVersion = Config.renderSettingsVersion;
		buff.bindTVF(tvf, obj, scale, x, y, z, Config.doAO, Config.doMat);
		return obj;
	}
}
