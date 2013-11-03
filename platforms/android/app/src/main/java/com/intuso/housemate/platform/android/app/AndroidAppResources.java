package com.intuso.housemate.platform.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.platform.android.common.AndroidLogWriter;
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
class AndroidAppResources implements ClientResources {

    private final Log log;
    private final AndroidAppRouter router;
    private final Map<String, String> properties = Maps.newHashMap();

    public AndroidAppResources(Context context) {
        log = new Log(context.getPackageName(), new AndroidLogWriter(LogLevel.DEBUG, context.getPackageName()));
        router = new AndroidAppRouter(this, context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        for(Map.Entry<String, ?> entry : preferences.getAll().entrySet())
            properties.put(entry.getKey(), entry.getValue().toString());
    }

    public void destroy() {
        router.disconnect();
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }
}
