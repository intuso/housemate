package com.intuso.housemate.addon.twitter;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyResources;
import com.intuso.utilities.listener.ListenerRegistration;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Main class for "tweeting" Housemate events
 */
public class HousemateTweeter {
	
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
	
	/**
	 * The log to use
	 */
	private final Resources resources;

    private final Map<SimpleProxyObject.Device, java.util.List<ListenerRegistration>> listeners;

    private DeviceListListener deviceListListener = new DeviceListListener();

    private DeviceListener deviceListener = new DeviceListener();

	/**
	 * Default constructor
	 * @throws HousemateException
	 */
	@SuppressWarnings("unused")
	public HousemateTweeter(final SimpleProxyResources<SimpleProxyFactory.All> resources) throws HousemateException {

		this.resources = resources;
        listeners = new HashMap<SimpleProxyObject.Device, java.util.List<ListenerRegistration>>();

		dateFormat = new SimpleDateFormat("h:mm a");
        DateFormatSymbols dateFormatSymbols = dateFormat.getDateFormatSymbols();
        dateFormatSymbols.setAmPmStrings(new String[]{"am", "pm"});
        dateFormat.setDateFormatSymbols(dateFormatSymbols);

		// set up the twitter stuff
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("dPL31ZtQCTBknLOvEY7Lg", "slOU1tdctmdGOdVAhgMv2E2Vxe4jZo52TbQ19elALE");
		getTokenCredentials();
		resources.getLog().d("Loaded token and secret. If these are wrong or no longer valid please delete the file \"" + getTokenCredentialsPropsFile().getAbsolutePath() + "\" to force the addon to get a new token and secret");

		// setup the housemate stuff
        final SimpleProxyObject.Root root = new SimpleProxyObject.Root(resources,  resources);
        root.addObjectListener(new RootListener<SimpleProxyObject.Root>() {

            @Override
            public void connectionStatusChanged(final SimpleProxyObject.Root root, ConnectionStatus status) {
                switch (status) {
                    case Disconnected:
                        resources.getLog().d("Disconnected from server");
                        break;
                    case Connecting:
                        resources.getLog().d("Reconnecting to server");
                        break;
                    case Unauthenticated:
                        resources.getLog().d("Connected to server but not authenticated");
                        break;
                    case Authenticating:
                        resources.getLog().d("Authenticating with server");
                        break;
                    case Authenticated:
                        resources.getLog().e("Authenticated with server");
                        root.load(new LoadManager("twitterClient", new HousemateObject.TreeLoadInfo(Root.DEVICES_ID),
                                new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)) {
                            @Override
                            protected void failed(HousemateObject.TreeLoadInfo tl) {
                                tweet("Could not load devices from server. Do you have permission?");
                            }

                            @Override
                            protected void allLoaded() {
                                root.getDevices().addObjectListener(deviceListListener, true);
                            }
                        });
                        break;
                }
            }

            @Override
            public void brokerInstanceChanged(SimpleProxyObject.Root root) {
                tweet("Broker instance changed. Reconnecting");
                root.login(new UsernamePassword(resources.getProperties().get("username"), resources.getProperties().get("password"), true));
            }
        });

