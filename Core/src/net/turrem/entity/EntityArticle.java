package net.turrem.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.turrem.EnumSide;
import net.turrem.mod.ModInstance;

abstract class EntityArticle
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected static @interface ArticleFactory
	{
		
	}
	
	EnumSide side = null;
	private final String id;
	ModInstance mod = null;
	
	public EntityArticle(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return this.mod.identifier + ":" + this.id;
	}
	
	public String getInternalId()
	{
		return this.id;
	}
	
	public ModInstance getMod()
	{
		return this.mod;
	}
	
	public EnumSide getSide()
	{
		return this.side;
	}
}
