package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeValue;
import com.intuso.housemate.api.object.type.TypeValues;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:22
 * To change this template use File | Settings | File Templates.
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
        setCommand = (F) getWrapper(SET_COMMAND);
    }

    @Override
    public void set(final String value, CommandListener<? super F> listener) {
        getSetCommand().perform(new TypeValues() {
            {
                put(VALUE, new TypeValue(value));
            }
        }, listener);
    }

    @Override
    public F getSetCommand() {
        return setCommand;
    }
}
