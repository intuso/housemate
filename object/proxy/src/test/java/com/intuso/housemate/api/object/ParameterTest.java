package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class ParameterTest {

    public final static String PARAMETER = "parameters";

    private SimpleProxyObject.List<ParameterWrappable, SimpleProxyObject.Parameter> proxyList
            = new SimpleProxyObject.List<ParameterWrappable, SimpleProxyObject.Parameter>(
            SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Parameter()),
            TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListWrappable(PARAMETER, PARAMETER, PARAMETER));
    private RealList<ParameterWrappable, RealParameter<?>> realList = new RealList<ParameterWrappable, RealParameter<?>>(TestEnvironment.TEST_INSTANCE.getRealResources(), PARAMETER, PARAMETER, PARAMETER, new ArrayList<RealParameter<?>>());
    private RealParameter realParameter;
    private SimpleProxyObject.Parameter proxyParameter;

    public ParameterTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addWrapper(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realParameter = BooleanType.createParameter(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-parameter", "My Parameter", "description");
        realList.add(realParameter);
        proxyParameter = proxyList.get("my-parameter");
    }

    @Test
    public void testCreateParameter() throws HousemateException {
        assertNotNull(proxyParameter);
        assertNotNull(proxyParameter.getType());
    }
}
