package com.intuso.housemate.web.client.bootstrap.extensions;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HasValue;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class ToggleSwitch extends ButtonBase implements HasValue<Boolean> {

    private final static String ID = "id";
    private final static String BASE_ID = "toggle-switch-";
    private static int NEXT_ID = 0;

    private final static String BASE_STYLE = "make-switch";
    private final static String ATTR_ANIMATED = "data-animated";
    private final static String ATTR_LABEL = "data-text-label";
    private final static String ATTR_LABEL_ICON = "data-label-icon";
    private final static String ATTR_ON_LABEL = "data-on-label";
    private final static String ATTR_OFF_LABEL = "data-off-label";
    private final static String ATTR_DISABLED = "disabled";

    public enum Size {
        Large("switch-large"),
        Medium(""),
        Small("switch-small"),
        Mini("switch-mini");

        private final String styleName;

        private Size(String styleName) {
            this.styleName = styleName;
        }
    }

    private final String id;
    private final DivElement divElem;
    private final InputElement inputElem;

    private boolean created= false;

    private Size currentSize = Size.Medium;

    public ToggleSwitch() {
        this(createElement());
    }

    public ToggleSwitch(Element elem) {
        super(elem);

        divElem = DivElement.as(elem);
        inputElem = InputElement.as(elem.getFirstChildElement());

        id = BASE_ID + NEXT_ID++;
        divElem.setAttribute(ID, id);
    }

    private static Element createElement() {
        Element div = DOM.createDiv();
        div.addClassName(BASE_STYLE);
        div.setAttribute(ATTR_ANIMATED, "true");
        Element checkbox = DOM.createInputCheck();
        div.appendChild(checkbox);
        return div;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        create(this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        destroy(this);
    }

    private native void create(ToggleSwitch toggleSwitch) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            var div = $wnd.$('#' + id);
            div.bootstrapSwitch();
            div.on('switch-change', function (e, data) {
                toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::valueChanged()();
            });
        });
    }-*/;

    private native void destroy(ToggleSwitch toggleSwitch) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            $wnd.$('#' + id).bootstrapSwitch('destroy');
        });
    }-*/;

    private void valueChanged() {
        this.fireEvent(new ChangeEvent(getValue()));
    }

    public void setSize(Size size) {
        divElem.removeClassName(currentSize.styleName);
        currentSize = size;
        divElem.addClassName(currentSize.styleName);
    }

    public void setAnimated(boolean animated) {
        divElem.setAttribute(ATTR_ANIMATED, Boolean.toString(animated));
    }

    public void setEnabled(boolean enabled) {
        setEnabled(this, enabled);
    }

    private native void setEnabled(ToggleSwitch toggleSwitch, boolean enabled) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            $wnd.$('#' + id).bootstrapSwitch('setActive', enabled);
        })
    }-*/;

    public void setTrueLabel(String label) {
        divElem.setAttribute(ATTR_ON_LABEL, label);
    }

    public void setOnLabelIcon(IconType icon) {
        divElem.setAttribute(ATTR_ON_LABEL, "<i class='" + icon.getCssName() + "'/>");
    }

    public void setFalseLabel(String label) {
        divElem.setAttribute(ATTR_OFF_LABEL, label);
    }

    public void setOffLabelIcon(IconType icon) {
        divElem.setAttribute(ATTR_OFF_LABEL, "<i class='" + icon.getCssName() + "'/>");
    }

    public void setLabel(String label) {
        divElem.setAttribute(ATTR_LABEL, label);
    }

    public void setLabelIcon(IconType icon) {
        divElem.setAttribute(ATTR_LABEL_ICON, icon.getCssName());
    }

    @Override
    public Boolean getValue() {
        if (isAttached()) {
            return inputElem.isChecked();
        } else {
            return inputElem.isDefaultChecked();
        }
    }

    @Override
    public void setValue(Boolean value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        if (value == null) {
            value = Boolean.FALSE;
        }

        Boolean oldValue = getValue();
        if (value.equals(oldValue))
            return;

        inputElem.setChecked(value);
        inputElem.setDefaultChecked(value);
        setState(this, value);
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    private native void setState(ToggleSwitch toggleSwitch, boolean value) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            $wnd.$('#' + id).bootstrapSwitch('setState', value);
        });
    }-*/;

    public String getFormValue() {
        return inputElem.getValue();
    }

    public void setFormValue(String value) {
        inputElem.setAttribute("value", value);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<Boolean> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    private class ChangeEvent extends ValueChangeEvent<Boolean> {
        protected ChangeEvent(Boolean value) {
            super(value);
        }
    }
}
