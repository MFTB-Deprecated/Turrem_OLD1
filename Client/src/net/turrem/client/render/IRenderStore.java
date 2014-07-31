package net.turrem.client.render;

import java.util.Map;

import net.turrem.client.render.object.IRenderObject;

public interface IRenderStore
{
	public void unloadAll();
	
	public int size();
	
	public Map<String, IRenderObject> getMap();
	
	public IRenderObject getObject(String name);
	
	public IRenderObject unloadObject(String name);
	
	public IRenderObject loadObject(String name);
	
	public IRenderObject reloadObject(String name);
}
