package com.intuso.housemate.client.real.api.internal.factory.hardware;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddHardwareCommand extends RealCommand {

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
    private final RealHardware.Factory hardwareFactory;
    private final RealHardware.RemovedListener removedListener;

    @Inject
    protected AddHardwareCommand(Log log,
                                 ListenersFactory listenersFactory,
                                 StringType stringType,
                                 HardwareFactoryType hardwareFactoryType,
                                 RealHardware.Factory hardwareFactory,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 @Assisted Callback callback,
                                 @Assisted RealHardware.RemovedListener removedListener) {
        super(log, listenersFactory, id, name, description,
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, hardwareFactoryType));
        this.hardwareFactoryType = hardwareFactoryType;
        this.callback = callback;
        this.hardwareFactory = hardwareFactory;
        this.removedListener = removedListener;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealHardware hardware = hardwareFactory.create(new HardwareData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removedListener);
        callback.addHardware(hardware);
        TypeInstances hardwareType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(hardwareType != null && hardwareType.getFirstValue() != null) {
            HardwareDriver.Factory<?> driverFactory = hardwareFactoryType.deserialise(hardwareType.getElements().get(0));
            hardware.getDriverProperty().setTypedValues(driverFactory);
        }
    }

    public interface Callback {
        void addHardware(RealHardware hardware);
    }

    public interface Factory {
        AddHardwareCommand create(@Assisted("id") String id,
                                  @Assisted("name") String name,
                                  @Assisted("description") String description,
                                  Callback callback,
                                  RealHardware.RemovedListener removedListener);
    }
}
