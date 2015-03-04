package com.intuso.housemate.web.client.bootstrap.widget.type;

import org.gwtbootstrap3.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
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

    public MultiSelectInput(GWTProxyType type, final TypeInstances typeInstances) {
        super(true);
        if(type.getChild(OPTIONS) != null) {
            options = (GWTProxyList<OptionData, GWTProxyOption>) type.getChild(OPTIONS);
            int i = 0;
            for(GWTProxyOption option : options) {
                optionIndices.put(option, i);
                optionsByName.put(option.getName(), option);
                addItem(option.getName());
                i++;
            }
            for(TypeInstance typeInstance : typeInstances.getElements())
                selectedOptions.add(typeInstance.getValue());
            for(String id : selectedOptions) {
                GWTProxyOption option = options.get(id);
                if(option != null) {
                    Integer index = optionIndices.get(option);
                    if(index != null)
                        setItemSelected(index, true);
                }
            }
        }
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                selectedOptions.clear();
                for(int i = 0; i < getItemCount(); i++)
                    if(isItemSelected(i))
                        selectedOptions.add(getItemText(i));
                typeInstances.getElements().clear();
                for(String selectedOption : selectedOptions)
                    typeInstances.getElements().add(new TypeInstance(selectedOption));
            }
        });
    }
}