        root.login(new UsernamePassword(resources.getProperties().get("username"), resources.getProperties().get("password"), true));
	}

	/**
	 * Get the file where token authentication is stored
	 * @return the file where token authentication is stored
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
		resources.getLog().d("Tweeting \"" + message + "\"");
		try {
			int i = 0;
			while(i + 140 < message.length()) {
				resources.getLog().d("Tweeting characters from " + i + " through to " + (i + 137));
				twitter.updateStatus(message.substring(i, i + 137) + "...");
				i += 137;
			}
			resources.getLog().d("Tweeting characters from " + i + " through to the end");
			twitter.updateStatus(message.substring(i));
		} catch(TwitterException e) {
			resources.getLog().e("Could not tweet \"" + message + "\" because: " + e.getMessage());
			e.printStackTrace();
		}
	}

    private class DeviceListListener implements ListListener<SimpleProxyObject.Device> {
        @Override
        public void elementAdded(SimpleProxyObject.Device device) {
            java.util.List<ListenerRegistration> registrations = new ArrayList<ListenerRegistration>();
            listeners.put(device, registrations);
            registrations.add(device.getCommands().addObjectListener(new CommandListListener(device, registrations), true));
            registrations.add(device.getValues().addObjectListener(new ValueListListener(device, registrations), true));
            registrations.add(device.getProperties().addObjectListener(new PropertyListListener(device, registrations), true));
            registrations.add(device.addObjectListener(deviceListener));
            tweet("\"" + device.getName() + "\" device added");
        }

        @Override
        public void elementRemoved(SimpleProxyObject.Device device) {
            if(listeners.get(device) != null)
                for(ListenerRegistration registration : listeners.remove(device))
                    registration.removeListener();
            tweet("\"" + device.getName() + "\" device removed");
        }
    };

    private class DeviceListener implements com.intuso.housemate.api.object.device.DeviceListener<SimpleProxyObject.Device> {
        @Override
        public void error(SimpleProxyObject.Device device, String description) {
            tweet("\"" + device.getName() + "\" device " + (description == null ? "not " : "") + "in error" + (description == null ? "" : ": " + description));
        }

        @Override
        public void running(SimpleProxyObject.Device device, boolean running) {
            tweet("\"" + device.getName() + "\" device is " + (running ? "" : "not ") + "running");
        }
    };

    private class CommandListListener implements ListListener<SimpleProxyObject.Command> {

        private final Map<SimpleProxyObject.Command, ListenerRegistration> commandListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ListenerRegistration> deviceListenerRegistrations;
        private final CommandListener listener;

        private CommandListListener(SimpleProxyObject.Device device, java.util.List<ListenerRegistration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new CommandListener(device);
        }

        @Override
        public void elementAdded(SimpleProxyObject.Command element) {
            ListenerRegistration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            commandListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyObject.Command element) {
            if(commandListenerRegistrations.get(element) != null)
                commandListenerRegistrations.remove(element).removeListener();
        }
    }

    private class CommandListener implements com.intuso.housemate.api.object.command.CommandListener<SimpleProxyObject.Command> {

        private final SimpleProxyObject.Device device;

        private CommandListener(SimpleProxyObject.Device device) {
            this.device = device;
        }

        @Override
        public void commandStarted(SimpleProxyObject.Command command) {
            tweet("Performing \"" + device.getName() + "\" command \"" + command.getId() + "\"");
        }

        @Override
        public void commandFinished(SimpleProxyObject.Command command) {
            tweet("Finished performing \"" + device.getName() + "\" command \"" + command.getId() + "\"");
        }

        @Override
        public void commandFailed(SimpleProxyObject.Command command, String reason) {
            tweet("Failed to perform \"" + device.getName() + "\" command \"" + command.getId() + "\": " + reason);
        }
    }

    private class ValueListListener implements ListListener<SimpleProxyObject.Value> {

        private final Map<SimpleProxyObject.Value, ListenerRegistration> valueListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ListenerRegistration> deviceListenerRegistrations;
        private final ValueListener listener;

        private ValueListListener(SimpleProxyObject.Device device, java.util.List<ListenerRegistration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new ValueListener(device);
        }

        @Override
        public void elementAdded(SimpleProxyObject.Value element) {
            ListenerRegistration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            valueListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyObject.Value element) {
            if(valueListenerRegistrations.get(element) != null)
                valueListenerRegistrations.remove(element).removeListener();
        }
    }

    private class ValueListener implements com.intuso.housemate.api.object.value.ValueListener<SimpleProxyObject.Value> {

        private final SimpleProxyObject.Device device;

        private ValueListener(SimpleProxyObject.Device device) {
            this.device = device;
        }

        @Override
        public void valueChanging(SimpleProxyObject.Value value) {
            // do nothing
        }

        @Override
        public void valueChanged(SimpleProxyObject.Value value) {
            tweet("\"" + device.getName() + "\" value \"" + value.getId() + "\" is \"" + value.getTypeInstances() + "\"");
        }
    }

    private class PropertyListListener implements ListListener<SimpleProxyObject.Property> {

        private final Map<SimpleProxyObject.Property, ListenerRegistration> propertyListenerRegistrations = Maps.newHashMap();
        private final java.util.List<ListenerRegistration> deviceListenerRegistrations;
        private final PropertyListener listener;

        private PropertyListListener(SimpleProxyObject.Device device, java.util.List<ListenerRegistration> deviceListenerRegistrations) {
            this.deviceListenerRegistrations = deviceListenerRegistrations;
            listener = new PropertyListener(device);
        }

        @Override
        public void elementAdded(SimpleProxyObject.Property element) {
            ListenerRegistration listenerRegistration = element.addObjectListener(listener);
            deviceListenerRegistrations.add(listenerRegistration);
            propertyListenerRegistrations.put(element, listenerRegistration);
        }

        @Override
        public void elementRemoved(SimpleProxyObject.Property element) {
            if(propertyListenerRegistrations.get(element) != null)
                propertyListenerRegistrations.remove(element).removeListener();
        }
    }

    private class PropertyListener implements com.intuso.housemate.api.object.value.ValueListener<SimpleProxyObject.Property> {

        private final SimpleProxyObject.Device device;

        private PropertyListener(SimpleProxyObject.Device device) {
            this.device = device;
        }

        @Override
        public void valueChanging(SimpleProxyObject.Property value) {
            // do nothing
        }

        @Override
        public void valueChanged(SimpleProxyObject.Property property) {
            tweet("\"" + device.getName() + "\" property \"" + property.getId() + "\" is now set to \"" + property.getTypeInstances() + "\"");
        }
    }
}
