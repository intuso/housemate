package com.intuso.housemate.broker.plugin;

import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.real.RealType;

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
    public void init(BrokerGeneralResources resources);
    public List<RealType<?, ?, ?>> getTypes();
    public List<RealDeviceFactory<?>> getDeviceFactories();
    public List<BrokerConditionFactory<?>> getConditionFactories();
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories();
}
