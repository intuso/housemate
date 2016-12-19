package com.intuso.housemate.client.real.impl.internal.utils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealFeature;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.*;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddFeatureCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new feature";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new feature";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new feature";

    public interface Callback {
        void addFeature(RealFeatureImpl feature);
    }

    public static class Factory {

        private final RealCommandImpl.Factory commandFactory;
        private final RealParameterImpl.Factory<String> stringParameterFactory;
        private final RealParameterImpl.Factory<PluginDependency<FeatureDriver.Factory<?>>> featureDriverParameterFactory;
        private final Performer.Factory performerFactory;

        @Inject
        public Factory(RealCommandImpl.Factory commandFactory,
                       RealParameterImpl.Factory<String> stringParameterFactory,
                       RealParameterImpl.Factory<PluginDependency<FeatureDriver.Factory<? extends FeatureDriver>>> featureDriverParameterFactory,
                       Performer.Factory performerFactory) {
            this.commandFactory = commandFactory;
            this.stringParameterFactory = stringParameterFactory;
            this.featureDriverParameterFactory = featureDriverParameterFactory;
            this.performerFactory = performerFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealFeature.RemoveCallback<RealFeatureImpl> removeCallback) {
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
                                    1),
                            featureDriverParameterFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID, TYPE_PARAMETER_ID),
                                    TYPE_PARAMETER_ID,
                                    TYPE_PARAMETER_NAME,
                                    TYPE_PARAMETER_DESCRIPTION,
                                    1,
                                    1)));
        }
    }

    public static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final Callback callback;
        private final RealFeature.RemoveCallback<RealFeatureImpl> removeCallback;
        private final RealTypeImpl<PluginDependency<FeatureDriver.Factory<? extends FeatureDriver>>> featureDriverType;
        private final RealFeatureImpl.Factory featureFactory;

        @Inject
        public Performer(@Assisted Logger logger,
                         @Assisted Callback callback,
                         @Assisted RealFeature.RemoveCallback<RealFeatureImpl> removeCallback,
                         RealTypeImpl<PluginDependency<FeatureDriver.Factory<? extends FeatureDriver>>> featureDriverType,
                         RealFeatureImpl.Factory featureFactory) {
            this.logger = logger;
            this.featureDriverType = featureDriverType;
            this.callback = callback;
            this.featureFactory = featureFactory;
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
            RealFeatureImpl feature = featureFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addFeature(feature);
            Type.Instances featureType = values.getChildren().get(TYPE_PARAMETER_ID);
            if(featureType != null && featureType.getFirstValue() != null)
                ((RealProperty)feature.getDriverProperty()).setValue(featureDriverType.deserialise(featureType.getElements().get(0)));
        }

        public interface Factory {
            Performer create(Logger logger,
                             Callback callback,
                             RealFeature.RemoveCallback<RealFeatureImpl> removeCallback);
        }
    }
}
