package net.turrem.client.states;

import net.turrem.client.Turrem;
import net.turrem.client.game.ClientGame;

public class StateGame implements IState
{
	protected Turrem theTurrem;
	public ClientGame theGame;
	
	public StateGame(Turrem turrem)
	{
		this.theTurrem = turrem;
		this.theGame = new ClientGame(turrem.theRender);
	}
	
	@Override
	public void start()
	{
		this.theGame.start();
	}

	@Override
	public void end()
	{
		this.theGame.end();
	}

	@Override
	public void render()
	{
		this.theGame.render();
	}

	@Override
	public void updateGL()
	{
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
