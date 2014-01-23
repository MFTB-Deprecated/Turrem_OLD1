package zap.turrem.client.control;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Deprecated
public class ControlList
{
	private IControl[] list;
	
	private static ControlList instance;
	
	public ControlList()
	{
		instance = this;
	}
	
	public static ControlList instance()
	{
		return instance;
	}

	private void doListSize()
	{
		int size = 0;
		size += Keyboard.getKeyCount();
		if (Mouse.hasWheel())
		{
			size += 1;
		}
		size += Mouse.getButtonCount();
		this.list = new IControl[size];
	}

	private void doList()
	{
		this.doListSize();
		int i = 0;
		int b = Mouse.getButtonCount();
		for (int j = 0; j < b; j++)
		{
			this.list[i] = new ControlClick(i, j);
			i++;
		}
		if (Mouse.hasWheel())
		{
			this.list[i] = new ControlScroll(i);
			i++;
		}
		int k = Keyboard.getKeyCount();
		for (int j = 0; j < k; j++)
		{
			this.list[i] = new ControlKey(i, j);
			i++;
		}
	}

	public void setup()
	{
		this.doList();
	}
	
	public void consoleAll()
	{
		for (IControl c : this.list)
		{
			System.out.println(c.getControlIdentifier());
		}
	}

	public int getControlIndex(String id)
	{
		for (int i = 0; i < this.list.length; i++)
		{
			IControl c = this.list[i];
			if (id.equals(c.getControlIdentifier()))
			{
				return i;
			}
		}
		return -1;
	}
	
	public IControl getControl(int index)
	{
		return this.list[index];
	}
	
	public void startTick()
	{
		for (IControl c : this.list)
		{
			c.startTick();
		}
	}
	
	public void endTick()
	{
		for (IControl c : this.list)
		{
			c.endTick();
		}
	}
}
