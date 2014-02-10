package units.turrem;

import org.lwjgl.opengl.GL11;

import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.article.EntityArticle;
import zap.turrem.utils.geo.Box;

public class Hut extends EntityArticle
{
	public ModelIcon hut = new ModelIcon("turrem.structure.house.hut_1");
	
	@Override
	public void draw(EntityClient entity)
	{
		GL11.glPushMatrix();
		GL11.glScalef(3.2F, 3.2F, 3.2F);
		this.drawAnIcon(-0.468F, 0.0F, -0.468F, hut);
		GL11.glPopMatrix();
	}

	@Override
	public void clientTick(EntityClient entity)
	{
		entity.rotation = 0;
	}

	@Override
	public void serverTick(EntityClient entity)
	{
	}

	@Override
	public void loadAssets(RenderManager man)
	{
		man.pushIcon(hut, "testhut", RenderObjectHolderSimple.class);
		hut.loadMe();
	}

	@Override
	public Box updateBounds()
	{
		return Box.getBox(-1.5D, 0.0D, -1.5D, 3.5D, 4.0D, 3.5D);
	}
}
