package com.intuso.housemate.broker.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.user.User;
import com.intuso.housemate.core.object.user.UserListener;
import com.intuso.housemate.core.object.user.UserWrappable;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealUser
        extends BrokerRealObject<UserWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ? ,?, ?>, UserListener>
        implements User<BrokerRealCommand> {

    private final BrokerRealCommand remove;

    public BrokerRealUser(BrokerRealResources resources, String id, String name, String description) {
        super(resources, new UserWrappable(id, name, description));
        this.remove = new BrokerRealCommand(resources, REMOVE_COMMAND, REMOVE_COMMAND, "Remove the user", Lists.<BrokerRealArgument<?>>newArrayList()) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                getResources().getRoot().getUsers().remove(BrokerRealUser.this.getId());
                getResources().getGeneralResources().getStorage().removeDetails(BrokerRealUser.this.getPath());
            }
        };
    }

    @Override
    public BrokerRealCommand getRemoveCommand() {
        return remove;
    }
}
