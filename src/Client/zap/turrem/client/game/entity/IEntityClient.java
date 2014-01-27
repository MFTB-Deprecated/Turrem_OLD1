package zap.turrem.client.game.entity;

import zap.turrem.utils.geo.Point;

public interface IEntityClient
{
	public void render();
	public void disappear();
	public void kill();
	public void onTick();
	public Point getLocation();
	public void setPosition(double x, double y, double z);
	public void setPosition(Point p);
}
