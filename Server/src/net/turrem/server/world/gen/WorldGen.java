package net.turrem.server.world.gen;

import java.util.ArrayList;

import net.turrem.server.world.Stratum;

public abstract class WorldGen
{
	public abstract ArrayList<Stratum> generateChunk(int chunkx, int chunky);
}
