package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.type.RegexTypeWrappable;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyRegexType;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.RealRegexType;
import com.intuso.housemate.object.real.impl.type.StringType;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class TypeTest {

    @Test
    public void testStringType() {
        TypeSerialiser<String> serialiser = StringType.SERIALISER;
        String value = "value";
        String serialised = serialiser.serialise(value);
        assertEquals("value", serialised);
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testIntegerType() {
        TypeSerialiser<Integer> serialiser = IntegerType.SERIALISER;
        Integer value = new Integer(1234);
        String serialised = serialiser.serialise(value);
        assertEquals("1234", serialised);
        assertEquals(value, serialiser.deserialise(serialised));
        value = new Integer(-1234);
        serialised = serialiser.serialise(value);
        assertEquals("-1234", serialised);
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testBooleanType() {
        TypeSerialiser<Boolean> serialiser = BooleanType.SERIALISER;
        Boolean value = Boolean.TRUE;
        String serialised = serialiser.serialise(value);
        assertEquals("true", serialised);
        assertEquals(value, serialiser.deserialise(serialised));
    }

    @Test
    public void testCustomType() throws HousemateException {
        TypeSerialiser<MyClass> serialiser = SERIALISER;
        MyClass mc = new MyClass("one", "two");
        String serialised = serialiser.serialise(mc);
        assertEquals("one;two", serialised);
        assertEquals(mc, serialiser.deserialise(serialised));
    }

    @Test
    public void testCustomFormat() throws HousemateException {
        ProxyRegexType pt = new MyProxyType(TestEnvironment.TEST_INSTANCE.getProxyNoChildrenResources(), new MyRealType(TestEnvironment.TEST_INSTANCE.getRealResources()).getWrappable());
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
        public String serialise(MyClass myClass) {
            return myClass.field1 + ";" + myClass.field2;
        }

        @Override
        public MyClass deserialise(String value) {
            String[] parts = value.split(";");
            return new MyClass(parts[0], parts[1]);
        }
    };

    private final static String ID = "my-type";
    private final static String NAME = "My Type";
    private final static String DESCRIPTION = "Two pieces of text without the character ';', separated by a ';'";
    private final static String REGEX = "[^;]+;[^;]+";

    private class MyRealType extends RealRegexType<MyClass> {

        protected MyRealType(RealResources resources) throws HousemateException {
            super(resources, ID, NAME, DESCRIPTION, REGEX, SERIALISER);
        }
    }

    private class MyProxyType extends ProxyRegexType<ProxyResources<NoChildrenProxyObjectFactory>, MyProxyType> {
        public MyProxyType(ProxyResources<NoChildrenProxyObjectFactory> resources, RegexTypeWrappable wrappable) {
            super(resources, wrappable);
        }
    }
}
