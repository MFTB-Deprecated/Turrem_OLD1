package units.turrem;

import org.lwjgl.input.Keyboard;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.Entity;
import zap.turrem.core.entity.article.EntityArticle;
import zap.turrem.utils.geo.Box;

public class Eekysam extends EntityArticle
{
	static ModelIcon eekysam = new ModelIcon("turrem.entity.human.eekysam",  0.35F);

	@Override
	public void draw(Entity entity)
	{
		this.drawAnIcon(-0.3125F * 0.35F, 0.0F, -0.25F * 0.35F, eekysam);
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
		return Box.getBox(-0.3125D * 0.35F, 0.0D, -0.25F * 0.35F, 0.3125D * 0.35F, 2.0D * 0.35F, 0.25F * 0.35F);
	}

	@Override
	public void tick(Entity entity)
	{
		entity.setPosition(entity.posX, entity.theWorld.scaleWorld(entity.theWorld.getHeight(entity.theWorld.unScaleWorld(entity.posX), entity.theWorld.unScaleWorld(entity.posZ)) + 1), entity.posZ);
	}

	@Override
	public void keyEvent(boolean me, Entity entity)
	{
		if (me)
		{
			if (!Keyboard.isRepeatEvent() && Keyboard.getEventKeyState())
			{
				int k = Keyboard.getEventKey();
				double d = entity.theWorld.scaleWorld(1);
				if (k == Keyboard.KEY_W)
				{
					entity.move(d, 0.0D, 0.0D);
				}
				if (k == Keyboard.KEY_S)
				{
					entity.move(-d, 0.0D, 0.0D);
				}
				if (k == Keyboard.KEY_A)
				{
					entity.move(0.0D, 0.0D, -d);
				}
				if (k == Keyboard.KEY_D)
				{
					entity.move(0.0D, 0.0D, d);
				}
				if (k == Keyboard.KEY_DELETE)
				{
					entity.kill();
				}
			}
		}
	}

	@Override
	public void mouseEvent(boolean me, Entity entity)
	{
	}
}
