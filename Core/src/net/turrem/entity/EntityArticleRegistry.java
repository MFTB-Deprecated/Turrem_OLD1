package net.turrem.entity;

import java.util.HashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import net.turrem.mod.INotedElementVisitor;

public class EntityArticleRegistry implements INotedElementVisitor
{
	public HashMap<String, Class<? extends SoftEntityArticle>> softArticles = new HashMap<String, Class<? extends SoftEntityArticle>>();
	public HashMap<String, Class<? extends SolidEntityArticle>> solidArticles = new HashMap<String, Class<? extends SolidEntityArticle>>();
	
	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element)
	{
		RegisterEntityArticle reg = (RegisterEntityArticle) annotation;
		Class<?> clas = (Class<?>) element;
		if (SoftEntityArticle.class.isAssignableFrom(clas))
		{
			this.registerSoftArticle(reg, clas.asSubclass(SoftEntityArticle.class));
		}
		else if (SolidEntityArticle.class.isAssignableFrom(clas))
		{
			this.registerSolidArticle(reg, clas.asSubclass(SolidEntityArticle.class));
		}
		else
		{
			System.out.printf("Class %s has @RegisterEntityArticle, but does not extend any EntityArticle classes.%n", clas.getName());
		}
	}
	
	public void registerSoftArticle(RegisterEntityArticle annotation, Class<? extends SoftEntityArticle> article)
	{
		if (this.softArticles.put(annotation.id(), article) != null)
		{
			System.out.printf("A soft entity article with id %s was already registered, it will be overwritten.%n", annotation.id());
		}
	}
	
	public void registerSolidArticle(RegisterEntityArticle annotation, Class<? extends SolidEntityArticle> article)
	{
		if (this.solidArticles.put(annotation.id(), article) != null)
		{
			System.out.printf("A solid entity article with id %s was already registered, it will be overwritten.%n", annotation.id());
		}
	}
}
