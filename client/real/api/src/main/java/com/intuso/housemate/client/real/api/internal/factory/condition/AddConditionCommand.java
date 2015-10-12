package com.intuso.housemate.client.real.api.internal.factory.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealCondition;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddConditionCommand extends RealCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new condition";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new condition";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new condition";

    private final Callback callback;
    private final ConditionFactoryType conditionFactoryType;
    private final RealCondition.Factory conditionFactory;
    private final RealCondition.RemovedListener removedListener;

    @Inject
    protected AddConditionCommand(Log log,
                                  ListenersFactory listenersFactory,
                                  StringType stringType,
                                  ConditionFactoryType conditionFactoryType,
                                  RealCondition.Factory conditionFactory,
                                  @Assisted("id") String id,
                                  @Assisted("name") String name,
                                  @Assisted("description") String description,
                                  @Assisted Callback callback,
                                  @Assisted RealCondition.RemovedListener removedListener) {
        super(log, listenersFactory, id, name, description,
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, conditionFactoryType));
        this.callback = callback;
        this.conditionFactoryType = conditionFactoryType;
        this.conditionFactory = conditionFactory;
        this.removedListener = removedListener;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealCondition condition = conditionFactory.create(new ConditionData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removedListener);
        callback.addCondition(condition);
        TypeInstances conditionType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(conditionType != null && conditionType.getFirstValue() != null) {
            ConditionDriver.Factory<?> driverFactory = conditionFactoryType.deserialise(conditionType.getElements().get(0));
            condition.getDriverProperty().setTypedValues(driverFactory);
        }
    }

    public interface Callback {
        void addCondition(RealCondition condition);
    }

    public interface Factory {
        AddConditionCommand create(@Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   Callback callback,
                                   RealCondition.RemovedListener removedListener);
    }
}
