package zap.turrem.client.control;

public interface IDeltaBoolControl extends IDeltaControl, IBoolControl
{
	public boolean changedPos();
	
	public boolean changedNeg();
}
