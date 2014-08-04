package com.intuso.housemate.extension.android.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.google.common.collect.HashBiMap;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.extension.android.widget.handler.WidgetHandler;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.simple.ProxyClientHelper;
import com.intuso.housemate.platform.android.app.HousemateService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 28/02/14
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class WidgetService extends HousemateService {

    private int requestCode= 0;

    public static enum Status {
        NOT_CONNECTED,
        NOT_ALLOWED,
        NOT_LOADED,
        LOADED
    }

    private final static String DELETE_WIDGETS_ACTION = "deleteWidgets";
    private final static String PERFORM_COMMAND_ACTION = "performCommand";
    private final static String WIDGET_ID = "widgetId";
    private final static String ACTION = "action";

    private final static String PROPERTY_PREFIX = "android.widget.";
    private final static String PROPERTY_VALUE_DELIMITER = "/";

    public final static ApplicationDetails APPLICATION_DETAILS
            = new ApplicationDetails(WidgetService.class.getName(), "Android Widgets", "Android Widgets");

    private final Binder binder = new Binder();

    private final HashBiMap<Integer, WidgetHandler<?>> widgetHandlers = HashBiMap.create();

    private ProxyClientHelper<AndroidProxyRoot> clientHelper;
    private AppWidgetManager appWidgetManager;
    private Status status = Status.NOT_CONNECTED;

    public static void deleteWidgets(Context context, int[] widgetIds) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(DELETE_WIDGETS_ACTION);
        intent.putExtra(WIDGET_ID, widgetIds);
        context.startService(intent);
    }

    public synchronized PendingIntent makePendingIntent(WidgetHandler widgetHandler, String action) {
        int widgetId = widgetHandlers.inverse().get(widgetHandler);
        Intent intent = new Intent(getApplicationContext(), WidgetService.class);
        intent.setAction(PERFORM_COMMAND_ACTION);
        intent.putExtra(WIDGET_ID, widgetId);
        intent.putExtra(ACTION, action);
        return PendingIntent.getService(getApplicationContext(), widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public AndroidProxyRoot getRoot() {
        return clientHelper.getRoot();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        clientHelper = ProxyClientHelper.newClientHelper(getLog(),
                new AndroidProxyRoot(getLog(), getListenersFactory(), getProperties(), getRouter()),
                getRouter())
                .applicationDetails(APPLICATION_DETAILS)
                .load(Root.DEVICES_ID)
                .callback(new LoadManager.Callback() {
                              @Override
                              public void failed(HousemateObject.TreeLoadInfo path) {
                                  updateStatus();
                              }

                              @Override
                              public void allLoaded() {
                                  updateStatus();
                              }
                          })
                .start();
        clientHelper.getRoot().addObjectListener(new RootListener<AndroidProxyRoot>() {
            @Override
            public void statusChanged(AndroidProxyRoot root, ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus) {
                updateStatus();
            }

            @Override
            public void newApplicationInstance(AndroidProxyRoot root, String instanceId) {
                // do nothing
            }

            @Override
            public void newServerInstance(AndroidProxyRoot root, String serverId) {
                // do nothing
            }
        });
        for(String key : getProperties().keySet())
            if(key.startsWith(PROPERTY_PREFIX))
                addWidgetHandler(Integer.parseInt(key.substring(PROPERTY_PREFIX.length())), decodePropertyValue(getProperties().get(key)));
    }

    private void updateStatus() {
        Status oldStatus = status;
        if(getRouter().getServerConnectionStatus() != ServerConnectionStatus.ConnectedToServer)
            status = Status.NOT_CONNECTED;
        else if(getRoot().getApplicationInstanceStatus() != ApplicationInstanceStatus.Allowed)
            status = Status.NOT_ALLOWED;
        else if(getRoot().getDevices() == null)
            status = Status.NOT_LOADED;
        else
            status = Status.LOADED;
        if(oldStatus != status)
            for(WidgetHandler<?> widgetHandler : widgetHandlers.values())
                widgetHandler.setServiceStatus(status);
    }

    @Override
    public void onDestroy() {
        if(clientHelper != null) {
            clientHelper.stop();
            clientHelper = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        if(DELETE_WIDGETS_ACTION.equals(intent.getAction())) {
            for(int widgetId : intent.getIntArrayExtra(WIDGET_ID)) {
                getProperties().remove(PROPERTY_PREFIX + widgetId);
                widgetHandlers.remove(widgetId);
            }
        } else if(PERFORM_COMMAND_ACTION.equals(intent.getAction())) {
            if(status == Status.LOADED) {
                int widgetId = intent.getIntExtra(WIDGET_ID, -1);
                WidgetHandler widgetHandler = widgetHandlers.get(widgetId);
                if (widgetHandler == null)
                    Toast.makeText(getApplicationContext(), "Unknown widget, please recreate it", Toast.LENGTH_SHORT).show();
                else if(widgetHandler.getStatus() != WidgetHandler.Status.READY)
                    Toast.makeText(getApplicationContext(), "Device/feature is not loaded. Please retry later", Toast.LENGTH_SHORT).show();
                else
                    widgetHandler.handleAction(intent.getStringExtra(ACTION));
            } else
                Toast.makeText(getApplicationContext(), "Not currently connected to server. Please retry later", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void addNewWidgetHandler(int widgetId, WidgetHandler<?> widgetHandler) {
        getProperties().set(PROPERTY_PREFIX + widgetId, encodePropertyValue(widgetHandler));
        addWidgetHandler(widgetId, widgetHandler);
    }

    private void addWidgetHandler(int widgetId, WidgetHandler<?> widgetHandler) {
        widgetHandlers.put(widgetId, widgetHandler);
        widgetHandler.setServiceStatus(status);
    }

    public void updateAppWidget(WidgetHandler<?> widgetHandler, RemoteViews views) {
        appWidgetManager.updateAppWidget(widgetHandlers.inverse().get(widgetHandler), views);
    }

    private String encodePropertyValue(WidgetHandler<?> widgetHandler) {
        return widgetHandler.getDeviceId() + PROPERTY_VALUE_DELIMITER + widgetHandler.getFeatureId();
    }

    private WidgetHandler<?> decodePropertyValue(String value) {
        String[] parts = value.split(PROPERTY_VALUE_DELIMITER);
        if(parts.length != 2)
            return null;
        return WidgetHandler.createFeatureWidget(this, parts[0], parts[1]);
    }

    public class Binder extends android.os.Binder {
        public void addWidget(int widgetId, String deviceId, String featureId) {
            addNewWidgetHandler(widgetId, WidgetHandler.createFeatureWidget(WidgetService.this, deviceId, featureId));
        }
    }
}