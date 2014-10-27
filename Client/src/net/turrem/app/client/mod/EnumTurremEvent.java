package net.turrem.app.client.mod;

import net.turrem.app.client.game.ClientGame;
import net.turrem.app.client.game.world.ClientWorld;

public enum EnumTurremEvent
{
	PRE_GAME_RENDER(Long.class, ClientGame.class),
	POST_GAME_RENDER(Long.class, ClientGame.class),
	PRE_WORLD_RENDER(ClientWorld.class),
	POST_WORLD_RENDER(ClientWorld.class);
	
	public final Class<?>[] parameters;
	
	EnumTurremEvent(Class<?>... parameters)
	{
		this.parameters = parameters;
	}
}
