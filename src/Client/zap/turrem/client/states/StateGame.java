package zap.turrem.client.states;

import zap.turrem.client.Turrem;
import zap.turrem.client.game.Game;

/**
 * Should only be used as an intermediary with the actual game objects. Any
 * render code here is temporary and for testing.
 */
public class StateGame implements IState
{
	protected Turrem theTurrem;
	
	public Game theGame;

	public StateGame(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		this.theGame = new Game(this.theTurrem);
		this.theGame.onStart();
	}

	@Override
	public void end()
	{
		this.theGame.theRender.end();
	}

	@Override
	public void tick()
	{
		this.theGame.tickGame();
		this.theGame.render();
	}
}
