package com.intuso.housemate.object.broker.real.consequence;

import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealObject;
import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealValue;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.consequence.Consequence;
import com.intuso.housemate.api.object.consequence.ConsequenceListener;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class BrokerRealConsequence
        extends BrokerRealObject<ConsequenceWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>,
            ConsequenceListener<? super BrokerRealConsequence>>
        implements Consequence<BrokerRealProperty<String>, BrokerRealValue<Boolean>, BrokerRealValue<String>,
            BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>, BrokerRealConsequence> {

    private BrokerRealValue<String> errorValue;
    private BrokerRealValue<Boolean> executingValue;
    private BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> propertyList;

    public BrokerRealConsequence(BrokerRealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<BrokerRealProperty<?>>(0));
    }

    public BrokerRealConsequence(BrokerRealResources resources, String id, String name, String description, java.util.List<BrokerRealProperty<?>> properties) {
        super(resources, new ConsequenceWrappable(id, name, description));
        errorValue = new BrokerRealValue<String>(resources, ERROR, ERROR, "The current error", new StringType(resources.getRealResources()), null);
        executingValue = new BrokerRealValue<Boolean>(resources, EXECUTING, EXECUTING, "Whether the consequence is executing", new BooleanType(resources.getRealResources()), false);
        propertyList = new BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>(resources, PROPERTIES, PROPERTIES, "The consequence's properties", properties);
        addWrapper(errorValue);
        addWrapper(executingValue);
        addWrapper(propertyList);
    }

    @Override
    public BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public BrokerRealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getTypedValue();
    }

    @Override
    public BrokerRealValue<Boolean> getExecutingValue() {
        return executingValue;
    }

    @Override
    public boolean isExecuting() {
        return executingValue.getTypedValue() != null ? executingValue.getTypedValue() : false;
    }

    public final void setError(String error) {
        getErrorValue().setTypedValue(error);
    }

    protected void consequenceExecuting(boolean executing) {
        if(executing != isExecuting()) {
            getExecutingValue().setTypedValue(executing);
            for(ConsequenceListener listener : getObjectListeners())
                listener.consequenceExecuting(this, executing);
        }
    }

    public void execute() throws HousemateException {
        getLog().d("Performing consequence " + getId());
        _execute();
    }

    public abstract void _execute() throws HousemateException;
}
