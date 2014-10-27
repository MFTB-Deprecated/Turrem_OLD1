package net.turrem.server.world.settings;

import net.turrem.utils.nbt.NBTCompound;

public class WorldSettings
{
	public final WorldType type;
	
	public NBTCompound settings = new NBTCompound();
	
	public WorldSettings(WorldType type)
	{
		this.type = type;
	}
}
