package zap.turrem.client.game.operation;

import zap.turrem.client.game.world.WorldClient;

public abstract class Operation
{
	public void push(WorldClient world)
	{
		world.operations.add(this);
	}
}
