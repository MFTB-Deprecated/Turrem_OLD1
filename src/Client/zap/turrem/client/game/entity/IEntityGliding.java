package zap.turrem.client.game.entity;

import zap.turrem.utils.geo.Point;

public interface IEntityGliding
{
	public void setMotion(Point start, Point end, int time);
	public void cancelMotion();
	public void doMotion();
}
