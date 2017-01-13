package com.intuso.housemate.client.real.impl.internal.utils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealUserImpl;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.housemate.client.v1_0.api.object.Command;
import org.slf4j.Logger;

import java.util.UUID;

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

        private final RealCommandImpl.Factory commandFactory;
        private final TypeRepository typeRepository;
        private final RealParameterImpl.Factory parameterFactory;
        private final Performer.Factory performerFactory;

        @Inject
        public Factory(RealCommandImpl.Factory commandFactory,
                       TypeRepository typeRepository,
                       RealParameterImpl.Factory parameterFactory, Performer.Factory performerFactory) {
            this.commandFactory = commandFactory;
            this.typeRepository = typeRepository;
            this.parameterFactory = parameterFactory;
            this.performerFactory = performerFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealUser.RemoveCallback<RealUserImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, performerFactory.create(baseLogger, callback, removeCallback),
                    Lists.newArrayList(
                            parameterFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID, NAME_PARAMETER_ID),
                                    NAME_PARAMETER_ID,
                                    NAME_PARAMETER_NAME,
                                    NAME_PARAMETER_DESCRIPTION,
                                    typeRepository.getType(new TypeSpec(String.class)),
                                    1,
                                    1),
                            parameterFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID, DESCRIPTION_PARAMETER_ID),
                                    DESCRIPTION_PARAMETER_ID,
                                    DESCRIPTION_PARAMETER_NAME,
                                    DESCRIPTION_PARAMETER_DESCRIPTION,
                                    typeRepository.getType(new TypeSpec(String.class)),
                                    1,
                                    1)));
        }
    }

    public static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final Callback callback;
        private final RealUser.RemoveCallback<RealUserImpl> removeCallback;
        private final RealUserImpl.Factory userFactory;

        @Inject
        public Performer(@Assisted Logger logger,
                         @Assisted Callback callback,
                         @Assisted RealUser.RemoveCallback<RealUserImpl> removeCallback,
                         RealUserImpl.Factory userFactory) {
            this.logger = logger;
            this.callback = callback;
            this.userFactory = userFactory;
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
            String id = UUID.randomUUID().toString();
            RealUserImpl user = userFactory.create(ChildUtil.logger(logger, id), id, name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addUser(user);
        }

        public interface Factory {
            Performer create(Logger logger,
                             Callback callback,
                             RealUser.RemoveCallback<RealUserImpl> removeCallback);
        }
    }
}
