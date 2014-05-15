package net.turrem.client;

import org.lwjgl.opengl.Display;

public class Config
{
	public static int chunkRenderDistance = 96;
	public static int entityRenderDistance = 96;
	public static float mouseSpeedX = 0.5F;
	public static float mouseSpeedY = 0.5F;
	public static float scrollSpeed = 0.012F;
	public static float camDistMin = 8.0F;
	public static float camDistMax = 120.0F;
	public static float terrainAoSampleMult = 0.5F;
	public static boolean doTerrainAo = true;
	
	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}

	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
}
