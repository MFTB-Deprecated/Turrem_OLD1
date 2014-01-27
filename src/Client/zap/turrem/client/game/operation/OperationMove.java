package zap.turrem.client.game.operation;

import zap.turrem.utils.geo.Point;

public class OperationMove extends Operation
{
	public Point end;
	
	public OperationMove(Point end)
	{
		this.end = end;
	}
}
