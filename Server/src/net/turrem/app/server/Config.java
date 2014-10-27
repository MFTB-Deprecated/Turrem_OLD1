package net.turrem.app.server;

public class Config
{
	public static int turremServerPort = 26555;
	public static int connectionTimeoutLimit = 1200;
	public static int connectionInQueueOverflow = 100000;
	public static int connectionOutQueueOverflow = 100000;
	public static int connectionWriteSleep = 2;
	public static int connectionReadSleep = 2;
	public static int chunkStorageWidth = 16;
	public static int chunkUnloadTickMod = 10;
}
