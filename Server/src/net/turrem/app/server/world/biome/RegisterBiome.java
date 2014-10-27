package net.turrem.app.server.world.biome;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.turrem.app.mod.registry.Registrable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Registrable
public @interface RegisterBiome
{
	public String id();
}
