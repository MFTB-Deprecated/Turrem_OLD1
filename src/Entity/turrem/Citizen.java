package turrem;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.core.entity.UnitLearn;
import zap.turrem.utils.geo.Box;

public class Citizen extends UnitLearn
{
	public Citizen()
	{
		super();
	}

	@Override
	public void loadAssets(RenderManager man)
	{
		super.loadAssets(man);
	}

	@Override
	public Box updateBounds()
	{
		return null;
	}

	@Override
	protected void renderEntity()
	{
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
