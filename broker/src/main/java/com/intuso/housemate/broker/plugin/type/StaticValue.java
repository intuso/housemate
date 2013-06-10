package com.intuso.housemate.broker.plugin.type;

import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.impl.type.RealSimpleType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class StaticValue extends ValueSource {

    private final SimpleValue value;

    protected StaticValue(TypeInstance newValue) {
        value = new SimpleValue(newValue);
    }

    @Override
    public Value<?, ?> getValue() {
        return value;
    }

    protected class SimpleValue implements Value<RealSimpleType<String>, SimpleValue> {

        private final TypeInstance value;
        private Listeners<ValueListener<? super SimpleValue>> listeners = new Listeners<ValueListener<? super SimpleValue>>();

        private SimpleValue(TypeInstance value) {
            this.value = value;
        }

        @Override
        public ListenerRegistration addObjectListener(ValueListener<? super SimpleValue> listener) {
            return listeners.addListener(listener);
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String[] getPath() {
            return null;
        }

        @Override
        public RealSimpleType<String> getType() {
            return null;
        }

        @Override
        public TypeInstance getTypeInstance() {
            return value;
        }
    }
}
