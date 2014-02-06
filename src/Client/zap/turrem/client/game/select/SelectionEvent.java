package zap.turrem.client.game.select;

import zap.turrem.client.game.world.WorldClient;

public abstract class SelectionEvent
{
	public void push(WorldClient world)
	{
		world.selectionEvents.add(this);
	}
	
	public abstract boolean getSelected(long uid, boolean current);
}
