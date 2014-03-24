package net.turrem.client.gui;

public interface IInteractable extends IElement
{
	public boolean mouseEvent();
	
	public boolean keyEvent();
	
	public boolean isInteractableAt(int x, int y);
	
	public boolean isClickSpot(int x, int y);
}
