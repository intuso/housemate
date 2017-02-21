package com.intuso.housemate.client.proxy.internal.annotation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.api.HousemateException;

/**
 * Created by tomc on 02/02/17.
 */
public interface ClassCreator {

    <T> T create(Class<? extends T> clazz);

    class FromInjector implements ClassCreator {

        private final Injector injector;

        @Inject
        public FromInjector(Injector injector) {
            this.injector = injector;
        }

        @Override
        public <T> T create(Class<? extends T> clazz) {
            return injector.getInstance(clazz);
        }
    }

    class NewInstance implements ClassCreator {

        @Override
        public <T> T create(Class<? extends T> clazz) {
            try {
                return clazz.newInstance();
            } catch (IllegalAccessException|InstantiationException e) {
                throw new HousemateException("Could not create instance of " + clazz.getName(), e);
            }
        }
    }
}
