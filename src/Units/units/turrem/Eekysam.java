package units.turrem;

import zap.turrem.client.game.entity.EntityClient;
import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.article.EntityArticle;
import zap.turrem.utils.geo.Box;

public class Eekysam extends EntityArticle
{
	static ModelIcon eekysam = new ModelIcon("turrem.entity.human.eekysam");

	@Override
	public void draw(EntityClient entity)
	{
		this.drawAnIcon(-0.3125F, 0.0F, -0.25F, eekysam);
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
		man.pushIcon(eekysam, "testeekysam", RenderObjectHolderSimple.class);
		eekysam.loadMe();
	}

	@Override
	public Box updateBounds()
	{
		return Box.getBox(-0.3125D, 0.0D, -0.25F, 0.3125D, 2.0D, 0.25F);
	}
}
