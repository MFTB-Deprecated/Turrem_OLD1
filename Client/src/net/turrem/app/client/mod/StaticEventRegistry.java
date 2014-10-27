package net.turrem.app.client.mod;

import java.util.ArrayList;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.turrem.app.client.game.ClientGame;
import net.turrem.app.client.game.world.ClientWorld;
import net.turrem.app.mod.ModInstance;
import net.turrem.app.mod.registry.StaticMethodRegistry;

public class StaticEventRegistry extends StaticMethodRegistry
{
	public ArrayList<Method> preTickCalls = new ArrayList<Method>();
	public ArrayList<Method> postTickCalls = new ArrayList<Method>();
	public ArrayList<Method> preWorldTickCalls = new ArrayList<Method>();
	public ArrayList<Method> postWorldTickCalls = new ArrayList<Method>();
	
	@Override
	protected void registerElement(Annotation annotation, Method met, ModInstance mod)
	{
		if (!(annotation instanceof TurremSubscribeStatic))
		{
			System.out.printf("Method %s has @%s not @%s but was sent to a StaticEventRegistry.%n", met.getName(), annotation.getClass().getSimpleName(), TurremSubscribeStatic.class.getSimpleName());
			return;
		}
		TurremSubscribeStatic subscribe = (TurremSubscribeStatic) annotation;
		this.registerEvent(subscribe.event(), met);
	}
	
	private void registerEvent(EnumTurremEvent event, Method method)
	{
		String arguments = "";
		{
			arguments += "(";
			boolean first = true;
			for (Class<?> clas : event.parameters)
			{
				if (!first)
				{
					arguments += ", ";
				}
				arguments += clas.getName();
				first = false;
			}
			arguments += ")";
		}
		
		Class<?>[] metPars = method.getParameterTypes();
		if (metPars.length == event.parameters.length)
		{
			boolean flag = true;
			for (int i = 0; i < metPars.length; i++)
			{
				if (!metPars[i].isAssignableFrom(event.parameters[i]))
				{
					flag = false;
					break;
				}
			}
			if (!flag)
			{
				System.out.printf("Method %s has @TurremSubscribeStatic(event=%s), but does not have the parameters %s.%n", method.getName(), event.name(), arguments);
				return;
			}
			switch (event)
			{
				case PRE_GAME_RENDER:
					this.preTickCalls.add(method);
					return;
				case POST_GAME_RENDER:
					this.postTickCalls.add(method);
					return;
				case PRE_WORLD_RENDER:
					this.preWorldTickCalls.add(method);
					return;
				case POST_WORLD_RENDER:
					this.postWorldTickCalls.add(method);
					return;
				default:
					return;
			}
		}
	}
	
	public void onPreGameRender(long renderTicks, ClientGame game)
	{
		for (Method met : this.preTickCalls)
		{
			try
			{
				met.invoke(null, renderTicks, game);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				
			}
		}
	}
	
	public void onPostGameRender(long renderTicks, ClientGame game)
	{
		for (Method met : this.postTickCalls)
		{
			try
			{
				met.invoke(null, renderTicks, game);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				
			}
		}
	}
	
	public void onPreWorldRender(ClientWorld game)
	{
		for (Method met : this.preWorldTickCalls)
		{
			try
			{
				met.invoke(null, game);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				
			}
		}
	}
	
	public void onPostWorldRender(ClientWorld game)
	{
		for (Method met : this.postWorldTickCalls)
		{
			try
			{
				met.invoke(null, game);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				
			}
		}
	}
}
