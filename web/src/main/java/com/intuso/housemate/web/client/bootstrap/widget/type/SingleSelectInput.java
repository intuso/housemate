package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.github.gwtbootstrap.client.ui.ListBox;
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
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyOption;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class SingleSelectInput extends Composite implements TypeInput {

    public final static String OPTIONS = "options";

    interface SingleSelectInputUiBinder extends UiBinder<FlowPanel, SingleSelectInput> {
    }

    private static SingleSelectInputUiBinder ourUiBinder = GWT.create(SingleSelectInputUiBinder.class);

    @UiField(provided = true)
    protected ListBox listBox;
    @UiField
    protected FlowPanel subTypesPanel;

    private final GWTProxyList<OptionData, GWTProxyOption> options;
    private final BiMap<GWTProxyOption, Integer> optionMap = HashBiMap.create();
    private TypeInstances typeInstances = new TypeInstances(new TypeInstance());

    public SingleSelectInput(GWTProxyType type) {

        options = (GWTProxyList<OptionData, GWTProxyOption>) type.getWrapper(OPTIONS);

        listBox = new ListBox(false);
        initWidget(ourUiBinder.createAndBindUi(this));
        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                typeInstances.get(0).setValue(optionMap.inverse().get(listBox.getSelectedIndex()).getId());
                selectedOptionChanged();
            }
        });
        int i = 0;
        for(GWTProxyOption option : options) {
            optionMap.put(option, i++);
            listBox.addItem(option.getName());
        }
        /*if(options.size() > 0) {
            typeInstance.setValue(options.iterator().next().getId());
            selectedOptionChanged();
        }*/
    }

    @Override
    public HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler) {
        return addHandler(handler, TypeInputEditedEvent.TYPE);
    }

    @Override
    public void setTypeInstances(TypeInstances typeInstances) {
        this.typeInstances = typeInstances != null ? typeInstances : new TypeInstances();
        if(this.typeInstances.size() == 0)
            this.typeInstances.add(new TypeInstance());
        if(this.typeInstances.get(0).getValue() == null) {
            if(options.size() > 0)
                this.typeInstances.get(0).setValue(options.getWrappers().iterator().next().getId());
            listBox.setSelectedIndex(optionMap.get(options.get(this.typeInstances.get(0).getValue())));
        } else if(options.getWrapper(this.typeInstances.get(0).getValue()) == null)
            listBox.setSelectedIndex(0);
        else
            listBox.setSelectedIndex(optionMap.get(options.get(this.typeInstances.get(0).getValue())));
        selectedOptionChanged();
    }

    private void selectedOptionChanged() {
        showOptions();
        fireEvent(new TypeInputEditedEvent(typeInstances));
    }

    private void showOptions() {
        subTypesPanel.clear();
        GWTProxyOption option = options.get(typeInstances.get(0).getValue());
        if(option != null && option.getSubTypes() != null) {
            for(GWTProxySubType subType : option.getSubTypes()) {
                TypeInput input = TypeInputTableRow.getInput(subType.getType());
                input.addTypeInputEditedHandler(new SubTypeEditedHandler(subType.getId(), typeInstances.get(0)));
                subTypesPanel.add(input);
                if(typeInstances.get(0).getChildValues().get(subType.getId()) == null)
                    typeInstances.get(0).getChildValues().put(subType.getId(), new TypeInstances());
                input.setTypeInstances(typeInstances.get(0).getChildValues().get(subType.getId()));
            }
        }
    }

    private class SubTypeEditedHandler implements TypeInputEditedHandler {

        private final String typeId;
        private final TypeInstance typeInstance;

        public SubTypeEditedHandler(String typeId, TypeInstance typeInstance) {
            this.typeId = typeId;
            this.typeInstance = typeInstance;
        }

        @Override
        public void onTypeInputEdited(TypeInputEditedEvent event) {
            typeInstance.getChildValues().put(typeId, event.getTypeInstances());
        }
    }
}
