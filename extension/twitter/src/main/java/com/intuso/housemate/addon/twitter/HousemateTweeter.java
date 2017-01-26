package com.intuso.housemate.addon.twitter;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.proxy.simple.*;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main class for "tweeting" Housemate events
 */
public class HousemateTweeter {

    private final static String INSTANCE_ID = "application.instance.id";

	/**
	 * Key in props file for the user's access key
	 */
	private final static String ACCESS_TOKEN_KEY = "access_token";
	
	/**
	 * Key in props file for the user's access secret
	 */
	private final static String ACCESS_SECRET_KEY = "access_secret";
	
	/**
	 * Twitter connection
	 */
	private final Twitter twitter;
	
	/**
	 * Formatter for the date
	 */
	private final SimpleDateFormat dateFormat;

    private final Logger logger;

    private final Map<SimpleProxyDevice, java.util.List<ManagedCollection.Registration>> listeners;
    private ServerListListener serverListListener = new ServerListListener();
    private DeviceListListener deviceListListener = new DeviceListListener();
    private DeviceListener deviceListener = new DeviceListener();

	/**
	 * Default constructor
	 */
	@Inject
	public HousemateTweeter(final Logger logger, final PropertyRepository properties, Injector injector) {

        this.logger = logger;

        listeners = new HashMap<>();

		dateFormat = new SimpleDateFormat("h:mm a");
        DateFormatSymbols dateFormatSymbols = dateFormat.getDateFormatSymbols();
        dateFormatSymbols.setAmPmStrings(new String[]{"am", "pm"});
        dateFormat.setDateFormatSymbols(dateFormatSymbols);

		// set up the twitter stuff
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("dPL31ZtQCTBknLOvEY7Lg", "slOU1tdctmdGOdVAhgMv2E2Vxe4jZo52TbQ19elALE");
		getTokenCredentials();
		logger.debug("Loaded token and secret. If these are wrong or no longer valid please delete the file \"" + getTokenCredentialsPropsFile().getAbsolutePath() + "\" to force the addon to get a new token and secret");

		// setup the housemate stuff
        // todo get all servers
        final SimpleProxyServer server = injector.getInstance(SimpleProxyServer.class);
        server.getDevices().addObjectListener(deviceListListener, true);
	}

	/**
	 * Get the file where oauth token is stored
	 * @return the file where oauth token is stored
	 */
	private File getTokenCredentialsPropsFile() {
		return new File(System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "twitter_auth");
	}

