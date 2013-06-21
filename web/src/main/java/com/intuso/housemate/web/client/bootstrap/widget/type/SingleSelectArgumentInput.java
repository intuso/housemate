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
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyOption;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class SingleSelectArgumentInput extends Composite implements TypeInput {

    public final static String OPTIONS = "options";

    interface SingleSelectArgumentInputUiBinder extends UiBinder<FlowPanel, SingleSelectArgumentInput> {
    }

    private static SingleSelectArgumentInputUiBinder ourUiBinder = GWT.create(SingleSelectArgumentInputUiBinder.class);

    @UiField(provided = true)
    protected ListBox listBox;
    @UiField
    protected FlowPanel subTypesPanel;

    private final GWTProxyList<OptionWrappable, GWTProxyOption> options;
    private final BiMap<GWTProxyOption, Integer> optionMap = HashBiMap.create();
    private TypeInstance typeInstance = new TypeInstance();

    public SingleSelectArgumentInput(GWTProxyType type) {

        options = (GWTProxyList<OptionWrappable, GWTProxyOption>) type.getWrapper(OPTIONS);

        listBox = new ListBox(false);
        initWidget(ourUiBinder.createAndBindUi(this));
        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                typeInstance.setValue(optionMap.inverse().get(listBox.getSelectedIndex()).getId());
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
    public void setTypeInstance(TypeInstance typeInstance) {
        if(typeInstance == null || typeInstance.getValue() == null
                || options.getWrapper(typeInstance.getValue()) == null)
            listBox.setSelectedIndex(0);
        else
            listBox.setSelectedIndex(optionMap.get(options.get(typeInstance.getValue())));
        this.typeInstance = typeInstance;
        selectedOptionChanged();
    }

    private void selectedOptionChanged() {
        showOptions();
        fireEvent(new TypeInputEditedEvent(typeInstance));
    }

    private void showOptions() {
        subTypesPanel.clear();
        GWTProxyOption option = options.get(typeInstance.getValue());
        if(option != null && option.getSubTypes() != null) {
            for(GWTProxySubType subType : option.getSubTypes()) {
                TypeInput argumentInput = TypeInputTableRow.getArgumentInput(subType.getType());
                argumentInput.addTypeInputEditedHandler(new SubTypeArgumentEditedHandler(subType.getId(), typeInstance));
                subTypesPanel.add(argumentInput);
                if(typeInstance.getChildValues().get(subType.getId()) == null)
                    typeInstance.getChildValues().put(subType.getId(), new TypeInstance());
                argumentInput.setTypeInstance(typeInstance.getChildValues().get(subType.getId()));
            }
        }
    }

    private class SubTypeArgumentEditedHandler implements TypeInputEditedHandler {

        private final String typeId;
        private final TypeInstance typeInstance;

        public SubTypeArgumentEditedHandler(String typeId, TypeInstance typeInstance) {
            this.typeId = typeId;
            this.typeInstance = typeInstance;
        }

        @Override
        public void onTypeInputEdited(TypeInputEditedEvent event) {
            typeInstance.getChildValues().put(typeId, event.getNewValue());
        }
    }
}
