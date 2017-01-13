package com.intuso.housemate.client.real.impl.internal.factory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealUserImpl;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class AddUserCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new user";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new user";

    public interface Callback {
        void addUser(RealUserImpl user);
    }

    public static class Factory {

        private final ListenersFactory listenersFactory;
        private final StringType stringType;
        private final RealCommandImpl.Factory commandFactory;
        private final RealUserImpl.Factory userFactory;

        @Inject
        public Factory(ListenersFactory listenersFactory, StringType stringType, RealCommandImpl.Factory commandFactory, RealUserImpl.Factory userFactory) {
            this.listenersFactory = listenersFactory;
            this.stringType = stringType;
            this.commandFactory = commandFactory;
            this.userFactory = userFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealUser.RemoveCallback<RealUserImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, new Performer(baseLogger, userFactory, callback, removeCallback),
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

        private final Logger logger;
        private final RealUserImpl.Factory userFactory;
        private final Callback callback;
        private final RealUser.RemoveCallback<RealUserImpl> removeCallback;

        private Performer(Logger logger,
                          RealUserImpl.Factory userFactory,
                          Callback callback,
                          RealUser.RemoveCallback<RealUserImpl> removeCallback) {
            this.logger = logger;
            this.userFactory = userFactory;
            this.callback = callback;
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
            callback.addUser(userFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback));
        }
    }
}
