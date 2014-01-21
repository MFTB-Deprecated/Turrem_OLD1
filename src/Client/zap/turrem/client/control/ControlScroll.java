package zap.turrem.client.control;

import org.lwjgl.input.Mouse;

public class ControlScroll extends ControlBase implements IValueControl, IDeltaValueControl
{
	private int delta = 0;
	private int value = 0;
	
	public ControlScroll(int id)
	{
		super(id);
	}

	@Override
	public void next()
	{
		this.delta = 0;
		this.update();
	}
	
	public void update()
	{
		int d = Mouse.getDWheel();
		this.delta += d;
		this.value += d;
	}

	@Override
	public int getDelta()
	{
		return this.delta;
	}

	@Override
	public int getValue()
	{
		return this.value;
	}

	@Override
	public void endTick()
	{
	}

	@Override
	public void startTick()
	{
		this.next();
	}

	@Override
	public String getControlIdentifier()
	{
		return "WHEEL";
	}
}
