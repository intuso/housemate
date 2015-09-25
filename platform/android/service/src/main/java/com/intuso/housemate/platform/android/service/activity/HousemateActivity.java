package com.intuso.housemate.platform.android.service.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.intuso.housemate.comms.v1_0.api.ClientRoot;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.RouterRoot;
import com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.housemate.platform.android.service.R;
import com.intuso.housemate.platform.android.service.service.ConnectionService;
import com.intuso.utilities.listener.ListenerRegistration;

public class HousemateActivity extends Activity implements ServiceConnection, ClientRoot.Listener<RouterRoot> {

    private ListenerRegistration routerRegistration;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // init the ui
        setContentView(R.layout.main_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectionService.class);
        startService(intent);
        getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bound)
            try {
                unbindService(this);
            } catch (Throwable t) {
                Log.d("HM Activity", "Failed to unbind from service", t);
            }
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
    public void onServiceConnected(ComponentName name, IBinder binder) {
        bound = true;
        Router router = ((ConnectionService.Binder)binder).getRouter();
        routerRegistration = router.addObjectListener(this);
        serverConnectionStatusChanged(null, router.getServerConnectionStatus());
        applicationStatusChanged(null, router.getApplicationStatus());
        applicationInstanceStatusChanged(null, router.getApplicationInstanceStatus());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
        routerRegistration.removeListener();
        routerRegistration = null;
    }

    @Override
    public void serverConnectionStatusChanged(RouterRoot root, final ServerConnectionStatus serverConnectionStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.server_connection_status)).setText("Server Connection Status: " + serverConnectionStatus);
            }
        });
    }

    @Override
    public void applicationStatusChanged(RouterRoot root, final Application.Status applicationStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.application_status)).setText("Application Status: " + applicationStatus);
            }
        });
    }

    @Override
    public void applicationInstanceStatusChanged(RouterRoot root, final ApplicationInstance.Status applicationInstanceStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.application_instance_status)).setText("Application Instance Status: " + applicationInstanceStatus);
            }
        });
    }

    @Override
    public void newApplicationInstance(RouterRoot root, String instanceId) {
        // do nothing
    }

    @Override
    public void newServerInstance(RouterRoot root, String serverId) {
        // do nothing
    }
}

