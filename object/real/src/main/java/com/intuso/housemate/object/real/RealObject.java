package com.intuso.housemate.object.real;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.housemate.api.object.root.real.RealRoot;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealObject<WBL extends HousemateObjectWrappable<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends RealObject<? extends SWBL, ?, ?, ?>,
            L extends ObjectListener>
        extends HousemateObject<RealResources, WBL, SWBL, SWR, L>
        implements BaseObject<L> {

    public final static String NAME_DESCRIPTION = "The name of this object";

    private RealRoot<?, ?, ?, ?, ?> realRoot;

    protected RealObject(RealResources resources, WBL wrappable) {
        super(resources, wrappable);
    }

    protected final <MV extends Message.Payload> void sendMessage(String type, MV value) {
        getRealRoot().sendMessage(new Message<MV>(
                getPath(), type, value));
    }

    protected RealRoot<?, ?, ?, ?, ?> getRealRoot() {
        return realRoot;
    }

    @Override
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {

        // get the broker for this object
        // get the broker for this object
        if(this instanceof RealRoot)
            realRoot = (RealRoot)this;
        else if(parent != null && parent instanceof RealObject)
            realRoot = ((RealObject)parent).realRoot;
    }
}
