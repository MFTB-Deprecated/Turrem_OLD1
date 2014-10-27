package net.turrem.app.client.render;

import net.turrem.app.client.render.icon.IRenderIcon;
import net.turrem.app.client.render.icon.TextureIcon;
import net.turrem.app.client.render.object.RenderObjectTexture;

public class RenderStoreTexture extends RenderStore<RenderObjectTexture>
{
	public RenderStoreTexture(RenderEngine render)
	{
		super(render);
	}
	
	@Override
	public RenderObjectTexture createObject(IRenderIcon ico)
	{
		if (ico instanceof TextureIcon)
		{
			TextureIcon tex = (TextureIcon) ico;
			return new RenderObjectTexture(tex.getSource(), tex.getIdentifier(), tex.isPixelArt);
		}
		return null;
	}
}
