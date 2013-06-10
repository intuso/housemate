package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.SingleChoiceTypeWrappable;
import com.intuso.housemate.api.object.option.HasOptions;
import com.intuso.housemate.api.object.option.OptionWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxySingleChoiceType<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, ListWrappable<OptionWrappable>, OL>>,
            SR extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?>, OptionWrappable, ? extends O>>,
            O extends ProxyOption<?, ?, ?, ?, O>,
            OL extends ProxyList<?, ?, OptionWrappable, O, OL>,
            T extends ProxySingleChoiceType<R, SR, O, OL, T>>
        extends ProxyType<R, SR, SingleChoiceTypeWrappable, ListWrappable<OptionWrappable>, OL, T>
        implements HasOptions {

    private static final String OPTIONS = "options";

    private OL options;

    public ProxySingleChoiceType(R resources, SR subResources, SingleChoiceTypeWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        options = (OL)getWrapper(OPTIONS);
    }

    public OL getOptions() {
        return options;
    }
}
