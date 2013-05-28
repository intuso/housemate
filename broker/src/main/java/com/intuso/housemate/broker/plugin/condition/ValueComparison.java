package com.intuso.housemate.broker.plugin.condition;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.bridge.ValueBridgeBase;
import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Condition which is true iff the current day of the week matches
 * those specified by the user
 * @author tclabon
 *
 */
public class ValueComparison
        extends BrokerRealCondition
        implements ObjectLifecycleListener, ValueListener<ValueBridgeBase<?, ?, ?, ?>> {

    private final BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>> valuePath;
    private final BrokerRealProperty<String> compareTo;
    private ValueBridgeBase<?, ?, ?, ? extends ValueBridgeBase<?, ?, ?, ?>> value;
    private ListenerRegistration<ObjectLifecycleListener> valueLifecycleListenerRegistration = null;
    private ListenerRegistration<? super ValueListener<ValueBridgeBase<?, ?, ?, ?>>> valueListenerRegistration = null;
    private boolean running;

    private final Root<?, ?> root;

    /**
	 * Create a new day of the week condition
     * @param name
	 * @throws com.intuso.housemate.api.HousemateException
	 */
	public ValueComparison(BrokerRealResources resources, String id, String name, String description, Root<?, ?> root) throws HousemateException {
        super(resources, id, name, description);
        this.root = root;
        valuePath = new BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>>(resources, "value-path", "Value Path", "The path to the value to compare",
                new RealObjectType(resources.getRealResources(), root), null);
        compareTo = new BrokerRealProperty<String>(resources, "comparison-value", "Comparison Value", "The value to compare to",
                new StringType(resources.getRealResources()), null);
        getProperties().add(valuePath);
        getProperties().add(compareTo);
        addValuePathListener();
        addCompareToListener();
    }

    private void addValuePathListener() {
        valuePath.addObjectListener(new ValueListener<BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>>>() {
            @Override
            public void valueChanged(BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>> property) {
                if(valueLifecycleListenerRegistration != null)
                    valueLifecycleListenerRegistration.removeListener();
                String[] path = property.getTypedValue().getPath();
                valueLifecycleListenerRegistration = root.addObjectLifecycleListener(path, ValueComparison.this);
                HousemateObject<?, ?, ?, ?, ?> object = root.getWrapper(path);
                if(object == null)
                    setError("Cannot find an object at path " + valuePath.getValue());
                else
                    objectCreated(path, object);
            }
        });
        String[] path = valuePath.getTypedValue().getPath();
        if(path != null)
            valueLifecycleListenerRegistration = root.addObjectLifecycleListener(path, ValueComparison.this);
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