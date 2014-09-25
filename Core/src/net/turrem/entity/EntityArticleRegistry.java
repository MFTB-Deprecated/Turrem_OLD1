package net.turrem.entity;

import java.util.ArrayList;
import java.util.HashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.turrem.EnumSide;
import net.turrem.mod.INotedElementVisitor;

public class EntityArticleRegistry implements INotedElementVisitor
{
	public HashMap<String, SoftEntityArticle> softArticles = new HashMap<String, SoftEntityArticle>();
	public HashMap<String, SolidEntityArticle> solidArticles = new HashMap<String, SolidEntityArticle>();
	
	public final EnumSide side;
	
	public EntityArticleRegistry(EnumSide side)
	{
		this.side = side;
	}

	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element)
	{
		RegisterEntityArticle reg = (RegisterEntityArticle) annotation;
		Class<?> clas = (Class<?>) element;
		if (EntityArticle.class.isAssignableFrom(clas))
		{
			this.registerArticle(reg, clas.asSubclass(EntityArticle.class));
		}
		else
		{
			System.out.printf("Class %s has @RegisterEntityArticle, but does not extend EntityArticle.%n", clas.getName());
		}
	}
	
	public void registerArticle(RegisterEntityArticle annotation, Class<? extends EntityArticle> clas)
	{
		for (Method met : clas.getMethods())
		{
			if (met.isAnnotationPresent(EntityArticle.ArticleFactory.class))
			{
				try
				{
					this.callFactory(annotation.id(), met);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					System.out.printf("Method %s has @ArticleFactory, but threw %s when invoked.%n", met.getName(), e.getClass().getSimpleName());
				}
			}
		}
	}
	
	private void callFactory(String id, Method met) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (!Modifier.isStatic(met.getModifiers()))
		{
			System.out.printf("Method %s has @ArticleFactory, but is not static.%n", met.getName());
			return;
		}
		ArrayList<EntityArticle> outs = new ArrayList<EntityArticle>();
		if (met.getParameterTypes().length == 3)
		{
			if (this.areParametersValid(met.getParameterTypes(), outs.getClass(), id.getClass(), this.side.getClass()))
			{
				met.invoke(null, outs, id, this.side);
			}
			else
			{
				System.out.printf("Method %s has @ArticleFactory, but requires invalid parameters.%n", met.getName());
			}
		}
		else if (met.getParameterTypes().length == 2)
		{
			if (this.areParametersValid(met.getParameterTypes(), outs.getClass(), id.getClass()))
			{
				met.invoke(null, outs, id);
			}
			else if (this.areParametersValid(met.getParameterTypes(), outs.getClass(), this.side.getClass()))
			{
				met.invoke(null, outs, this.side);
			}
			else
			{
				System.out.printf("Method %s has @ArticleFactory, but requires invalid parameters.%n", met.getName());
			}
		}
		else if (met.getParameterTypes().length == 1)
		{
			if (this.areParametersValid(met.getParameterTypes(), outs.getClass()))
			{
				met.invoke(null, outs);
			}
			else
			{
				System.out.printf("Method %s has @ArticleFactory, but requires invalid parameters.%n", met.getName());
			}
		}
		else
		{
			System.out.printf("Method %s has @ArticleFactory, but requires %d parameters.%n", met.getName(), met.getParameterTypes().length);
		}
		for (EntityArticle out : outs)
		{
			this.addArticle(out);
		}
	}
	
	private void addArticle(EntityArticle art)
	{
		if (art instanceof SoftEntityArticle)
		{
			if (this.softArticles.put(art.getId(), (SoftEntityArticle) art) != null)
			{
				System.out.printf("A soft entity article with id %s was already registered, it will be overridden.%n", art.getId());
			}
		}
		else if (art instanceof SolidEntityArticle)
		{
			if (this.solidArticles.put(art.getId(), (SolidEntityArticle) art) != null)
			{
				System.out.printf("A solid entity article with id %s was already registered, it will be overridden.%n", art.getId());
			}
		}
	}
	
	public boolean areParametersValid(Class<?>[] take, Class<?>... pass)
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
}