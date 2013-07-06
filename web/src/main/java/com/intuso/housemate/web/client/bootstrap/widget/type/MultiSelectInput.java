package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyOption;
import com.intuso.housemate.web.client.object.GWTProxyType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class MultiSelectInput extends ListBox implements TypeInput {

    public final static String OPTIONS = "options";

    private Map<GWTProxyOption, Integer> optionIndices = new HashMap<GWTProxyOption, Integer>();
    private Set<String> selectedOptions = new HashSet<String>();
    private GWTProxyList<OptionData, GWTProxyOption> options;
    private Map<String, GWTProxyOption> optionsByName = new HashMap<String, GWTProxyOption>();

    public MultiSelectInput(GWTProxyType type) {
        super(true);
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                selectedOptions.clear();
                for(int i = 0; i < getItemCount(); i++)
                    if(isItemSelected(i))
                        selectedOptions.add(getItemText(i));
                TypeInstances typeInstances = new TypeInstances();
                for(String selectedOption : selectedOptions)
                    typeInstances.add(new TypeInstance(selectedOption));
                fireEvent(new TypeInputEditedEvent(typeInstances));
            }
        });
        optionIndices.clear();
        options = null;
        optionsByName.clear();
        if(type.getWrapper(OPTIONS) != null) {
            options = (GWTProxyList<OptionData, GWTProxyOption>) type.getWrapper(OPTIONS);
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
    public HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler) {
        return addHandler(handler, TypeInputEditedEvent.TYPE);
    }

    @Override
    public void setTypeInstances(TypeInstances typeInstances) {
        selectedOptions.clear();
        for(TypeInstance typeInstance : typeInstances)
            selectedOptions.add(typeInstance.getValue());
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
