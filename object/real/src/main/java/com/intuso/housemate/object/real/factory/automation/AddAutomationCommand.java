package com.intuso.housemate.object.real.factory.automation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddAutomationCommand extends RealCommand {

    private final RealAutomationOwner owner;
    private final RealAutomationFactory automationFactory;

    @Inject
    protected AddAutomationCommand(Log log, ListenersFactory listenersFactory, StringType stringType,
                                   RealAutomationFactory automationFactory, @Assisted RealAutomationOwner owner) {
        super(log, listenersFactory,
                owner.getAddAutomationCommandDetails().getId(), owner.getAddAutomationCommandDetails().getName(), owner.getAddAutomationCommandDetails().getDescription(),
                new RealParameter<>(log, listenersFactory, RealRoot.NAME_PARAMETER_ID, RealRoot.NAME_PARAMETER_NAME, RealRoot.NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.DESCRIPTION_PARAMETER_ID, RealRoot.DESCRIPTION_PARAMETER_NAME, RealRoot.DESCRIPTION_PARAMETER_DESCRIPTION, stringType));
        this.owner = owner;
        this.automationFactory = automationFactory;
    }

    @Override
    public void perform(TypeInstanceMap values) throws HousemateException {
        TypeInstances description = values.getChildren().get(RealRoot.DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No description specified");
        TypeInstances name = values.getChildren().get(RealRoot.NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No name specified");
        owner.addAutomation(automationFactory.create(new AutomationData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner));
    }

    public interface Factory {
        public AddAutomationCommand create(RealAutomationOwner owner);
    }
}
