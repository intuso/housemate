package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.api.internal.object.ConvertingList;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;

/**
 * Created by tomc on 02/05/17.
 */
public class ProxyConvertingList<
        FROM extends Object<?, ?, ?>,
        TO extends ProxyObject<?, ?, ?>>
        extends ConvertingList<FROM, TO> implements List.Listener<TO,ConvertingList<FROM,TO>> {

    private boolean ancestors = false;
    private final java.util.List<ListView<?>> views = Lists.newArrayList();

    public ProxyConvertingList(List<? extends FROM, ?> list, Converter<? super FROM, ? extends TO> converter) {
        super(list, converter);
        addObjectListener(this, true);
    }

    public void load(ListView<?> view) {
        if(view == null || view.getMode() == null)
            return;

        // if already viewing ancestors, don't do anything as entire object tree will already be loaded
        if (ancestors)
            return;

        switch (view.getMode()) {

            // if ancestors, set that flag, subscribe to all children, and view any current children with an ancestors view
            case ANCESTORS:
                ancestors = true;
                views.clear();
                for (TO to : this)
                    ((ProxyObject) to).load(to.createView(View.Mode.ANCESTORS));
                break;

            // else if children, make sure we subscribed to all, and view any current elements with the element view
            case CHILDREN:
                views.add(view);
                for (TO to : this)
                    ((ProxyObject) to).load(to.createView(View.Mode.SELECTION));
                break;

            // else if selection, make sure we're subscribed to those elements and view any current relevant elements
            case SELECTION:
                views.add(view);
                if (view.getElements() != null) {
                    for (String elementName : view.getElements()) {
                        TO to = get(elementName);
                        if (to != null)
                            ((ProxyObject) to).load(view.getView());
                    }
                }
                break;
        }
    }

    @Override
    public void elementAdded(ConvertingList<FROM, TO> list, TO element) {
        if(ancestors)
            ((ProxyObject) element).load(element.createView(View.Mode.ANCESTORS));
        else {
            // check each list view to see if it's for this element
            for (ListView<?> view : views)
                if (view.getMode() == View.Mode.CHILDREN || (view.getElements() != null && view.getElements().contains(element.getId())))
                    ((ProxyObject) element).load(view.getView());
        }
    }

    @Override
    public void elementRemoved(ConvertingList<FROM, TO> list, TO element) {
        // do nothing
    }
}
