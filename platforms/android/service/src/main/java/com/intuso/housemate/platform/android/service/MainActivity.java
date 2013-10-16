package com.intuso.housemate.platform.android.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.intuso.home.platform.android.common.HousemateService;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.utilities.listener.ListenerRegistration;

public class MainActivity extends Activity implements RootListener<RouterRootObject>,ServiceConnection {

    private ListenerRegistration routerStatusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init the ui
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.main_view);

        // bind to the service
        bindService(new Intent(this, HousemateServiceImpl.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbind the service
        unbindService(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void connectionStatusChanged(RouterRootObject root, final ConnectionStatus status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.router_status)).setText(status.name());
            }
        });
    }

    @Override
    public void brokerInstanceChanged(RouterRootObject root) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        // register the app listeners
        HousemateServiceImpl housemateService = (HousemateServiceImpl)((HousemateService.Binder)binder).getService();
        routerStatusListener = housemateService.getEnvironment().getResources().getRouter().addObjectListener(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if(routerStatusListener != null)
            routerStatusListener.removeListener();
    }
}

