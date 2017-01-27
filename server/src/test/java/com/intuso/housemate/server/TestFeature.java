package com.intuso.housemate.server;

import com.intuso.housemate.client.api.internal.annotation.*;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * Created by tomc on 20/12/16.
 */
@Feature
@Id(value = "feature", name = "Feature", description = "Feature")
interface TestFeature {

    @Command
    @Id(value = "set-value", name = "Set Value", description = "Set the value")
    void setValue(double value);

    @AddListener
    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {
        @Value
        @Id(value = "value", name = "Value", description = "Value")
        void doubleValue(double value);
    }
}
