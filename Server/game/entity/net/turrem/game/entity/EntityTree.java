package net.turrem.game.entity;

import net.turrem.server.TurremServer;
import net.turrem.server.load.control.GameEntity;
import net.turrem.server.load.control.SubscribeLoad;

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
}