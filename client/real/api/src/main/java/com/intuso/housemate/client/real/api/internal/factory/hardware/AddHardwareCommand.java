package com.intuso.housemate.client.real.api.internal.factory.hardware;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
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
    private final AnnotationProcessor annotationProcessor;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealHardwareOwner owner;

    @Inject
    protected AddHardwareCommand(Log log, ListenersFactory listenersFactory, StringType stringType,
                                 HardwareFactoryType hardwareFactoryType, AnnotationProcessor annotationProcessor,
                                 RealList<TypeData<?>, RealType<?, ?, ?>> types,
                                 @Assisted RealHardwareOwner owner) {
        super(log, listenersFactory,
                owner.getAddHardwareCommandDetails().getId(), owner.getAddHardwareCommandDetails().getName(), owner.getAddHardwareCommandDetails().getDescription(),
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, hardwareFactoryType));
        this.hardwareFactoryType = hardwareFactoryType;
        this.annotationProcessor = annotationProcessor;
        this.types = types;
        this.owner = owner;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances hardwareType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(hardwareType == null || hardwareType.getFirstValue() == null)
            throw new HousemateCommsException("No type specified");
        RealHardwareFactory<?> hardwareFactory = hardwareFactoryType.deserialise(hardwareType.getElements().get(0));
        if(hardwareFactory == null)
            throw new HousemateCommsException("No factory known for hardware type " + hardwareType);
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealHardware hardware = hardwareFactory.create(new HardwareData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
        annotationProcessor.process(types, hardware);
        owner.addHardware(hardware);
    }

    public interface Factory {
        public AddHardwareCommand create(RealHardwareOwner owner);
    }
}
