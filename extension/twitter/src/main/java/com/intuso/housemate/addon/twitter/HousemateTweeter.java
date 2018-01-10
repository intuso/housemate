package com.intuso.housemate.addon.twitter;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.*;
import com.intuso.housemate.client.v1_0.proxy.object.*;
import com.intuso.utilities.collection.ManagedCollection;
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

    private final Map<ProxyDeviceGroup.Simple, java.util.List<ManagedCollection.Registration>> listeners;
    private ServerListListener serverListListener = new ServerListListener();
    private DeviceGroupListListener deviceGroupListListener = new DeviceGroupListListener();
    private DeviceGroupListener deviceGroupListener = new DeviceGroupListener();

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
        final ProxyServer.Simple server = injector.getInstance(ProxyServer.Simple.class);
        server.getDeviceGroups().addObjectListener(deviceGroupListListener, true);
	}

	/**
	 * Get the file where oauth token is stored
	 * @return the file where oauth token is stored
	 */
	private File getTokenCredentialsPropsFile() {
		return new File(java.lang.System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "twitter_auth");
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
			java.lang.System.out.println("Please visit " + rt.getAuthenticationURL() + " and sign in. When done, press Enter to continue");
			java.lang.System.in.read();
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

    private class ServerListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<ProxyServer.Simple, ProxyList.Simple<ProxyServer.Simple>> {
        @Override
        public void elementAdded(ProxyList.Simple<ProxyServer.Simple> list, ProxyServer.Simple server) {
            server.getDeviceGroups().addObjectListener(deviceGroupListListener, true);
        }

        @Override
        public void elementRemoved(ProxyList.Simple<ProxyServer.Simple> list, ProxyServer.Simple server) {
            // remove the old listener
        }
    };

    private class DeviceGroupListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<ProxyDeviceGroup.Simple, ProxyList.Simple<ProxyDeviceGroup.Simple>> {
        @Override
        public void elementAdded(ProxyList.Simple<ProxyDeviceGroup.Simple> list, ProxyDeviceGroup.Simple device) {
            java.util.List<ManagedCollection.Registration> registrations = new ArrayList<>();
            listeners.put(device, registrations);
            registrations.add(device.addObjectListener(deviceGroupListener));
            tweet("\"" + device.getName() + "\" device added");
        }

        @Override
        public void elementRemoved(ProxyList.Simple<ProxyDeviceGroup.Simple> list, ProxyDeviceGroup.Simple device) {
            if(listeners.get(device) != null)
                for(ManagedCollection.Registration registration : listeners.remove(device))
                    registration.remove();
            tweet("\"" + device.getName() + "\" device removed");
        }
    };

    private class DeviceGroupListener implements Device.Group.Listener<ProxyDeviceGroup.Simple> {

        @Override
        public void renamed(ProxyDeviceGroup.Simple device, String oldName, String newName) {
            tweet("\"" + oldName + "\" was renamed to \"" + newName + "\"");
        }

        @Override
        public void error(ProxyDeviceGroup.Simple device, String description) {
            tweet("\"" + device.getName() + "\" device " + (description == null ? "not " : "") + "in error" + (description == null ? "" : ": " + description));
        }
    };

    private class CommandListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<ProxyCommand.Simple, ProxyList.Simple<ProxyCommand.Simple>> {

        private final Map<ProxyCommand.Simple, ManagedCollection.Registration> commandListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ManagedCollection.Registration> deviceListenerRegistrations;
        private final CommandListener listener;

        private CommandListListener(ProxyDeviceGroup.Simple device, java.util.List<ManagedCollection.Registration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new CommandListener(device);
        }

        @Override
        public void elementAdded(ProxyList.Simple<ProxyCommand.Simple> list, ProxyCommand.Simple element) {
            ManagedCollection.Registration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            commandListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(ProxyList.Simple<ProxyCommand.Simple> list, ProxyCommand.Simple element) {
            if(commandListenerRegistrations.get(element) != null)
                commandListenerRegistrations.remove(element).remove();
        }
    }

    private class CommandListener implements Command.Listener<ProxyCommand.Simple> {

        private final ProxyDeviceGroup.Simple device;

        private CommandListener(ProxyDeviceGroup.Simple device) {
            this.device = device;
        }

        @Override
        public void commandEnabled(ProxyCommand.Simple command, boolean enabled) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + (enabled ? "\" enabled" : "\" disabled"));
        }

        @Override
        public void commandStarted(ProxyCommand.Simple command, String user) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + "\" started by " + user);
        }

        @Override
        public void commandFinished(ProxyCommand.Simple command) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + "\" finished");
        }

        @Override
        public void commandFailed(ProxyCommand.Simple command, String reason) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + "\" failed: " + reason);
        }
    }

    private class ValueListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<ProxyValue.Simple, ProxyList.Simple<ProxyValue.Simple>> {

        private final Map<ProxyValue.Simple, ManagedCollection.Registration> valueListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ManagedCollection.Registration> deviceListenerRegistrations;
        private final ValueListener listener;

        private ValueListListener(ProxyDeviceGroup.Simple device, java.util.List<ManagedCollection.Registration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new ValueListener(device);
        }

        @Override
        public void elementAdded(ProxyList.Simple<ProxyValue.Simple> list, ProxyValue.Simple element) {
            ManagedCollection.Registration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            valueListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(ProxyList.Simple<ProxyValue.Simple> list, ProxyValue.Simple element) {
            if(valueListenerRegistrations.get(element) != null)
                valueListenerRegistrations.remove(element).remove();
        }
    }

    private class ValueListener implements Value.Listener<ProxyValue.Simple> {

        private final ProxyDeviceGroup.Simple device;

        private ValueListener(ProxyDeviceGroup.Simple device) {
            this.device = device;
        }

        @Override
        public void valueChanging(ProxyValue.Simple value) {
            // do nothing
        }

        @Override
        public void valueChanged(ProxyValue.Simple value) {
            tweet("\"" + device.getName() + "\" value \"" + value.getName() + "\" is \"" + value.getValues() + "\"");
        }
    }

    private class PropertyListListener implements com.intuso.housemate.client.v1_0.api.object.List.Listener<ProxyProperty.Simple, ProxyList.Simple<ProxyProperty.Simple>> {

        private final Map<ProxyProperty.Simple, ManagedCollection.Registration> propertyListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ManagedCollection.Registration> deviceListenerRegistrations;
        private final PropertyListener listener;

        private PropertyListListener(ProxyDeviceGroup.Simple device, java.util.List<ManagedCollection.Registration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new PropertyListener(device);
        }

        @Override
        public void elementAdded(ProxyList.Simple<ProxyProperty.Simple> list, ProxyProperty.Simple element) {
            ManagedCollection.Registration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            propertyListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(ProxyList.Simple<ProxyProperty.Simple> list, ProxyProperty.Simple element) {
            if(propertyListenerRegistrations.get(element) != null)
                propertyListenerRegistrations.remove(element).remove();
        }
    }

    private class PropertyListener implements Property.Listener<ProxyProperty.Simple> {

        private final ProxyDeviceGroup.Simple device;

        private PropertyListener(ProxyDeviceGroup.Simple device) {
            this.device = device;
        }

        @Override
        public void valueChanging(ProxyProperty.Simple value) {
            // do nothing
        }

        @Override
        public void valueChanged(ProxyProperty.Simple property) {
            tweet("\"" + device.getName() + "\" property \"" + property.getName() + "\" is now set to \"" + property.getValues() + "\"");
        }
    }
}
