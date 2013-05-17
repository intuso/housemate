package com.intuso.housemate.broker.object.real.condition;

import com.intuso.housemate.broker.object.bridge.BridgeObject;
import com.intuso.housemate.broker.object.bridge.ValueBridgeBase;
import com.intuso.housemate.broker.object.real.BrokerRealProperty;
import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.core.object.ObjectLifecycleListener;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.real.impl.type.RealObjectType;
import com.intuso.housemate.real.impl.type.StringType;
import com.intuso.listeners.ListenerRegistration;

/**
 * Condition which is true iff the current day of the week matches
 * those specified by the user
 * @author tclabon
 *
 */
public class ValueComparison
        extends BrokerRealCondition
        implements ObjectLifecycleListener, ValueListener<ValueBridgeBase<?, ?, ?, ?>> {

    private final BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>> valuePath;
    private final BrokerRealProperty<String> compareTo;
    private ValueBridgeBase<?, ?, ?, ? extends ValueBridgeBase<?, ?, ?, ?>> value;
    private ListenerRegistration<ObjectLifecycleListener> valueLifecycleListenerRegistration = null;
    private ListenerRegistration<? super ValueListener<ValueBridgeBase<?, ?, ?, ?>>> valueListenerRegistration = null;
    private boolean running;

    /**
	 * Create a new day of the week condition
     * @param name
	 * @throws com.intuso.housemate.core.HousemateException
	 */
	public ValueComparison(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        super(resources, id, name, description);
        valuePath = new BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>>(resources, "value-path", "Value Path", "The path to the value to compare",
                new RealObjectType(resources.getGeneralResources().getClientResources(), resources.getGeneralResources().getBridgeResources().getRoot()), null);
        compareTo = new BrokerRealProperty<String>(resources, "comparison-value", "Comparison Value", "The value to compare to",
                new StringType(resources.getGeneralResources().getClientResources()), null);
        getProperties().add(valuePath);
        getProperties().add(compareTo);
        addValuePathListener();
        addCompareToListener();
    }

    private void addValuePathListener() {
        valuePath.addObjectListener(new ValueListener<BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>>>() {
            @Override
            public void valueChanged(BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>> property) {
                if(valueLifecycleListenerRegistration != null)
                    valueLifecycleListenerRegistration.removeListener();
                String[] path = property.getTypedValue().getPath();
                valueLifecycleListenerRegistration = getResources().getGeneralResources().getBridgeResources().getRoot().addObjectLifecycleListener(path, ValueComparison.this);
                HousemateObject<?, ?, ?, ?, ?> object = getResources().getGeneralResources().getBridgeResources().getRoot().getWrapper(path);
                if(object == null)
                    setError("Cannot find an object at path " + valuePath.getValue());
                else
                    objectCreated(path, object);
            }
        });
        String[] path = valuePath.getTypedValue().getPath();
        if(path != null)
            valueLifecycleListenerRegistration = getResources().getGeneralResources().getBridgeResources().getRoot().addObjectLifecycleListener(path, ValueComparison.this);
    }

    private void addCompareToListener() {
        compareTo.addObjectListener(new ValueListener<BrokerRealProperty<String>>() {
            @Override
            public void valueChanged(BrokerRealProperty<String> property) {
                if(value != null)
                    ValueComparison.this.valueChanged(value);
            }
        });
    }

    @Override
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(valueListenerRegistration != null)
            valueListenerRegistration.removeListener();
        if(!(object instanceof ValueBridgeBase))
            setError("Object at path " + valuePath.getTypedValue() + " is not a value");
        else {
            setError(null);
            value = (ValueBridgeBase)object;
            if(running)
                valueListenerRegistration = value.addObjectListener(this);
        }
    }

    @Override
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
        value = null;
        if(valueListenerRegistration != null)
            valueListenerRegistration.removeListener();
        setError("Cannot find an object at path " + valuePath.getTypedValue());
    }

    @Override
    public void valueChanged(ValueBridgeBase<?, ?, ?, ?> value) {
        if(value != null)
            conditionSatisfied(value.getValue().equals(compareTo.getValue()));
    }

    @Override
	public void start() {
        running = true;
		if(value != null)
            valueListenerRegistration = value.addObjectListener(this);
	}
	
	@Override
	public void stop() {
        running = false;
        if(valueListenerRegistration != null)
            valueListenerRegistration.removeListener();
	}
}