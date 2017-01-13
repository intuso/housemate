package com.intuso.housemate.client.real.impl.internal.factory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealHardwareImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddHardwareCommand {
    
    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new hardware";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new hardware";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new hardware";

    public interface Callback {
        void addHardware(RealHardwareImpl hardware);
    }

    public static class Factory {

        private final ListenersFactory listenersFactory;
        private final StringType stringType;
        private final HardwareFactoryType hardwareFactoryType;
        private final RealCommandImpl.Factory commandFactory;
        private final RealHardwareImpl.Factory hardwareFactory;

        @Inject
        public Factory(ListenersFactory listenersFactory, StringType stringType, HardwareFactoryType hardwareFactoryType, RealCommandImpl.Factory commandFactory, RealHardwareImpl.Factory HardwareFactory) {
            this.listenersFactory = listenersFactory;
            this.stringType = stringType;
            this.hardwareFactoryType = hardwareFactoryType;
            this.commandFactory = commandFactory;
            this.hardwareFactory = HardwareFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealHardware.RemoveCallback<RealHardwareImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, new Performer(baseLogger, hardwareFactoryType, hardwareFactory, callback, removeCallback),
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
                                    hardwareFactoryType)));
        }
    }

    private static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final HardwareFactoryType hardwareFactoryType;
        private final RealHardwareImpl.Factory hardwareFactory;
        private final Callback callback;
        private final RealHardware.RemoveCallback<RealHardwareImpl> removeCallback;

        private Performer(Logger logger, HardwareFactoryType hardwareFactoryType, RealHardwareImpl.Factory hardwareFactory, Callback callback, RealHardware.RemoveCallback<RealHardwareImpl> removeCallback) {
            this.logger = logger;
            this.hardwareFactoryType = hardwareFactoryType;
            this.callback = callback;
            this.hardwareFactory = hardwareFactory;
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
            RealHardwareImpl hardware = hardwareFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addHardware(hardware);
            Type.Instances hardwareType = values.getChildren().get(TYPE_PARAMETER_ID);
            if(hardwareType != null && hardwareType.getFirstValue() != null)
                ((RealProperty)hardware.getDriverProperty()).setValue(hardwareFactoryType.deserialise(hardwareType.getElements().get(0)));
        }
    }
}
