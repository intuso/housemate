package com.intuso.housemate.client.real.impl.internal.factory.automation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.AutomationData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddAutomationCommand extends RealCommandImpl {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new automation";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new automation";
    
    private final Callback callback;
    private final RealAutomation.Factory automationFactory;
    private final RealAutomation.RemovedListener removedListener;

    @Inject
    protected AddAutomationCommand(Log log,
                                   ListenersFactory listenersFactory,
                                   StringType stringType,
                                   RealAutomation.Factory automationFactory,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   @Assisted Callback callback,
                                   @Assisted RealAutomation.RemovedListener removedListener) {
        super(log, listenersFactory, id, name, description,
                new RealParameterImpl<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameterImpl<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType));
        this.callback = callback;
        this.automationFactory = automationFactory;
        this.removedListener = removedListener;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        callback.addAutomation(automationFactory.create(new AutomationData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removedListener));
    }

    public interface Callback {
        void addAutomation(RealAutomation automation);
    }

    public interface Factory {
        AddAutomationCommand create(@Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    Callback callback,
                                    RealAutomation.RemovedListener removedListener);
    }
}
