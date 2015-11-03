package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.api.internal.annotations.Property;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.housemate.plugin.api.internal.Comparator;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.housemate.server.plugin.main.type.comparison.Comparison;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueAvailableListener;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

/**
 * Condition which is true iff the current day of the week matches
 * those specified by the user
 *
 */
@TypeInfo(id = "value-comparison", name = "Value Comparison", description = "Condition that compares values")
public class ValueComparison implements ConditionDriver {

    private Comparison comparison;
    private final ValueSourceListener firstValueSourceListener = new ValueSourceListener(0);
    private final ValueSourceListener secondValueSourceListener = new ValueSourceListener(1);
    private ListenerRegistration firstValueSourceListenerRegistration;
    private ListenerRegistration secondValueSourceListenerRegistration;
    private Value<TypeInstances, ?> firstValue = null;
    private Value<TypeInstances, ?> secondValue = null;

    private final Log log;
    private final RealList<RealType<?>> types;
    private final ConditionDriver.Callback callback;

    @Inject
	public ValueComparison(Log log,
                           RealList<RealType<?>> types,
                           @Assisted ConditionDriver.Callback callback) {
        this.log = log;
        this.types = types;
        this.callback = callback;
    }

    @Override
    public boolean hasChildConditions() {
        return false;
    }

    @Override
	public void start() {
        firstValue = null;
        secondValue = null;
        if(comparison != null) {
            addListeners();
            compare();
        }
	}
	
	@Override
	public void stop() {
        removeListeners();
	}

    @Property(id = "comparison", name = "Comparison", description = "Comparison", typeId = "comparison")
    public void setComparison(Comparison comparison) {
        removeListeners();
        this.comparison = comparison;
        if(comparison != null)
            addListeners();
    }

    private void addListeners() {
        firstValueSourceListenerRegistration = comparison.getFirstValueSource().addValueAvailableListener(firstValueSourceListener, true);
        secondValueSourceListenerRegistration = comparison.getSecondValueSource().addValueAvailableListener(secondValueSourceListener, true);
    }

    private void removeListeners() {
        firstValueSourceListener.removeListener();
        secondValueSourceListener.removeListener();
        if(firstValueSourceListenerRegistration != null)
            firstValueSourceListenerRegistration.removeListener();
        if(secondValueSourceListenerRegistration != null)
            secondValueSourceListenerRegistration.removeListener();
    }

    private void compare() {
        if(comparison == null)
            callback.setError("No comparison available");
        else if(comparison.getComparisonType().getId() == null)
            callback.setError("No operator defined");
        else if(comparison.getComparatorsByType() == null)
            callback.setError("No comparators available for operator " + comparison.getComparisonType().getId());
        else if(firstValue == null && secondValue == null)
            callback.setError("Neither value is available");
        else if(firstValue == null)
            callback.setError("First value is not available");
        else if(secondValue == null)
            callback.setError("Second value is not available");
        else if(firstValue.getTypeId() == null)
            callback.setError("First value has no type");
        else if(secondValue.getTypeId() == null)
            callback.setError("Second value has no type");
        else if(!firstValue.getTypeId().equals(secondValue.getTypeId()))
            callback.setError("The two values have different types (" + firstValue.getTypeId() + "," + secondValue.getTypeId() + ")");
        else if(comparison.getComparatorsByType().get(firstValue.getTypeId()) == null)
            callback.setError("No comparator for operator " + comparison.getComparisonType().getName() + " and value type " + firstValue.getTypeId());
        else {
            RealType<?> type = types.get(firstValue.getTypeId());
            if(type == null)
                callback.setError("No type found for id " + firstValue.getTypeId());
            else {
                Comparator<Object> comparator = (Comparator<Object>) comparison.getComparatorsByType().get(firstValue.getTypeId());
                if(comparator == null)
                    callback.setError("No comparator found for type id " + firstValue.getTypeId());
                else {
                    try {
                        Object first = firstValue.getValue() != null && firstValue.getValue().getElements().size() > 0
                                ? type.deserialise(firstValue.getValue().getElements().get(0))
                                : null;
                        Object second = secondValue.getValue() != null && secondValue.getValue().getElements().size() > 0
                                ? type.deserialise(secondValue.getValue().getElements().get(0))
                                : null;
                        callback.conditionSatisfied(comparator.compare(first, second));
                        callback.setError(null);
                    } catch(Throwable t) {
                        callback.setError("Error comparing values: " + t.getMessage());
                        log.e("Error comparing values", t);
                    }
                }
            }
        }
    }

    private class ValueSourceListener implements ValueAvailableListener {

        private final int index;
        private final ValueChangedListener valueChangedListener;
        private ListenerRegistration valueChangedListenerRegistration;

        private ValueSourceListener(int index) {
            this.index = index;
            valueChangedListener = new ValueChangedListener();
        }

        private void addListener(Value<TypeInstances, ?> value) {
            if(value != null) {
                valueChangedListenerRegistration = value.addObjectListener(valueChangedListener);
                if(index == 0)
                    firstValue = value;
                else
                    secondValue = value;
                compare();
            }
        }

        private void removeListener() {
            if(valueChangedListenerRegistration != null)
                valueChangedListenerRegistration.removeListener();
        }


        @Override
        public void valueAvailable(ValueSource source, Value<TypeInstances, ?> value) {
            removeListener();
            addListener(value);
        }

        @Override
        public void valueUnavailable(ValueSource source) {
            removeListener();
        }
    }

    private class ValueChangedListener implements Value.Listener<Value<?, ?>> {
        @Override
        public void valueChanging(Value<?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(Value<?, ?> value) {
            compare();
        }
    }
}