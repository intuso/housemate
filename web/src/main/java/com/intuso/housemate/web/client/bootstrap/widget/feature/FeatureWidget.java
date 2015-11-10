package com.intuso.housemate.web.client.bootstrap.widget.feature;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.proxy.api.feature.ProxyFeatureFactory;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.object.GWTProxyFeature;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created by tomc on 08/11/15.
 */
public interface FeatureWidget extends IsWidget {

    void setTypes(GWTProxyList<TypeData<?>, GWTProxyType> types);

    Factory FACTORY = new Factory();

    final class Factory extends ProxyFeatureFactory<GWTProxyFeature, FeatureWidget> {

        private Factory() {}

        @Override
        public PowerControl getPowerControl(GWTProxyFeature feature) {
            return new PowerControl(feature);
        }

        @Override
        public StatefulPowerControl getStatefulPowerControl(GWTProxyFeature feature) {
            return new StatefulPowerControl(feature);
        }

        @Override
        public PlaybackControl getPlaybackControl(GWTProxyFeature feature) {
            return new PlaybackControl(feature);
        }

        @Override
        public StatefulPlaybackControl getStatefulPlaybackControl(GWTProxyFeature feature) {
            return new StatefulPlaybackControl(feature);
        }

        @Override
        public VolumeControl getVolumeControl(GWTProxyFeature feature) {
            return new VolumeControl(feature);
        }

        @Override
        public StatefulVolumeControl getStatefulVolumeControl(GWTProxyFeature feature) {
            return new StatefulVolumeControl(feature);
        }

        @Override
        protected UnknownFeature getUnknown(GWTProxyFeature feature) {
            return new UnknownFeature(feature);
        }
    }
}
