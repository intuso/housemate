package com.intuso.housemate.web.client.bootstrap.ioc;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.intuso.housemate.web.client.bootstrap.view.*;
import com.intuso.housemate.web.client.bootstrap.widget.login.LoginModal;
import com.intuso.housemate.web.client.ui.view.LoginView;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/09/13
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public class BootstrapUiModule extends AbstractGinModule {

    @Override
    protected void configure() {
    
        // ui
        bind(LoginView.class).to(LoginModal.class).in(Singleton.class);
        bind(com.intuso.housemate.web.client.ui.view.Page.class).to(Page.class).in(Singleton.class);
        bind(com.intuso.housemate.web.client.ui.view.ApplicationsView.class).to(ApplicationsView.class).in(Singleton.class);
        bind(com.intuso.housemate.web.client.ui.view.AutomationsView.class).to(AutomationsView.class).in(Singleton.class);
        bind(com.intuso.housemate.web.client.ui.view.DevicesView.class).to(DevicesView.class).in(Singleton.class);
        bind(com.intuso.housemate.web.client.ui.view.HardwaresView.class).to(HardwaresView.class).in(Singleton.class);
        bind(com.intuso.housemate.web.client.ui.view.UsersView.class).to(UsersView.class).in(Singleton.class);
    }
}
