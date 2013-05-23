package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:00
 * To change this template use File | Settings | File Templates.
 */
public interface PluginDescriptor {

    public static String MANIFEST_ATTRIBUTE = "Housemate-Plugin";

    public String getId();
    public String getDescription();
    public String getAuthor();
    public void init(Resources resources);
    public List<RealType<?, ?, ?>> getTypes(RealResources resources);
    public List<RealDeviceFactory<?>> getDeviceFactories();
    public List<BrokerConditionFactory<?>> getConditionFactories();
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories();
}
