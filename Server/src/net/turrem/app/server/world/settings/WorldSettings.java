package net.turrem.app.server.world.settings;

import net.turrem.blueutils.nbt.NBTCompound;

public class WorldSettings
{
	public final WorldType type;
	
	public NBTCompound settings = new NBTCompound();
	
	public WorldSettings(WorldType type)
	{
		this.type = type;
	}
}
