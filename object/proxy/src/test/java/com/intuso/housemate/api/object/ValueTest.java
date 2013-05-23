package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class ValueTest {

    public final static String VALUES = "values";

    private SimpleProxyObject.List<ValueWrappable, SimpleProxyObject.Value> proxyList
            = new SimpleProxyObject.List<ValueWrappable, SimpleProxyObject.Value>(
                SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Value()),
                TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListWrappable(VALUES, VALUES, VALUES));
    private RealList<ValueWrappableBase<NoChildrenWrappable>, RealValue<?>> realList = new RealList<ValueWrappableBase<NoChildrenWrappable>, RealValue<?>>(TestEnvironment.TEST_INSTANCE.getRealResources(), VALUES, VALUES, VALUES, new ArrayList<RealValue<?>>());
    private RealValue<Integer> realValue;
    private SimpleProxyObject.Value proxyValue;

    public ValueTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addWrapper(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realValue = IntegerType.createValue(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-value", "My Value", "description", 1234);
        realList.add(realValue);
        proxyValue = proxyList.get("my-value");
    }

    @Test
    public void testCreateProxyValue() throws HousemateException {
        assertNotNull(proxyValue);
    }

    @Test
    public void testSetRealValue() throws HousemateException {
        realValue.setTypedValue(-1234);
        assertEquals("-1234", proxyValue.getValue());
    }

    @Test
    public void testListenerCalled() throws HousemateException {
        final AtomicBoolean called = new AtomicBoolean(false);
        proxyValue.addObjectListener(new ValueListener<SimpleProxyObject.Value>() {
            @Override
            public void valueChanged(SimpleProxyObject.Value value) {
                called.set(true);
            }
        });
        realValue.setTypedValue(-1234);
        assertEquals(true, called.get());
    }
}
