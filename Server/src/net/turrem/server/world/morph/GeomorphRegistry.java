package net.turrem.server.world.morph;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import net.turrem.mod.INotedElementVisitor;
import net.turrem.mod.ModInstance;

public class GeomorphRegistry implements INotedElementVisitor
{
	@Override
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod)
	{
	}
}
