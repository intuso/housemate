package com.intuso.housemate.platform.android.app.object;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import com.intuso.utilities.object.ObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/01/14
 * Time: 18:47
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyFactory implements ObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final Logger logger;
    private final ListenersFactory listenersFactory;

    @Inject
    public AndroidProxyFactory(Logger logger, ListenersFactory listenersFactory) {
        this.logger = logger;
        this.listenersFactory = listenersFactory;
    }

    @Override
    public ProxyObject<?, ?, ?, ?, ?> create(HousemateData<?> data) {
        if(data instanceof ParameterData)
            return createParameter((ParameterData) data);
        else if(data instanceof CommandData)
            return createCommand((CommandData) data);
        else if(data instanceof ConditionData)
            return createCondition((ConditionData) data);
        else if(data instanceof ApplicationData)
            return createApplication((ApplicationData) data);
        else if(data instanceof ApplicationInstanceData)
            return createApplicationInstance((ApplicationInstanceData) data);
        else if(data instanceof UserData)
            return createUser((UserData) data);
        else if(data instanceof TaskData)
            return createTask((TaskData) data);
        else if(data instanceof DeviceData)
            return createDevice((DeviceData) data);
        else if(data instanceof FeatureData)
            return createFeature((FeatureData) data);
        else if(data instanceof HardwareData)
            return createHardware((HardwareData) data);
        else if(data instanceof ListData)
            return this.<HousemateData<?>, ProxyObject<HousemateData<?>, ?, ?, ?, ?>>createList((ListData<HousemateData<?>>) data);
        else if(data instanceof OptionData)
            return createOption((OptionData) data);
        else if(data instanceof PropertyData)
            return createProperty((PropertyData) data);
        else if(data instanceof AutomationData)
            return createAutomation((AutomationData) data);
        else if(data instanceof ServerData)
            return createServer((ServerData) data);
        else if(data instanceof SubTypeData)
            return createSubType((SubTypeData) data);
        else if(data instanceof TypeData)
            return createType((TypeData) data);
        else if(data instanceof ValueData)
            return createValue((ValueData) data);
        logger.error("Don't know how to create an object from " + data.getClass().getName());
        return null;
    }

    public AndroidProxyParameter createParameter(ParameterData data) {
        return new AndroidProxyParameter(logger, listenersFactory, data, this);
    }

    public AndroidProxyCommand createCommand(CommandData data) {
        return new AndroidProxyCommand(logger, listenersFactory, data, this);
    }

    public AndroidProxyCondition createCondition(ConditionData data) {
        return new AndroidProxyCondition(logger, listenersFactory, data, this);
    }

    public AndroidProxyApplication createApplication(ApplicationData data) {
        return new AndroidProxyApplication(logger, listenersFactory, data, this);
    }

    public AndroidProxyApplicationInstance createApplicationInstance(ApplicationInstanceData data) {
        return new AndroidProxyApplicationInstance(logger, listenersFactory, data, this);
    }

    public AndroidProxyUser createUser(UserData data) {
        return new AndroidProxyUser(logger, listenersFactory, data, this);
    }

    public AndroidProxyHardware createHardware(HardwareData data) {
        return new AndroidProxyHardware(logger, listenersFactory, data, this);
    }

    public AndroidProxyTask createTask(TaskData data) {
        return new AndroidProxyTask(logger, listenersFactory, data, this);
    }

    public AndroidProxyDevice createDevice(DeviceData data) {
        return new AndroidProxyDevice(logger, listenersFactory, data, this);
    }

    public AndroidProxyFeature createFeature(FeatureData data) {
        return new AndroidProxyFeature(logger, listenersFactory, data, this);
    }

    public <CHILD_DATA extends HousemateData<?>, CHILD extends ProxyObject<CHILD_DATA, ?, ?, ?, ?>>
                AndroidProxyList<CHILD_DATA, CHILD> createList(ListData<CHILD_DATA> data) {
        return new AndroidProxyList<>(logger, listenersFactory, data, this);
    }

    public AndroidProxyOption createOption(OptionData data) {
        return new AndroidProxyOption(logger, listenersFactory, data, this);
    }

    public AndroidProxyProperty createProperty(PropertyData data) {
        return new AndroidProxyProperty(logger, listenersFactory, data, this);
    }

    public AndroidProxyAutomation createAutomation(AutomationData data) {
        return new AndroidProxyAutomation(logger, listenersFactory, data, this);
    }

    public AndroidProxyServer createServer(ServerData data) {
        return new AndroidProxyServer(logger, listenersFactory, data, this);
    }

    public AndroidProxySubType createSubType(SubTypeData data) {
        return new AndroidProxySubType(logger, listenersFactory, data, this);
    }

    public AndroidProxyValue createValue(ValueData data) {
        return new AndroidProxyValue(logger, listenersFactory, data);
    }

    public AndroidProxyType createType(TypeData<HousemateData<?>> data) {
        return new AndroidProxyType(logger, listenersFactory, data, this);
    }
}
