package com.intuso.housemate.extension.android.widget.configure;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.device.feature.StatefulPowerControl;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.extension.android.widget.R;
import com.intuso.housemate.extension.android.widget.WidgetService;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.ProxyRealClient;
import com.intuso.housemate.object.proxy.ProxyRoot;
import com.intuso.housemate.object.proxy.simple.ProxyClientHelper;
import com.intuso.housemate.platform.android.app.HousemateActivity;
import com.intuso.housemate.platform.android.app.object.AndroidProxyDevice;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRealClient;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 28/02/14
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
public class WidgetConfigureActivity
        extends HousemateActivity
        implements ServiceConnection, LoadManager.Callback, AdapterView.OnItemClickListener {

    private final String featureId = StatefulPowerControl.ID;

    private boolean bound = false;
    private ProxyClientHelper<AndroidProxyRoot> clientHelper;
    private List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();
    private DeviceListAdapter listAdapter;
    private String chosenClientId;
    private String chosenDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configure);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setResult(RESULT_CANCELED); // set this now in case the user backs out of the configuration
        setStatus("Loading devices");
        listAdapter = new DeviceListAdapter();
        ((ListView)findViewById(R.id.device_list)).setAdapter(listAdapter);
        ((ListView)findViewById(R.id.device_list)).setOnItemClickListener(this);
        clientHelper = ProxyClientHelper.newClientHelper(getLog(),
                new AndroidProxyRoot(getLog(), getListenersFactory(), getProperties(), getRouter()), getRouter());
        clientHelper.applicationDetails(WidgetService.APPLICATION_DETAILS)
                .component(WidgetConfigureActivity.class.getName())
                .load(ProxyRoot.REAL_CLIENTS_ID, HousemateObject.EVERYTHING, ProxyRealClient.DEVICES_ID, HousemateObject.EVERYTHING)
                .callback(this)
                .start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bound) {
            unbindService(this);
            bound = false;
        }
        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        if(clientHelper != null) {
            clientHelper.stop();
            clientHelper = null;
        }
    }

    @Override
    public void failed(HousemateObject.TreeLoadInfo path) {
        setStatus("Failed to load device names");
    }

    @Override
    public void allLoaded() {
        setStatus("Pick device to control");
        listenerRegistrations.add(clientHelper.getRoot().getRealClients().addObjectListener(new ClientListListener(), true));
        listAdapter.notifyDataSetChanged();
        listAdapter.setNotifyOnChange(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        chosenClientId = listAdapter.getItem(position).getClient().getId();
        chosenDeviceId = listAdapter.getItem(position).getDevice().getId();
        Intent intent = new Intent(this, WidgetService.class);
        startService(intent);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        bound = true;
        int widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        ((WidgetService.Binder)service).addWidget(widgetId, chosenClientId, chosenDeviceId, featureId);
        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }

    private void setStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.status_label)).setText(message);
            }
        });
    }

    private class ClientListListener implements ListListener<AndroidProxyRealClient> {

        @Override
        public void elementAdded(AndroidProxyRealClient client) {
            listenerRegistrations.add(client.getDevices().addObjectListener(new DeviceListListener(client), true));
        }

        @Override
        public void elementRemoved(AndroidProxyRealClient client) {
            // should probably clean up the list! todo
        }
    }

    private class DeviceListListener implements ListListener<AndroidProxyDevice> {

        private final AndroidProxyRealClient client;

        private DeviceListListener(AndroidProxyRealClient client) {
            this.client = client;
        }

        @Override
        public void elementAdded(AndroidProxyDevice device) {
            if(device.getFeatureIds().contains(featureId))
                listAdapter.add(new DeviceInfo(client, device));
        }

        @Override
        public void elementRemoved(AndroidProxyDevice device) {
            listAdapter.remove(new DeviceInfo(client, device));
        }
    }

    private class DeviceInfo {

        private final AndroidProxyRealClient client;
        private final AndroidProxyDevice device;

        private DeviceInfo(AndroidProxyRealClient client, AndroidProxyDevice device) {
            this.client = client;
            this.device = device;
        }

        public AndroidProxyRealClient getClient() {
            return client;
        }

        public AndroidProxyDevice getDevice() {
            return device;
        }
    }

    private class DeviceListAdapter extends ArrayAdapter<DeviceInfo> {

        public DeviceListAdapter() {
            super(WidgetConfigureActivity.this.getApplicationContext(), R.layout.device_list_element, Lists.<DeviceInfo>newArrayList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // get the view to use
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_element, parent, false);

            // set the text as the device name
            ((TextView) convertView.findViewById(R.id.device_label)).setText(getItem(position).getClient().getName() + ": " + getItem(position).getDevice().getName());

            return convertView;
        }
    }
}
