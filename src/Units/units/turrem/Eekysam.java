package units.turrem;

import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.article.EntityArticle;

public class Eekysam extends EntityArticle
{
	static ModelIcon eekysam = new ModelIcon("turrem.entity.human.eekysam");

	@Override
	public void draw(EntityClient entity)
	{
		this.drawAnIcon(entity.posX, entity.posY, entity.posZ, eekysam);
	}

	@Override
	public void clientTick(EntityClient entity)
	{
	}

	@Override
	public void serverTick(EntityClient entity)
	{
	}

	@Override
	public void loadAssets(RenderManager man)
	{
		man.pushIcon(eekysam, "testrenders", RenderObjectHolderSimple.class);
		eekysam.loadMe();
	}
}
