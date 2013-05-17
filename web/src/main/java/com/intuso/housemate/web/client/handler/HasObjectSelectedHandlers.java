package com.intuso.housemate.web.client.handler;

import com.intuso.housemate.proxy.ProxyObject;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 06/12/12
 * Time: 00:05
 * To change this template use File | Settings | File Templates.
 */
public interface HasObjectSelectedHandlers<O extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
    void addObjectSelectedHandler(ObjectSelectedHandler<O> handler);
}
