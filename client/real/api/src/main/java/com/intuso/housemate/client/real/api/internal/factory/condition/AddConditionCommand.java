package com.intuso.housemate.client.real.api.internal.factory.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.ConditionData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
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
    
    private final ConditionFactoryType conditionFactoryType;
    private final AnnotationProcessor annotationProcessor;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealConditionOwner owner;

    @Inject
    protected AddConditionCommand(Log log, ListenersFactory listenersFactory, StringType stringType,
                                  ConditionFactoryType conditionFactoryType, AnnotationProcessor annotationProcessor,
                                  RealList<TypeData<?>, RealType<?, ?, ?>> types,
                                  @Assisted RealConditionOwner owner) {
        super(log, listenersFactory,
                owner.getAddConditionCommandDetails().getId(), owner.getAddConditionCommandDetails().getName(), owner.getAddConditionCommandDetails().getDescription(),
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, conditionFactoryType));
        this.conditionFactoryType = conditionFactoryType;
        this.annotationProcessor = annotationProcessor;
        this.types = types;
        this.owner = owner;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances conditionType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(conditionType == null || conditionType.getFirstValue() == null)
            throw new HousemateCommsException("No type specified");
        RealConditionFactory<?> conditionFactory = conditionFactoryType.deserialise(conditionType.getElements().get(0));
        if(conditionFactory == null)
            throw new HousemateCommsException("No factory known for condition type " + conditionType.getElements().get(0));
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealCondition condition = conditionFactory.create(new ConditionData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
        annotationProcessor.process(types, condition);
        owner.addCondition(condition);
    }

    public interface Factory {
        public AddConditionCommand create(RealConditionOwner owner);
    }
}
