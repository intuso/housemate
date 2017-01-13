package com.intuso.housemate.client.real.impl.internal.factory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealCondition;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealConditionImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddConditionCommand {
    
    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new condition";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new condition";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new condition";

    public interface Callback {
        void addCondition(RealConditionImpl condition);
    }

    public static class Factory {

        private final ListenersFactory listenersFactory;
        private final StringType stringType;
        private final ConditionFactoryType conditionFactoryType;
        private final RealCommandImpl.Factory commandFactory;
        private final RealConditionImpl.Factory conditionFactory;

        @Inject
        public Factory(ListenersFactory listenersFactory, StringType stringType, ConditionFactoryType conditionFactoryType, RealCommandImpl.Factory commandFactory, RealConditionImpl.Factory ConditionFactory) {
            this.listenersFactory = listenersFactory;
            this.stringType = stringType;
            this.conditionFactoryType = conditionFactoryType;
            this.commandFactory = commandFactory;
            this.conditionFactory = ConditionFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealCondition.RemoveCallback<RealConditionImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, new Performer(baseLogger, conditionFactoryType, conditionFactory, callback, removeCallback),
                    Lists.newArrayList(new RealParameterImpl<>(ChildUtil.logger(logger, NAME_PARAMETER_ID),
                                    NAME_PARAMETER_ID,
                                    NAME_PARAMETER_NAME,
                                    NAME_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    stringType),
                            new RealParameterImpl<>(ChildUtil.logger(logger, DESCRIPTION_PARAMETER_ID),
                                    DESCRIPTION_PARAMETER_ID,
                                    DESCRIPTION_PARAMETER_NAME,
                                    DESCRIPTION_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    stringType),
                            new RealParameterImpl<>(ChildUtil.logger(logger, TYPE_PARAMETER_ID),
                                    TYPE_PARAMETER_ID,
                                    TYPE_PARAMETER_NAME,
                                    TYPE_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    conditionFactoryType)));
        }
    }

    private static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final ConditionFactoryType conditionFactoryType;
        private final RealConditionImpl.Factory conditionFactory;
        private final Callback callback;
        private final RealCondition.RemoveCallback<RealConditionImpl> removeCallback;

        private Performer(Logger logger, ConditionFactoryType conditionFactoryType, RealConditionImpl.Factory conditionFactory, Callback callback, RealCondition.RemoveCallback<RealConditionImpl> removeCallback) {
            this.logger = logger;
            this.conditionFactoryType = conditionFactoryType;
            this.callback = callback;
            this.conditionFactory = conditionFactory;
            this.removeCallback = removeCallback;
        }

        @Override
        public void perform(Type.InstanceMap values) {
            Type.Instances name = values.getChildren().get(NAME_PARAMETER_ID);
            if(name == null || name.getFirstValue() == null)
                throw new HousemateException("No name specified");
            Type.Instances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
            if(description == null || description.getFirstValue() == null)
                throw new HousemateException("No description specified");
            RealConditionImpl condition = conditionFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addCondition(condition);
            Type.Instances conditionType = values.getChildren().get(TYPE_PARAMETER_ID);
            if(conditionType != null && conditionType.getFirstValue() != null)
                ((RealProperty)condition.getDriverProperty()).setValue(conditionFactoryType.deserialise(conditionType.getElements().get(0)));
        }
    }
}
