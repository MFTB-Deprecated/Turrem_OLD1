package zap.turrem.client.control;

import org.lwjgl.input.Mouse;

public class ControlClick extends ControlBase implements IBoolControl, IDeltaBoolControl
{
	private int but;
	private boolean last;
	private boolean current;
	
	public ControlClick(int id, int button)
	{
		super(id);
		this.but = button;
	}
	
	@Override
	public boolean getBool()
	{
		return this.current;
	}

	@Override
	public void endTick()
	{

	}

	@Override
	public void next()
	{
		this.last = this.current;
		this.current = Mouse.isButtonDown(this.but);
	}

	@Override
	public boolean changedPos()
	{
		return !this.last && this.current;
	}

	@Override
	public boolean changedNeg()
	{
		return this.last && !this.current;
	}

	@Override
	public void startTick()
	{
		this.next();
	}

	@Override
	public String getControlIdentifier()
	{
		return "CLICK_" + Mouse.getButtonName(this.but).toUpperCase();
	}
}
