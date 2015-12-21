package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

public class ServerProxyCondition
        extends ServerProxyObject<
        ConditionData,
        HousemateData<?>,
        ServerProxyObject<?, ?, ?, ?, ?>,
        ServerProxyCondition,
        Condition.Listener<? super ServerProxyCondition>>
        implements Condition<
        ServerProxyCommand,
        ServerProxyValue,
        ServerProxyProperty,
        ServerProxyValue,
        ServerProxyValue,
        ServerProxyList<PropertyData, ServerProxyProperty>,
        ServerProxyCommand,
        ServerProxyCondition,
        ServerProxyList<ConditionData, ServerProxyCondition>,
        ServerProxyCondition> {

    private ServerProxyCommand remove;
    private ServerProxyValue error;
    private ServerProxyValue satisfied;
    private ServerProxyProperty driverProperty;
    private ServerProxyValue driverLoaded;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;
    private ServerProxyCommand addCondition;
    private ServerProxyList<ConditionData, ServerProxyCondition> conditions;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyCondition(ListenersFactory listenersFactory,
                                ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                                @Assisted Logger logger,
                                @Assisted ConditionData data) {
        super(listenersFactory, objectFactory, logger, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (ServerProxyCommand) getChild(ConditionData.REMOVE_ID);
        error = (ServerProxyValue) getChild(ConditionData.ERROR_ID);
        satisfied = (ServerProxyValue) getChild(ConditionData.SATISFIED_ID);
        driverProperty = (ServerProxyProperty) getChild(ConditionData.DRIVER_ID);
        driverLoaded = (ServerProxyValue) getChild(ConditionData.DRIVER_LOADED_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(ConditionData.PROPERTIES_ID);
        addCondition = (ServerProxyCommand) getChild(ConditionData.ADD_CONDITION_ID);
        conditions = (ServerProxyList<ConditionData, ServerProxyCondition>) getChild(ConditionData.CONDITIONS_ID);
    }

    @Override
    public ServerProxyCommand getAddConditionCommand() {
        return addCondition;
    }

    @Override
    public ServerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public ServerProxyValue getSatisfiedValue() {
        return satisfied;
    }

    @Override
    public ServerProxyProperty getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ServerProxyValue getDriverLoadedValue() {
        return driverLoaded;
    }

    @Override
    public ServerProxyList<PropertyData, ServerProxyProperty> getProperties() {
        return properties;
    }

    @Override
    public ServerProxyList<ConditionData, ServerProxyCondition> getConditions() {
        return conditions;
    }

    @Override
    public ServerProxyCommand getRemoveCommand() {
        return remove;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof ConditionData) {
            getConditions().copyValues(data.getChildData(ConditionData.CONDITIONS_ID));
            getProperties().copyValues(data.getChildData(ConditionData.PROPERTIES_ID));
        }
    }
}
