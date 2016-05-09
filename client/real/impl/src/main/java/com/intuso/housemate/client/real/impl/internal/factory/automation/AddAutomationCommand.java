package com.intuso.housemate.client.real.impl.internal.factory.automation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Automation;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealAutomationImpl;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

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
    private final RealAutomationImpl.Factory automationFactory;
    private final RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback;

    @Inject
    protected AddAutomationCommand(ListenersFactory listenersFactory,
                                   StringType stringType,
                                   RealAutomationImpl.Factory automationFactory,
                                   @Assisted Logger logger,
                                   @Assisted Command.Data data,
                                   @Assisted Callback callback,
                                   @Assisted RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback) {
        super(logger, data, listenersFactory,
                new RealParameterImpl<>(ChildUtil.logger(logger, NAME_PARAMETER_ID),
                        new Parameter.Data(NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION),
                        listenersFactory,
                        stringType),
                new RealParameterImpl<>(ChildUtil.logger(logger, DESCRIPTION_PARAMETER_ID),
                        new Parameter.Data(DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION),
                        listenersFactory,
                        stringType));
        this.callback = callback;
        this.automationFactory = automationFactory;
        this.removeCallback = removeCallback;
    }

    @Override
    public void perform(Type.InstanceMap values) {
        Type.Instances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No description specified");
        Type.Instances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No name specified");
        callback.addAutomation(automationFactory.create(ChildUtil.logger(logger, name.getFirstValue()), new Automation.Data(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removeCallback));
    }

    public interface Callback {
        void addAutomation(RealAutomationImpl automation);
    }

    public interface Factory {
        AddAutomationCommand create(Logger logger,
                                    Command.Data data,
                                    Callback callback,
                                    RealAutomation.RemoveCallback<RealAutomationImpl> removeCallback);
    }
}
