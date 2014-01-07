package com.intuso.housemate.platform.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.intuso.housemate.platform.android.common.AndroidLogWriter;
import com.intuso.housemate.platform.android.common.SharedPreferencesReader;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.api.PropertyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 08:54
 * To change this template use File | Settings | File Templates.
 */
public class AndroidAppResources implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final Log log;
    private final AndroidAppRouter router;
    private final PropertyContainer properties = new PropertyContainer();

    public AndroidAppResources(Context context) {
        log = new Log(new AndroidLogWriter(LogLevel.DEBUG, context.getPackageName()));
        router = new AndroidAppRouter(log, context);
        properties.read(new SharedPreferencesReader("androidProperties", 1, context));
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
    }

    public void destroy() {
        router.disconnect();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        properties.set(s, new PropertyValue("androidProperties", 1, sharedPreferences.getAll().get(s).toString()));
    }
}
