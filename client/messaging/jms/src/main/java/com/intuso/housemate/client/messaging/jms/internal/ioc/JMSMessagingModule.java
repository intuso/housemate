package com.intuso.housemate.client.messaging.jms.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.messaging.api.internal.ioc.Messaging;
import com.intuso.housemate.client.messaging.jms.internal.JMS;
import com.intuso.housemate.client.messaging.jms.internal.MessageConverter;
import com.intuso.housemate.client.serialisation.javabin.internal.JavabinSerialiser;
import com.intuso.housemate.client.serialisation.javabin.internal.ioc.JavabinSerialiserModule;

import javax.jms.Connection;

/**
 * Created by tomc on 02/03/17.
 */
public class JMSMessagingModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JavabinSerialiserModule());
        bind(MessageConverter.Javabin.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public Receiver.Factory getDefaultReceiver(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Receiver.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    public Sender.Factory getDefaultSender(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Sender.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.CONTENT_TYPE)
    public Receiver.Factory getJavabinReceiver(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Receiver.FactoryImpl(messageConverter, connection);
    }

    @Provides
    @Singleton
    @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.CONTENT_TYPE)
    public Sender.Factory getJavabinSender(MessageConverter.Javabin messageConverter, Connection connection) {
        return new JMS.Sender.FactoryImpl(messageConverter, connection);
    }
}
