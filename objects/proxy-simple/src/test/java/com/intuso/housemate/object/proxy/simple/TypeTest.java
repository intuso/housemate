package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.RegexTypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.object.proxy.NoChildrenProxyObject;
import com.intuso.housemate.object.proxy.ProxyRegexType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.RealRegexType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 */
public class TypeTest {

    @Test
    public void testStringType() {
        TypeSerialiser<String> serialiser = StringType.SERIALISER;
        String value = "value";
        TypeInstance serialised = serialiser.serialise(value);
        assertEquals("value", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testIntegerType() {
        TypeSerialiser<Integer> serialiser = IntegerType.SERIALISER;
        Integer value = new Integer(1234);
        TypeInstance serialised = serialiser.serialise(value);
        assertEquals("1234", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
        value = new Integer(-1234);
        serialised = serialiser.serialise(value);
        assertEquals("-1234", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testBooleanType() {
        TypeSerialiser<Boolean> serialiser = BooleanType.SERIALISER;
        Boolean value = Boolean.TRUE;
        TypeInstance serialised = serialiser.serialise(value);
        assertEquals("true", serialised.getValue());
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testCustomType() throws HousemateException {
        TypeSerialiser<MyClass> serialiser = SERIALISER;
        MyClass mc = new MyClass("one", "two");
        TypeInstance serialised = serialiser.serialise(mc);
        assertEquals("one;two", serialised.getValue());
        assertEquals(mc, serialiser.deserialise(serialised));
    }

    @Test
    public void testCustomFormat() throws HousemateException {
        ProxyRegexType pt = new MyProxyType(
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                TestEnvironment.TEST_INSTANCE.getInjector(),
                new MyRealType(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                        TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class)).getData());
        assertFalse(pt.isCorrectFormat("some string"));
        assertFalse(pt.isCorrectFormat(";two"));
        assertFalse(pt.isCorrectFormat("one;"));
        assertFalse(pt.isCorrectFormat("one;two;three"));
        assertTrue(pt.isCorrectFormat("one;two"));
        assertEquals(DESCRIPTION, pt.getDescription());
    }

    private class MyClass {
        String field1;
        String field2;

        private MyClass(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        @Override
        public boolean equals(Object otherObject) {
            MyClass otherMyClass = (MyClass)otherObject;
            return otherMyClass.field1.equals(field1) && otherMyClass.field2.equals(field2);
        }
    }

    private TypeSerialiser<MyClass> SERIALISER = new TypeSerialiser<MyClass>() {
        @Override
        public TypeInstance serialise(MyClass myClass) {
            return new TypeInstance(myClass.field1 + ";" + myClass.field2);
        }

        @Override
        public MyClass deserialise(TypeInstance value) {
            if(value == null || value.getValue() == null)
                return null;
            String[] parts = value.getValue().split(";");
            return new MyClass(parts[0], parts[1]);
        }
    };

    private final static String ID = "my-type";
    private final static String NAME = "My Type";
    private final static String DESCRIPTION = "Two pieces of text without the character ';', separated by a ';'";
    private final static String REGEX = "[^;]+;[^;]+";

    private class MyRealType extends RealRegexType<MyClass> {

        protected MyRealType(Log log, ListenersFactory listenersFactory) throws HousemateException {
            super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1, REGEX);
        }

        @Override
        public TypeInstance serialise(MyClass o) {
            return SERIALISER.serialise(o);
        }

        @Override
        public MyClass deserialise(TypeInstance value) {
            return SERIALISER.deserialise(value);
        }
    }

    private class MyProxyType extends ProxyRegexType<MyProxyType> {

        @Inject
        public MyProxyType(Log log, ListenersFactory listenersFactory, Injector injector, RegexTypeData data) {
            super(log, listenersFactory, data, injector.getInstance(RegexMatcherFactory.class).createRegexMatcher(data.getRegexPattern()));
        }

        @Override
        protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
            return null;
        }
    }
}