	/**
	 * Get the token key/secret and set them
	 */
	private void getTokenCredentials() {

		// try and read access key/secret
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(getTokenCredentialsPropsFile()));
			if(props.get(ACCESS_TOKEN_KEY) != null && ((String)props.get(ACCESS_TOKEN_KEY)).length() > 0 &&
					props.get(ACCESS_SECRET_KEY) != null && ((String)props.get(ACCESS_SECRET_KEY)).length() > 0) {
				twitter.setOAuthAccessToken(new AccessToken((String) props.get(ACCESS_TOKEN_KEY), (String) props.get(ACCESS_SECRET_KEY)));
			}
		} catch(FileNotFoundException e) {
			requestAccessToken();
		} catch(IOException e) {
			throw new HousemateException("Failed to read twitter token/secret file", e);
		}
	}

	/**
	 * Request a new
	 */
	private void requestAccessToken() {
		RequestToken rt;
		try {
			rt = twitter.getOAuthRequestToken();
			System.out.println("Please visit " + rt.getAuthenticationURL() + " and sign in. When done, press Enter to continue");
			System.in.read();
		} catch (TwitterException e) {
			throw new HousemateException("Failed to request access token", e);
		} catch(IOException e) {
			throw new HousemateException("Failed to read Enter key-press from user", e);
		}

		try {
			AccessToken at = twitter.getOAuthAccessToken(rt);
			Properties props = new Properties();
			props.put(ACCESS_TOKEN_KEY, at.getToken());
			props.put(ACCESS_SECRET_KEY, at.getTokenSecret());
			props.store(new FileOutputStream(getTokenCredentialsPropsFile()), "Token key and secret for Housemate Twitter addon");
		} catch(TwitterException e) {
			throw new HousemateException("Failed to get Twitter access token from request token", e);
		} catch(IOException e) {
			throw new HousemateException("Failed to save Twitter access token and secret", e);
		}
	}

	/**
	 * Send a tweet
	 * @param to_tweet the message to tweet
	 */
	private synchronized void tweet(String to_tweet) {
		String message = dateFormat.format(new Date()) + ": " + to_tweet;
		logger.debug("Tweeting \"" + message + "\"");
		try {
			int i = 0;
			while(i + 140 < message.length()) {
				logger.debug("Tweeting characters from " + i + " through to " + (i + 137));
				twitter.updateStatus(message.substring(i, i + 137) + "...");
				i += 137;
			}
			logger.debug("Tweeting characters from " + i + " through to the end");
			twitter.updateStatus(message.substring(i));
		} catch(TwitterException e) {
			logger.error("Could not tweet \"" + message + "\" because: " + e.getMessage());
			e.printStackTrace();
		}
	}

    private class ServerListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<SimpleProxyServer, SimpleProxyList<SimpleProxyServer>> {
        @Override
        public void elementAdded(SimpleProxyList<SimpleProxyServer> list, SimpleProxyServer server) {
            server.getDevices().addObjectListener(deviceListListener, true);
        }

        @Override
        public void elementRemoved(SimpleProxyList<SimpleProxyServer> list, SimpleProxyServer server) {
            // remove the old listener
        }
    };

    private class DeviceListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<SimpleProxyDevice, SimpleProxyList<SimpleProxyDevice>> {
        @Override
        public void elementAdded(SimpleProxyList<SimpleProxyDevice> list, SimpleProxyDevice device) {
            java.util.List<ManagedCollection.Registration> registrations = new ArrayList<>();
            listeners.put(device, registrations);
            registrations.add(device.getFeatures().addObjectListener(new FeatureListListener(device, registrations), true));
            registrations.add(device.addObjectListener(deviceListener));
            tweet("\"" + device.getName() + "\" device added");
        }

        @Override
        public void elementRemoved(SimpleProxyList<SimpleProxyDevice> list, SimpleProxyDevice device) {
            if(listeners.get(device) != null)
                for(ManagedCollection.Registration registration : listeners.remove(device))
                    registration.remove();
            tweet("\"" + device.getName() + "\" device removed");
        }
    };

    private class DeviceListener implements Device.Listener<SimpleProxyDevice> {

        @Override
        public void renamed(SimpleProxyDevice device, String oldName, String newName) {
            tweet("\"" + oldName + "\" was renamed to \"" + newName + "\"");
        }

        @Override
        public void error(SimpleProxyDevice device, String description) {
            tweet("\"" + device.getName() + "\" device " + (description == null ? "not " : "") + "in error" + (description == null ? "" : ": " + description));
        }

        // todo add a feature listener
        /*@Override
        public void driverLoaded(SimpleProxyDevice device, boolean loaded) {
            tweet("\"" + device.getName() + "\" device's driver is " + (loaded ? "" : "not ") + "loaded");
        }*/

        @Override
        public void running(SimpleProxyDevice device, boolean running) {
            tweet("\"" + device.getName() + "\" device is " + (running ? "" : "not ") + "running");
        }
    };

    private class FeatureListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<SimpleProxyFeature, SimpleProxyList<SimpleProxyFeature>> {

        private final java.util.List<ManagedCollection.Registration> deviceListenerRegistrations;
        private final SimpleProxyDevice device;

        private FeatureListListener(SimpleProxyDevice device, java.util.List<ManagedCollection.Registration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            this.device = device;
        }

        @Override
        public void elementAdded(SimpleProxyList<SimpleProxyFeature> list, SimpleProxyFeature feature) {
            feature.getCommands().addObjectListener(new CommandListListener(device, feature, deviceListenerRegistrations));
            feature.getValues().addObjectListener(new ValueListListener(device, feature, deviceListenerRegistrations));
            feature.getProperties().addObjectListener(new PropertyListListener(device, feature, deviceListenerRegistrations));
            tweet("\"" + feature.getName() + "\" feature added");
        }

        @Override
        public void elementRemoved(SimpleProxyList<SimpleProxyFeature> list, SimpleProxyFeature feature) {
            tweet("\"" + feature.getName() + "\" feature removed");
        }
    };

    private class CommandListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<SimpleProxyCommand, SimpleProxyList<SimpleProxyCommand>> {

        private final Map<SimpleProxyCommand, ManagedCollection.Registration> commandListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ManagedCollection.Registration> deviceListenerRegistrations;
        private final CommandListener listener;

        private CommandListListener(SimpleProxyDevice device, SimpleProxyFeature feature, java.util.List<ManagedCollection.Registration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new CommandListener(device, feature);
        }

        @Override
        public void elementAdded(SimpleProxyList<SimpleProxyCommand> list, SimpleProxyCommand element) {
            ManagedCollection.Registration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            commandListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyList<SimpleProxyCommand> list, SimpleProxyCommand element) {
            if(commandListenerRegistrations.get(element) != null)
                commandListenerRegistrations.remove(element).remove();
        }
    }

    private class CommandListener implements Command.Listener<SimpleProxyCommand> {

        private final SimpleProxyDevice device;
        private final SimpleProxyFeature feature;

        private CommandListener(SimpleProxyDevice device, SimpleProxyFeature feature) {
            this.device = device;
            this.feature = feature;
        }

        @Override
        public void commandEnabled(SimpleProxyCommand command, boolean enabled) {
            tweet("\"" + device.getName() + "\" feature \"" + feature.getName() + "\" command \"" + command.getName() + (enabled ? "\" enabled" : "\" disabled"));
        }

        @Override
        public void commandStarted(SimpleProxyCommand command, String user) {
            tweet("\"" + device.getName() + "\" feature \"" + feature.getName() + "\" command \"" + command.getName() + "\" started by " + user);
        }

        @Override
        public void commandFinished(SimpleProxyCommand command) {
            tweet("\"" + device.getName() + "\" feature \"" + feature.getName() + "\" command \"" + command.getName() + "\" finished");
        }

        @Override
        public void commandFailed(SimpleProxyCommand command, String reason) {
            tweet("\"" + device.getName() + "\" feature \"" + feature.getName() + "\" command \"" + command.getName() + "\" failed: " + reason);
        }
    }

    private class ValueListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<SimpleProxyValue, SimpleProxyList<SimpleProxyValue>> {

        private final Map<SimpleProxyValue, ManagedCollection.Registration> valueListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ManagedCollection.Registration> deviceListenerRegistrations;
        private final ValueListener listener;

        private ValueListListener(SimpleProxyDevice device, SimpleProxyFeature feature, java.util.List<ManagedCollection.Registration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new ValueListener(device, feature);
        }

        @Override
        public void elementAdded(SimpleProxyList<SimpleProxyValue> list, SimpleProxyValue element) {
            ManagedCollection.Registration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            valueListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyList<SimpleProxyValue> list, SimpleProxyValue element) {
            if(valueListenerRegistrations.get(element) != null)
                valueListenerRegistrations.remove(element).remove();
        }
    }

    private class ValueListener implements Value.Listener<SimpleProxyValue> {

        private final SimpleProxyDevice device;
        private final SimpleProxyFeature feature;

        private ValueListener(SimpleProxyDevice device, SimpleProxyFeature feature) {
            this.device = device;
            this.feature = feature;
        }

        @Override
        public void valueChanging(SimpleProxyValue value) {
            // do nothing
        }

        @Override
        public void valueChanged(SimpleProxyValue value) {
            tweet("\"" + device.getName() + "\" feature \"" + feature.getName() + "\" value \"" + value.getName() + "\" is \"" + value.getValue() + "\"");
        }
    }

    private class PropertyListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<SimpleProxyProperty, SimpleProxyList<SimpleProxyProperty>> {

        private final Map<SimpleProxyProperty, ManagedCollection.Registration> propertyListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ManagedCollection.Registration> deviceListenerRegistrations;
        private final PropertyListener listener;

        private PropertyListListener(SimpleProxyDevice device, SimpleProxyFeature feature, java.util.List<ManagedCollection.Registration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new PropertyListener(device, feature);
        }

        @Override
        public void elementAdded(SimpleProxyList<SimpleProxyProperty> list, SimpleProxyProperty element) {
            ManagedCollection.Registration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            propertyListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyList<SimpleProxyProperty> list, SimpleProxyProperty element) {
            if(propertyListenerRegistrations.get(element) != null)
                propertyListenerRegistrations.remove(element).remove();
        }
    }

    private class PropertyListener implements Property.Listener<SimpleProxyProperty> {

        private final SimpleProxyDevice device;
        private final SimpleProxyFeature feature;

        private PropertyListener(SimpleProxyDevice device, SimpleProxyFeature feature) {
            this.device = device;
            this.feature = feature;
        }

        @Override
        public void valueChanging(SimpleProxyProperty value) {
            // do nothing
        }

        @Override
        public void valueChanged(SimpleProxyProperty property) {
            tweet("\"" + device.getName() + "\" feature \"" + feature.getName() + "\" property \"" + property.getName() + "\" is now set to \"" + property.getValue() + "\"");
        }
    }
}
