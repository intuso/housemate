package com.intuso.housemate.object.real.factory.hardware;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddHardwareCommand extends RealCommand {

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
                new RealParameter<>(log, listenersFactory, RealRoot.NAME_PARAMETER_ID, RealRoot.NAME_PARAMETER_NAME, RealRoot.NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.DESCRIPTION_PARAMETER_ID, RealRoot.DESCRIPTION_PARAMETER_NAME, RealRoot.DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.TYPE_PARAMETER_ID, RealRoot.TYPE_PARAMETER_NAME, RealRoot.TYPE_PARAMETER_DESCRIPTION, hardwareFactoryType));
        this.hardwareFactoryType = hardwareFactoryType;
        this.annotationProcessor = annotationProcessor;
        this.types = types;
        this.owner = owner;
    }

    @Override
    public void perform(TypeInstanceMap values) throws HousemateException {
        TypeInstances hardwareType = values.getChildren().get(RealRoot.TYPE_PARAMETER_ID);
        if(hardwareType == null || hardwareType.getFirstValue() == null)
            throw new HousemateException("No type specified");
        RealHardwareFactory<?> hardwareFactory = hardwareFactoryType.deserialise(hardwareType.getElements().get(0));
        if(hardwareFactory == null)
            throw new HousemateException("No factory known for hardware type " + hardwareType);
        TypeInstances name = values.getChildren().get(RealRoot.NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No name specified");
        TypeInstances description = values.getChildren().get(RealRoot.DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No description specified");
        RealHardware hardware = hardwareFactory.create(new HardwareData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
        annotationProcessor.process(types, hardware);
        owner.addHardware(hardware);
    }

    public interface Factory {
        public AddHardwareCommand create(RealHardwareOwner owner);
    }
}
