package com.intuso.housemate.plugin.api;

import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Descriptor of what features a plugin provides
 */
public interface PluginDescriptor {

    public static String MANIFEST_ATTRIBUTE = "Housemate-Plugin";

    /**
     * Gets the id of the plugin
     * @return the id of the plugin
     */
    public String getId();

    /**
     * Gets the name of the plugin
     * @return the name of the plugin
     */
    public String getName();

    /**
     * Gets the description of the plugin
     * @return the description of the plugin
     */
    public String getDescription();

    /**
     * Gets the author of the plugin
     * @return the author of the plugin
     */
    public String getAuthor();

    /**
     * Initialises the plugin
     * @param log the log to use
     * @param injector the injector to use
     * @throws HousemateException if the plugin cannot be initialised
     */
    public void init(Log log, Injector injector) throws HousemateException;

    /**
     * Gets the types provided by this plugin
     * @param log the log to use
     * @return the types provided by this plugin
     */
    public List<RealType<?, ?, ?>> getTypes(Log log);

    /**
     * Gets the comparators provided by this plugin
     * @param log the log to use
     * @return the comparators provided by this plugin
     */
    public List<Comparator<?>> getComparators(Log log);

    /**
     * Gets the operators provided by this plugin
     * @param log the log to use
     * @return the operators provided by this plugin
     */
    public List<Operator<?, ?>> getOperators(Log log);

    /**
     * Gets the transformers provided by this plugin
     * @param log the log to use
     * @return the transformers provided by this plugin
     */
    public List<Transformer<?, ?>> getTransformers (Log log);

    /**
     * Gets the device factories provided by this plugin
     * @return the device factories provided by this plugin
     */
    public List<RealDeviceFactory<?>> getDeviceFactories();

    /**
     * Gets the condition factories provided by this plugin
     * @return the condition factories provided by this plugin
     */
    public List<ServerConditionFactory<?>> getConditionFactories();

    /**
     * Gets the task factories provided by this plugin
     * @return the task factories provided by this plugin
     */
    public List<ServerTaskFactory<?>> getTaskFactories();
}
