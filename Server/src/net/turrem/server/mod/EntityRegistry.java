package net.turrem.server.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import net.turrem.mod.INotedElementVisitor;

public class EntityRegistry implements INotedElementVisitor
{
	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element)
	{
	}
}
