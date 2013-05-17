package com.intuso.housemate.core.object;

import com.intuso.housemate.real.RealObject;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.RealRootObject;
import com.intuso.housemate.real.impl.type.BooleanType;
import com.intuso.housemate.real.impl.type.IntegerType;
import com.intuso.housemate.real.impl.type.StringType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:48
 * To change this template use File | Settings | File Templates.
 */
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
