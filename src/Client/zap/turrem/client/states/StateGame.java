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

	protected long lastTime;
	protected long timeoff = 0;

	public StateGame(Turrem turrem)
	{
		this.theTurrem = turrem;
	}

	@Override
	public void start()
	{
		this.theGame = new Game(this.theTurrem);
		this.theGame.onStart();
		this.lastTime = System.currentTimeMillis();
	}

	@Override
	public void end()
	{
		this.theGame.theRender.end();
	}

	@Override
	public void tick()
	{
		long time = System.currentTimeMillis();
		if (time - this.lastTime > 100 - this.timeoff)
		{
			this.timeoff += (time - this.lastTime) - 100;
			if (this.timeoff < 0)
			{
				this.timeoff = 0;
			}
			if (this.timeoff > 50)
			{
				this.timeoff = 50;
			}
			this.lastTime = time;
			this.theGame.tickGame();
		}
		this.theGame.render();
	}

	@Override
	public void updateGL()
	{
		this.theGame.updateGL();
	}

	@Override
	public void mouseEvent()
	{
		this.theGame.mouseEvent();
	}

	@Override
	public void keyEvent()
	{
		this.theGame.keyEvent();
	}
}
