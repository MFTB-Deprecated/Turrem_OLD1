package net.turrem.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.lang.annotation.Annotation;

import net.turrem.EnumSide;
import net.turrem.mod.ModInstance;
import net.turrem.mod.registry.NotedElementWithFactoryRegistry;

public class EntityArticleRegistry extends NotedElementWithFactoryRegistry
{
	private final static List<Class<?>[]> valadParameters = new ArrayList<Class<?>[]>();
	
	public HashMap<String, SoftEntityArticle> softArticles = new HashMap<String, SoftEntityArticle>();
	public HashMap<String, SolidEntityArticle> solidArticles = new HashMap<String, SolidEntityArticle>();
	
	public final EnumSide side;
	
	public EntityArticleRegistry(EnumSide side)
	{
		super(EntityArticle.class);
		this.side = side;
	}
	
	@Override
	protected List<Class<?>[]> getPossibleFactoryParameters()
	{
		return valadParameters;
	}
	
	@Override
	protected Object[] getArgs(int argsType, Annotation annotation, ModInstance mod)
	{
		RegisterEntityArticle reg = (RegisterEntityArticle) annotation;
		switch (argsType)
		{
			case 0:
				return new Object[] {};
			case 1:
				return new Object[] { this.side };
			case 2:
				return new Object[] { this.side, reg.id() };
			case 3:
				return new Object[] { reg.id() };
			default:
				return new Object[] {};
		}
	}
	
	@Override
	protected void addItem(Object item, ModInstance mod)
	{
		EntityArticle art = (EntityArticle) item;
		art.mod = mod;
		art.side = this.side;
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
	
	static
	{
		valadParameters.add(new Class<?>[] {});
		valadParameters.add(new Class<?>[] { EnumSide.class });
		valadParameters.add(new Class<?>[] { EnumSide.class, String.class });
		valadParameters.add(new Class<?>[] { String.class });
	}
}