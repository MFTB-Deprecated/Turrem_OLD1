package net.turrem.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class NotedElementRegistry<A extends Annotation, E extends AnnotatedElement> implements INotedElementVisitor
{	
	@SuppressWarnings("unchecked")
	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element)
	{
		try
		{
			this.regesterElement((A) annotation, (E) element); 
		}
		catch (ClassCastException e)
		{
			
		}
	}
	
	public abstract void regesterElement(A annotation, E element);
}
