package net.turrem.mod.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import net.turrem.mod.ModInstance;

public interface INotedElementRegister
{
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod);
}
