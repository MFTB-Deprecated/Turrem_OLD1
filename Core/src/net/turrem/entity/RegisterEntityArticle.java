package net.turrem.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.turrem.mod.registry.Registrable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Registrable
public @interface RegisterEntityArticle
{
	public String id();
}
