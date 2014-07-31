package net.turrem.client.render;

import net.turrem.client.render.icon.IRenderIcon;
import net.turrem.client.render.object.RenderObjectTexture;

public class RenderStoreTexture extends RenderStore<RenderObjectTexture>
{
	public RenderStoreTexture(RenderEngine render)
	{
		super(render);
	}

	@Override
	public RenderObjectTexture createObject(IRenderIcon ico)
	{
		return new RenderObjectTexture(ico.getSource(), ico.getIdentifier());
	}
}
