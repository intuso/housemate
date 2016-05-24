package com.intuso.housemate.client.real.impl.internal.type.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.util.Types;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;

import java.lang.reflect.Type;

/**
 * Created by tomc on 13/05/16.
 */
public class TypeModule extends AbstractModule {

    private final Class<? extends RealTypeImpl<?>> typeImplClass;
    private final RealTypeImpl<?> typeImpl;
    private final Type type;

    public TypeModule(Class<? extends RealTypeImpl<?>> typeImplClass, Type type) {
        this(typeImplClass, null, type);
    }

    public TypeModule(RealTypeImpl<?> typeImpl, Type type) {
        this(null, typeImpl, type);
    }

    private TypeModule(Class<? extends RealTypeImpl<?>> typeImplClass, RealTypeImpl<?> typeImpl, Type type) {
        this.typeImplClass = typeImplClass;
        this.typeImpl = typeImpl;
        this.type = type;
    }

    @Override
    protected void configure() {
        if(typeImpl != null)
            bind((TypeLiteral<RealTypeImpl<?>>) TypeLiteral.get(Types.newParameterizedType(RealTypeImpl.class, type))).toInstance(typeImpl);
        else {
            bind(typeImplClass).in(Scopes.SINGLETON);
            bind((TypeLiteral<RealTypeImpl<?>>) TypeLiteral.get(Types.newParameterizedType(RealTypeImpl.class, type))).to(typeImplClass);
        }
        install(new FactoryModuleBuilder().build(TypeLiteral.get(Types.newParameterizedTypeWithOwner(RealParameterImpl.class, RealParameterImpl.Factory.class, type))));
        install(new FactoryModuleBuilder().build(TypeLiteral.get(Types.newParameterizedTypeWithOwner(RealPropertyImpl.class, RealPropertyImpl.Factory.class, type))));
        install(new FactoryModuleBuilder().build(TypeLiteral.get(Types.newParameterizedTypeWithOwner(RealValueImpl.class, RealValueImpl.Factory.class, type))));
    }
}
