package com.intuso.home.platform.android.app;

import android.content.Context;
import com.google.common.collect.Maps;
import com.intuso.home.platform.android.common.AndroidLogWriter;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 08:54
 * To change this template use File | Settings | File Templates.
 */
public class AndroidAppResources implements ClientResources {

    private final Map<String, String> properties;
    private final Log log;
    private final Router router;

    public AndroidAppResources(Context context) {
        properties = Maps.newHashMap();
        log = new Log(context.getPackageName(), new AndroidLogWriter(LogLevel.DEBUG, context.getPackageName()));
        router = new AndroidAppRouter(this, context);
    }

    @Override
    public Router getRouter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Log getLog() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getProperties() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
