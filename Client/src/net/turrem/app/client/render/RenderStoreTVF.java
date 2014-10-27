package net.turrem.app.client.render;

import net.turrem.app.client.render.icon.IRenderIcon;
import net.turrem.app.client.render.icon.TVFIcon;
import net.turrem.app.client.render.object.RenderObjectTVF;

public class RenderStoreTVF extends RenderStore<RenderObjectTVF>
{
	public RenderStoreTVF(RenderEngine render)
	{
		super(render);
	}
	
	@Override
	public RenderObjectTVF createObject(IRenderIcon ico)
	{
		if (ico instanceof TVFIcon)
		{
			TVFIcon tvf = (TVFIcon) ico;
			return new RenderObjectTVF(tvf.getSource(), tvf.getIdentifier(), tvf.scale);
		}
		return null;
	}
}
