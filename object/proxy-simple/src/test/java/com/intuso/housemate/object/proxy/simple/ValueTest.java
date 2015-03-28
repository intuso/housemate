package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 */
public class ValueTest {

    public final static String VALUES = "values";

    private SimpleProxyList<ValueData, SimpleProxyValue> proxyList
            = new SimpleProxyList<>(
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                TestEnvironment.TEST_INSTANCE.getInjector(),
            new ListData(VALUES, VALUES, VALUES));
    private RealList<ValueBaseData<NoChildrenData>, RealValue<?>> realList = new RealList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            VALUES, VALUES, VALUES, new ArrayList<RealValue<?>>());
    private RealValue<Integer> realValue;
    private SimpleProxyValue proxyValue;

    public ValueTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realValue = IntegerType.createValue(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-value", "My Value", "description", 1234);
        realList.add(realValue);
        proxyValue = proxyList.get("my-value");
    }

    @Test
    public void testCreateProxyValue() throws HousemateException {
        assertNotNull(proxyValue);
    }

    @Test
    public void testSetRealValue() throws HousemateException {
        realValue.setTypedValues(-1234);
        assertEquals("-1234", proxyValue.getTypeInstances().getFirstValue());
    }

    @Test
    public void testListenerCalled() throws HousemateException {
        final AtomicBoolean called = new AtomicBoolean(false);
        proxyValue.addObjectListener(new ValueListener<SimpleProxyValue>() {

            @Override
            public void valueChanging(SimpleProxyValue value) {
                // do nothing
            }

            @Override
            public void valueChanged(SimpleProxyValue value) {
                called.set(true);
            }
        });
        realValue.setTypedValues(-1234);
        assertEquals(true, called.get());
    }
}
