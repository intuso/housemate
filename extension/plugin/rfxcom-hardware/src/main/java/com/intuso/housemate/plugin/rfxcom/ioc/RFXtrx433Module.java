package com.intuso.housemate.plugin.rfxcom.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.plugin.rfxcom.Handler;
import com.intuso.housemate.plugin.rfxcom.Lighting1Handler;
import com.intuso.housemate.plugin.rfxcom.Lighting2Handler;
import com.intuso.housemate.plugin.rfxcom.TemperatureSensorsHandler;

/**
 * Created by tomc on 02/02/17.
 */
public class RFXtrx433Module extends AbstractModule {
    @Override
    protected void configure() {

        // bind lighting1 handlers as singletons
        bind(Lighting1Handler.ARC.class).in(Scopes.SINGLETON);
        bind(Lighting1Handler.ChaconEMW200.class).in(Scopes.SINGLETON);
        bind(Lighting1Handler.ELROAB400D.class).in(Scopes.SINGLETON);
        bind(Lighting1Handler.IMPULS.class).in(Scopes.SINGLETON);
        bind(Lighting1Handler.Waveman.class).in(Scopes.SINGLETON);
        bind(Lighting1Handler.X10.class).in(Scopes.SINGLETON);

        // bind lighting2 handlers as singletons
        bind(Lighting2Handler.AC.class).in(Scopes.SINGLETON);
        bind(Lighting2Handler.ANSLUT.class).in(Scopes.SINGLETON);
        bind(Lighting2Handler.HomeEasyEU.class).in(Scopes.SINGLETON);

        // bind temperature sensors handlers as singletons
        bind(TemperatureSensorsHandler.TEMP1.class).in(Scopes.SINGLETON);
        bind(TemperatureSensorsHandler.TEMP2.class).in(Scopes.SINGLETON);
        bind(TemperatureSensorsHandler.TEMP3.class).in(Scopes.SINGLETON);
        bind(TemperatureSensorsHandler.TEMP4.class).in(Scopes.SINGLETON);
        bind(TemperatureSensorsHandler.TEMP5.class).in(Scopes.SINGLETON);

        // bind lighting1 handlers
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting1Handler.ARC.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting1Handler.ChaconEMW200.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting1Handler.ELROAB400D.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting1Handler.IMPULS.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting1Handler.Waveman.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting1Handler.X10.class);

        // bind lighting2 handlers
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting2Handler.AC.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting2Handler.ANSLUT.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(Lighting2Handler.HomeEasyEU.class);

        // bind temperature sensors
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(TemperatureSensorsHandler.TEMP1.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(TemperatureSensorsHandler.TEMP2.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(TemperatureSensorsHandler.TEMP3.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(TemperatureSensorsHandler.TEMP4.class);
        Multibinder.newSetBinder(binder(), Handler.class).addBinding().to(TemperatureSensorsHandler.TEMP5.class);
    }
}
