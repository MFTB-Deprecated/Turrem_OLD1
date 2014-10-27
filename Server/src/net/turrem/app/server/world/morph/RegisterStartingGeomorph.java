package net.turrem.app.server.world.morph;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.turrem.app.mod.registry.Registrable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Registrable
public @interface RegisterStartingGeomorph
{
	
}
