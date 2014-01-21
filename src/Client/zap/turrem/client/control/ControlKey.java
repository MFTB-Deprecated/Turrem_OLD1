package zap.turrem.client.control;

import org.lwjgl.input.Keyboard;

public class ControlKey extends ControlBase implements IBoolControl, IDeltaBoolControl
{
	private int key;
	private boolean last;
	private boolean current;

	public ControlKey(int id, String key)
	{
		super(id);
		this.key = Keyboard.getKeyIndex(key);
	}
	
	public ControlKey(int id, int key)
	{
		super(id);
		this.key = key;
	}
	
	public String getName()
	{
		return Keyboard.getKeyName(this.key);
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
		this.current = Keyboard.isKeyDown(this.key);
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
}
