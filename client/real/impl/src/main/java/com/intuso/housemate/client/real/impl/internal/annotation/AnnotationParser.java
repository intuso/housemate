package com.intuso.housemate.client.real.impl.internal.annotation;

import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;
import org.slf4j.Logger;

/**
 * Processor of annotated devices etc
 */
public interface AnnotationParser {
    Iterable<RealCommandImpl> findCommands(Logger logger, String idPrefix, Object object);
    Iterable<RealValueImpl<?>> findValues(Logger logger, String idPrefix, Object object);
    Iterable<RealPropertyImpl<?>> findProperties(Logger logger, String idPrefix, Object object);
}
