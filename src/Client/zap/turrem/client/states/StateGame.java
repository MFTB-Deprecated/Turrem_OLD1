package zap.turrem.client.states;

import zap.turrem.client.Turrem;
import zap.turrem.client.render.RenderGame;
import zap.turrem.client.render.RenderWorld;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateGame implements IState
{
	protected Turrem theTurrem;
	
	public RenderGame render;

	public StateGame(Turrem turrem, RenderWorld world)
	{
		this.theTurrem = turrem;
		this.render = new RenderGame(world);
	}

	@Override
	public void start()
	{
		this.render.start();
	}

	@Override
	public void end()
	{
		this.render.end();
	}

	@Override
	public void tick()
	{
		this.render.tick();
	}
}
