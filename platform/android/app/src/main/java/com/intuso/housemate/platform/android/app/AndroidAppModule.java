package com.intuso.housemate.platform.android.app;

import android.content.Context;
import com.google.inject.AbstractModule;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.platform.android.app.comms.AndroidAppRouter;
import com.intuso.housemate.platform.android.common.AndroidLogWriter;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 08:54
 * To change this template use File | Settings | File Templates.
 */
public class AndroidAppModule extends AbstractModule {

    private final Context context;

    public AndroidAppModule(Context context) {
        this.context = context;
    }

    @Override
    public void configure() {
        bind(Context.class).toInstance(context);
        bind(Log.class).toInstance(new Log(new AndroidLogWriter(LogLevel.DEBUG, context.getPackageName())));
        bind(PropertyRepository.class).to(SharedPreferencesPropertyRepository.class);
        bind(Router.class).to(AndroidAppRouter.class);
}
}
