package net.turrem.game.entity;

import java.util.Random;

import net.turrem.server.TurremServer;
import net.turrem.server.load.control.GameEntity;
import net.turrem.server.load.control.SubscribeDecorate;
import net.turrem.server.load.control.SubscribeLoad;
import net.turrem.server.world.Chunk;
import net.turrem.server.world.World;

@GameEntity(from = "turrem", author = "eekysam")
public class EntityTree extends EntityWorldDec
{
	@SubscribeLoad
	public static void onLoad(TurremServer server)
	{
		 System.out.println("...Test Load...");
	}
	
	@SubscribeDecorate(seed = 46834677L)
	public static void decorate(Chunk chunk, World world, Random rand)
	{
		int n = rand.nextInt(4) + 2;
		for (int i = 0; i < n; i++)
		{
			int x = rand.nextInt(16);
			int z = rand.nextInt(16);
			chunk.getTopStratum(x, z);
		}
	}
}