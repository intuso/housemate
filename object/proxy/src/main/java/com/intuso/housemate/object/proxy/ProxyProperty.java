package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 */
public abstract class ProxyProperty<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, CommandWrappable, F>>,
            SR extends ProxyResources<?>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>,
            F extends ProxyCommand<?, ?, ?, ?, F>,
            P extends ProxyProperty<R, SR, T, F, P>>
        extends ProxyValueBase<R, SR, PropertyWrappable, CommandWrappable, F, T, P>
        implements Property<T, F, P> {

    private F setCommand;

    public ProxyProperty(R resources, SR subResources, PropertyWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = (F) getWrapper(SET_COMMAND_ID);
    }

    @Override
    public void set(final TypeInstance value, CommandListener<? super F> listener) {
        getSetCommand().perform(new TypeInstances() {
            {
                put(VALUE_ID, value);
            }
        }, listener);
    }

    @Override
    public F getSetCommand() {
        return setCommand;
    }
}
