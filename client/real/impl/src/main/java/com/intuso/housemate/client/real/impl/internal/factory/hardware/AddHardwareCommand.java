package com.intuso.housemate.client.real.impl.internal.factory.hardware;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealHardwareImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddHardwareCommand extends RealCommandImpl {
    
    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new hardware";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new hardware";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new hardware";

    private final HardwareFactoryType hardwareFactoryType;
    private final Callback callback;
    private final RealHardwareImpl.Factory hardwareFactory;
    private final RealHardware.RemoveCallback<RealHardwareImpl<?>> removeCallback;

    @Inject
    protected AddHardwareCommand(ListenersFactory listenersFactory,
                                 StringType stringType,
                                 HardwareFactoryType hardwareFactoryType,
                                 RealHardwareImpl.Factory hardwareFactory,
                                 @Assisted Logger logger,
                                 @Assisted Command.Data data,
                                 @Assisted Callback callback,
                                 @Assisted RealHardware.RemoveCallback<RealHardwareImpl<?>> removeCallback) {
        super(logger, data, listenersFactory,
                new RealParameterImpl<>(ChildUtil.logger(logger, NAME_PARAMETER_ID),
                        new Parameter.Data(NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION),
                        listenersFactory,
                        stringType),
                new RealParameterImpl<>(ChildUtil.logger(logger, DESCRIPTION_PARAMETER_ID),
                        new Parameter.Data(DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION),
                        listenersFactory,
                        stringType),
                new RealParameterImpl<>(ChildUtil.logger(logger, TYPE_PARAMETER_ID),
                        new Parameter.Data(TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION),
                        listenersFactory,
                        hardwareFactoryType));
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
        RealHardwareImpl<?> hardware = hardwareFactory.create(ChildUtil.logger(logger, name.getFirstValue()), new Hardware.Data(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removeCallback);
        callback.addHardware(hardware);
        Type.Instances hardwareType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(hardwareType != null && hardwareType.getFirstValue() != null)
            ((RealProperty)hardware.getDriverProperty()).setValue(hardwareFactoryType.deserialise(hardwareType.getElements().get(0)));
    }

    public interface Callback {
        void addHardware(RealHardwareImpl<?> hardware);
    }

    public interface Factory {
        AddHardwareCommand create(Logger logger,
                                  Command.Data data,
                                  Callback callback,
                                  RealHardware.RemoveCallback<RealHardwareImpl<?>> removeCallback);
    }
}
