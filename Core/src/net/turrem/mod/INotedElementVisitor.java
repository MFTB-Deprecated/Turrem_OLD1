package net.turrem.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface INotedElementVisitor
{
	public void visitElement(Annotation annotation, AnnotatedElement element, ModInstance mod);
}
