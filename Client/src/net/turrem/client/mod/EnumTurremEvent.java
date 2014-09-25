package net.turrem.client.mod;

public enum EnumTurremEvent
{
	PRE_GAME_RENDER(Long.class),
	POST_GAME_RENDER(Long.class);
	
	public final Class<?>[] parameters;
	
	EnumTurremEvent(Class<?>... parameters)
	{
		this.parameters = parameters;
	}
}
