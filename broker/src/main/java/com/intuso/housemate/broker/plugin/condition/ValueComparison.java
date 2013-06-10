package com.intuso.housemate.broker.plugin.condition;

import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.plugin.type.ValueAvailableListener;
import com.intuso.housemate.broker.plugin.type.ValueSource;
import com.intuso.housemate.broker.plugin.type.ValueSourceType;
import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Condition which is true iff the current day of the week matches
 * those specified by the user
 * @author tclabon
 *
 */
@FactoryInformation(id = "value-comparison", name = "Value Comparison", description = "Compare a value")
public class ValueComparison extends BrokerRealCondition {

    private final int NUM_SOURCES = 2;

    private final BrokerRealProperty<ValueSource>[] sources = new BrokerRealProperty[NUM_SOURCES];
    private Value<?, ?>[] values = new Value<?, ?>[NUM_SOURCES];
    private final PropertyListener[] propertyListeners = new PropertyListener[NUM_SOURCES];
    private ListenerRegistration[] valueListenerRegistrations = new ListenerRegistration[NUM_SOURCES];

    /**
	 * Create a new day of the week condition
     * @param name
	 * @throws com.intuso.housemate.api.HousemateException
	 */
	public ValueComparison(BrokerRealResources resources, String id, String name, String description, Root<?, ?> root) throws HousemateException {
        super(resources, id, name, description);
        for(int i = 0; i < NUM_SOURCES; i++) {
            sources[i] = new BrokerRealProperty<ValueSource>(resources, "value-" + i, "Value " + i, "Value to compare",
                    new ValueSourceType(resources.getRealResources(), root), null);
            getProperties().add(sources[i]);
            propertyListeners[i] = new PropertyListener(i);
        }
    }

    @Override
	public void start() {
        for(int i = 0; i < NUM_SOURCES; i++) {
            valueListenerRegistrations[i] = sources[i].addObjectListener(propertyListeners[i]);
            propertyListeners[i].start();
        }
        compare();
	}
	
	@Override
	public void stop() {
        for(int i = 0; i < NUM_SOURCES; i++) {
            if(valueListenerRegistrations[i] != null)
                valueListenerRegistrations[i].removeListener();
            propertyListeners[i].stop();
        }
	}

    private void compare() {
        for(int i = 0; i < NUM_SOURCES; i++) {
            if(values[i] == null) {
                setError("Value " + i + " is not available");
                return;
            }
        }
        // todo compare properly!
        TypeInstance compareTo = values[0].getTypeInstance();
        for(int i = 1; i < NUM_SOURCES; i++) {
            if((compareTo == null && values[i].getTypeInstance() == null)
                    || (compareTo != null && compareTo.equals(values[i].getTypeInstance()))) {
                continue;
            } else {
                conditionSatisfied(false);
                return;
            }
        }
        conditionSatisfied(true);
    }

    private class PropertyListener
            implements ValueListener<BrokerRealProperty<ValueSource>>, ValueAvailableListener {

        private final int index;
        private final ValueChangedListener valueListener;

        private ListenerRegistration valueAvailableListenerRegistration = null;
        private ListenerRegistration valueListenerRegistration = null;

        private PropertyListener(int index) {
            this.index = index;
            valueListener = new ValueChangedListener(index);
        }

        @Override
        public void valueChanging(BrokerRealProperty<ValueSource> value) {
            stop();
        }

        private void stop() {
            if(valueAvailableListenerRegistration != null)
                valueAvailableListenerRegistration.removeListener();
            if(valueListenerRegistration != null)
                valueListenerRegistration.removeListener();
        }

        @Override
        public void valueChanged(BrokerRealProperty<ValueSource> value) {
            start();
        }

        private void start() {
            ValueSource source = sources[index].getTypedValue();
            if(source != null)
                valueAvailableListenerRegistration = source.addValueAvailableListener(this, true);
        }

        @Override
        public void valueAvailable(ValueSource source, Value<?, ?> value) {
            values[index] = value;
            if(valueListenerRegistration != null)
                valueListenerRegistration.removeListener();
            valueListenerRegistration = value.addObjectListener(valueListener);
            compare();
        }

        @Override
        public void valueUnavailable(ValueSource source) {
            values[index] = null;
            if(valueListenerRegistration != null)
                valueListenerRegistration.removeListener();
        }
    }

    private class ValueChangedListener implements ValueListener<Value<?, ?>> {

        private final int index;

        private ValueChangedListener(int index) {
            this.index = index;
        }

        @Override
        public void valueChanging(Value<?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(Value<?, ?> value) {
            values[index] = value;
            compare();
        }
    }
}