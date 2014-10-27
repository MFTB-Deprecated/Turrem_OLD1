package net.turrem.app.server.world.gen;

import java.util.Set;

import net.turrem.app.server.TurremServer;
import net.turrem.app.server.world.World;
import net.turrem.app.server.world.mesh.WorldMesh;
import net.turrem.app.server.world.morph.IGeomorph;
import net.turrem.app.server.world.settings.WorldSettings;

public class WorldGen
{
	protected TurremServer theServer;
	protected World theWorld;
	protected WorldSettings settings;
	public final long seed;
	
	public Set<IGeomorph> startingMorphs;
	
	public WorldMesh[] mesh;
	
	public WorldGen(long seed, TurremServer theServer, World theWorld)
	{
		this.theServer = theServer;
		this.theWorld = theWorld;
		this.seed = seed;
		this.settings = this.theWorld.genSettings;
		
		this.startingMorphs = this.theServer.startingGeomorphRegistry.getStarting(this.settings);
		
		this.mesh = new WorldMesh[this.settings.settings.getShort("meshLayers")];
		
		for (int i = 0; i < this.mesh.length; i++)
		{
			WorldMesh up = null;
			if (i >= 1)
			{
				up = this.mesh[i - 1];
			}
			this.mesh[i] = new WorldMesh(this.seed + i, up, this);
		}
	}
	
	public WorldMesh getFinalMesh()
	{
		return this.mesh[this.mesh.length - 1];
	}
}
