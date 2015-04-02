package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class ServerProxyCondition
        extends ServerProxyObject<
            ConditionData,
            HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyCondition,
            ConditionListener<? super ServerProxyCondition>>
        implements Condition<
                    ServerProxyCommand,
                    ServerProxyValue,
                    ServerProxyValue,
                    ServerProxyList<PropertyData, ServerProxyProperty>,
                    ServerProxyCommand,
                    ServerProxyCondition,
                    ServerProxyList<ConditionData, ServerProxyCondition>> {

    private ServerProxyCommand remove;
    private ServerProxyValue error;
    private ServerProxyValue satisfied;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;
    private ServerProxyCommand addCondition;
    private ServerProxyList<ConditionData, ServerProxyCondition> conditions;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyCondition(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted ConditionData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (ServerProxyCommand) getChild(REMOVE_ID);
        error = (ServerProxyValue) getChild(ERROR_ID);
        satisfied = (ServerProxyValue) getChild(SATISFIED_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(PROPERTIES_ID);
        addCondition = (ServerProxyCommand) getChild(ADD_CONDITION_ID);
        conditions = (ServerProxyList<ConditionData, ServerProxyCondition>) getChild(CONDITIONS_ID);
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
    public String getError() {
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, error.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ServerProxyValue getSatisfiedValue() {
        return satisfied;
    }

    @Override
    public boolean isSatisfied() {
        List<Boolean> isSatisfieds = RealType.deserialiseAll(BooleanType.SERIALISER, satisfied.getTypeInstances());
        return isSatisfieds != null && isSatisfieds.size() > 0 && isSatisfieds.get(0) != null ? isSatisfieds.get(0) : false;
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
            getConditions().copyValues(data.getChildData(CONDITIONS_ID));
            getProperties().copyValues(data.getChildData(PROPERTIES_ID));
        }
    }
}
