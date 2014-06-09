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
	@Override
	public String getEntityType()
	{
		return "tree";
	}
	
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
			if (chunk.getTopStratum(x, z).getMaterial().canGrowTrees(rand.nextFloat()))
			{
				EntityTree tree = new EntityTree();
				tree.x = chunk.chunkx * 16 + x + 0.5D;
				tree.z = chunk.chunkz * 16 + z + 0.5D;
				tree.y = chunk.getHeight(x, z);
				world.addEntity(tree);
			}
		}
	}
}