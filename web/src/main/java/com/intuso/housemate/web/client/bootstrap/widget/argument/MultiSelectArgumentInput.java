package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.core.object.type.option.OptionWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.real.impl.type.RealMultiChoiceType;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyOption;
import com.intuso.housemate.web.client.object.GWTProxyType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class MultiSelectArgumentInput extends ListBox implements ArgumentInput {

    public final static String OPTIONS = "options";

    private Map<GWTProxyOption, Integer> optionIndices = new HashMap<GWTProxyOption, Integer>();
    private Set<String> selectedOptions = new HashSet<String>();
    private GWTProxyList<OptionWrappable, GWTProxyOption> options;
    private Map<String, GWTProxyOption> optionsByName = new HashMap<String, GWTProxyOption>();

    public MultiSelectArgumentInput(GWTProxyType type) {
        super(true);
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                selectedOptions.clear();
                for(int i = 0; i < getItemCount(); i++)
                    if(isItemSelected(i))
                        selectedOptions.add(getItemText(i));
                fireEvent(new ArgumentEditedEvent(RealMultiChoiceType.SERIALISER.serialise(selectedOptions)));
            }
        });
        optionIndices.clear();
        options = null;
        optionsByName.clear();
        if(type.getWrapper(OPTIONS) != null) {
            options = (GWTProxyList<OptionWrappable, GWTProxyOption>) type.getWrapper(OPTIONS);
            int i = 0;
            for(GWTProxyOption option : options) {
                optionIndices.put(option, i);
                optionsByName.put(option.getName(), option);
                addItem(option.getName());
                i++;
            }
        }
    }

    @Override
    public HandlerRegistration addArgumentEditedHandler(ArgumentEditedHandler handler) {
        return addHandler(handler, ArgumentEditedEvent.TYPE);
    }

    @Override
    public void setValue(Value<?, ?> value) {
        selectedOptions.clear();
        selectedOptions.addAll(RealMultiChoiceType.SERIALISER.deserialise(value.getValue()));
        for(int i = 0; i < options.size(); i++)
            setItemSelected(i, false);
        for(String id : selectedOptions) {
            GWTProxyOption option = options.get(id);
            if(option != null) {
                Integer index = optionIndices.get(option);
                if(index != null)
                    setItemSelected(index, true);
            }
        }
    }
}
