package net.turrem.server.load.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.turrem.server.network.client.ClientPacket;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface SubscribePacketByClass
{
	Class<? extends ClientPacket> typeClass();
	boolean review() default false;
}
