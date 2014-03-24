package net.turrem.client.render.object;

import net.turrem.client.render.IRenderer;

public interface IRenderObject extends IRenderer, IRenderObjectBase
{
	public int getEngineIndex();
}
