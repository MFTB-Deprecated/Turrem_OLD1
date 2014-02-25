package units.turrem.foliage;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.Entity;
import zap.turrem.core.entity.article.EntityArticle;
import zap.turrem.utils.geo.Box;

public class Tree extends EntityArticle
{
	static final float scale = 1.0F;
	static ModelIcon tree1 = new ModelIcon("turrem.feature.tree.small.tree_1",  scale);
	
	@Override
	public void draw(Entity entity)
	{
		this.drawAnIcon(-0.46875F * scale, 0.0F, 0.4375F * scale, tree1);
	}

	@Override
	public void tick(Entity entity)
	{
	}

	@Override
	public void loadAssets(RenderManager man)
	{
		man.pushIcon(tree1, "foliage", RenderObjectHolderSimple.class);
		tree1.loadMe();
	}

	@Override
	public Box updateBounds()
	{
		return Box.getBox(-0.2F * scale, 0.0F * scale, -0.2F * scale, 0.2F * scale, 2.0F * scale, 0.2F * scale);
	}

	@Override
	public void keyEvent(boolean me, Entity entity)
	{
	}

	@Override
	public void mouseEvent(boolean me, Entity entity)
	{
	}
}
