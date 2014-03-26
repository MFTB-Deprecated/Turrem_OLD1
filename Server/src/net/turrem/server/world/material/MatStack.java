package net.turrem.server.world.material;

public class MatStack
{
	protected String id;
	protected int size;

	public MatStack(String name)
	{
		this.id = name;
		this.size = 1;
	}

	public MatStack(String name, int size)
	{
		this.id = name;
		this.size = size;
	}

	public String getId()
	{
		return this.id;
	}

	public int getSize()
	{
		return this.size;
	}
}
