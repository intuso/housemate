package com.intuso.housemate.platform.android.app;

import android.app.Activity;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.platform.android.app.object.AndroidProxyServer;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 26/02/14
 * Time: 09:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateActivity extends Activity {

    private Logger logger;
    private ManagedCollectionFactory managedCollectionFactory;
    private TypeSerialiser.Repository typeSerialiserRepository;
    private PropertyRepository properties;
    private AppServiceClient appServiceClient;

    @Override
    protected void onStart() {
        super.onStart();
        logger = LoggerFactory.getLogger(this.getClass());
        managedCollectionFactory = new ManagedCollectionFactory() {
            @Override
            public <LISTENER> ManagedCollection<LISTENER> create() {
                return new ManagedCollection<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        typeSerialiserRepository = new TypeSerialiser.Repository() {
            // todo at least register all the "system" types eg String
            @Override
            public <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec) {
                throw new HousemateException("Unknown type requested: " + typeSpec.toString());
            }
        };
        properties = new SharedPreferencesPropertyRepository(managedCollectionFactory, getApplicationContext());
        appServiceClient = new AppServiceClient(logger, getApplicationContext());
        appServiceClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger = null;
        properties = null;
        appServiceClient.disconnect();
    }

    public Logger getLogger() {
        return logger;
    }

    public ManagedCollectionFactory getManagedCollectionFactory() {
        return managedCollectionFactory;
    }

    public TypeSerialiser.Repository getTypeSerialiserRepository() {
        return typeSerialiserRepository;
    }

    public PropertyRepository getProperties() {
        return properties;
    }

    public AndroidProxyServer createServer(Logger logger) {
        return new AndroidProxyServer(logger, managedCollectionFactory, appServiceClient, appServiceClient);
    }
}
