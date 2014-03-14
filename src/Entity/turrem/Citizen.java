package turrem;

import org.lwjgl.input.Keyboard;

import zap.turrem.core.entity.UnitLearn;
import zap.turrem.utils.geo.Box;

public class Citizen extends UnitLearn
{
	@Override
	public Box pickBounds()
	{
		return Box.getBox(-1, 0, -2, 2, 1, 3);
	}

	@Override
	protected void renderEntity()
	{
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
