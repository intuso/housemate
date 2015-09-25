package com.intuso.housemate.client.real.api.internal.factory.user;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddUserCommand extends RealCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new user";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new user";

    private final RealUserOwner owner;
    private final RealUserFactory userFactory;

    @Inject
    protected AddUserCommand(Log log, ListenersFactory listenersFactory, StringType stringType,
                             RealUserFactory userFactory, @Assisted RealUserOwner owner) {
        super(log, listenersFactory,
                owner.getAddUserCommandDetails().getId(), owner.getAddUserCommandDetails().getName(), owner.getAddUserCommandDetails().getDescription(),
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType));
        this.owner = owner;
        this.userFactory = userFactory;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        owner.addUser(userFactory.create(new UserData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner));
    }

    public interface Factory {
        public AddUserCommand create(RealUserOwner owner);
    }
}
