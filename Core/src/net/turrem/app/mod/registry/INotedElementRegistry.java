package net.turrem.app.mod.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import net.turrem.app.mod.ModInstance;

public interface INotedElementRegistry
{
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod);
}
