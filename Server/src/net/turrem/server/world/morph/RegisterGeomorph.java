package net.turrem.server.world.morph;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.turrem.mod.Visitable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Visitable
public @interface RegisterGeomorph
{
	public String id();
}
