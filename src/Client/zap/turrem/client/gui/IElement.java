package zap.turrem.client.gui;

import zap.turrem.client.render.engine.RenderManager;

public interface IElement
{
	public int getXPos();
	public int getYPos();
	public int getWidth();
	public int getHeight();
	
	public void onStart(RenderManager manager);
	
	public void render();
	
	public boolean mouseEvent();
	
	public boolean keyEvent();
	
	public void setPos(int x, int y);
}
