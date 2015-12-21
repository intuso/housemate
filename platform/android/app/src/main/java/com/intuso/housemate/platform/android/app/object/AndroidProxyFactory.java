package com.intuso.housemate.platform.android.app.object;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/01/14
 * Time: 18:47
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyFactory implements ObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final ListenersFactory listenersFactory;

    @Inject
    public AndroidProxyFactory(ListenersFactory listenersFactory) {
        this.listenersFactory = listenersFactory;
    }

    @Override
    public ProxyObject<?, ?, ?, ?, ?> create(Logger logger, HousemateData<?> data) {
        if(data instanceof ParameterData)
            return createParameter(logger, (ParameterData) data);
        else if(data instanceof CommandData)
            return createCommand(logger, (CommandData) data);
        else if(data instanceof ConditionData)
            return createCondition(logger, (ConditionData) data);
        else if(data instanceof ApplicationData)
            return createApplication(logger, (ApplicationData) data);
        else if(data instanceof ApplicationInstanceData)
            return createApplicationInstance(logger, (ApplicationInstanceData) data);
        else if(data instanceof UserData)
            return createUser(logger, (UserData) data);
        else if(data instanceof TaskData)
            return createTask(logger, (TaskData) data);
        else if(data instanceof DeviceData)
            return createDevice(logger, (DeviceData) data);
        else if(data instanceof FeatureData)
            return createFeature(logger, (FeatureData) data);
        else if(data instanceof HardwareData)
            return createHardware(logger, (HardwareData) data);
        else if(data instanceof ListData)
            return this.<HousemateData<?>, ProxyObject<HousemateData<?>, ?, ?, ?, ?>>createList(logger, (ListData<HousemateData<?>>) data);
        else if(data instanceof OptionData)
            return createOption(logger, (OptionData) data);
        else if(data instanceof PropertyData)
            return createProperty(logger, (PropertyData) data);
        else if(data instanceof AutomationData)
            return createAutomation(logger, (AutomationData) data);
        else if(data instanceof ServerData)
            return createServer(logger, (ServerData) data);
        else if(data instanceof SubTypeData)
            return createSubType(logger, (SubTypeData) data);
        else if(data instanceof TypeData)
            return createType(logger, (TypeData) data);
        else if(data instanceof ValueData)
            return createValue(logger, (ValueData) data);
        logger.error("Don't know how to create an object from " + data.getClass().getName());
        return null;
    }

    public AndroidProxyParameter createParameter(Logger logger, ParameterData data) {
        return new AndroidProxyParameter(logger, listenersFactory, data, this);
    }

    public AndroidProxyCommand createCommand(Logger logger, CommandData data) {
        return new AndroidProxyCommand(logger, listenersFactory, data, this);
    }

    public AndroidProxyCondition createCondition(Logger logger, ConditionData data) {
        return new AndroidProxyCondition(logger, listenersFactory, data, this);
    }

    public AndroidProxyApplication createApplication(Logger logger, ApplicationData data) {
        return new AndroidProxyApplication(logger, listenersFactory, data, this);
    }

    public AndroidProxyApplicationInstance createApplicationInstance(Logger logger, ApplicationInstanceData data) {
        return new AndroidProxyApplicationInstance(logger, listenersFactory, data, this);
    }

    public AndroidProxyUser createUser(Logger logger, UserData data) {
        return new AndroidProxyUser(logger, listenersFactory, data, this);
    }

    public AndroidProxyHardware createHardware(Logger logger, HardwareData data) {
        return new AndroidProxyHardware(logger, listenersFactory, data, this);
    }

    public AndroidProxyTask createTask(Logger logger, TaskData data) {
        return new AndroidProxyTask(logger, listenersFactory, data, this);
    }

    public AndroidProxyDevice createDevice(Logger logger, DeviceData data) {
        return new AndroidProxyDevice(logger, listenersFactory, data, this);
    }

    public AndroidProxyFeature createFeature(Logger logger, FeatureData data) {
        return new AndroidProxyFeature(logger, listenersFactory, data, this);
    }

    public <CHILD_DATA extends HousemateData<?>, CHILD extends ProxyObject<CHILD_DATA, ?, ?, ?, ?>>
                AndroidProxyList<CHILD_DATA, CHILD> createList(Logger logger, ListData<CHILD_DATA> data) {
        return new AndroidProxyList<>(logger, listenersFactory, data, this);
    }

    public AndroidProxyOption createOption(Logger logger, OptionData data) {
        return new AndroidProxyOption(logger, listenersFactory, data, this);
    }

    public AndroidProxyProperty createProperty(Logger logger, PropertyData data) {
        return new AndroidProxyProperty(logger, listenersFactory, data, this);
    }

    public AndroidProxyAutomation createAutomation(Logger logger, AutomationData data) {
        return new AndroidProxyAutomation(logger, listenersFactory, data, this);
    }

    public AndroidProxyServer createServer(Logger logger, ServerData data) {
        return new AndroidProxyServer(logger, listenersFactory, data, this);
    }

    public AndroidProxySubType createSubType(Logger logger, SubTypeData data) {
        return new AndroidProxySubType(logger, listenersFactory, data, this);
    }

    public AndroidProxyValue createValue(Logger logger, ValueData data) {
        return new AndroidProxyValue(logger, listenersFactory, data);
    }

    public AndroidProxyType createType(Logger logger, TypeData<HousemateData<?>> data) {
        return new AndroidProxyType(logger, listenersFactory, data, this);
    }
}
