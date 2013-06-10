package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.argument.ArgumentWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealArgument;
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
public class ArgumentTest {

    public final static String ARGUMENTS = "arguments";

    private SimpleProxyObject.List<ArgumentWrappable, SimpleProxyObject.Argument> proxyList
            = new SimpleProxyObject.List<ArgumentWrappable, SimpleProxyObject.Argument>(
            SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Argument()),
            TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListWrappable(ARGUMENTS, ARGUMENTS, ARGUMENTS));
    private RealList<ArgumentWrappable, RealArgument<?>> realList = new RealList<ArgumentWrappable, RealArgument<?>>(TestEnvironment.TEST_INSTANCE.getRealResources(), ARGUMENTS, ARGUMENTS, ARGUMENTS, new ArrayList<RealArgument<?>>());
    private RealArgument realArgument;
    private SimpleProxyObject.Argument proxyArgument;

    public ArgumentTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addWrapper(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realArgument = BooleanType.createArgument(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-argument", "My Argument", "description");
        realList.add(realArgument);
        proxyArgument = proxyList.get("my-argument");
    }

    @Test
    public void testCreateArgument() throws HousemateException {
        assertNotNull(proxyArgument);
        assertNotNull(proxyArgument.getType());
    }
}
