package net.turrem.app.client.game.world.material;

import net.turrem.app.utils.graphics.MakeColor;

public class Material
{
	public String name = "-BROKEN MATERIAL-";
	public byte red = (byte) 0xFF;
	public byte green = (byte) 0xFF;
	public byte blue = (byte) 0xFF;
	
	public int getColor()
	{
		return MakeColor.RGB(this.red & 0xFF, this.green & 0xFF, this.blue & 0xFF);
	}
}
