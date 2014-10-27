package net.turrem.app.server.world.material;

import java.awt.Color;

public class Material
{
	private int color;
	private String name;
	private short numId;
	
	protected Material(String name, byte red, byte green, byte blue)
	{
		this.name = name;
		this.color = 0;
		this.color |= red;
		this.color <<= 8;
		this.color |= green;
		this.color <<= 8;
		this.color |= blue;
		this.numId = (short) MaterialList.list.size();
		MaterialList.list.add(this);
	}
	
	protected Material(String name, int color)
	{
		this.name = name;
		this.color = color;
		this.numId = (short) MaterialList.list.size();
		MaterialList.list.add(this);
	}
	
	public boolean isSolid()
	{
		return true;
	}
	
	public short getNumId()
	{
		return this.numId;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getColorInt()
	{
		return this.color;
	}
	
	public Color getColor()
	{
		return new Color(this.color);
	}
}
