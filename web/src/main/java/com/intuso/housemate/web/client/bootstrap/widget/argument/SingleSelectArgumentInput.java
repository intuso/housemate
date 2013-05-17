package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.core.object.type.option.OptionWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyOption;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class SingleSelectArgumentInput extends ListBox implements ArgumentInput {

    public final static String OPTIONS = "options";

    public SingleSelectArgumentInput(GWTProxyType type) {
        super(false);
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                fireEvent(new ArgumentEditedEvent(getValue(getSelectedIndex())));
            }
        });
        if(type.getWrapper(OPTIONS) != null) {
            GWTProxyList<OptionWrappable, GWTProxyOption> options = (GWTProxyList<OptionWrappable, GWTProxyOption>) type.getWrapper(OPTIONS);
            for(GWTProxyOption option : options)
                addItem(option.getName());
            if(options.size() > 0)
                fireEvent(new ArgumentEditedEvent(getValue(getSelectedIndex())));
        }
    }

    @Override
    public HandlerRegistration addArgumentEditedHandler(ArgumentEditedHandler handler) {
        HandlerRegistration result = addHandler(handler, ArgumentEditedEvent.TYPE);
        handler.onArgumentEdited(new ArgumentEditedEvent(getValue(getSelectedIndex())));
        return result;
    }

    @Override
    public void setValue(Value<?, ?> value) {
        setSelectedValue(value.getValue());
    }
}
