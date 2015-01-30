package com.intuso.housemate.addon.twitter;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.simple.*;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
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

    private final Log log;

    private final ApplicationDetails applicationDetails;
    private final String applicationInstanceId;

    private final Map<SimpleProxyDevice, java.util.List<ListenerRegistration>> listeners;
    private DeviceListListener deviceListListener = new DeviceListListener();
    private DeviceListener deviceListener = new DeviceListener();

	/**
	 * Default constructor
	 * @throws HousemateException
	 */
	@Inject
	public HousemateTweeter(final Log log, final PropertyRepository properties, Injector injector) throws HousemateException {

        this.log = log;
        this.applicationDetails = new ApplicationDetails(HousemateTweeter.class.getName(), "Housemate Tweeter", "Housemate Tweeter");
        this.applicationInstanceId = properties.get(INSTANCE_ID);

        listeners = new HashMap<SimpleProxyDevice, java.util.List<ListenerRegistration>>();

		dateFormat = new SimpleDateFormat("h:mm a");
        DateFormatSymbols dateFormatSymbols = dateFormat.getDateFormatSymbols();
        dateFormatSymbols.setAmPmStrings(new String[]{"am", "pm"});
        dateFormat.setDateFormatSymbols(dateFormatSymbols);

		// set up the twitter stuff
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("dPL31ZtQCTBknLOvEY7Lg", "slOU1tdctmdGOdVAhgMv2E2Vxe4jZo52TbQ19elALE");
		getTokenCredentials();
		log.d("Loaded token and secret. If these are wrong or no longer valid please delete the file \"" + getTokenCredentialsPropsFile().getAbsolutePath() + "\" to force the addon to get a new token and secret");

		// setup the housemate stuff
        final SimpleProxyRoot root = injector.getInstance(SimpleProxyRoot.class);
        root.addObjectListener(new RootListener<SimpleProxyRoot>() {

            @Override
            public void serverConnectionStatusChanged(SimpleProxyRoot root, ServerConnectionStatus serverConnectionStatus) {
                switch (serverConnectionStatus) {
                    case DisconnectedPermanently:
                        log.d("Disconnected permanently from server");
                        tweet("Disconnected permanently from server");
                        return;
                    case DisconnectedTemporarily:
                        log.d("Disconnected temporarily from server");
                        tweet("Disconnected temporarily from server");
                        return;
                    case Connecting:
                        log.d("Connected to server");
                        tweet("Connected to server");
                        return;
                    case ConnectedToRouter:
                        log.d("Connected to router");
                        tweet("Connected to router");
                        return;
                    case ConnectedToServer:
                        log.d("Connected to server");
                        tweet("Connected to server");
                        // don't return in this case, let it drop through to handle the other status(es)
                }
            }

            @Override
            public void applicationStatusChanged(SimpleProxyRoot root, ApplicationStatus applicationStatus) {}

            @Override
            public void applicationInstanceStatusChanged(final SimpleProxyRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
                switch (applicationInstanceStatus) {
                    case Unregistered:
                        log.d("Application not registered with the server");
                        tweet("Application not registered with the server");
                        break;
                    case Registering:
                        log.d("Application registering with the server");
                        tweet("Application registering with the server");
                        break;
                    case Rejected:
                        log.d("Access to the server rejected");
                        tweet("Access to the server rejected");
                        break;
                    case Expired:
                        log.d("Access to the server expired");
                        tweet("Access to the server expired");
                        break;
                    case Pending:
                        log.d("Access to the server pending");
                        tweet("Access to the server pending");
                        break;
                    case Allowed:
                        log.d("Access to the server allowed");
                        tweet("Access to the server allowed");
                        root.load(new LoadManager(new LoadManager.Callback() {
                            @Override
                            public void failed(HousemateObject.TreeLoadInfo tl) {
                                tweet("Could not load devices from server. Do you have permission?");
                            }

                            @Override
                            public void allLoaded() {
                                root.getDevices().addObjectListener(deviceListListener, true);
                            }
                        }, "twitterClientInitialLoad",
                                new HousemateObject.TreeLoadInfo(Root.DEVICES_ID),
                                new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
                        break;
                }
            }

            @Override
            public void newApplicationInstance(SimpleProxyRoot root, String instanceId) {
                // do nothing
            }

            @Override
            public void newServerInstance(SimpleProxyRoot root, String serverId) {
                log.d("Server instance changed");
                tweet("Server instance changed");
                root.register(applicationDetails);
            }
        });

        root.register(applicationDetails);
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
	 * @throws HousemateException
	 */
	private void getTokenCredentials() throws HousemateException {

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
	 * @throws HousemateException
	 */
	private void requestAccessToken() throws HousemateException {
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
		log.d("Tweeting \"" + message + "\"");
		try {
			int i = 0;
			while(i + 140 < message.length()) {
				log.d("Tweeting characters from " + i + " through to " + (i + 137));
				twitter.updateStatus(message.substring(i, i + 137) + "...");
				i += 137;
			}
			log.d("Tweeting characters from " + i + " through to the end");
			twitter.updateStatus(message.substring(i));
		} catch(TwitterException e) {
			log.e("Could not tweet \"" + message + "\" because: " + e.getMessage());
			e.printStackTrace();
		}
	}

    private class DeviceListListener implements ListListener<SimpleProxyDevice> {
        @Override
        public void elementAdded(SimpleProxyDevice device) {
            java.util.List<ListenerRegistration> registrations = new ArrayList<ListenerRegistration>();
            listeners.put(device, registrations);
            registrations.add(device.getCommands().addObjectListener(new CommandListListener(device, registrations), true));
            registrations.add(device.getValues().addObjectListener(new ValueListListener(device, registrations), true));
            registrations.add(device.getProperties().addObjectListener(new PropertyListListener(device, registrations), true));
            registrations.add(device.addObjectListener(deviceListener));
            tweet("\"" + device.getName() + "\" device added");
        }

        @Override
        public void elementRemoved(SimpleProxyDevice device) {
            if(listeners.get(device) != null)
                for(ListenerRegistration registration : listeners.remove(device))
                    registration.removeListener();
            tweet("\"" + device.getName() + "\" device removed");
        }
    };

    private class DeviceListener implements com.intuso.housemate.api.object.device.DeviceListener<SimpleProxyDevice> {

        @Override
        public void deviceConnected(SimpleProxyDevice device, boolean connected) {
            tweet("\"" + device.getName() + "\" device is " + (connected ? "not " : "") + "connected");
        }

        @Override
        public void renamed(SimpleProxyDevice device, String oldName, String newName) {
            tweet("\"" + oldName + "\" was renamed to \"" + newName + "\"");
        }

        @Override
        public void error(SimpleProxyDevice device, String description) {
            tweet("\"" + device.getName() + "\" device " + (description == null ? "not " : "") + "in error" + (description == null ? "" : ": " + description));
        }

        @Override
        public void running(SimpleProxyDevice device, boolean running) {
            tweet("\"" + device.getName() + "\" device is " + (running ? "" : "not ") + "running");
        }
    };

    private class CommandListListener implements ListListener<SimpleProxyCommand> {

        private final Map<SimpleProxyCommand, ListenerRegistration> commandListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ListenerRegistration> deviceListenerRegistrations;
        private final CommandListener listener;

        private CommandListListener(SimpleProxyDevice device, java.util.List<ListenerRegistration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new CommandListener(device);
        }

        @Override
        public void elementAdded(SimpleProxyCommand element) {
            ListenerRegistration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            commandListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyCommand element) {
            if(commandListenerRegistrations.get(element) != null)
                commandListenerRegistrations.remove(element).removeListener();
        }
    }

    private class CommandListener implements com.intuso.housemate.api.object.command.CommandListener<SimpleProxyCommand> {

        private final SimpleProxyDevice device;

        private CommandListener(SimpleProxyDevice device) {
            this.device = device;
        }

        @Override
        public void commandEnabled(SimpleProxyCommand command, boolean enabled) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + (enabled ? "\" enabled" : "\" disabled"));
        }

        @Override
        public void commandStarted(SimpleProxyCommand command, String user) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + "\" started by " + user);
        }

        @Override
        public void commandFinished(SimpleProxyCommand command) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + "\" finished");
        }

        @Override
        public void commandFailed(SimpleProxyCommand command, String reason) {
            tweet("\"" + device.getName() + "\" command \"" + command.getName() + "\" failed: " + reason);
        }
    }

    private class ValueListListener implements ListListener<SimpleProxyValue> {

        private final Map<SimpleProxyValue, ListenerRegistration> valueListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ListenerRegistration> deviceListenerRegistrations;
        private final ValueListener listener;

        private ValueListListener(SimpleProxyDevice device, java.util.List<ListenerRegistration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new ValueListener(device);
        }

        @Override
        public void elementAdded(SimpleProxyValue element) {
            ListenerRegistration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            valueListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyValue element) {
            if(valueListenerRegistrations.get(element) != null)
                valueListenerRegistrations.remove(element).removeListener();
        }
    }

    private class ValueListener implements com.intuso.housemate.api.object.value.ValueListener<SimpleProxyValue> {

        private final SimpleProxyDevice device;

        private ValueListener(SimpleProxyDevice device) {
            this.device = device;
        }

        @Override
        public void valueChanging(SimpleProxyValue value) {
            // do nothing
        }

        @Override
        public void valueChanged(SimpleProxyValue value) {
            tweet("\"" + device.getName() + "\" value \"" + value.getId() + "\" is \"" + value.getTypeInstances() + "\"");
        }
    }

    private class PropertyListListener implements ListListener<SimpleProxyProperty> {

        private final Map<SimpleProxyProperty, ListenerRegistration> propertyListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ListenerRegistration> deviceListenerRegistrations;
        private final PropertyListener listener;

        private PropertyListListener(SimpleProxyDevice device, java.util.List<ListenerRegistration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new PropertyListener(device);
        }

        @Override
        public void elementAdded(SimpleProxyProperty element) {
            ListenerRegistration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            propertyListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyProperty element) {
            if(propertyListenerRegistrations.get(element) != null)
                propertyListenerRegistrations.remove(element).removeListener();
        }
    }

    private class PropertyListener implements com.intuso.housemate.api.object.value.ValueListener<SimpleProxyProperty> {

        private final SimpleProxyDevice device;

        private PropertyListener(SimpleProxyDevice device) {
            this.device = device;
        }

        @Override
        public void valueChanging(SimpleProxyProperty value) {
            // do nothing
        }

        @Override
        public void valueChanged(SimpleProxyProperty property) {
            tweet("\"" + device.getName() + "\" property \"" + property.getId() + "\" is now set to \"" + property.getTypeInstances() + "\"");
        }
    }
}
