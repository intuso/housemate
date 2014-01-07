package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.log.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 */
public class ParameterTest {

    public final static String PARAMETER = "parameters";

    private SimpleProxyObject.List<ParameterData, SimpleProxyObject.Parameter> proxyList
            = new SimpleProxyObject.List<ParameterData, SimpleProxyObject.Parameter>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector(),
            new ListData(PARAMETER, PARAMETER, PARAMETER));
    private RealList<ParameterData, RealParameter<?>> realList = new RealList<ParameterData, RealParameter<?>>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            PARAMETER, PARAMETER, PARAMETER, new ArrayList<RealParameter<?>>());
    private RealParameter realParameter;
    private SimpleProxyObject.Parameter proxyParameter;

    public ParameterTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realParameter = BooleanType.createParameter(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                "my-parameter", "My Parameter", "description");
        realList.add(realParameter);
        proxyParameter = proxyList.get("my-parameter");
    }

    @Test
    public void testCreateParameter() throws HousemateException {
        assertNotNull(proxyParameter);
        assertNotNull(proxyParameter.getType());
    }
}
