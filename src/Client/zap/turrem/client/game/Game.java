package zap.turrem.client.game;

import zap.turrem.client.Turrem;
import zap.turrem.client.game.player.face.PlayerFace;
import zap.turrem.client.render.RenderGame;
import zap.turrem.client.render.RenderWorld;

public class Game
{
	public PlayerFace face;
	
	public WorldClient theWorld;
	
	public RenderGame theRender;
	
	protected Turrem theTurrem;
	
	public Game(Turrem turrem)
	{
		this.face = new PlayerFace();
		this.theTurrem = turrem;
		this.theWorld = new WorldClient(new RenderWorld(turrem.theRender, this));
		this.theRender = new RenderGame(this);
	}
	
	public void onStart()
	{
		this.theRender.start();
		this.face.reset();
	}
	
	public void tickGame()
	{
		this.face.tickCamera();
		this.theWorld.tickWorld();
	}

	public void render()
	{
		this.theRender.render();
	}
	
	public void renderWorld()
	{
		this.theWorld.render();
	}
}
