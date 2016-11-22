package com.intuso.housemate.pkg.server.jar.ioc.jetty;

import com.google.common.util.concurrent.AbstractService;
import com.google.inject.Inject;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.eclipse.jetty.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 27/10/14.
 */
public class JettyServerService extends AbstractService {

    public final static String ID = "jetty";
    public final static String CONNECTORS = "connectors";
    public final static String HOST = "host";
    public final static String PORT = "port";
    public final static String SSL = "ssl";

    private final static Logger logger = LoggerFactory.getLogger(JettyServerService.class);
    private final Server server;
    private final PropertyRepository properties;

    @Inject
    public JettyServerService(Server server,
                              PropertyRepository properties) {
        this.server = server;
        this.properties = properties;
    }

    @Override
    protected void doStart() {
        try {
            // clear the connectors, and re-add them once we can load the config
            if(server.getConnectors() != null)
                for(Connector connector : server.getConnectors())
                    server.removeConnector(connector);
            addConnectors();
            server.start();
        } catch (Exception e) {
            logger.error("Failed to start jetty server", e);
        }
        notifyStarted();
    }

    private void addConnectors() {
        for(String portKey : properties.get("web.ports").split(",")) {

            String propertyPrefix = "web.ports." + portKey + ".";

            String type = properties.get(propertyPrefix + "type");

            if ("http".equals(type)) {

                String host = properties.get(propertyPrefix + "host");
                String portString = properties.get(propertyPrefix + "port");
                Integer port;

                if(portString == null) {
                    logger.error("No port specified for web server port config {}. Missing property {}. Skipping configuration for port {}", portKey, propertyPrefix + "port", portKey);
                    continue;
                }

                try {
                    port = Integer.parseInt(portString);
                } catch(NumberFormatException e) {
                    logger.error("Could not parse port number from property {}. Skipping configuration for port {}", propertyPrefix + "port", portKey);
                    continue;
                }

                // create the connector
                ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(new HttpConfiguration()));

                // configure the host and port
                if (host != null && host.length() > 0)
                    connector.setHost(host);
                connector.setPort(port);

                server.addConnector(connector);
            } else
                logger.error("No type specified for web server port config {}. Missing property {}. Skipping configuration for port {}", portKey, propertyPrefix + "type", portKey);
        }
    }

    @Override
    protected void doStop() {
        try {
            server.stop();
        } catch (Exception e) {
            logger.error("Failed to stop jetty server", e);
        }
        notifyStopped();
    }


}
