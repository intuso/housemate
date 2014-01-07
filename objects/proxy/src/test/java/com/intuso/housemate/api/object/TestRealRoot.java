package com.intuso.housemate.api.object;

import com.google.inject.Inject;
import com.intuso.housemate.api.comms.RealRouterImpl;
import com.intuso.housemate.object.real.RealObject;
import com.intuso.housemate.object.real.RealRootObject;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.log.Log;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestRealRoot extends RealRootObject {

    @Inject
    public TestRealRoot(Log log, RealRouterImpl router) {
        super(log, router);
    }

    public void connect() {
        // do nothing
    }

    public void init() {
        addType(new StringType(getLog()));
        addType(new IntegerType(getLog()));
        addType(new BooleanType(getLog()));
    }

    public void addWrapper(RealObject<?, ?, ?, ?> wrapper) {
        removeChild(wrapper.getId());
        super.addChild(wrapper);
        wrapper.init(this);
    }
}
