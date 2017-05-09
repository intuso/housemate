package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <O> the type of the property's value
 */
public class RealPropertyImpl<O>
        extends RealValueBaseImpl<O, Property.Data, Property.Listener<? super RealPropertyImpl<O>>, RealPropertyImpl<O>>
        implements RealProperty<O, RealTypeImpl<O>, RealCommandImpl, RealPropertyImpl<O>> {

    private RealCommandImpl setCommand;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type the property's type
     * @param values the property's initial value
     */
    @Inject
    public RealPropertyImpl(@Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted RealTypeImpl type,
                            @Assisted("min") int minValues,
                            @Assisted("max") int maxValues,
                            @Assisted Iterable values,
                            ManagedCollectionFactory managedCollectionFactory,
                            Receiver.Factory receiverFactory,
                            Sender.Factory senderFactory,
                            RealCommandImpl.Factory commandFactory,
                            RealParameterImpl.Factory parameterFactory) {
        super(logger, new Property.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory, receiverFactory, senderFactory, type, values);
        setCommand = commandFactory.create(ChildUtil.logger(logger, Property.SET_COMMAND_ID),
                Property.SET_COMMAND_ID,
                Property.SET_COMMAND_ID,
                "The function to change the property's value",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap serialisedValues) {
                        List<O> values = RealTypeImpl.deserialiseAll(getType(), serialisedValues.getChildren().get(Property.VALUE_PARAM));
                        RealPropertyImpl.this.setValues(values);
                    }
                },
                Lists.newArrayList(parameterFactory.create(
                        ChildUtil.logger(logger, Property.SET_COMMAND_ID, Property.VALUE_PARAM),
                        Property.VALUE_PARAM,
                        Property.VALUE_PARAM,
                        "The new value for the property",
                        type,
                        minValues,
                        maxValues)));
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        setCommand.init(ChildUtil.name(name, Property.SET_COMMAND_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        setCommand.uninit();
    }

    @Override
    public void set(final O value, Command.PerformListener<? super RealCommandImpl> listener) {
        getSetCommand().perform(new Type.InstanceMap() {
            {
                getChildren().put(Property.VALUE_ID, RealTypeImpl.serialiseAll(getType(), value));
            }
        }, listener);
    }

    @Override
    public RealCommandImpl getSetCommand() {
        return setCommand;
    }

    @Override
    public RealObject<?, ?> getChild(String id) {
        if(SET_COMMAND_ID.equals(id))
            return setCommand;
        return null;
    }

    public interface Factory {
        RealPropertyImpl<?> create(Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   RealTypeImpl type,
                                   @Assisted("min") int minValues,
                                   @Assisted("max") int maxValues,
                                   Iterable values);
    }
}
