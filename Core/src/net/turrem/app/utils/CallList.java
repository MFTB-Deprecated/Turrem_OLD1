package net.turrem.app.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.lang.reflect.Method;

public class CallList
{
	private List<Method> calls = new ArrayList<Method>();
	
	public int addCall(Method call)
	{
		this.calls.add(call);
		return this.calls.size();
	}
	
	public Iterator<Method> getCalls()
	{
		return this.calls.iterator();
	}
}
