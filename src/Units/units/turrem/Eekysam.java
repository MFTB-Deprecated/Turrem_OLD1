package units.turrem;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.Entity;
import zap.turrem.core.entity.article.EntityArticle;
import zap.turrem.utils.geo.Box;

public class Eekysam extends EntityArticle
{
	static ModelIcon eekysam = new ModelIcon("turrem.entity.human.eekysam");

	@Override
	public void draw(Entity entity)
	{
		this.drawAnIcon(-0.3125F, 0.0F, -0.25F, eekysam);
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

	@Override
	public void tick(Entity entity)
	{
	}
}
