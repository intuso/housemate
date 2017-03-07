package com.intuso.housemate.platform.android.app;

import com.intuso.housemate.client.v1_0.messaging.mqtt.MQTTReceiver;
import com.intuso.housemate.client.v1_0.messaging.mqtt.MessageConverter;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;

/**
 * Created by tomc on 07/03/17.
 */
public class MQTTReceiverFactoryImpl implements MQTTReceiver.Factory {

    private final MqttClient client;
    private final MessageConverter messageConverter = new MessageConverter.Javabin(new JavabinSerialiser());

    public MQTTReceiverFactoryImpl(MqttClient client) {
        this.client = client;
    }

    @Override
    public MQTTReceiver<?> create(Logger logger, String name, Class tClass) {
        return new MQTTReceiver<>(logger, name, tClass, client, messageConverter);
    }
}
