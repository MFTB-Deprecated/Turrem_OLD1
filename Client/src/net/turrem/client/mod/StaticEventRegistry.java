package net.turrem.client.mod;

import java.util.ArrayList;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.turrem.client.game.ClientGame;
import net.turrem.client.game.world.ClientWorld;
import net.turrem.mod.INotedElementVisitor;
import net.turrem.mod.ModInstance;

public class StaticEventRegistry implements INotedElementVisitor
{
	public ArrayList<Method> preTickCalls = new ArrayList<Method>();
	public ArrayList<Method> postTickCalls = new ArrayList<Method>();
	public ArrayList<Method> preWorldTickCalls = new ArrayList<Method>();
	public ArrayList<Method> postWorldTickCalls = new ArrayList<Method>();

	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod)
	{
		TurremSubscribeStatic subscribe = (TurremSubscribeStatic) annotation;
		this.registerEvent(subscribe.event(), (Method) element);
	}

	private void registerEvent(EnumTurremEvent event, Method method)
	{
		if (!Modifier.isStatic(method.getModifiers()))
		{
			System.out.printf("Method %s has @TurremSubscribeStatic(event=%s), but is not static%n", method.getName(), event.name());
			return;
		}
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
