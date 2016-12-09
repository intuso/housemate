package com.intuso.housemate.client.real.impl.internal.utils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.real.api.internal.object.RealDevice;
import com.intuso.housemate.client.real.impl.internal.*;
import com.intuso.housemate.client.v1_0.api.object.Command;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddDeviceCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new device";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new device";

    public interface Callback {
        void addDevice(RealDeviceImpl device);
    }

    public static class Factory {

        private final RealCommandImpl.Factory commandFactory;
        private final RealParameterImpl.Factory<String> stringParameterFactory;
        private final RealParameterImpl.Factory<PluginDependency<FeatureDriver.Factory<?>>> deviceDriverParameterFactory;
        private final Performer.Factory performerFactory;

        @Inject
        public Factory(RealCommandImpl.Factory commandFactory,
                       RealParameterImpl.Factory<String> stringParameterFactory,
                       RealParameterImpl.Factory<PluginDependency<FeatureDriver.Factory<? extends FeatureDriver>>> deviceDriverParameterFactory,
                       Performer.Factory performerFactory) {
            this.commandFactory = commandFactory;
            this.stringParameterFactory = stringParameterFactory;
            this.deviceDriverParameterFactory = deviceDriverParameterFactory;
            this.performerFactory = performerFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealDevice.RemoveCallback<RealDeviceImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, performerFactory.create(baseLogger, callback, removeCallback),
                    Lists.newArrayList(stringParameterFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID, NAME_PARAMETER_ID),
                            NAME_PARAMETER_ID,
                            NAME_PARAMETER_NAME,
                            NAME_PARAMETER_DESCRIPTION,
                            1,
                            1),
                            stringParameterFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID, DESCRIPTION_PARAMETER_ID),
                                    DESCRIPTION_PARAMETER_ID,
                                    DESCRIPTION_PARAMETER_NAME,
                                    DESCRIPTION_PARAMETER_DESCRIPTION,
                                    1,
                                    1)));
        }
    }

    public static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final Callback callback;
        private final RealDevice.RemoveCallback<RealDeviceImpl> removeCallback;
        private final RealTypeImpl<PluginDependency<FeatureDriver.Factory<? extends FeatureDriver>>> deviceDriverType;
        private final RealDeviceImpl.Factory deviceFactory;

        @Inject
        public Performer(@Assisted Logger logger,
                         @Assisted Callback callback,
                         @Assisted RealDevice.RemoveCallback<RealDeviceImpl> removeCallback,
                         RealTypeImpl<PluginDependency<FeatureDriver.Factory<? extends FeatureDriver>>> deviceDriverType,
                         RealDeviceImpl.Factory deviceFactory) {
            this.logger = logger;
            this.deviceDriverType = deviceDriverType;
            this.callback = callback;
            this.deviceFactory = deviceFactory;
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
            RealDeviceImpl device = deviceFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addDevice(device);
        }

        public interface Factory {
            Performer create(Logger logger,
                             Callback callback,
                             RealDevice.RemoveCallback<RealDeviceImpl> removeCallback);
        }
    }
}
