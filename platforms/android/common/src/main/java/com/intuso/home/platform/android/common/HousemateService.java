package com.intuso.home.platform.android.common;

import android.app.Service;
import com.intuso.housemate.api.comms.Sender;
import com.intuso.housemate.api.object.root.Root;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 21:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateService extends Service implements Sender {

    public abstract void login(Root<?> router);

    public static class Binder extends android.os.Binder {

        private final HousemateService service;

        public Binder(HousemateService service) {
            this.service = service;
        }

        public HousemateService getService() {
            return service;
        }
    }
}
