package net.turrem.client;

import org.lwjgl.opengl.Display;

public class Config
{
	public static int chunkRenderDistance = 96;
	public static int entityRenderDistance = 96;
	
	public static float getMouseSpeed()
	{
		return 0.5F;
	}

	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}

	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
}
