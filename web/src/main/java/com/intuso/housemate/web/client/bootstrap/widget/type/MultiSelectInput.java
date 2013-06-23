package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
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
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class MultiSelectInput extends ListBox implements TypeInput {

    public final static String OPTIONS = "options";

    private Map<GWTProxyOption, Integer> optionIndices = new HashMap<GWTProxyOption, Integer>();
    private Set<String> selectedOptions = new HashSet<String>();
    private GWTProxyList<OptionWrappable, GWTProxyOption> options;
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
                TypeInstance typeInstance = new TypeInstance();
                for(String selectedOption : selectedOptions)
                    typeInstance.getChildValues().put(selectedOption, new TypeInstance());
                fireEvent(new TypeInputEditedEvent(typeInstance));
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
    public HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler) {
        return addHandler(handler, TypeInputEditedEvent.TYPE);
    }

    @Override
    public void setTypeInstance(TypeInstance typeInstance) {
        selectedOptions.clear();
        selectedOptions.addAll(typeInstance.getChildValues().keySet());
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
