package com.intuso.housemate.api.object;

import com.intuso.housemate.object.real.RealObject;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealRootObject;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.StringType;
import org.junit.Ignore;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:48
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class TestRealRoot extends RealRootObject {

    public TestRealRoot(RealResources resources) {
        super(resources);
    }

    public void connect() {
        // do nothing
    }

    public void init() {
        addType(new StringType(getResources()));
        addType(new IntegerType(getResources()));
        addType(new BooleanType(getResources()));
    }

    public void addWrapper(RealObject<?, ?, ?, ?> wrapper) {
        removeWrapper(wrapper.getId());
        super.addWrapper(wrapper);
        wrapper.init(this);
    }
}
