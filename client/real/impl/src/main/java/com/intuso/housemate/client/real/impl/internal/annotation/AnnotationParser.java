package com.intuso.housemate.client.real.impl.internal.annotation;

import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealDeviceComponentImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;
import org.slf4j.Logger;

/**
 * Processor of annotated devices etc
 */
public interface AnnotationParser {
    Iterable<RealDeviceComponentImpl> findDeviceComponents(Logger logger, Object object);
    Iterable<RealCommandImpl> findCommands(Logger logger, Object object);
    Iterable<RealValueImpl<?>> findValues(Logger logger, Object object);
    Iterable<RealPropertyImpl<?>> findProperties(Logger logger, Object object);
}
