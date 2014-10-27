package net.turrem.app.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.lang.reflect.Array;

public class ReflectionUtils
{
	public static boolean areParametersValid(Class<?>[] take, Class<?>... pass)
	{
		if (take.length != pass.length)
		{
			return false;
		}
		for (int i = 0; i < take.length; i++)
		{
			if (!take[i].isAssignableFrom(pass[i]))
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean isClassNumberofClass(Class<?> object, Class<?> expect, boolean expectIsSuper)
	{
		if (expectIsSuper)
		{
			if (expect.isAssignableFrom(object))
			{
				return true;
			}
			if (Collection.class.isAssignableFrom(object))
			{
				return true;
			}
			if (object.isArray())
			{
				if (expect.isAssignableFrom(object.getComponentType()))
				{
					return true;
				}
			}
		}
		else
		{
			if (object.isAssignableFrom(expect))
			{
				return true;
			}
			if (Collection.class.isAssignableFrom(object))
			{
				return true;
			}
			if (object.isArray())
			{
				if (object.getComponentType().isAssignableFrom(expect))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static List<Object> getNumberofClass(Object object, Class<?> expect, boolean expectIsSuper)
	{
		ArrayList<Object> out = new ArrayList<Object>();
		if (object == null)
		{
			return out;
		}
		if (expectIsSuper)
		{
			if (expect.isAssignableFrom(object.getClass()))
			{
				out.add(object);
			}
			else if (Collection.class.isInstance(object))
			{
				for (Object o : (Collection<?>) object)
				{
					if (expect.isAssignableFrom(o.getClass()))
					{
						out.add(o);
					}
				}
			}
			else if (object.getClass().isArray() && expect.isAssignableFrom(object.getClass().getComponentType()))
			{
				for (int i = 0; i < Array.getLength(object); i++)
				{
					out.add(Array.get(object, i));
				}
			}
		}
		else
		{
			if (object.getClass().isAssignableFrom(expect))
			{
				out.add(object);
			}
			else if (Collection.class.isInstance(object))
			{
				for (Object o : (Collection<?>) object)
				{
					if (o.getClass().isAssignableFrom(expect))
					{
						out.add(o);
					}
				}
			}
			else if (object.getClass().isArray() && object.getClass().getComponentType().isAssignableFrom(expect))
			{
				for (int i = 0; i < Array.getLength(object); i++)
				{
					out.add(Array.get(object, i));
				}
			}
		}
		return out;
	}
}
