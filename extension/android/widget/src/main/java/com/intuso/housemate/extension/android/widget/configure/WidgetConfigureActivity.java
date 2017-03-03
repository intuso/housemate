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
import com.intuso.housemate.client.v1_0.api.ability.Power;
import com.intuso.housemate.extension.android.widget.R;
import com.intuso.housemate.extension.android.widget.service.WidgetService;
import com.intuso.housemate.platform.android.app.HousemateActivity;
import com.intuso.housemate.platform.android.app.object.*;
import com.intuso.utilities.collection.ManagedCollection;

import java.util.ArrayList;
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

    private final String featureId = Power.ID;

    private AndroidProxyServer server;
    private List<ManagedCollection.Registration> listenerRegistrations = new ArrayList<>();
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
        server = getObjectFactories().server().create(getLogger());
        server.start();
        setStatus("Pick device to control");
        listenerRegistrations.add(server.getNodes().addObjectListener(new NodeListListener(), true));
        listAdapter.notifyDataSetChanged();
        listAdapter.setNotifyOnChange(true);
    }

    @Override
    protected void onStop() {
        if(server != null)
            server.stop();
        for(ManagedCollection.Registration listenerRegistration : listenerRegistrations)
            listenerRegistration.remove();
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DeviceInfo deviceInfo = listAdapter.getItem(position);
        String deviceId = deviceInfo.getDevice().getId();
        int widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        WidgetService.addWidget(getApplicationContext(), widgetId, deviceId, featureId);
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

    private class NodeListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<AndroidProxyNode, AndroidProxyList<AndroidProxyNode>> {

        @Override
        public void elementAdded(AndroidProxyList<AndroidProxyNode> list, AndroidProxyNode node) {
            node.getHardwares().addObjectListener(new HardwareListListener());
        }

        @Override
        public void elementRemoved(AndroidProxyList<AndroidProxyNode> list, AndroidProxyNode node) {
            // todo remove the devices for all hardwares for the node. Or ignore because it's not going to be displayed for long
        }
    }

    private class HardwareListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<AndroidProxyHardware, AndroidProxyList<AndroidProxyHardware>> {

        @Override
        public void elementAdded(AndroidProxyList<AndroidProxyHardware> list, AndroidProxyHardware hardware) {
            hardware.getDevices().addObjectListener(new DeviceListListener());
        }

        @Override
        public void elementRemoved(AndroidProxyList<AndroidProxyHardware> list, AndroidProxyHardware hardware) {
            // todo remove the devices for the hardware. Or ignore because it's not going to be displayed for long
        }
    }

    private class DeviceListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<AndroidProxyDevice, AndroidProxyList<AndroidProxyDevice>> {

        @Override
        public void elementAdded(AndroidProxyList<AndroidProxyDevice> list, AndroidProxyDevice device) {
            listAdapter.add(new DeviceInfo(device));
        }

        @Override
        public void elementRemoved(AndroidProxyList<AndroidProxyDevice> list, AndroidProxyDevice device) {
            listAdapter.remove(new DeviceInfo(device));
        }
    }

    private class DeviceInfo {

        private final AndroidProxyDevice device;

        private DeviceInfo(AndroidProxyDevice device) {
            this.device = device;
        }

        public AndroidProxyDevice getDevice() {
            return device;
        }
    }

    private class DeviceListAdapter extends ArrayAdapter<DeviceInfo> {

        public DeviceListAdapter() {
            super(WidgetConfigureActivity.this.getApplicationContext(), R.layout.device_list_element, new ArrayList<DeviceInfo>());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // get the view to use
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_element, parent, false);

            // set the text as the device name
            ((TextView) convertView.findViewById(R.id.device_label)).setText(getItem(position).getDevice().getName());

            return convertView;
        }
    }
}
