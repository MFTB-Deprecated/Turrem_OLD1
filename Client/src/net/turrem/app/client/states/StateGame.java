package net.turrem.app.client.states;

import net.turrem.app.client.Turrem;
import net.turrem.app.client.game.ClientGame;

public class StateGame implements IState
{
	protected Turrem theTurrem;
	public ClientGame theGame;
	public static long gameTime = 0;
	
	public StateGame(Turrem turrem)
	{
		this.theTurrem = turrem;
		this.theGame = new ClientGame(turrem.theRender, this.theTurrem);
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
		this.theTurrem.staticEventRegistry.onPreGameRender(gameTime, this.theGame);
		this.theGame.render();
		this.theTurrem.staticEventRegistry.onPostGameRender(gameTime, this.theGame);
		gameTime++;
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
