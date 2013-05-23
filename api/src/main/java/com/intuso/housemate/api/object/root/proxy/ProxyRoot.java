package com.intuso.housemate.api.object.root.proxy;

import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.HasDevices;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.rule.HasRules;
import com.intuso.housemate.api.object.rule.Rule;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.user.HasUsers;
import com.intuso.housemate.api.object.user.User;
import com.intuso.listeners.ListenerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public interface ProxyRoot<UL extends List<? extends User>,
            TL extends List<? extends Type>,
            DL extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            RL extends List<? extends Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            C extends Command<?, ?>, R extends ProxyRoot<UL, TL, DL, RL, C, R>>
        extends Root<R, ProxyRootListener<? super R>>, HasUsers<UL>, HasTypes<TL>, HasDevices<DL>, HasRules<RL> {

    public C getAddUserCommand();
    public C getAddDeviceCommand();
    public C getAddRuleCommand();

    public ListenerRegistration<ObjectLifecycleListener> addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener);
}
