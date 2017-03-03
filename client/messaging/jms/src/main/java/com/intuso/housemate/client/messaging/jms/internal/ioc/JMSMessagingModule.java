package com.intuso.housemate.client.messaging.jms.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.messaging.jms.internal.JMSReceiver;
import com.intuso.housemate.client.messaging.jms.internal.JMSSender;
import com.intuso.housemate.client.messaging.jms.internal.MessageConverter;

/**
 * Created by tomc on 02/03/17.
 */
public abstract class JMSMessagingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Sender.Factory.class).to(JMSSender.FactoryImpl.class);
        bind(Receiver.Factory.class).to(JMSReceiver.FactoryImpl.class);
        install(new FactoryModuleBuilder().build(JMSSender.Factory.class));
        install(new FactoryModuleBuilder().build(JMSReceiver.Factory.class));
    }

    public static class Javabin extends JMSMessagingModule {
        @Override
        protected void configure() {
            super.configure();
            bind(MessageConverter.class).to(MessageConverter.Javabin.class);
        }
    }
}
