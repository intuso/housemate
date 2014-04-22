package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealProperty;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.server.plugin.main.type.comparison.Comparison;
import com.intuso.housemate.server.plugin.main.type.comparison.ComparisonType;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueAvailableListener;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Condition which is true iff the current day of the week matches
 * those specified by the user
 *
 */
@TypeInfo(id = "value-comparison", name = "Value Comparison", description = "Condition that compares values")
public class ValueComparison extends ServerRealCondition {

    public final static String COMPARISON_ID = "comparison";
    public final static String COMPARISON_NAME = "Comparison";
    public final static String COMPARISON_DESCRIPTION = "Comparison";

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

    private final ServerRealProperty<Comparison> comparisonProperty;
    private final PropertyListener propertyListener;
    private ListenerRegistration propertyListenerRegistration;
    private Value<?, ?> firstValue = null;
    private Value<?, ?> secondValue = null;

    @Inject
	public ValueComparison(Log log,
                           ListenersFactory listenersFactory,
                           @Assisted ConditionData data,
                           @Assisted ServerRealConditionOwner owner,
                           LifecycleHandler lifecycleHandler,
                           RealList<TypeData<?>, RealType<?, ?, ?>> types,
                           ComparisonType comparisonType) {
        super(log, listenersFactory, data, owner, lifecycleHandler);
        this.types = types;
        comparisonProperty = new ServerRealProperty<Comparison>(log, listenersFactory, COMPARISON_ID, COMPARISON_NAME,
                COMPARISON_DESCRIPTION, comparisonType, (List)null);
        getProperties().add(comparisonProperty);
        propertyListener = new PropertyListener();
    }

    @Override
	public void start() {
        firstValue = null;
        secondValue = null;
        propertyListenerRegistration = comparisonProperty.addObjectListener(propertyListener);
        if(comparisonProperty.getTypedValue() != null) {
            propertyListener.addListeners(comparisonProperty.getTypedValue());
            compare();
        }
	}
	
	@Override
	public void stop() {
        if(propertyListenerRegistration != null)
            propertyListenerRegistration.removeListener();
        propertyListener.removeListeners();
	}

    private void compare() {
        Comparison comparison = comparisonProperty.getTypedValue();
        if(comparison == null)
            setError("No comparison available");
        else if(comparison.getComparisonType().getId() == null)
            setError("No operator defined");
        else if(comparison.getComparatorsByType() == null)
            setError("No comparators available for operator " + comparison.getComparisonType().getId());
        else if(firstValue == null && secondValue == null)
            setError("Neither value is available");
        else if(firstValue == null)
            setError("First value is not available");
        else if(secondValue == null)
            setError("Second value is not available");
        else if(firstValue.getType() == null)
            setError("First value has no type");
        else if(secondValue.getType() == null)
            setError("Second value has no type");
        else if(!firstValue.getType().getId().equals(secondValue.getType().getId()))
            setError("The two values have different types (" + firstValue.getType().getName() + "," + secondValue.getType().getName() + ")");
        else if(comparison.getComparatorsByType().get(firstValue.getType().getId()) == null)
            setError("No comparator for operator " + comparison.getComparisonType().getName() + " and value type " + firstValue.getType().getId());
        else {
            RealType<?, ?, ?> type = types.get(firstValue.getType().getId());
            if(type == null)
                setError("No type found for id " + firstValue.getType().getId());
            else {
                Comparator<Object> comparator = (Comparator<Object>) comparison.getComparatorsByType().get(firstValue.getType().getId());
                if(comparator == null)
                    setError("No comparator found for type id " + firstValue.getType().getId());
                else {
                    try {
                        Object first = firstValue.getTypeInstances() != null && firstValue.getTypeInstances().size() > 0
                                ? type.deserialise(firstValue.getTypeInstances().get(0))
                                : null;
                        Object second = secondValue.getTypeInstances() != null && secondValue.getTypeInstances().size() > 0
                                ? type.deserialise(secondValue.getTypeInstances().get(0))
                                : null;
                        conditionSatisfied(comparator.compare(first, second));
                        setError(null);
                    } catch(HousemateException e) {
                        setError("Error comparing values: " + e.getMessage());
                        getLog().e("Error comparing values", e);
                    }
                }
            }
        }
    }

    private class PropertyListener implements ValueListener<ServerRealProperty<Comparison>> {

        private final ValueSourceListener firstValueSourceListener = new ValueSourceListener(0);
        private final ValueSourceListener secondValueSourceListener = new ValueSourceListener(1);

        private ListenerRegistration firstValueSourceListenerRegistration;
        private ListenerRegistration secondValueSourceListenerRegistration;

        private void addListeners(Comparison comparison) {
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

        @Override
        public void valueChanging(ServerRealProperty<Comparison> value) {
            removeListeners();
        }

        @Override
        public void valueChanged(ServerRealProperty<Comparison> value) {
            if(value.getTypedValue() != null)
                addListeners(value.getTypedValue());
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

        private void addListener(Value<?, ?> value) {
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
        public void valueAvailable(ValueSource source, Value<?, ?> value) {
            removeListener();
            addListener(value);
        }

        @Override
        public void valueUnavailable(ValueSource source) {
            removeListener();
        }
    }

    private class ValueChangedListener implements ValueListener<Value<?, ?>> {
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