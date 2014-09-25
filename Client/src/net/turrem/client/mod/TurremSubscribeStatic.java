package net.turrem.client.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.turrem.mod.Visitable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Visitable
public @interface TurremSubscribeStatic
{
	public EnumTurremEvent event();
}
