package com.intuso.housemate.object.broker.real.condition;

import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealObject;
import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealValue;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
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
public abstract class BrokerRealCondition
        extends BrokerRealObject<ConditionWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>,
            ConditionListener<? super BrokerRealCondition>>
        implements Condition<BrokerRealProperty<String>, BrokerRealValue<String>, BrokerRealValue<Boolean>,
            BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>, BrokerRealCommand, BrokerRealCondition,
            BrokerRealList<ConditionWrappable, BrokerRealCondition>> {

    private BrokerRealValue<String> errorValue;
    private BrokerRealValue<Boolean> satisfiedValue;
    private BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> propertyList;
    private BrokerRealCommand addConditionCommand;
    private BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions;

    public BrokerRealCondition(BrokerRealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<BrokerRealProperty<?>>(0));
    }

    public BrokerRealCondition(final BrokerRealResources resources, String id, String name, String description, java.util.List<BrokerRealProperty<?>> properties) {
        super(resources, new ConditionWrappable(id, name, description));
        errorValue = new BrokerRealValue<String>(resources, ERROR, ERROR, "The current error", new StringType(resources.getRealResources()), null);
        satisfiedValue = new BrokerRealValue<Boolean>(resources, SATISFIED, SATISFIED, "Whether the condition is satisfied", new BooleanType(resources.getRealResources()), false);
        propertyList = new BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>(resources, PROPERTIES, PROPERTIES, "The condition's properties", properties);
        conditions = new BrokerRealList<ConditionWrappable, BrokerRealCondition>(resources, CONDITIONS, CONDITIONS, "The condition's sub-conditions");
        // add a command to add rules to the rule list
        addConditionCommand = getResources().getLifecycleHandler().createAddConditionCommand(conditions);
        addWrapper(errorValue);
        addWrapper(satisfiedValue);
        addWrapper(propertyList);
        addWrapper(addConditionCommand);
        addWrapper(conditions);
    }

    @Override
    public BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public BrokerRealList<ConditionWrappable, BrokerRealCondition> getConditions() {
        return conditions;
    }

    @Override
    public BrokerRealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public BrokerRealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getValue();
    }

    @Override
    public BrokerRealValue<Boolean> getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public boolean isSatisfied() {
        return satisfiedValue.getTypedValue();
    }

    public final void setError(String error) {
        getErrorValue().setTypedValue(error);
    }

    protected void conditionSatisfied(boolean satisfied) {
        if(satisfied != isSatisfied()) {
            getSatisfiedValue().setTypedValue(satisfied);
            for(ConditionListener<? super BrokerRealCondition> listener : getObjectListeners())
                listener.conditionSatisfied(this, satisfied);
        }
    }

    public abstract void start();
    public abstract void stop();
}
