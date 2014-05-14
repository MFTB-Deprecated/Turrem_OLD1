package net.turrem.server.load;

public class EntityLoader implements IGameLoad
{
	protected GameLoader theLoader;
	
	public EntityLoader(GameLoader loader)
	{
		this.theLoader = loader;
	}

	@Override
	public void processClass(Class<?> clss)
	{
	}
}
