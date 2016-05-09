package com.intuso.housemate.extension.android.widget.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import com.intuso.housemate.extension.android.widget.R;
import com.intuso.housemate.extension.android.widget.handler.WidgetHandler;
import com.intuso.housemate.platform.android.app.HousemateService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;
import com.intuso.housemate.platform.android.app.object.feature.AndroidProxyFeatureFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 28/02/14
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class WidgetService extends HousemateService {

    public enum Status {
        SERVICE_STOPPED,
        NO_NETWORK,
        NOT_CONNECTED,
        NOT_ALLOWED,
        NOT_LOADED,
        LOADED
    }

    public final static String NETWORK_AVAILABLE_ACTION = "networkAvailable";
    public final static String NETWORK_AVAILABLE = "networkAvailable";

    private final static int NOTIFICATION_ID = 54;

    private final static String ADD_WIDGET_ACTION = "addWidget";
    private final static String DELETE_WIDGETS_ACTION = "deleteWidgets";
    private final static String PERFORM_COMMAND_ACTION = "performCommand";

    private final static String WIDGET_ID = "widgetId";
    private final static String CLIENT_ID = "clientId";
    private final static String DEVICE_ID = "deviceId";
    private final static String FEATURE_ID = "featureId";

    private final static String ACTION = "action";

    private final static String PROPERTY_PREFIX = "android.widget.";
    private final static String PROPERTY_VALUE_DELIMITER = "___";

    private final Binder binder = new Binder();

    private final HashBiMap<Integer, WidgetHandler<?>> widgetHandlers = HashBiMap.create();
    private final AndroidProxyFeatureFactory proxyFeatureFactory;

    private AndroidProxyRoot root;
    private AppWidgetManager appWidgetManager;
    private Status status = Status.NOT_CONNECTED;

    private boolean networkAvailable = true;

    public static void addWidget(Context context, int widgetId, String clientId, String deviceId, String featureId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ADD_WIDGET_ACTION);
        intent.putExtra(WIDGET_ID, widgetId);
        intent.putExtra(CLIENT_ID, clientId);
        intent.putExtra(DEVICE_ID, deviceId);
        intent.putExtra(FEATURE_ID, featureId);
        context.startService(intent);
    }

    public static void deleteWidgets(Context context, int[] widgetIds) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(DELETE_WIDGETS_ACTION);
        intent.putExtra(WIDGET_ID, widgetIds);
        context.startService(intent);
    }

    public WidgetService() {
        this.proxyFeatureFactory = new AndroidProxyFeatureFactory();
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
        return root;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Housemate Widgets")
                .setContentText("Service is running")
                .setPriority(Notification.PRIORITY_MIN)
                .build());
        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        root = new AndroidProxyRoot(getLogger(), getListenersFactory(), getConnection(), null /* todo */);
        updateStatus();
        for (String key : Sets.newHashSet(getProperties().keySet())) {
            if (key.startsWith(PROPERTY_PREFIX)) {
                String encodedWidget = getProperties().get(key);
                getLogger().debug("Loading widget from " + encodedWidget);
                WidgetHandler<?> widgetHandler = decodePropertyValue(encodedWidget);
                if (widgetHandler != null) {
                    getLogger().debug("Decoded widget to a " + widgetHandler.getClass().getName());
                    addWidgetHandler(Integer.parseInt(key.substring(PROPERTY_PREFIX.length())), widgetHandler, false);
                } else {
                    getLogger().debug("Decoding widget config failed, removing property");
                    getProperties().remove(key);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        root = null;
        status = Status.SERVICE_STOPPED;
        notifyNewStatus();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Housemate Widgets")
                .setContentText("Service stopped. No widgets wil update. Tap here to restart the service")
                .setContentIntent(PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), WidgetService.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .setPriority(Notification.PRIORITY_HIGH)
                .build());
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent != null) {
            if (PERFORM_COMMAND_ACTION.equals(intent.getAction())) {
                Toast.makeText(getApplicationContext(), "Widget action received", Toast.LENGTH_SHORT);
                if (status == Status.LOADED) {
                    int widgetId = intent.getIntExtra(WIDGET_ID, -1);
                    WidgetHandler widgetHandler = widgetHandlers.get(widgetId);
                    if (widgetHandler == null)
                        Toast.makeText(getApplicationContext(), "Unknown widget, please recreate it", Toast.LENGTH_SHORT).show();
                    else if (widgetHandler.getStatus() != WidgetHandler.Status.READY)
                        Toast.makeText(getApplicationContext(), "Device/feature is not loaded. Please retry later", Toast.LENGTH_SHORT).show();
                    else
                        widgetHandler.handleAction(intent.getStringExtra(ACTION));
                } else
                    Toast.makeText(getApplicationContext(), "Not currently connected to server. Please retry later", Toast.LENGTH_SHORT).show();
            } else if (NETWORK_AVAILABLE_ACTION.equals(intent.getAction())) {
                if (intent.getExtras().containsKey(NETWORK_AVAILABLE)) {
                    getLogger().debug("Received network available update: " + intent.getBooleanExtra(NETWORK_AVAILABLE, true));
                    networkAvailable = intent.getBooleanExtra(NETWORK_AVAILABLE, true);
                    updateStatus();
                }
            } else if (DELETE_WIDGETS_ACTION.equals(intent.getAction())) {
                for (int widgetId : intent.getIntArrayExtra(WIDGET_ID)) {
                    getProperties().remove(PROPERTY_PREFIX + widgetId);
                    widgetHandlers.remove(widgetId);
                }
            } else if(ADD_WIDGET_ACTION.equals(intent.getAction())) {
                int widgetId = intent.getIntExtra(WIDGET_ID, 0);
                String clientId = intent.getStringExtra(CLIENT_ID);
                String deviceId = intent.getStringExtra(DEVICE_ID);
                String featureId = intent.getStringExtra(FEATURE_ID);
                addWidgetHandler(widgetId, WidgetHandler.createFeatureWidget(WidgetService.this, proxyFeatureFactory, clientId, deviceId, featureId), true);
            }
        }
        return START_STICKY;
    }

    private void updateStatus() {
        Status oldStatus = status;
        if (!networkAvailable)
            status = Status.NO_NETWORK;
        else if(root == null)
            status = Status.NOT_CONNECTED;
        else if (getRoot().getServers() == null)
            status = Status.NOT_LOADED;
        else
            status = Status.LOADED;
        if (oldStatus != status)
            notifyNewStatus();
    }

    private void notifyNewStatus() {
        for(WidgetHandler<?> widgetHandler : widgetHandlers.values())
            widgetHandler.setServiceStatus(status);
    }

    private void addWidgetHandler(int widgetId, WidgetHandler<?> widgetHandler, boolean isNew) {
        if(isNew)
            getProperties().set(PROPERTY_PREFIX + widgetId, encodePropertyValue(widgetHandler));
        widgetHandlers.put(widgetId, widgetHandler);
        widgetHandler.setServiceStatus(status);
    }

    public void updateAppWidget(WidgetHandler<?> widgetHandler, RemoteViews views) {
        appWidgetManager.updateAppWidget(widgetHandlers.inverse().get(widgetHandler), views);
    }

    private String encodePropertyValue(WidgetHandler<?> widgetHandler) {
        return widgetHandler.getClientId() + PROPERTY_VALUE_DELIMITER + widgetHandler.getDeviceId() + PROPERTY_VALUE_DELIMITER + widgetHandler.getFeatureId();
    }

    private WidgetHandler<?> decodePropertyValue(String value) {
        String[] parts = value.split(PROPERTY_VALUE_DELIMITER);
        if(parts.length != 3)
            return null;
        return WidgetHandler.createFeatureWidget(this, proxyFeatureFactory, parts[0], parts[1], parts[2]);
    }
}
