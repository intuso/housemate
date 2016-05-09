package com.intuso.housemate.extension.android.widget.configure;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.proxy.api.object.feature.StatefulPowerControl;
import com.intuso.housemate.extension.android.widget.R;
import com.intuso.housemate.extension.android.widget.service.WidgetService;
import com.intuso.housemate.platform.android.app.HousemateActivity;
import com.intuso.housemate.platform.android.app.object.AndroidProxyDevice;
import com.intuso.housemate.platform.android.app.object.AndroidProxyList;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;
import com.intuso.housemate.platform.android.app.object.AndroidProxyServer;
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
        implements AdapterView.OnItemClickListener {

    private final String featureId = StatefulPowerControl.ID;

    private AndroidProxyRoot root;
    private List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();
    private DeviceListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configure);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setResult(RESULT_CANCELED); // set this now in case the user backs out of the configuration
        setStatus("Connecting");
        listAdapter = new DeviceListAdapter();
        ((ListView)findViewById(R.id.device_list)).setAdapter(listAdapter);
        ((ListView)findViewById(R.id.device_list)).setOnItemClickListener(this);
        root = new AndroidProxyRoot(getLogger(), getListenersFactory(), getConnection(), null /* todo */);
        setStatus("Pick device to control");
        listenerRegistrations.add(root.getServers().addObjectListener(new ClientListListener(), true));
        listAdapter.notifyDataSetChanged();
        listAdapter.setNotifyOnChange(true);
    }

    @Override
    protected void onStop() {
        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DeviceInfo deviceInfo = listAdapter.getItem(position);
        String clientId = deviceInfo.getClient().getId();
        String deviceId = deviceInfo.getDevice().getId();
        int widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        WidgetService.addWidget(getApplicationContext(), widgetId, clientId, deviceId, featureId);
        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, result);
        finish();
    }

    private void setStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.status_label)).setText(message);
            }
        });
    }

    private class ClientListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<AndroidProxyServer, AndroidProxyList<AndroidProxyServer>> {

        @Override
        public void elementAdded(AndroidProxyList<AndroidProxyServer> list, AndroidProxyServer server) {
            listenerRegistrations.add(server.getDevices().addObjectListener(new DeviceListListener(server), true));
        }

        @Override
        public void elementRemoved(AndroidProxyList<AndroidProxyServer> list, AndroidProxyServer server) {
            // should probably clean up the list! todo
        }
    }

    private class DeviceListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<AndroidProxyDevice, AndroidProxyList<AndroidProxyDevice>> {

        private final AndroidProxyServer client;

        private DeviceListListener(AndroidProxyServer client) {
            this.client = client;
        }

        @Override
        public void elementAdded(AndroidProxyList<AndroidProxyDevice> list, AndroidProxyDevice device) {
            if(device.getFeatures().get(featureId) != null)
                listAdapter.add(new DeviceInfo(client, device));
        }

        @Override
        public void elementRemoved(AndroidProxyList<AndroidProxyDevice> list, AndroidProxyDevice device) {
            listAdapter.remove(new DeviceInfo(client, device));
        }
    }

    private class DeviceInfo {

        private final AndroidProxyServer client;
        private final AndroidProxyDevice device;

        private DeviceInfo(AndroidProxyServer client, AndroidProxyDevice device) {
            this.client = client;
            this.device = device;
        }

        public AndroidProxyServer getClient() {
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
