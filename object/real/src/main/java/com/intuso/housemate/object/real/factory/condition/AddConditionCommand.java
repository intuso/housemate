package com.intuso.housemate.object.real.factory.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddConditionCommand extends RealCommand {

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
                new RealParameter<>(log, listenersFactory, RealRoot.NAME_PARAMETER_ID, RealRoot.NAME_PARAMETER_NAME, RealRoot.NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.DESCRIPTION_PARAMETER_ID, RealRoot.DESCRIPTION_PARAMETER_NAME, RealRoot.DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.TYPE_PARAMETER_ID, RealRoot.TYPE_PARAMETER_NAME, RealRoot.TYPE_PARAMETER_DESCRIPTION, conditionFactoryType));
        this.conditionFactoryType = conditionFactoryType;
        this.annotationProcessor = annotationProcessor;
        this.types = types;
        this.owner = owner;
    }

    @Override
    public void perform(TypeInstanceMap values) throws HousemateException {
        TypeInstances conditionType = values.getChildren().get(RealRoot.TYPE_PARAMETER_ID);
        if(conditionType == null || conditionType.getFirstValue() == null)
            throw new HousemateException("No type specified");
        RealConditionFactory<?> conditionFactory = conditionFactoryType.deserialise(conditionType.getElements().get(0));
        if(conditionFactory == null)
            throw new HousemateException("No factory known for condition type " + conditionType);
        TypeInstances name = values.getChildren().get(RealRoot.NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No name specified");
        TypeInstances description = values.getChildren().get(RealRoot.DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No description specified");
        RealCondition condition = conditionFactory.create(new ConditionData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
        annotationProcessor.process(types, condition);
        owner.addCondition(condition);
    }

    public interface Factory {
        public AddConditionCommand create(RealConditionOwner owner);
    }
}
