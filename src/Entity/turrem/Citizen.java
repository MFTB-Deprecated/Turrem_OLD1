package turrem;

import org.lwjgl.input.Keyboard;

import zap.turrem.client.render.engine.RenderManager;
import zap.turrem.client.render.object.model.ModelIcon;
import zap.turrem.core.entity.UnitLearn;
import zap.turrem.utils.geo.Box;

public class Citizen extends UnitLearn
{
	private ModelIcon person = new ModelIcon("turrem.entity.unit.citizen.cave", 2.0F, 0.5F, 0.0F, 0.5F);

	@Override
	public Box pickBounds()
	{
		return Box.getBox(-1, 0, -1, 1, 2, 1);
	}

	@Override
	protected void renderEntity()
	{
		person.render();
	}

	public void loadAssets(RenderManager man)
	{
		super.loadAssets(man);
		man.pushIcon(person, "citizen");
		person.load(man);
	}
	
	public void onTick()
	{
		if (this.theWorld != null)
		{
			this.posY = this.theWorld.scaleWorld(this.theWorld.getHeight(this.theWorld.unScaleWorld(this.posX), this.theWorld.unScaleWorld(this.posZ)));
		}
		this.posZ += 0.02F;
	}
	
	public void keyEvent(boolean me)
	{
		if (me)
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_SPACE && Keyboard.getEventKeyState())
			{
				this.rotation++;
			}
		}
	}
}
