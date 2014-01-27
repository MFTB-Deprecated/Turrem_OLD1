package zap.turrem.client.game;

import units.turrem.Eekysam;
import zap.turrem.client.Turrem;
import zap.turrem.client.game.entity.EntityClient;
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
		this.theWorld = new WorldClient(new RenderWorld(turrem.theRender, this), this);
		this.theRender = new RenderGame(this);
	}
	
	public void onStart()
	{
		this.theRender.start();
		this.face.reset();
		
		(new EntityClient(new Eekysam())).push(this.theWorld, this.theTurrem.theRender);
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
