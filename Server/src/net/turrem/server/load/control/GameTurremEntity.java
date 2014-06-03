package net.turrem.server.load.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
/**
 * Only for classes in server.jar
 */
public @interface GameTurremEntity
{

}
