package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyOption;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.ListBox;

/**
 */
public class SingleSelectInput extends Composite implements TypeInput, UserInputHandler, ChangeHandler {

    public final static String OPTIONS = "options";

    interface SingleSelectInputUiBinder extends UiBinder<FlowPanel, SingleSelectInput> {
    }

    private static SingleSelectInputUiBinder ourUiBinder = GWT.create(SingleSelectInputUiBinder.class);

    @UiField
    protected ListBox listBox;
    @UiField
    protected FlowPanel subTypesPanel;

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;
    private final TypeInstances typeInstances;
    private final GWTProxyList<OptionData, GWTProxyOption> options;
    private final BiMap<GWTProxyOption, Integer> optionMap = HashBiMap.create();

    public SingleSelectInput(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyType type, final TypeInstances typeInstances) {

        this.types = types;
        this.typeInstances = typeInstances;

        if(typeInstances.getElements().size() == 0)
            typeInstances.getElements().add(new TypeInstance());

        options = (GWTProxyList<OptionData, GWTProxyOption>) type.getChild(OPTIONS);

        initWidget(ourUiBinder.createAndBindUi(this));
        listBox.setMultipleSelect(false);

        // add to the list for each option
        int i = 0;
        for(GWTProxyOption option : options) {
            optionMap.put(option, i++);
            listBox.addItem(option.getName());
        }

        // get the selected id, or if not set choose the first one
        String optionId = null;
        if(typeInstances == null || typeInstances.getElements().get(0) == null || typeInstances.getElements().get(0).getValue() == null) {
            if(options.size() > 0)
                optionId = options.iterator().next().getId();
        } else
            optionId = typeInstances.getElements().get(0).getValue();
        if(typeInstances != null && typeInstances.getElements().get(0) != null)
            typeInstances.getElements().get(0).setValue(optionId);

        // highlight the selected option in the list and show it's sub options
        if(optionId != null && options.getChild(optionId) != null) {
            listBox.setSelectedIndex(optionMap.get(options.get(optionId)));
            showOptions(optionId, typeInstances);
        }

        listBox.addChangeHandler(this);
    }

    private void showOptions(String optionId, TypeInstances typeInstances) {
        subTypesPanel.clear();
        GWTProxyOption option = options.get(optionId);
        if(option != null && option.getSubTypes() != null) {
            for(GWTProxySubType subType : option.getSubTypes()) {
                if(typeInstances.getElements().get(0) == null)
                    typeInstances.getElements().add(0, new TypeInstance());
                if(typeInstances.getElements().get(0).getChildValues() == null)
                    typeInstances.getElements().add(0, new TypeInstance());
                if(typeInstances.getElements().get(0).getChildValues().getChildren().get(subType.getId()) == null)
                    typeInstances.getElements().get(0).getChildValues().getChildren().put(subType.getId(), new TypeInstances());
                TypeInput input = TypeInput.FACTORY.create(types, subType.getTypeId(), typeInstances.getElements().get(0).getChildValues().getChildren().get(subType.getId()), this);
                subTypesPanel.add(input);
            }
        }
    }

    @Override
    public void onChange(ChangeEvent event) {
        String optionId = optionMap.inverse().get(listBox.getSelectedIndex()).getId();
        typeInstances.getElements().get(0).setValue(optionId);
        showOptions(optionId, typeInstances);
        fireEvent(new UserInputEvent());
    }

    @Override
    public TypeInstances getTypeInstances() {
        return typeInstances;
    }

    @Override
    public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
        return addHandler(handler, UserInputEvent.TYPE);
    }

    @Override
    public void onUserInput(UserInputEvent event) {
        fireEvent(new UserInputEvent());
    }
}
