package turrem;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.engine.holders.RenderObjectHolderSimple;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.UnitLearn;
import zap.turrem.utils.geo.Box;

public class Citizen extends UnitLearn
{
	private ModelIcon person = new ModelIcon("turrem.entity.human.cave", 16.0F, 5.0F, 0.0F, 4.0F);
	
	public Citizen()
	{
		super();
	}

	@Override
	public void loadAssets(RenderManager man)
	{
		super.loadAssets(man);
		man.pushIcon(person, "people", RenderObjectHolderSimple.class);
		this.person.loadMe();
	}

	@Override
	public Box updateBounds()
	{
		return Box.getBox(-1, 0.0F, -1, 1, 2.0F, 1);
	}

	@Override
	protected void renderEntity()
	{
		person.render();
	}

	@Override
	public void renderSingle()
	{
	}

	@Override
	public Box singleBox()
	{
		float minx = 5.0F / 16;
		float miny = 4.0F / 16;
		return Box.getBox(-minx, 0.0F, -miny, minx, 2.0F, miny);
	}

	@Override
	public int defaultNumber()
	{
		return 5;
	}

	@Override
	protected int addHowMany(int numtoadd)
	{
		int num = 20 - this.number;
		if (numtoadd <= num)
		{
			return numtoadd;
		}
		else
		{
			return num;
		}
	}
}
