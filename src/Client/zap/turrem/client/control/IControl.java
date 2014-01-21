package zap.turrem.client.control;

public interface IControl
{
	public int getControlIndex();
	
	public void endTick();
	
	public void startTick();
	
	public String getControlIdentifier();
}
