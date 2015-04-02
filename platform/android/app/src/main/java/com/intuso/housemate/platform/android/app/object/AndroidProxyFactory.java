package com.intuso.housemate.platform.android.app.object;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/01/14
 * Time: 18:47
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyFactory implements HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final AndroidProxyFeatureFactory featureFactory = new AndroidProxyFeatureFactory();

    @Inject
    public AndroidProxyFactory(Log log, ListenersFactory listenersFactory) {
        this.log = log;
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
        else if(data instanceof RealClientData)
            return createRealClient((RealClientData) data);
        else if(data instanceof SubTypeData)
            return createSubType((SubTypeData) data);
        else if(data instanceof TypeData)
            return createType((TypeData) data);
        else if(data instanceof ValueData)
            return createValue((ValueData) data);
        log.e("Don't know how to create an object from " + data.getClass().getName());
        return null;
    }

    public AndroidProxyParameter createParameter(ParameterData data) {
        return new AndroidProxyParameter(log, listenersFactory, data, this);
    }

    public AndroidProxyCommand createCommand(CommandData data) {
        return new AndroidProxyCommand(log, listenersFactory, data, this);
    }

    public AndroidProxyCondition createCondition(ConditionData data) {
        return new AndroidProxyCondition(log, listenersFactory, data, this);
    }

    public AndroidProxyApplication createApplication(ApplicationData data) {
        return new AndroidProxyApplication(log, listenersFactory, data, this);
    }

    public AndroidProxyApplicationInstance createApplicationInstance(ApplicationInstanceData data) {
        return new AndroidProxyApplicationInstance(log, listenersFactory, data, this);
    }

    public AndroidProxyUser createUser(UserData data) {
        return new AndroidProxyUser(log, listenersFactory, data, this);
    }

    public AndroidProxyHardware createHardware(HardwareData data) {
        return new AndroidProxyHardware(log, listenersFactory, data, this);
    }

    public AndroidProxyTask createTask(TaskData data) {
        return new AndroidProxyTask(log, listenersFactory, data, this);
    }

    public AndroidProxyDevice createDevice(DeviceData data) {
        return new AndroidProxyDevice(log, listenersFactory, data, this);
    }

    public <CHILD_DATA extends HousemateData<?>, CHILD extends ProxyObject<CHILD_DATA, ?, ?, ?, ?>>
                AndroidProxyList<CHILD_DATA, CHILD> createList(ListData<CHILD_DATA> data) {
        return new AndroidProxyList<>(log, listenersFactory, data, this);
    }

    public AndroidProxyOption createOption(OptionData data) {
        return new AndroidProxyOption(log, listenersFactory, data, this);
    }

    public AndroidProxyProperty createProperty(PropertyData data) {
        return new AndroidProxyProperty(log, listenersFactory, data, this);
    }

    public AndroidProxyAutomation createAutomation(AutomationData data) {
        return new AndroidProxyAutomation(log, listenersFactory, data, this);
    }

    public AndroidProxyRealClient createRealClient(RealClientData data) {
        return new AndroidProxyRealClient(log, listenersFactory, data, this);
    }

    public AndroidProxySubType createSubType(SubTypeData data) {
        return new AndroidProxySubType(log, listenersFactory, data, this);
    }

    public AndroidProxyValue createValue(ValueData data) {
        return new AndroidProxyValue(log, listenersFactory, data);
    }

    public AndroidProxyType createType(TypeData<HousemateData<?>> data) {
        return new AndroidProxyType(log, listenersFactory, data, this);
    }

    public <F extends AndroidProxyFeature> F createFeature(AndroidProxyDevice androidProxyDevice, String featureId) {
        return featureFactory.getFeature(featureId, androidProxyDevice);
    }
}
