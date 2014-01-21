package zap.turrem.client.control;

public abstract class ControlBase implements IControl
{
	private final int id;
	
	public ControlBase(int id)
	{
		this.id = id;
	}
	
	@Override
	public int getControlId()
	{
		return 0;
	}
}
