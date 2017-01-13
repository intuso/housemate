package com.intuso.housemate.client.real.impl.internal.factory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealAutomationImpl;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class AddAutomationCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new automation";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new automation";

    public interface Callback {
        void addAutomation(RealAutomationImpl automation);
    }

    public static class Factory {

        private final ListenersFactory listenersFactory;
        private final StringType stringType;
        private final RealCommandImpl.Factory commandFactory;
        private final RealAutomationImpl.Factory automationFactory;

        @Inject
        public Factory(ListenersFactory listenersFactory, StringType stringType, RealCommandImpl.Factory commandFactory, RealAutomationImpl.Factory automationFactory) {
            this.listenersFactory = listenersFactory;
            this.stringType = stringType;
            this.commandFactory = commandFactory;
            this.automationFactory = automationFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, new Performer(baseLogger, automationFactory, callback, removeCallback),
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
                                    stringType)));
        }
    }

    private static class Performer implements RealCommandImpl.Performer {

        private final Logger baseLogger;
        private final RealAutomationImpl.Factory automationFactory;
        private final Callback callback;
        private final RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback;

        protected Performer(Logger baseLogger,
                            RealAutomationImpl.Factory automationFactory,
                            Callback callback,
                            RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback) {
            this.baseLogger = baseLogger;
            this.callback = callback;
            this.automationFactory = automationFactory;
            this.removeCallback = removeCallback;
        }

        @Override
        public void perform(Type.InstanceMap values) {
            Type.Instances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
            if(description == null || description.getFirstValue() == null)
                throw new HousemateException("No description specified");
            Type.Instances name = values.getChildren().get(NAME_PARAMETER_ID);
            if(name == null || name.getFirstValue() == null)
                throw new HousemateException("No name specified");
            callback.addAutomation(automationFactory.create(ChildUtil.logger(baseLogger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback));
        }
    }
}
