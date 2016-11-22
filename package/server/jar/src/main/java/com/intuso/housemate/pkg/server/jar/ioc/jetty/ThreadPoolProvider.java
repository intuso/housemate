package com.intuso.housemate.pkg.server.jar.ioc.jetty;

import com.google.inject.Provider;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

/**
 * Created by Ravn Systems
 * User: wills
 * Date: 9/18/13
 * Time: 5:03 PM
 */
public class ThreadPoolProvider implements Provider<ThreadPool> {

    @Override
    public ThreadPool get() {
        return new ExecutorThreadPool();
    }
}
