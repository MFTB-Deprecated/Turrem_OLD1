package zap.turrem.client.render.engine.holders;

import zap.turrem.client.render.object.model.ModelIcon;

public interface IRenderObjectHolder
{	
	public boolean handModel(ModelIcon model);
	
	public void load();
	
	public void unload();
	
	public void forceReload();
	
	public void forceLoad();
	
	public int numHeld();
	
	public void tickLoad();
	
	public void renderObject(ModelIcon icon);
	
	public void renderObject(int engineid);
	
	public String getIdentifier();

	public int getIndex();
}
