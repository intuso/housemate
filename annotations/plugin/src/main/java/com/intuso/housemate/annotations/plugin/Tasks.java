package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.object.server.real.ServerRealTask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the tasks with simple constructors that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tasks {

    /**
     * The list of classes of tasks with simple constructors that the plugin provides
     * @return the list of classes of tasks with simple constructors that the plugin provides
     */
    Class<? extends ServerRealTask>[] value();
}
