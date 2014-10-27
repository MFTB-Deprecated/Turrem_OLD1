package net.turrem.app.client.states;

public interface IState
{
	public void start();
	
	public void end();
	
	public void render();
	
	public void updateGL();
	
	public void mouseEvent();
	
	public void keyEvent();
}
