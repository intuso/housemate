package com.intuso.housemate.web.client.bootstrap.extensions;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HasValue;

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

    private final static String STATE = "state";
    private final static String SIZE = "size";
    private final static String ANIMATE = "animate";
    private final static String DISABLED = "disabled";
    private final static String READONLY = "readonly";
    private final static String INDETERMINATE = "indeterminate";
    private final static String INVERSE = "inverse";
    private final static String RADIO_ALL_OFF = "radioAllOff";
    private final static String ON_COLOUR = "onColor";
    private final static String OFF_COLOUR = "offColor";
    private final static String ON_TEXT = "onText";
    private final static String OFF_TEXT = "offText";
    private final static String LABEL_TEXT = "labelText";
    private final static String HANDLE_WIDTH = "handleWidth";
    private final static String LABEL_WIDTH = "labelWidth";
    private final static String BASE_CLASS = "baseClass";
    private final static String WRAPPER_CLASS = "wrapperClass";

    public enum Size {
        Large("large"),
        Normal("normal"),
        Small("small"),
        Mini("mini");

        private final String name;

        private Size(String name) {
            this.name = name;
        }
    }

    public enum Colour {
        Primary("primary"),
        Info("info"),
        Success("success"),
        Warning("warning"),
        Danger("danger"),
        Default("default");

        private final String name;

        private Colour(String name) {
            this.name = name;
        }
    }

    private final String id;
    private final InputElement inputElem;

    private boolean state = false;
    private Size size = Size.Small;
    private boolean animate = true;
    private boolean disabled = false;
    private boolean readonly = false;
    private boolean indeterminate = false;
    private boolean inverse = false;
    private boolean radioAllOff = false;
    private Colour onColour = Colour.Primary;
    private Colour offColour = Colour.Default;
    private String onText = "On";
    private String offText = "Off";
    private String labelText = null;
    private String handleWidth = null;
    private String labelWidth = null;
    private String baseClass = null;
    private String wrapperClass = null;

    private boolean created = false;

    public ToggleSwitch() {
        super(DOM.createDiv()); // need an outer-div so people can add their own styles
        DivElement divElement = DivElement.as(getElement());
        inputElem = InputElement.as(DOM.createInputCheck());
        divElement.appendChild(inputElem);
        id = BASE_ID + NEXT_ID++;
        inputElem.setAttribute(ID, id);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if(!created) {
            created = true;
            create(this);
            setState(state, true);
            setSize(size);
            setAnimate(animate);
            setDisabled(disabled);
            setReadonly(readonly);
            setIndeterminate(indeterminate);
            setInverse(inverse);
            setRadioAllOff(radioAllOff);
            setOnColour(onColour);
            setOffColour(offColour);
            if (onText != null)
                setOnText(onText);
            if (offText != null)
                setOffText(offText);
            if (labelText != null)
                setLabelText(labelText);
            if (handleWidth != null)
                setHandleWidth(handleWidth);
            if (labelWidth != null)
                setLabelWidth(labelWidth);
            if (baseClass != null)
                setBaseClass(baseClass);
            if (wrapperClass != null)
                setWrapperClass(wrapperClass);
        }
    }

    private native void create(ToggleSwitch toggleSwitch) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            var elem = $wnd.$('#' + id);
            elem.bootstrapSwitch();
            elem.on('switchChange.bootstrapSwitch', function (event, state) {
                toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::valueChanged(Z)(state);
            });
        });
    }-*/;

    // not unused, called by js function above
    @SuppressWarnings("unused")
    private void valueChanged(boolean state) {
        fireEvent(new ChangeEvent(state));
    }

    public void setState(boolean state) {
        setValue(state);
    }

    public void setState(boolean state, boolean skipEvents) {
        setValue(state, skipEvents);
    }

    public void setSize(Size size) {
        this.size = size;
        setString(this, SIZE, size.name);
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
        setBoolean(this, ANIMATE, animate);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        setBoolean(this, DISABLED, disabled);
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        setBoolean(this, READONLY, readonly);
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        setBoolean(this, INDETERMINATE, indeterminate);
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
        setBoolean(this, INVERSE, inverse);
    }

    public void setRadioAllOff(boolean radioAllOff) {
        this.radioAllOff = radioAllOff;
        setBoolean(this, RADIO_ALL_OFF, radioAllOff);
    }

    public void setOnColour(Colour onColour) {
        this.onColour = onColour;
        setString(this, ON_COLOUR, onColour.name);
    }

    public void setOffColour(Colour offColour) {
        this.offColour = offColour;
        setString(this, OFF_COLOUR, offColour.name);
    }

    public void setOnText(String onText) {
        this.onText = onText;
        setString(this, ON_TEXT, onText);
    }

    public void setOffText(String offText) {
        this.offText = offText;
        setString(this, OFF_TEXT, offText);
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        setString(this, LABEL_TEXT, labelText);
    }

    public void setHandleWidth(String handleWidth) {
        this.handleWidth = handleWidth;
        setString(this, HANDLE_WIDTH, handleWidth);
    }

    public void setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
        setString(this, LABEL_WIDTH, labelWidth);
    }

    public void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
        setString(this, BASE_CLASS, baseClass);
    }

    public void setWrapperClass(String wrapperClass) {
        this.wrapperClass = wrapperClass;
        setString(this, WRAPPER_CLASS, wrapperClass);
    }

    @Override
    public Boolean getValue() {
        return state;
    }

    @Override
    public void setValue(Boolean state) {
        setValue(state, false);
    }

    @Override
    public void setValue(Boolean state, boolean skipEvents) {
        if (state == null)
            state = Boolean.FALSE;

        setBoolean(this, STATE, state, this.state == state || skipEvents); // always skip events if the value is the same
        this.state = state;
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

    private native void setString(ToggleSwitch toggleSwitch, String field, String value) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            $wnd.$('#' + id).bootstrapSwitch(field, value);
        });
    }-*/;

    private native void setBoolean(ToggleSwitch toggleSwitch, String field, boolean value) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            $wnd.$('#' + id).bootstrapSwitch(field, value);
        });
    }-*/;

    private native void setBoolean(ToggleSwitch toggleSwitch, String field, boolean value, boolean extra) /*-{
        $wnd.$(function() {
            var id = toggleSwitch.@com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch::id;
            $wnd.$('#' + id).bootstrapSwitch(field, value, extra);
        });
    }-*/;
}
