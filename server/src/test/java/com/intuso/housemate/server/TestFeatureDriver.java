package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.Id;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;
import com.intuso.housemate.client.real.api.internal.driver.FeatureDriver;
import org.slf4j.Logger;

@Id(value = "test-feature", name = "Test Feature", description = "Test Feature")
public class TestFeatureDriver implements FeatureDriver {

    public TestFeature.MyValues values;

    @Inject
    public TestFeatureDriver(@Assisted Logger logger, @Assisted FeatureDriver.Callback callback) {}

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Feature
    @Id(value = "feature", name = "Feature", description = "Feature")
    interface TestFeature {

        @Values
        interface MyValues {
            @Value("double")
            @Id(value = "value", name = "Value", description = "Value")
            void doubleValue(double value);
        }
    }
}
