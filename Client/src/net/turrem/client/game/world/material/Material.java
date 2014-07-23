package net.turrem.client.game.world.material;

import net.turrem.utils.graphics.MakeColor;

public class Material
{
	public String name = "-BROKEN MATERIAL-";
	public byte red = (byte) 0xFF;
	public byte green = (byte) 0xFF;
	public byte blue = (byte) 0xFF;
	
	public int getColor()
	{
		return MakeColor.RGB(red & 0xFF, green & 0xFF, blue & 0xFF);
	}
}
