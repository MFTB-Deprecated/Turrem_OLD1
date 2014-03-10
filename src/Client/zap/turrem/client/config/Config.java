package zap.turrem.client.config;

public class Config
{
	private static int lwjglSyncRate = 60;
	private static int width = 960;
	private static int height = 600;
	private static float mouseSpeed = 0.5F;
	private static boolean fullscreen = false;
	private static boolean vsync = true;
	
	public static boolean drawBounds = true;
	public static boolean debugInfo = true;
	
	private static boolean isChanged = false;

	public static final int getLwjglSyncRate()
	{
		return lwjglSyncRate;
	}

	public static final void setLwjglSyncRate(int lwjglSyncRate)
	{
		if (Config.lwjglSyncRate != lwjglSyncRate)
		{
			Config.lwjglSyncRate = lwjglSyncRate;
			isChanged = true;
		}
	}

	public static final int getWidth()
	{
		return width;
	}

	public static final void setWidth(int width)
	{
		if (Config.width != width)
		{
			Config.width = width;
			isChanged = true;
		}
	}

	public static final int getHeight()
	{
		return height;
	}

	public static final void setHeight(int height)
	{
		if (Config.height != height)
		{
			Config.height = height;
			isChanged = true;
		}
	}

	public static final boolean isFullscreen()
	{
		return fullscreen;
	}

	public static final void setFullscreen(boolean fullscreen)
	{
		if (Config.fullscreen != fullscreen)
		{
			Config.fullscreen = fullscreen;
			isChanged = true;
		}
	}

	public static final boolean isVsync()
	{
		return vsync;
	}

	public static final void setVsync(boolean vsync)
	{
		if (Config.vsync != vsync)
		{
			Config.vsync = vsync;
			isChanged = true;
		}
	}

	public static boolean isChanged()
	{
		return isChanged;
	}

	public static void setRefreshed()
	{
		Config.isChanged = false;
	}

	public static final float getMouseSpeed()
	{
		return mouseSpeed;
	}

	public static final void setMouseSpeed(float mouseSpeed)
	{
		if (Config.mouseSpeed != mouseSpeed)
		{
			Config.mouseSpeed = mouseSpeed;
			isChanged = true;
		}
	}
}