package com.intuso.housemate.platform.android.service.comms;

import com.intuso.housemate.client.v1_0.messaging.mqtt.MessageConverter;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Created by tomc on 16/03/17.
 */
public class StringMessageConverter implements MessageConverter {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    public byte[] toPayload(Serializable object) throws MqttException {
        return ((String) object).getBytes(charset);
    }

    @Override
    public <T extends Serializable> T fromPayload(byte[] payload, Class<T> tClass) {
        return (T) new String(payload, charset);
    }
}
