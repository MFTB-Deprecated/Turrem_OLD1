package zap.turrem.client.control;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ControlList
{
	private IControl[] list;
	
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
}
